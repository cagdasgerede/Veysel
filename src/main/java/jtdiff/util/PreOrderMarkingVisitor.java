package jtdiff.util;

/**
 * A visitor to be used in marking preorder positions of each tree node
 */
public class PreOrderMarkingVisitor extends Visitor {
	private Tree tree;
	int position = 1;
	
	public PreOrderMarkingVisitor(Tree tree) {
		this.tree = tree;
	}
			
	@Override
	public void visit(TreeNode node) {
		tree.setNodeAt(position, node);
		position += 1;		
	}	
}
