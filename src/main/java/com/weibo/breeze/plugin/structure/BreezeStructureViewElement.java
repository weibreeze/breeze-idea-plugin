package com.weibo.breeze.plugin.structure;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.weibo.breeze.plugin.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanglei28
 * @date 2021/12/9.
 */
public class BreezeStructureViewElement implements StructureViewTreeElement, SortableTreeElement {
  private static final Class[] FILE_CHILDREN_TYPES = new Class[]{
          BreezeOption.class, BreezeMessage.class,
          BreezeEnum.class, BreezeService.class, BreezeConfig.class};
  private final NavigatablePsiElement element;

  public BreezeStructureViewElement(@NotNull NavigatablePsiElement element) {
    this.element = element;
  }

  @Override
  public Object getValue() {
    return element;
  }

  @Override
  public @NotNull String getAlphaSortKey() {
    String name = element.getName();
    return name == null ? "" : name;
  }

  @Override
  public @NotNull ItemPresentation getPresentation() {
    ItemPresentation presentation = element.getPresentation();
    return presentation == null ? new PresentationData() : presentation;
  }

  @Override
  public TreeElement @NotNull [] getChildren() {
    if (element instanceof BreezeFile) {
      return getFileChildren();
    } else if (element instanceof BreezeConfig) {
      BreezeConfig config = (BreezeConfig) element;
      return getChildren(null, config.getInnerOptionList());
    } else if (element instanceof BreezeService) {
      BreezeService service = (BreezeService) element;
      return getChildren(service.getOptions(), service.getMethodList());
    } else if (element instanceof BreezeMessage) {
      BreezeMessage message = (BreezeMessage) element;
      return getChildren(message.getOptions(), message.getFieldList());
    } else if (element instanceof BreezeEnum) {
      BreezeEnum message = (BreezeEnum) element;
      return getChildren(message.getOptions(), message.getFieldList());
    }
    return EMPTY_ARRAY;
  }

  private <T extends PsiElement> TreeElement @NotNull [] getChildren(BreezeOptions options, @NotNull List<T> fields) {
    List<TreeElement> treeElements = new ArrayList<>();
    if (options != null) {
      for (BreezeInnerOption option : options.getInnerOptionList()) {
        treeElements.add(new BreezeStructureViewElement((NavigatablePsiElement) option));
      }
    }
    for (T field : fields) {
      treeElements.add(new BreezeStructureViewElement((NavigatablePsiElement) field));
    }
    return treeElements.toArray(new TreeElement[0]);
  }

  private TreeElement @NotNull [] getFileChildren() {
    List<TreeElement> treeElements = new ArrayList<>();
    for (Class clazz : FILE_CHILDREN_TYPES) {
      addChildrenByType(treeElements, clazz);
    }
    return treeElements.toArray(new TreeElement[0]);
  }

  private <T extends PsiElement> void addChildrenByType(List<TreeElement> treeElements, Class<T> clazz) {
    List<T> children =
            PsiTreeUtil.getChildrenOfTypeAsList(element, clazz);
    for (T child : children) {
      treeElements.add(new BreezeStructureViewElement((NavigatablePsiElement) child));
    }
  }

  @Override
  public void navigate(boolean requestFocus) {
    element.navigate(requestFocus);
  }

  @Override
  public boolean canNavigate() {
    return element.canNavigate();
  }

  @Override
  public boolean canNavigateToSource() {
    return element.canNavigateToSource();
  }
}
