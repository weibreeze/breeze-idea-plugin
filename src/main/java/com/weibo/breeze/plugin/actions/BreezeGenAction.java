package com.weibo.breeze.plugin.actions;

import com.intellij.ide.actions.ShowSettingsUtilImpl;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.weibo.breeze.plugin.setting.BreezeSettingState;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;


/**
 * @author zhanglei28
 * @date 2021/11/24.
 */
public class BreezeGenAction extends AnAction {
    protected static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        generate(e, false);
    }

    @Override
    public void update(AnActionEvent e) {
        // 当前文件为目录或breeze后缀的文件时可用
        VirtualFile file = PlatformDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
        boolean enable = file != null &&
                (file.isDirectory() || "breeze".equals(file.getExtension()));
        e.getPresentation().setEnabled(enable);
    }

    protected void generate(AnActionEvent e, boolean withMotanConfig) {
        // check setting
        BreezeSettingState state = BreezeSettingState.getInstance();
        if (StringUtils.isBlank(state.breezeGenerateUrl)) {
            Messages.showMessageDialog("Breeze generate server URL not set. please set it in `Preferences -> Tools -> Breeze Setting`", "Need Setting", Messages.getInformationIcon());
            ShowSettingsUtilImpl.showSettingsDialog(e.getProject(), "com.weibo.breeze.plugin.setting.BreezeSettingConfigurable", null);
            return;
        }

        // read files content
        JSONObject filesContent = new JSONObject();
        VirtualFile[] files = e.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);
        try {
            if (files != null) {
                for (VirtualFile virtualFile : files) {
                    putFileContent(virtualFile, filesContent);
                }
            }

            if (filesContent.isEmpty()) {
                Messages.showMessageDialog("Breeze schema file not found. the file name should be `*.breeze`", "Breeze File Not Found", Messages.getInformationIcon());
                return;
            }

            // request to generate server
            JSONObject options = new JSONObject();
            options.put("with_motan_config", String.valueOf(withMotanConfig));
            Project project = e.getProject();
            if (project == null) {
                Messages.showMessageDialog("Project is null.", "Project Not Found", Messages.getInformationIcon());
                return;
            }
            String targetLanguage = getTargetBreezeLanguageString(project);
            Request request = new Request.Builder()
                    .url(state.breezeGenerateUrl)
                    .post(new FormBody.Builder()
                            .add("target_language", targetLanguage)
                            .add("file_content", filesContent.toJSONString())
                            .add("options", options.toJSONString())
                            .build())
                    .build();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            JSONObject resJson = (JSONObject) JSONValue.parse(res);
            boolean result = (boolean) resJson.getOrDefault("result", false);
            if (!result) {
                throw new RuntimeException("server response fail. res:" + res);
            }
            JSONObject codeFiles = (JSONObject) resJson.get("code_content");
            if (codeFiles.isEmpty()) {
                Messages.showMessageDialog("generate code file is empty. return string:" + res, "Generate Fail", Messages.getInformationIcon());
                return;
            }

            Module module = ModuleUtil.findModuleForFile(files[0], project);
            String rootPath = project.getBasePath();
            if (module != null) {
                rootPath = ModuleRootManager.getInstance(module).getContentRoots()[0].getPath() + File.separator;
            }
            final String baseCodePath;
            final String baseConfigPath;
            final boolean useModulePath = StringUtils.isBlank(state.generatedDir);
            if (useModulePath) { // TODO path based on language
                baseCodePath = rootPath + "src/main/java/";
                baseConfigPath = rootPath + "src/main/resources/motan/";
            } else {
                String customDir = state.generatedDir.trim();
                if (customDir.startsWith("." + File.separator)) { // use module path as relative path root
                    customDir = rootPath + customDir.substring(("." + File.separator).length());
                    if (!customDir.endsWith(File.separator)) {
                        customDir = customDir + File.separator;
                    }
                }
                baseCodePath = customDir;
                baseConfigPath = customDir;
            }

            // write code files
            codeFiles.forEach((k, v) -> writeToCodeFile(project, baseCodePath, k, (String) v));

            // write config file
            Object configFiles = resJson.get("config_content"); // maybe not exist
            if (configFiles != null && !((JSONObject) configFiles).isEmpty()) {
                ((JSONObject) configFiles).forEach((k, v) -> {
                    try {
                        VirtualFile file = getOrCreateFile(project, baseConfigPath, k);
                        file.setBinaryContent(((String) v).getBytes(StandardCharsets.UTF_8));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
            }
        } catch (Exception exception) {
            Messages.showInfoMessage("error: " + exception.getMessage(), "Generate Fail");
            return;
        }
        Messages.showMessageDialog("Generate file finish~", "Generate Finish", Messages.getInformationIcon());
    }

    protected static void putFileContent(VirtualFile virtualFile, JSONObject filesContent) throws IOException {
        if (virtualFile.isDirectory()) {
            VirtualFile[] vfs = virtualFile.getChildren();
            if (vfs != null) {
                for (VirtualFile file : vfs) {
                    putFileContent(file, filesContent);
                }
            }
        } else if ("breeze".equals(virtualFile.getExtension())) {
            String content = new String(virtualFile.contentsToByteArray(), StandardCharsets.UTF_8);
            filesContent.put(virtualFile.getName(), content);
        }
    }

    protected static String getTargetBreezeLanguageString(Project project) {
        String targetClass = BreezeSettingState.getInstance().targetLanguage;
        if (StringUtils.isBlank(targetClass)
                || "auto".equals(targetClass)) { // 根据项目语言来自动设置target language
            targetClass = "java";
            // TODO 根据project的sdk类型区分不同IDE，支持golang等其他语言的IDE
//        String sdkType = ProjectRootManager.getInstance(project).getProjectSdkTypeName();
//        if (StringUtils.isNotBlank(sdkType)) {
//            if (sdkType.contains("Java")) { // java project
//                targetClass = "java";
//            }
//        }
        }
        return targetClass;
    }

    protected static void writeToCodeFile(Project project, String rootPath, String name, String content) {
        ApplicationManager.getApplication().runWriteAction(() -> {
            VirtualFile outFile;
            try {
                String packageStr = "";
                String nameStr = name;
                int index = name.lastIndexOf("/");
                if (index > 0) {
                    packageStr = name.substring(0, index);
                    nameStr = name.substring(index + 1);
                }
                String path = FileUtil.toSystemIndependentName(rootPath + StringUtil.replace(packageStr, ".", "/"));
                outFile = getOrCreateFile(project, path, nameStr);
                outFile.setBinaryContent(content.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    protected static VirtualFile getOrCreateFile(Project project, String path, String fileName) throws IOException {
        if (!"/".equals(File.separator)) {
            path = path.replaceAll("/", File.separator);
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fullFileName = path + File.separator + fileName;
        file = new File(fullFileName);
        if (file.exists()) {
            return LocalFileSystem.getInstance().refreshAndFindFileByPath(fullFileName);
        }
        VirtualFile dir = LocalFileSystem.getInstance().refreshAndFindFileByPath(path);
        return dir.createChildData(project, fileName);
    }
}
