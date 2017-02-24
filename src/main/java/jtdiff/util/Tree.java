package jtdiff.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
/**
 * Defines a tree structure of <code>TreeNode</code> elements
 */
public class Tree {
	private TreeNode root;

	private HashMap<Integer, TreeNode> preorderPositionToNode;

	public Tree(TreeNode root) {
		this.root = root;
		this.preorderPositionToNode = new HashMap<>();
	}

	/**
	 * @return the number of nodes in the tree
	 */
	public int size() {
		return preorderPositionToNode.size();
	}

	/**
	 * Builds the cached preorder positions of the nodes in the tree. Call this
	 * method after the tree structure is finalized
	 */
	public void buildCaches() {
		Visitor visitor = new PreOrderMarkingVisitor(this);
		this.performPreorderTraversal(visitor);
	}

	/**
	 * Performs a preorder traversal on the tree
	 * @param visitor
	 *            is used for taking an action on the visited node (the visitor
	 *            must have a visit method)
	 */
	public void performPreorderTraversal(Visitor visitor) {
		this.root.preorderTraversal(visitor);
	}

	/**
	 * Does a preorder traversal and prints the node labels
	 */
	public void printPreorderTraversal() {
		Visitor visitor = new DebugVisitor();
		this.performPreorderTraversal(visitor);
	}

	/**
	 * @param preorderPosition
	 * @return the <code>TreeNode</code> node at the given preorder position
	 */
	public TreeNode nodeAt(int preorderPosition) {
		return this.preorderPositionToNode.get(preorderPosition);
	}

	/**
	 * Marks the node to be in the given preorder position in the tree
	 * @param preorderPosition
	 * @param node
	 */
	public void setNodeAt(int preorderPosition, TreeNode node) {
		this.preorderPositionToNode.put(preorderPosition, node);
		node.setPreorderPosition(preorderPosition);
	}

	/**
	 * @param preorderPosition
	 * @return the father of the node at the given preorder position
	 * @throws IllegalArgumentException
	 */
	public TreeNode fatherOf(int preorderPosition) 
			throws IllegalArgumentException {
		TreeNode node = this.preorderPositionToNode.get(preorderPosition);
		if (node != null) {
			return node.father();
		}
		throw new IllegalArgumentException(
				"No node at the given position: " + preorderPosition);
	}

	/**
	 * Produces ancestor positions towards the root starting from the given
	 * position
	 * @param startingPreorderPosition
	 * @return preorder positions of the nodes along the path from the node at
	 *         the given position to the root
	 * @throws IllegalArgumentException
	 */
	public ArrayList<Integer> ancestors(int startingPreorderPosition) 
			throws IllegalArgumentException {
		ArrayList<Integer> positions = new ArrayList<>();

		if (startingPreorderPosition > this.preorderPositionToNode.size()) {
			throw new IllegalArgumentException(
					"No node at the given position: " 
							+ startingPreorderPosition);
		}

		positions.add(startingPreorderPosition);
		while (true) {
			TreeNode node = this.fatherOf(startingPreorderPosition);
			if (node != null) {
				startingPreorderPosition = node.preorderPosition();
				positions.add(startingPreorderPosition);
			} else {
				break;
			}
		}
		return positions;
	}

	/**
	 * Finds a child node between the parent and the descendant.
	 * @param parentPosition
	 * @param descendantPosition
	 * @return the child of a node which is on the path from a descendant of 
	 * 		   the node to the node (there can only be one such child). 
	 *         Returns null if no such node.
	 * @throws IllegalArgumentException
	 *             if no node found at the <code>descendantPosition</code>
	 */
	public TreeNode childOnPathFromDescendant(int parentPosition, 
			int descendantPosition) throws IllegalArgumentException {
		TreeNode currentChildNode = this.nodeAt(descendantPosition);
		TreeNode fatherNode = currentChildNode.father();

		if (fatherNode == null) {
			throw new IllegalArgumentException(
					"No father node at the given position: " 
							+ descendantPosition);
		}

		while (fatherNode.preorderPosition() != parentPosition) {
			currentChildNode = fatherNode;
			fatherNode = currentChildNode.father();
			if (fatherNode == null) {
				return null;
			}
		}
		return currentChildNode;
	}

	@Override
	public String toString() {
		List<String> stringList = new ArrayList<>();
		Visitor visitor = new Visitor() {
			@Override
			public void visit(TreeNode node) {
				stringList.add(node.debugString());
			}
		};
		performPreorderTraversal(visitor);
		return String.join("\n", stringList);
	}

  @Override
	public boolean equals(Object o) {
		if (o instanceof Tree) {
			Tree t = (Tree) o;
			return toString().equals(t.toString());
		}
		return super.equals(o);
	}
}
