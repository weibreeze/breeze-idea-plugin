package com.weibo.breeze.plugin.psi;

import com.intellij.psi.tree.IElementType;
import com.weibo.breeze.plugin.language.BreezeLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author zhanglei28
 * @date 2021/12/7.
 */
public class BreezeTokenType extends IElementType {
  public BreezeTokenType(@NonNls @NotNull String debugName) {
    super(debugName, BreezeLanguage.INSTANCE);
  }

  @Override
  public String toString() {
    return "BreezeTokenType." + super.toString();
  }
}
