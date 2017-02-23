package jtdiff.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Tests <code>nodeAt</code> method of <code>Tree</code> class
 */
public class TestNodeAt extends TestTree {
	/**
	 * Testing getting a node from the tree by its preorder position
	 */
	@Test
	public void testNodeAt() {
		// Tree 1
		assertEquals("A", treeOne.nodeAt(1).label());
		assertEquals("B", treeOne.nodeAt(2).label());
		assertEquals("C", treeOne.nodeAt(3).label());
		assertNull(treeOne.nodeAt(4));
		
		// Tree 2
		assertEquals("A", treeTwo.nodeAt(1).label());
		assertEquals("B", treeTwo.nodeAt(2).label());
		assertEquals("C", treeTwo.nodeAt(3).label());
		assertEquals("D", treeTwo.nodeAt(4).label());
		assertNull(treeTwo.nodeAt(5));
	}
}
