package com.weibo.breeze.plugin.setting;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author zhanglei28
 * @date 2022/3/23.
 */
public class BreezeSettingConfigurable implements Configurable {
    private BreezeSettingComponent breezeSettingComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Breeze Setting";
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return breezeSettingComponent.getPreferredFocusedComponent();
    }

    @Override
    public void reset() {
        BreezeSettingState settings = BreezeSettingState.getInstance();
        breezeSettingComponent.setGenerateUrlText(settings.breezeGenerateUrl);
        breezeSettingComponent.setGeneratedDirText(settings.generatedDir);
        breezeSettingComponent.setDefaultRegistryHostText(settings.defaultRegistryHost);
        breezeSettingComponent.setTargetLanguage(settings.targetLanguage);
    }

    @Override
    public void disposeUIResources() {
        breezeSettingComponent = null;
    }

    @Override
    public @Nullable JComponent createComponent() {
        breezeSettingComponent = new BreezeSettingComponent();
        return breezeSettingComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        BreezeSettingState settings = BreezeSettingState.getInstance();
        boolean modified = !breezeSettingComponent.getGenerateUrlText().equals(settings.breezeGenerateUrl);
        modified |= !breezeSettingComponent.getGeneratedDirText().equals(settings.generatedDir);
        modified |= !breezeSettingComponent.getDefaultRegistryHostText().equals(settings.defaultRegistryHost);
        modified |= !breezeSettingComponent.getTargetLanguage().equals(settings.targetLanguage);
        return modified;
    }

    @Override
    public void apply() {
        BreezeSettingState settings = BreezeSettingState.getInstance();
        settings.breezeGenerateUrl = breezeSettingComponent.getGenerateUrlText();
        settings.generatedDir = breezeSettingComponent.getGeneratedDirText();
        settings.defaultRegistryHost = breezeSettingComponent.getDefaultRegistryHostText();
        settings.targetLanguage = breezeSettingComponent.getTargetLanguage();
    }
}
