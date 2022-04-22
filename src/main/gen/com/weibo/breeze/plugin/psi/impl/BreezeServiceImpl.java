// This is a generated file. Not intended for manual editing.
package com.weibo.breeze.plugin.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.weibo.breeze.plugin.psi.BreezeTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.weibo.breeze.plugin.psi.*;
import com.weibo.breeze.plugin.utils.BreezePsiImplUtil;
import com.intellij.navigation.ItemPresentation;

public class BreezeServiceImpl extends ASTWrapperPsiElement implements BreezeService {

  public BreezeServiceImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull BreezeVisitor visitor) {
    visitor.visitService(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof BreezeVisitor) accept((BreezeVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<BreezeMethod> getMethodList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, BreezeMethod.class);
  }

  @Override
  @Nullable
  public BreezeOptions getOptions() {
    return findChildByClass(BreezeOptions.class);
  }

  @Override
  public String getName() {
    return BreezePsiImplUtil.getName(this);
  }

  @Override
  public ItemPresentation getPresentation() {
    return BreezePsiImplUtil.getPresentation(this);
  }

}
