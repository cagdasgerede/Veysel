package jtdiff.antlr;

import org.antlr.v4.runtime.tree.ParseTree;

public class ParseTreeWrapper {
	private ParseTree mTree;
  private String mName;
  private int mPosition;

  public ParseTreeWrapper(ParseTree tree, String name, int position) {
    mTree = tree;
    mName = name;
    mPosition = position;
  }

  public String toString() {
    return mName;
  }

  public String name() {
    return mName;
  }

  public ParseTree parseTree() {
    return mTree;
  }

  public int position() {
    return mPosition;
  }
}