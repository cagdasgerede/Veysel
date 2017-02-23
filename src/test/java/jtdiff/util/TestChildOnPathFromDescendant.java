package jtdiff.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jtdiff.util.TreeNode;

/**
 * Tests <code>childOnPathFromDescendant</code> method of <code>Tree</code>
 * class
 */
public class TestChildOnPathFromDescendant extends TestTree {
	/**
	 * When there is a node between ancestor and descendant
	 */
	@Test
	public void testSuccess() {
		TreeNode node = treeTwo.childOnPathFromDescendant(1, 4);
		assertEquals("C", node.label());
		assertEquals(3, node.preorderPosition());

		node = treeThree.childOnPathFromDescendant(1, 5);
		assertEquals("C", node.label());
		assertEquals(3, node.preorderPosition());

		node = treeThree.childOnPathFromDescendant(3, 5);
		assertEquals("D", node.label());
		assertEquals(4, node.preorderPosition());
	}

	/**
	 * No node between ancestor and descendant
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWhenNoSuchNode() {
		treeTwo.childOnPathFromDescendant(1, 1);
	}
}
