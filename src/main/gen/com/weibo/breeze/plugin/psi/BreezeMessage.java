// This is a generated file. Not intended for manual editing.
package com.weibo.breeze.plugin.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.navigation.ItemPresentation;

public interface BreezeMessage extends PsiElement {

  @NotNull
  List<BreezeField> getFieldList();

  @Nullable
  BreezeOptions getOptions();

  String getName();

  ItemPresentation getPresentation();

}
