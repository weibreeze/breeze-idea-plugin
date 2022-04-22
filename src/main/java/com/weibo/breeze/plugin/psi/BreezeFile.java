package com.weibo.breeze.plugin.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.weibo.breeze.plugin.language.BreezeFileType;
import com.weibo.breeze.plugin.language.BreezeLanguage;
import org.jetbrains.annotations.NotNull;

/**
 * @author zhanglei28
 * @date 2021/12/7.
 */
public class BreezeFile extends PsiFileBase {
  protected BreezeFile(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, BreezeLanguage.INSTANCE);
  }

  @Override
  public @NotNull FileType getFileType() {
    return BreezeFileType.INSTANCE;
  }

  @Override
  public String toString() {
    return "Breeze File";
  }
}
