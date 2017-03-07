package jtdiff.util;

/**
 * A visitor to be used in printing each node's debug information
 */
public class DebugVisitor extends Visitor {
	@Override
	public void enter(TreeNode node) {
		System.out.println(node.debugString());
	}

  @Override
  public void exit(TreeNode node) {}
}
