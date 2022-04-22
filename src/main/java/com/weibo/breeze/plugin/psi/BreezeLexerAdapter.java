package com.weibo.breeze.plugin.psi;

import com.intellij.lexer.FlexAdapter;

/**
 * @author zhanglei28
 * @date 2021/12/7.
 */
public class BreezeLexerAdapter extends FlexAdapter {
  public BreezeLexerAdapter() {
    super(new BreezeLexer(null));
  }
}
