// This is a generated file. Not intended for manual editing.
package com.weibo.breeze.plugin.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.weibo.breeze.plugin.psi.impl.*;

public interface BreezeTypes {

  IElementType CONFIG = new BreezeElementType("CONFIG");
  IElementType ENUM = new BreezeElementType("ENUM");
  IElementType FIELD = new BreezeElementType("FIELD");
  IElementType INNER_OPTION = new BreezeElementType("INNER_OPTION");
  IElementType MESSAGE = new BreezeElementType("MESSAGE");
  IElementType METHOD = new BreezeElementType("METHOD");
  IElementType OPTION = new BreezeElementType("OPTION");
  IElementType OPTIONS = new BreezeElementType("OPTIONS");
  IElementType PACKAGE = new BreezeElementType("PACKAGE");
  IElementType PARAM = new BreezeElementType("PARAM");
  IElementType SERVICE = new BreezeElementType("SERVICE");

  IElementType COMMENT = new BreezeTokenType("COMMENT");
  IElementType CONFIG_KEYWORD = new BreezeTokenType("config");
  IElementType ENUM_KEYWORD = new BreezeTokenType("enum");
  IElementType INDEX = new BreezeTokenType("INDEX");
  IElementType KEY = new BreezeTokenType("KEY");
  IElementType MESSAGE_KEYWORD = new BreezeTokenType("message");
  IElementType NAME = new BreezeTokenType("NAME");
  IElementType OPTION_KEYWORD = new BreezeTokenType("option");
  IElementType PACKAGE_KEYWORD = new BreezeTokenType("package");
  IElementType PARAM_NAME = new BreezeTokenType("PARAM_NAME");
  IElementType SEPARATOR = new BreezeTokenType("SEPARATOR");
  IElementType SERVICE_KEYWORD = new BreezeTokenType("service");
  IElementType SIGN = new BreezeTokenType("SIGN");
  IElementType SIGN_COMMA = new BreezeTokenType("SIGN_COMMA");
  IElementType SIGN_LINE_END = new BreezeTokenType("SIGN_LINE_END");
  IElementType SIGN_SECTION_END = new BreezeTokenType("SIGN_SECTION_END");
  IElementType SIGN_SECTION_START = new BreezeTokenType("SIGN_SECTION_START");
  IElementType TYPE = new BreezeTokenType("TYPE");
  IElementType UNKNOWN_WORD = new BreezeTokenType("UNKNOWN_WORD");
  IElementType VALUE = new BreezeTokenType("VALUE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == CONFIG) {
        return new BreezeConfigImpl(node);
      }
      else if (type == ENUM) {
        return new BreezeEnumImpl(node);
      }
      else if (type == FIELD) {
        return new BreezeFieldImpl(node);
      }
      else if (type == INNER_OPTION) {
        return new BreezeInnerOptionImpl(node);
      }
      else if (type == MESSAGE) {
        return new BreezeMessageImpl(node);
      }
      else if (type == METHOD) {
        return new BreezeMethodImpl(node);
      }
      else if (type == OPTION) {
        return new BreezeOptionImpl(node);
      }
      else if (type == OPTIONS) {
        return new BreezeOptionsImpl(node);
      }
      else if (type == PACKAGE) {
        return new BreezePackageImpl(node);
      }
      else if (type == PARAM) {
        return new BreezeParamImpl(node);
      }
      else if (type == SERVICE) {
        return new BreezeServiceImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
