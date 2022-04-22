package com.weibo.breeze.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.weibo.breeze.plugin.language.BreezeLanguage;
import org.jetbrains.annotations.NotNull;

/**
 * @author zhanglei28
 * @date 2021/12/7.
 */
public class BreezeParserDefinition implements ParserDefinition {
  public static final TokenSet WHITE_SPACES = TokenSet.WHITE_SPACE;
  public static final TokenSet COMMENTS = TokenSet.create(BreezeTypes.COMMENT);

  public static final IFileElementType FILE = new IFileElementType(BreezeLanguage.INSTANCE);

  @Override
  public @NotNull Lexer createLexer(Project project) {
    return new BreezeLexerAdapter();
  }

  @Override
  public @NotNull PsiParser createParser(Project project) {
    return new BreezeParser();
  }

  @Override
  public @NotNull IFileElementType getFileNodeType() {
    return FILE;
  }

  @Override
  public @NotNull TokenSet getCommentTokens() {
    return COMMENTS;
  }

  @Override
  public @NotNull TokenSet getWhitespaceTokens() {
    return WHITE_SPACES;
  }

  @Override
  public @NotNull SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
    return SpaceRequirements.MAY;
  }

  @Override
  public @NotNull TokenSet getStringLiteralElements() {
    return TokenSet.EMPTY;
  }

  @Override
  public @NotNull PsiElement createElement(ASTNode node) {
    return BreezeTypes.Factory.createElement(node);
  }

  @Override
  public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
    return new BreezeFile(viewProvider);
  }
}
