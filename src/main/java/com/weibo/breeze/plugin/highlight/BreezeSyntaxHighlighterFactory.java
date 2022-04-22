package com.weibo.breeze.plugin.highlight;

import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author zhanglei28
 * @date 2021/12/8.
 */
public class BreezeSyntaxHighlighterFactory extends SyntaxHighlighterFactory {
  @Override
  public @NotNull SyntaxHighlighter getSyntaxHighlighter(
      @Nullable Project project, @Nullable VirtualFile virtualFile) {
    return new BreezeSyntaxHighlighter();
  }
}
