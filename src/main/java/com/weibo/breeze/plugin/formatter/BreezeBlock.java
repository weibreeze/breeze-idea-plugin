package com.weibo.breeze.plugin.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import com.weibo.breeze.plugin.psi.BreezeElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.weibo.breeze.plugin.psi.BreezeTypes.*;

/**
 * @author zhanglei28
 * @date 2021/12/9.
 */
public class BreezeBlock extends AbstractBlock {
  private final SpacingBuilder spacingBuilder;

  protected BreezeBlock(
          @NotNull ASTNode node,
          @Nullable Wrap wrap,
          @Nullable Alignment alignment,
          @NotNull SpacingBuilder spacingBuilder) {
    super(node, wrap, alignment);
    this.spacingBuilder = spacingBuilder;
  }

  @Override
  protected List<Block> buildChildren() {
    List<Block> blocks = new ArrayList<>();
    ASTNode child = myNode.getFirstChildNode();
    while (child != null) {
      if (child.getElementType() != TokenType.WHITE_SPACE) {
        Block block =
                new BreezeBlock(
                        child,
                        Wrap.createWrap(WrapType.NONE, false),
                        Alignment.createAlignment(),
                        spacingBuilder);
        blocks.add(block);
      }
      child = child.getTreeNext();
    }
    return blocks;
  }

  @Override
  public Indent getIndent() {
    IElementType type = myNode.getElementType();
    ASTNode parent = myNode.getTreeParent();
    if (type == METHOD
            || type == FIELD
            || (type == COMMENT
            && parent != null
            && parent.getElementType() instanceof BreezeElementType)
            || (type == INNER_OPTION && parent != null && parent.getElementType() == CONFIG)) {
      return Indent.getNormalIndent();
    }
    return Indent.getNoneIndent();
  }

  @Override
  public @Nullable Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
    return spacingBuilder.getSpacing(this, child1, child2);
  }

  @Override
  public boolean isLeaf() {
    return myNode.getFirstChildNode() == null;
  }
}
