package jtdiff.util;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Single node in a tree
 */
public class TreeNode {
	// Label of the current node
	private String label;

	// List of TreeNode instances
	private ArrayList<TreeNode> children;

	// The father of the current node
	private TreeNode father = null;

	// The cached positions of the current node in pre/post order traversals
	private int preorderPosition = -1;
	private int postorderPosition = -1;

	// The cached depth of the current node
	private int depth = -1;

	public TreeNode(String label) {
		this.setLabel(label);
		this.children = new ArrayList<TreeNode>();
	}

	/**
	 * @return the label of this node
	 */
	public String label() {
		return label;
	}

	/**
	 * Sets the label of this node
	 * @param label representing new label to be set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Returns the father of the node
	 * @return <code>TreeNode</code> object representing the father of the node
	 */
	public TreeNode father() {
		return father;
	}

	/**
	 * Sets the father of the node
	 * @param father is an instance of <code>TreeNode</code>
	 */
	public void setFather(TreeNode father) {
		this.father = father;
	}

	/**
	 * Returns an iterator for iterating TreeNode children
	 * 
	 * @return Iterator in <code>TreeNode</code> type
	 */
	public Iterator<TreeNode> children() {
		return this.children.iterator();
	}

	/**
	 * Adds a new child to the node
	 * 
	 * @param node
	 *            is a <code>TreeNode</code> object
	 */
	public void addChild(TreeNode node) {
		this.children.add(node);
		node.setFather(this);
	}

	/**
	 * Returns the position of the node in the preorder traversal
	 * @return int value representing preorder position
	 */
	public int preorderPosition() {
		return preorderPosition;
	}

	/**
	 * Returns the position of the node in the postorder traversal
	 * @return int value representing postorder position
	 */
	public int postorderPosition() {
		return postorderPosition;
	}

	/**
	 * Sets the preorder traversal position
	 * @param newPosition for new preorder position
	 */
	public void setPreorderPosition(int newPosition) {
		preorderPosition = newPosition;
	}

	/**
	 * Sets the postorder traversal position
	 * @param newPosition for new postorder position
	 */
	public void setPostorderPosition(int newPosition) {
		postorderPosition = newPosition;
	}

	/**
	 * @param depth for new depth
	 */
	public void setDepth(int depth) {
		this.depth = depth;		
	}

	/**
	 * @return int value representing the depth of the node (depth of root is 1)
	 */
	public int depth() {
		return depth;
	}

	/**
	 * Does a traversal of the subtree rooted at this node
	 * @param visitor
	 *            should have a <code>visit</code> method accepting a
	 *            <code>TreeNode</code>
	 */
	public void visit(Visitor visitor) {
		visitor.enter(this);
		for (TreeNode child : this.children) {
			child.visit(visitor);
		}
		visitor.exit(this);
	}

	/**
	 * @return a <code>String</code> representation for this node for debugging
	 */
	public String debugString() {
		return String.format(
				"label: %s, preorder_position: %d, postorder_position: %d",
				label,
				preorderPosition,
				postorderPosition);
	}
}
