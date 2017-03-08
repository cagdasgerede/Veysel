package jtdiff.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jtdiff.util.TreeNode;

public class TestDepthComputation extends TestTree {
	@Test
	public void testTreeDepth() {
		assertEquals(3, treeOne.depth());
		assertEquals(3, treeTwo.depth());
		assertEquals(4, treeThree.depth());
	}

	@Test
	public void testNodeDepth() {
		assertEquals(1, treeThree.nodeAt(1, true).depth());
		assertEquals(2, treeThree.nodeAt(2, true).depth());
		assertEquals(2, treeThree.nodeAt(3, true).depth());
		assertEquals(3, treeThree.nodeAt(4, true).depth());
		assertEquals(4, treeThree.nodeAt(5, true).depth());
	}
}
