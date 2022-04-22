package com.weibo.breeze.plugin.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;


/**
 * @author zhanglei28
 * @date 2021/11/24.
 */
public class BreezeGenWithMotanAction extends BreezeGenAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        generate(e, true);
    }
}
