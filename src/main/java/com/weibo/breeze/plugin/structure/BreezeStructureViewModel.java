package com.weibo.breeze.plugin.structure;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiFile;
import com.weibo.breeze.plugin.psi.BreezeOption;
import com.weibo.breeze.plugin.psi.BreezePackage;
import org.jetbrains.annotations.NotNull;

/**
 * @author zhanglei28
 * @date 2021/12/9.
 */
public class BreezeStructureViewModel extends StructureViewModelBase
        implements StructureViewModel.ElementInfoProvider {
  public BreezeStructureViewModel(@NotNull PsiFile psiFile) {
    super(psiFile, new BreezeStructureViewElement(psiFile));
  }

  @Override
  public Sorter @NotNull [] getSorters() {
    return new Sorter[]{Sorter.ALPHA_SORTER};
  }

  @Override
  public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
    return false;
  }

  @Override
  public boolean isAlwaysLeaf(StructureViewTreeElement element) {
    return (element.getValue() instanceof BreezeOption)
            || (element.getValue() instanceof BreezePackage);
  }
}
