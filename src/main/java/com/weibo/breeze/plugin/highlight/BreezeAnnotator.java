package com.weibo.breeze.plugin.highlight;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.weibo.breeze.plugin.psi.*;
import com.weibo.breeze.plugin.utils.BreezePsiImplUtil;
import org.jetbrains.annotations.NotNull;

import static com.weibo.breeze.plugin.psi.BreezeTypes.*;

/**
 * @author zhanglei28
 * @date 2022/4/1.
 */
public class BreezeAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof BreezeMessage
                || element instanceof BreezeEnum
                || element instanceof BreezeService
                || element instanceof BreezeConfig
                || element instanceof BreezeMethod) {
            if (sameByType(element, NAME)) { // same name message
                errorAnnotation(holder, element.getNode().getElementType() + " `" + BreezePsiImplUtil.getByTypes(element, NAME) + "` is already defined.");
            }
        } else if (element instanceof BreezeField) {
            if (sameByType(element, PARAM_NAME)) {
                errorAnnotation(holder, "field name `" + BreezePsiImplUtil.getByTypes(element, PARAM_NAME) + "` is already defined.");
            }
            if (sameByType(element, INDEX)) {
                errorAnnotation(holder, "field index `" + BreezePsiImplUtil.getByTypes(element, INDEX) + "` is already defined.");
            }
        } else if (element instanceof BreezeInnerOption) {
            if (sameByType(element, KEY)) {
                errorAnnotation(holder, "config key `" + BreezePsiImplUtil.getByTypes(element, KEY) + "` is already defined.");

            }
        }
    }

    //find same type string at the same tree level
    private boolean sameByType(PsiElement element, IElementType propertyType) {
        String typeString = BreezePsiImplUtil.getByTypes(element, propertyType);
        IElementType elementType = element.getNode().getElementType();
        PsiElement[] elems = element.getParent().getChildren(); // all elements in the same tree level
        for (PsiElement e : elems) {
            if (e != element
                    && e.getNode().getElementType() == elementType
                    && typeString.equals(BreezePsiImplUtil.getByTypes(e, propertyType))) {
                return true;
            }
        }
        return false;
    }

    private void errorAnnotation(AnnotationHolder holder, String info) {
        holder.newAnnotation(HighlightSeverity.ERROR, info)
                .highlightType(ProblemHighlightType.GENERIC_ERROR)
                .create();
    }
}
