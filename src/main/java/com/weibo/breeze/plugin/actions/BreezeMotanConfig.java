package com.weibo.breeze.plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.weibo.breeze.plugin.psi.BreezeConfig;
import com.weibo.breeze.plugin.setting.BreezeSettingState;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * generate basic motan config
 *
 * @author zhanglei28
 * @date 2022/3/22.
 */
public class BreezeMotanConfig extends AnAction {
    private static final byte[] templateBytesPart1 = ("\n\nconfig MotanBasicConfig{\n    // common config\n").getBytes(StandardCharsets.UTF_8);
    private static final byte[] templateBytesPart2 = ("    default.registry.address = ${replacedMe};\n").getBytes(StandardCharsets.UTF_8);
    private static final byte[] templateBytesPart3 = ("    default.protocol.name = motan2; //which rpc protocol will be used\n" +
            "    default.protocol.serialization = breeze;\n" +
            "\n" +
            "    // server config\n" +
            "    default.protocol.maxWorkerThread = 1000;\n" +
            "    service.group = ${replacedMe}; //rpc group of server end\n" +
            "    service.application = ${replacedMe}; //rpc application of server end\n" +
            "    service.export = :8002; //:${export port} or ${protocol.id}:${export port}\n" +
            "\n" +
            "    // client config\n" +
            "    default.protocol.loadbalance = configurableWeight;\n" +
            "    default.protocol.haStrategy = failover;\n" +
            "    referer.group = ${replacedMe}; //rpc group of server end\n" +
            "    referer.application = ${replacedMe}; //rpc application of server end\n" +
            "    referer.requestTimeout = 1000; //rpc request time out\n" +
            "}").getBytes(StandardCharsets.UTF_8);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        DataContext context = e.getDataContext();
        PsiFile psiFile = context.getData(PlatformDataKeys.PSI_FILE);
        List<BreezeConfig> configs = PsiTreeUtil.getChildrenOfTypeAsList(psiFile, BreezeConfig.class);
        for (BreezeConfig config : configs) {
            if ("MotanBasicConfig".equals(config.getName())) {
                Messages.showMessageDialog("Config name `MotanBasicConfig` already exist!", "Generate Motan Config Fail", Messages.getInformationIcon());
                return;
            }
        }

        ApplicationManager.getApplication().runWriteAction(() -> {
            VirtualFile file = e.getData(PlatformDataKeys.VIRTUAL_FILE);
            if (file != null) {
                try {
                    byte[] contents = file.contentsToByteArray();
                    byte[] part2 = templateBytesPart2;
                    if (StringUtils.isNotBlank(BreezeSettingState.getInstance().defaultRegistryHost)) {
                        part2 = ("    default.registry.address = " + BreezeSettingState.getInstance().defaultRegistryHost.trim() + ";\n").getBytes(StandardCharsets.UTF_8);
                    }
                    ByteBuffer buffer = ByteBuffer.allocate(contents.length + templateBytesPart1.length + part2.length + templateBytesPart3.length);
                    buffer.put(contents).put(templateBytesPart1).put(part2).put(templateBytesPart3);
                    file.setBinaryContent(buffer.array());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void update(AnActionEvent e) {
        VirtualFile file = PlatformDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
        boolean enable = file != null && "breeze".equals(file.getExtension());
        e.getPresentation().setEnabledAndVisible(enable);
    }
}
