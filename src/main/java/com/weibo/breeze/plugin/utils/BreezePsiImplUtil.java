package com.weibo.breeze.plugin.utils;

import com.intellij.icons.AllIcons;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.weibo.breeze.plugin.psi.*;

import javax.swing.*;

/**
 * @author zhanglei28
 * @date 2021/12/8.
 */
public class BreezePsiImplUtil {

  public static String getKey(BreezeInnerOption element) {
    return getByTypes(element, BreezeTypes.KEY, "\\\\ ");
  }

  public static String getValue(BreezeInnerOption element) {
    return getByTypes(element, BreezeTypes.VALUE);
  }

  public static String getKey(BreezeOption element) {
    return getByTypes(element, BreezeTypes.KEY, "\\\\ ");
  }

  public static String getValue(BreezeOption element) {
    return getByTypes(element, BreezeTypes.VALUE);
  }

  public static String getPackage(BreezePackage element) {
    return getByTypes(element, BreezeTypes.VALUE);
  }

  public static String getType(BreezeParam element) {
    return getByTypes(element, BreezeTypes.TYPE);
  }

  public static String getName(BreezeParam element) {
    return getByTypes(element, BreezeTypes.NAME);
  }

  public static String getName(BreezeMethod element) {
    return getByTypes(element, BreezeTypes.NAME);
  }

  public static String getReturnType(BreezeMethod element) {
    return getByTypes(element, BreezeTypes.TYPE);
  }

  public static String getName(BreezeService element) {
    return getByTypes(element, BreezeTypes.NAME);
  }

  public static String getType(BreezeField element) {
    return getByTypes(element, BreezeTypes.TYPE);
  }

  public static String getName(BreezeField element) {
    return getByTypes(element, BreezeTypes.PARAM_NAME);
  }

  public static String getIndex(BreezeField element) {
    return getByTypes(element, BreezeTypes.INDEX);
  }

  public static String getName(BreezeMessage element) {
    return getByTypes(element, BreezeTypes.NAME);
  }

  public static String getName(BreezeEnum element) {
    return getByTypes(element, BreezeTypes.NAME);
  }

  public static String getName(BreezeConfig element) {
    return getByTypes(element, BreezeTypes.NAME);
  }

  public static String getByTypes(PsiElement element, IElementType type) {
    return getByTypes(element, type, null);
  }

  private static String getByTypes(PsiElement element, IElementType type, String replace) {
    ASTNode node = element.getNode().findChildByType(type);
    if (node != null) {
      if (replace == null) {
        return node.getText();
      } else {
        return node.getText().replace(replace, " ");
      }
    }
    return null;
  }

  // inner option : option in brackets, or option in config.
  public static ItemPresentation getPresentation(final BreezeInnerOption element) {
    return new ItemPresentation() {
      @Override
      public @NlsSafe String getPresentableText() {
        return element.getKey() + ": \"" + element.getValue() + "\"";
      }

      @Override
      public Icon getIcon(boolean unused) {
        return AllIcons.Nodes.Property;
      }
    };
  }

  // breeze `option` keywords
  public static ItemPresentation getPresentation(final BreezeOption element) {
    return new ItemPresentation() {
      @Override
      public @NlsSafe String getPresentableText() {
        return element.getKey() + ": \"" + element.getValue() + "\"";
      }

      @Override
      public Icon getIcon(boolean unused) {
        return AllIcons.Nodes.Property;
      }
    };
  }

  // breeze field in message or enum
  public static ItemPresentation getPresentation(final BreezeField element) {
    return new ItemPresentation() {
      @Override
      public @NlsSafe String getPresentableText() {
        String typeString = element.getType();
        if (typeString == null) { // enum field
          return element.getName() + " = " + element.getIndex();
        }
        // message field
        return element.getName() + ": " + element.getType() + " = " + element.getIndex();
      }

      @Override
      public Icon getIcon(boolean unused) {
        return AllIcons.Nodes.Field;
      }
    };
  }

  // breeze method in service
  public static ItemPresentation getPresentation(final BreezeMethod element) {
    return new ItemPresentation() {
      @Override
      public @NlsSafe String getPresentableText() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(element.getName()).append("(");
        boolean hasParam = false;
        for (BreezeParam param : element.getParamList()) {
          hasParam = true;
          stringBuilder.append(param.getType()).append(", ");
        }
        if (hasParam) {
          stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }
        stringBuilder.append("):").append(element.getReturnType() != null ? element.getReturnType() : "void");
        return stringBuilder.toString();
      }

      @Override
      public Icon getIcon(boolean unused) {
        return AllIcons.Nodes.Method;
      }
    };
  }

  // breeze `enum` keywords
  public static ItemPresentation getPresentation(final BreezeEnum element) {
    return new ItemPresentation() {
      @Override
      public @NlsSafe String getPresentableText() {
        return element.getName();
      }

      @Override
      public Icon getIcon(boolean unused) {
        return AllIcons.Nodes.Enum;
      }
    };
  }

  // breeze `message` keywords
  public static ItemPresentation getPresentation(final BreezeMessage element) {
    return new ItemPresentation() {
      @Override
      public @NlsSafe String getPresentableText() {
        return element.getName();
      }

      @Override
      public Icon getIcon(boolean unused) {
        return AllIcons.Nodes.Class;
      }
    };
  }

  // breeze `config` keywords
  public static ItemPresentation getPresentation(final BreezeConfig element) {
    return new ItemPresentation() {
      @Override
      public @NlsSafe String getPresentableText() {
        return element.getName();
      }

      @Override
      public Icon getIcon(boolean unused) {
        return AllIcons.Nodes.Class;
      }
    };
  }

  // breeze `config` keywords
  public static ItemPresentation getPresentation(final BreezeService element) {
    return new ItemPresentation() {
      @Override
      public @NlsSafe String getPresentableText() {
        return element.getName();
      }

      @Override
      public Icon getIcon(boolean unused) {
        return AllIcons.Nodes.Class;
      }
    };
  }
}
