package com.weibo.breeze.plugin.setting;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author zhanglei28
 * @date 2022/3/23.
 */
@State(
        name = "com.weibo.breeze.plugin.setting.BreezeSettingState",
        storages = @Storage("BreezePlugin.xml")
)
public class BreezeSettingState implements PersistentStateComponent<BreezeSettingState> {
    public String breezeGenerateUrl;
    public String generatedDir; // 生成代码到指定路径。 为空时默认生成为项目类代码，指定后生成到对应目录
    public String defaultRegistryHost; // 默认注册中心的地址
    public String targetLanguage; // 生成代码到语言类型。默认按project sdk来判断语言类型，如果指定则生成指定语言代码

    public static BreezeSettingState getInstance() {
        return ApplicationManager.getApplication().getService(BreezeSettingState.class);
    }

    @Override
    public @Nullable BreezeSettingState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull BreezeSettingState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
