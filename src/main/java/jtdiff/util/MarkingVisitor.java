package jtdiff.util;

/**
 * A visitor to be used in marking preorder/postorder positions of
 * each tree node
 */
public class MarkingVisitor extends Visitor {
	private Tree tree;
	private int preorderPosition = 1;
	private int postorderPosition = 1;
	private int depth = 1;
	private int maxDepth = 0;

	
	public MarkingVisitor(Tree tree) {
		this.tree = tree;
	}
			
	@Override
	public void enter(TreeNode node) {
		tree.setNodeAtPreorderPosition(preorderPosition, node);
		preorderPosition++;

		node.setDepth(depth);
		if (depth > maxDepth) {
			maxDepth = depth;
		}
		depth++;
	}

	@Override
	public void exit(TreeNode node) {
		tree.setNodeAtPostorderPosition(postorderPosition, node);
		depth--;
		postorderPosition++;
	}

	public int maxDepth() {
		return maxDepth;
	}
}
