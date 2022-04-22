package com.weibo.breeze.plugin.actions;

import com.intellij.ide.actions.ShowSettingsUtilImpl;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author zhanglei28
 * @date 2022/3/22.
 */
public class BreezeConfig extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ShowSettingsUtilImpl.showSettingsDialog(e.getProject(), "com.weibo.breeze.plugin.setting.BreezeSettingConfigurable", null);
    }
}
