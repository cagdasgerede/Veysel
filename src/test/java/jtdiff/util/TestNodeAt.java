package jtdiff.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Tests <code>nodeAt</code> method of <code>Tree</code> class
 */
public class TestNodeAt extends TestTree {
	@Test
	public void testNodeAtPreorderPosition() {
		// Tree 1
		assertEquals("A", treeOne.nodeAt(1, true).label());
		assertEquals("B", treeOne.nodeAt(2, true).label());
		assertEquals("C", treeOne.nodeAt(3, true).label());
		assertNull(treeOne.nodeAt(4, true));
		
		// Tree 2
		assertEquals("A", treeTwo.nodeAt(1, true).label());
		assertEquals("B", treeTwo.nodeAt(2, true).label());
		assertEquals("C", treeTwo.nodeAt(3, true).label());
		assertEquals("D", treeTwo.nodeAt(4, true).label());
		assertNull(treeTwo.nodeAt(5, true));
	}

	@Test
	public void testNodeAtPostorderPosition() {
		// Tree 1
		assertEquals("A", treeOne.nodeAt(3, false).label());
		assertEquals("B", treeOne.nodeAt(2, false).label());
		assertEquals("C", treeOne.nodeAt(1, false).label());
		assertNull(treeOne.nodeAt(4, false));
		
		// Tree 2
		assertEquals("A", treeTwo.nodeAt(4, false).label());
		assertEquals("B", treeTwo.nodeAt(1, false).label());
		assertEquals("C", treeTwo.nodeAt(3, false).label());
		assertEquals("D", treeTwo.nodeAt(2, false).label());
		assertNull(treeTwo.nodeAt(5, false));
	}
}
