package jtdiff.util;

/**
 * A visitor to be used in marking preorder positions of each tree node
 */
public class PreOrderMarkingVisitor extends Visitor {
	private Tree tree;
	private int position = 1;
	private int depth = 1;
	private int maxDepth = 0;

	
	public PreOrderMarkingVisitor(Tree tree) {
		this.tree = tree;
	}
			
	@Override
	public void enter(TreeNode node) {
		tree.setNodeAt(position, node);
		position += 1;

		node.setDepth(depth);
		if (depth > maxDepth) {
			maxDepth = depth;
		}
		depth++;
	}

	@Override
	public void exit(TreeNode node) {
		depth--;
	}

	public int maxDepth() {
		return maxDepth;
	}
}
