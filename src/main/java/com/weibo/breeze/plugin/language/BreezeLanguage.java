package com.weibo.breeze.plugin.language;

import com.intellij.lang.Language;

/**
 * @author zhanglei28
 * @date 2021/12/6.
 */
public class BreezeLanguage extends Language {
  public static final BreezeLanguage INSTANCE = new BreezeLanguage();

  private BreezeLanguage() {
    super("Breeze");
  }
}
