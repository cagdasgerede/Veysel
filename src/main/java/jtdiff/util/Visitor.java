package jtdiff.util;

/**
 * A visitor class to apply the visitor pattern for navigating the tree
 */
abstract public class Visitor {
	abstract public void enter(TreeNode node);
  abstract public void exit(TreeNode node);
}
