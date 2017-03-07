package jtdiff.util;

import java.util.ArrayList;
import org.junit.Before;
import jtdiff.util.Tree;
import jtdiff.util.TreeNode;
import jtdiff.util.Visitor;

/**
 * Builds an array of node labels it visits
 */
class TraversalVisitor extends Visitor {
	private ArrayList<String> traversal = new ArrayList<>();

	@Override
	public void enter(TreeNode node) {
		traversal.add(node.debugString());

	}

	@Override
	public void exit(TreeNode node) {}

	public String[] getTraversal() {
		return traversal.toArray(new String[traversal.size()]);
	}
}

/**
 * Builds the <code>Tree</code> objects to be used by its inherited classes
 */
public class TestTree {
	protected Tree treeOne, treeTwo, treeThree;

	@Before
	public void setUp() throws Exception {
		TreeNode aNode = new TreeNode("A");
		TreeNode bNode = new TreeNode("B");
		aNode.addChild(bNode);
		TreeNode cNode = new TreeNode("C");
		bNode.addChild(cNode);
		treeOne = new Tree(aNode);
		treeOne.buildCaches();

		aNode = new TreeNode("A");
		bNode = new TreeNode("B");
		cNode = new TreeNode("C");
		TreeNode dNode = new TreeNode("D");
		aNode.addChild(bNode);
		aNode.addChild(cNode);
		cNode.addChild(dNode);
		treeTwo = new Tree(aNode);
		treeTwo.buildCaches();

		aNode = new TreeNode("A");
		bNode = new TreeNode("B");
		cNode = new TreeNode("C");
		dNode = new TreeNode("D");
		TreeNode eNode = new TreeNode("E");
		aNode.addChild(bNode);
		aNode.addChild(cNode);
		cNode.addChild(dNode);
		dNode.addChild(eNode);
		treeThree = new Tree(aNode);
		treeThree.buildCaches();
	}
}
