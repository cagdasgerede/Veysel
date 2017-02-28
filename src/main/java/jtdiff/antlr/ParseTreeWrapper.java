package jtdiff.antlr;

import org.antlr.v4.runtime.tree.ParseTree;

public class ParseTreeWrapper {
	private ParseTree mTree;
  private String mName;
  private int mPreorderPosition;

  public ParseTreeWrapper(ParseTree tree, String name, int preorderPosition) {
    mTree = tree;
    mName = name;
    mPreorderPosition = preorderPosition;
  }

  public String toString() {
    return mName;
  }

  public ParseTree parseTree() {
    return mTree;
  }

  public int preorderPosition() {
    return mPreorderPosition;
  }
}