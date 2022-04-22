package com.weibo.breeze.plugin.commenter;

import com.intellij.lang.Commenter;
import org.jetbrains.annotations.Nullable;

/**
 * @author zhanglei28
 * @date 2021/12/9.
 */
public class BreezeCommenter implements Commenter {
  @Override
  public @Nullable String getLineCommentPrefix() {
    return "//";
  }

  @Override
  public @Nullable String getBlockCommentPrefix() {
    return "";
  }

  @Override
  public @Nullable String getBlockCommentSuffix() {
    return "";
  }

  @Override
  public @Nullable String getCommentedBlockCommentPrefix() {
    return "";
  }

  @Override
  public @Nullable String getCommentedBlockCommentSuffix() {
    return "";
  }
}
