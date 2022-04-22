// This is a generated file. Not intended for manual editing.
package com.weibo.breeze.plugin.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.navigation.ItemPresentation;

public interface BreezeMethod extends PsiElement {

  @NotNull
  List<BreezeParam> getParamList();

  String getReturnType();

  String getName();

  ItemPresentation getPresentation();

}
