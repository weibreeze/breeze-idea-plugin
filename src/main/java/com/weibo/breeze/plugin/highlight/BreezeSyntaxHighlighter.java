package com.weibo.breeze.plugin.highlight;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.weibo.breeze.plugin.psi.BreezeLexerAdapter;
import com.weibo.breeze.plugin.psi.BreezeTypes;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/**
 * @author zhanglei28
 * @date 2021/12/8.
 */
public class BreezeSyntaxHighlighter extends SyntaxHighlighterBase {
  public static final TextAttributesKey SEPARATOR =
      createTextAttributesKey("BREEZE_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
  public static final TextAttributesKey KEY =
      createTextAttributesKey("BREEZE_KEY", DefaultLanguageHighlighterColors.IDENTIFIER);
  public static final TextAttributesKey VALUE =
      createTextAttributesKey("BREEZE_VALUE", DefaultLanguageHighlighterColors.STRING);
  public static final TextAttributesKey COMMENT =
      createTextAttributesKey("BREEZE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
  public static final TextAttributesKey BAD_CHARACTER =
      createTextAttributesKey("BREEZE_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);
  public static final TextAttributesKey INDEX =
          createTextAttributesKey("BREEZE_INDEX", DefaultLanguageHighlighterColors.NUMBER);
  public static final TextAttributesKey KEYWORD =
          createTextAttributesKey("BREEZE_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
  public static final TextAttributesKey NAME =
          createTextAttributesKey("BREEZE_NAME", DefaultLanguageHighlighterColors.INSTANCE_METHOD);
  public static final TextAttributesKey PARAM_NAME =
          createTextAttributesKey("BREEZE_PARAM_NAME", DefaultLanguageHighlighterColors.IDENTIFIER);
  public static final TextAttributesKey TYPE =
          createTextAttributesKey("BREEZE_TYPE", DefaultLanguageHighlighterColors.CLASS_NAME);

  private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[] {BAD_CHARACTER};
  private static final TextAttributesKey[] SEPARATOR_KEYS = new TextAttributesKey[] {SEPARATOR};
  private static final TextAttributesKey[] KEY_KEYS = new TextAttributesKey[] {KEY};
  private static final TextAttributesKey[] VALUE_KEYS = new TextAttributesKey[] {VALUE};
  private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[] {COMMENT};
  private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEYWORD};
  private static final TextAttributesKey[] INDEX_KEYS = new TextAttributesKey[]{INDEX};
  private static final TextAttributesKey[] NAME_KEYS = new TextAttributesKey[]{NAME};
  private static final TextAttributesKey[] PARAM_NAME_KEYS = new TextAttributesKey[]{PARAM_NAME};
  private static final TextAttributesKey[] TYPE_KEYS = new TextAttributesKey[]{TYPE};
  private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

  @Override
  public @NotNull Lexer getHighlightingLexer() {
    return new BreezeLexerAdapter();
  }

  @Override
  public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
    if (tokenType.equals(BreezeTypes.SEPARATOR)) {
      return SEPARATOR_KEYS;
    }
    if (tokenType.equals(BreezeTypes.KEY)) {
      return KEY_KEYS;
    }
    if (tokenType.equals(BreezeTypes.VALUE)) {
      return VALUE_KEYS;
    }
    if (tokenType.equals(BreezeTypes.COMMENT)) {
      return COMMENT_KEYS;
    }
    if (tokenType.equals(BreezeTypes.PACKAGE_KEYWORD)
            || tokenType.equals(BreezeTypes.OPTION_KEYWORD)
            || tokenType.equals(BreezeTypes.SERVICE_KEYWORD)
            || tokenType.equals(BreezeTypes.MESSAGE_KEYWORD)
            || tokenType.equals(BreezeTypes.ENUM_KEYWORD)
            || tokenType.equals(BreezeTypes.CONFIG_KEYWORD)) {
      return KEYWORD_KEYS;
    }
    if (tokenType.equals(BreezeTypes.INDEX)) {
      return INDEX_KEYS;
    }
    if (tokenType.equals(BreezeTypes.NAME)) {
      return NAME_KEYS;
    }
    if (tokenType.equals(BreezeTypes.PARAM_NAME)) {
      return PARAM_NAME_KEYS;
    }
    if (tokenType.equals(BreezeTypes.TYPE)) {
      return TYPE_KEYS;
    }
    if (tokenType.equals(TokenType.BAD_CHARACTER)) {
      return BAD_CHAR_KEYS;
    }
    return EMPTY_KEYS;
  }
}
