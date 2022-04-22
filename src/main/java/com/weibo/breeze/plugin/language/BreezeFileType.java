package com.weibo.breeze.plugin.language;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author zhanglei28
 * @date 2021/12/6.
 */
public class BreezeFileType extends LanguageFileType {
  public static final BreezeFileType INSTANCE = new BreezeFileType();

  private BreezeFileType() {
    super(BreezeLanguage.INSTANCE);
  }

  @Override
  public @NonNls @NotNull String getName() {
    return "Breeze File";
  }

  @Override
  public @NotNull String getDescription() {
    return "Breeze file";
  }

  @Override
  public @NotNull String getDefaultExtension() {
    return "breeze";
  }

  @Override
  public @Nullable Icon getIcon() {
    return BreezeIcons.FILE;
  }
}
