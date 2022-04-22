package com.weibo.breeze.plugin.formatter;

import com.intellij.formatting.*;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

import static com.weibo.breeze.plugin.language.BreezeLanguage.INSTANCE;
import static com.weibo.breeze.plugin.psi.BreezeTypes.*;

/**
 * @author zhanglei28
 * @date 2021/12/9.
 */
public class BreezeFormattingModelBuilder implements FormattingModelBuilder {
  private final TokenSet afterLineElements =
          TokenSet.create(
                  MESSAGE,
                  ENUM,
                  CONFIG,
                  SERVICE,
                  PACKAGE,
                  OPTION,
                  COMMENT,
                  SIGN_LINE_END,
                  SIGN_SECTION_START);
  private final TokenSet aroundLineElements = TokenSet.create(FIELD, METHOD);
  private final TokenSet beforeUnLineElements = TokenSet.create(SIGN_LINE_END, SIGN_SECTION_START);
  private final TokenSet aroundUnLineElements = TokenSet.create(OPTIONS, SIGN_COMMA);
  private final TokenSet keywords =
          TokenSet.create(
                  MESSAGE_KEYWORD,
                  ENUM_KEYWORD,
                  CONFIG_KEYWORD,
                  SERVICE_KEYWORD,
                  PACKAGE_KEYWORD,
                  OPTION_KEYWORD);
  private final TokenSet names = TokenSet.create(NAME, PARAM_NAME);
  private final TokenSet lineCommentTokens = TokenSet.orSet(afterLineElements, aroundLineElements); // can end with line comment

  @Override
  public @NotNull FormattingModel createModel(@NotNull FormattingContext formattingContext) {
    final CodeStyleSettings codeStyleSettings = formattingContext.getCodeStyleSettings();
    return FormattingModelProvider.createFormattingModelForPsiFile(
            formattingContext.getContainingFile(),
            new BreezeBlock(
                    formattingContext.getNode(),
                    Wrap.createWrap(WrapType.NONE, false),
                    Alignment.createAlignment(true),
                    createCommonSpaceBuilder(formattingContext.getCodeStyleSettings())),
            codeStyleSettings);
  }

  private SpacingBuilder createCommonSpaceBuilder(CodeStyleSettings settings) {
    CommonCodeStyleSettings commonCodeStyleSettings = settings.getCommonSettings(INSTANCE);
    commonCodeStyleSettings.KEEP_BLANK_LINES_IN_DECLARATIONS = 1;
    return new SpacingBuilder(settings, INSTANCE)
            .around(SEPARATOR)
            .spaceIf(settings.getCommonSettings(INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
            .after(keywords)
            .spaces(1)
            .after(SIGN_COMMA)
            .spaces(1)
            .between(TYPE, names)
            .spaces(1)
            .between(lineCommentTokens, COMMENT)
            .spaces(1)
            .after(afterLineElements)
            .blankLines(0)
            .around(aroundLineElements)
            .blankLines(0)
            .before(beforeUnLineElements)
            .blankLines(-1)
            .around(aroundUnLineElements)
            .blankLines(-1);
  }
}
