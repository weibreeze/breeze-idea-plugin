// This is a generated file. Not intended for manual editing.
package com.weibo.breeze.plugin.psi;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.weibo.breeze.plugin.psi.BreezeTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class BreezeParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return breezeFile(b, l + 1);
  }

  /* ********************************************************** */
  // item_*
  static boolean breezeFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "breezeFile")) return false;
    while (true) {
      int c = current_position_(b);
      if (!item_(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "breezeFile", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // CONFIG_KEYWORD NAME SIGN_SECTION_START (innerOption SIGN_LINE_END)* SIGN_SECTION_END
  public static boolean config(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "config")) return false;
    if (!nextTokenIs(b, CONFIG_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, CONFIG_KEYWORD, NAME, SIGN_SECTION_START);
    r = r && config_3(b, l + 1);
    r = r && consumeToken(b, SIGN_SECTION_END);
    exit_section_(b, m, CONFIG, r);
    return r;
  }

  // (innerOption SIGN_LINE_END)*
  private static boolean config_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "config_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!config_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "config_3", c)) break;
    }
    return true;
  }

  // innerOption SIGN_LINE_END
  private static boolean config_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "config_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = innerOption(b, l + 1);
    r = r && consumeToken(b, SIGN_LINE_END);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ENUM_KEYWORD NAME options? SIGN_SECTION_START field* SIGN_SECTION_END
  public static boolean enum_$(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_$")) return false;
    if (!nextTokenIs(b, ENUM_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, ENUM_KEYWORD, NAME);
    r = r && enum_2(b, l + 1);
    r = r && consumeToken(b, SIGN_SECTION_START);
    r = r && enum_4(b, l + 1);
    r = r && consumeToken(b, SIGN_SECTION_END);
    exit_section_(b, m, ENUM, r);
    return r;
  }

  // options?
  private static boolean enum_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_2")) return false;
    options(b, l + 1);
    return true;
  }

  // field*
  private static boolean enum_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!field(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "enum_4", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // TYPE? PARAM_NAME SEPARATOR INDEX SIGN_LINE_END
  public static boolean field(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field")) return false;
    if (!nextTokenIs(b, "<field>", PARAM_NAME, TYPE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FIELD, "<field>");
    r = field_0(b, l + 1);
    r = r && consumeTokens(b, 0, PARAM_NAME, SEPARATOR, INDEX, SIGN_LINE_END);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // TYPE?
  private static boolean field_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field_0")) return false;
    consumeToken(b, TYPE);
    return true;
  }

  /* ********************************************************** */
  // KEY SEPARATOR VALUE
  public static boolean innerOption(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "innerOption")) return false;
    if (!nextTokenIs(b, KEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, KEY, SEPARATOR, VALUE);
    exit_section_(b, m, INNER_OPTION, r);
    return r;
  }

  /* ********************************************************** */
  // package|option|service|method|message|enum|config|COMMENT
  static boolean item_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item_")) return false;
    boolean r;
    r = package_$(b, l + 1);
    if (!r) r = option(b, l + 1);
    if (!r) r = service(b, l + 1);
    if (!r) r = method(b, l + 1);
    if (!r) r = message(b, l + 1);
    if (!r) r = enum_$(b, l + 1);
    if (!r) r = config(b, l + 1);
    if (!r) r = consumeToken(b, COMMENT);
    return r;
  }

  /* ********************************************************** */
  // MESSAGE_KEYWORD NAME options? SIGN_SECTION_START field* SIGN_SECTION_END
  public static boolean message(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "message")) return false;
    if (!nextTokenIs(b, MESSAGE_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, MESSAGE_KEYWORD, NAME);
    r = r && message_2(b, l + 1);
    r = r && consumeToken(b, SIGN_SECTION_START);
    r = r && message_4(b, l + 1);
    r = r && consumeToken(b, SIGN_SECTION_END);
    exit_section_(b, m, MESSAGE, r);
    return r;
  }

  // options?
  private static boolean message_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "message_2")) return false;
    options(b, l + 1);
    return true;
  }

  // field*
  private static boolean message_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "message_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!field(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "message_4", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // NAME '(' ((param) (SIGN_COMMA param)*)* ')' TYPE? SIGN_LINE_END
  public static boolean method(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method")) return false;
    if (!nextTokenIs(b, NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NAME);
    r = r && consumeToken(b, "(");
    r = r && method_2(b, l + 1);
    r = r && consumeToken(b, ")");
    r = r && method_4(b, l + 1);
    r = r && consumeToken(b, SIGN_LINE_END);
    exit_section_(b, m, METHOD, r);
    return r;
  }

  // ((param) (SIGN_COMMA param)*)*
  private static boolean method_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!method_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "method_2", c)) break;
    }
    return true;
  }

  // (param) (SIGN_COMMA param)*
  private static boolean method_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = method_2_0_0(b, l + 1);
    r = r && method_2_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (param)
  private static boolean method_2_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_2_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = param(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (SIGN_COMMA param)*
  private static boolean method_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_2_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!method_2_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "method_2_0_1", c)) break;
    }
    return true;
  }

  // SIGN_COMMA param
  private static boolean method_2_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_2_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SIGN_COMMA);
    r = r && param(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // TYPE?
  private static boolean method_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_4")) return false;
    consumeToken(b, TYPE);
    return true;
  }

  /* ********************************************************** */
  // OPTION_KEYWORD KEY SEPARATOR VALUE SIGN_LINE_END
  public static boolean option(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "option")) return false;
    if (!nextTokenIs(b, OPTION_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, OPTION_KEYWORD, KEY, SEPARATOR, VALUE, SIGN_LINE_END);
    exit_section_(b, m, OPTION, r);
    return r;
  }

  /* ********************************************************** */
  // '(' (innerOption)(SIGN_COMMA innerOption)* ')'
  public static boolean options(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "options")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, OPTIONS, "<options>");
    r = consumeToken(b, "(");
    r = r && options_1(b, l + 1);
    r = r && options_2(b, l + 1);
    r = r && consumeToken(b, ")");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (innerOption)
  private static boolean options_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "options_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = innerOption(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (SIGN_COMMA innerOption)*
  private static boolean options_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "options_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!options_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "options_2", c)) break;
    }
    return true;
  }

  // SIGN_COMMA innerOption
  private static boolean options_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "options_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SIGN_COMMA);
    r = r && innerOption(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PACKAGE_KEYWORD VALUE SIGN_LINE_END
  public static boolean package_$(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_$")) return false;
    if (!nextTokenIs(b, PACKAGE_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, PACKAGE_KEYWORD, VALUE, SIGN_LINE_END);
    exit_section_(b, m, PACKAGE, r);
    return r;
  }

  /* ********************************************************** */
  // TYPE PARAM_NAME
  public static boolean param(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param")) return false;
    if (!nextTokenIs(b, TYPE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, TYPE, PARAM_NAME);
    exit_section_(b, m, PARAM, r);
    return r;
  }

  /* ********************************************************** */
  // SERVICE_KEYWORD NAME options? SIGN_SECTION_START method* SIGN_SECTION_END
  public static boolean service(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "service")) return false;
    if (!nextTokenIs(b, SERVICE_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, SERVICE_KEYWORD, NAME);
    r = r && service_2(b, l + 1);
    r = r && consumeToken(b, SIGN_SECTION_START);
    r = r && service_4(b, l + 1);
    r = r && consumeToken(b, SIGN_SECTION_END);
    exit_section_(b, m, SERVICE, r);
    return r;
  }

  // options?
  private static boolean service_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "service_2")) return false;
    options(b, l + 1);
    return true;
  }

  // method*
  private static boolean service_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "service_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!method(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "service_4", c)) break;
    }
    return true;
  }

}
