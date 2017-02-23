package jtdiff.util;

import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

/**
 * Tests <code>performPreorderTraversal</code> method of 
 * <code>Tree</code> class
 */
public class TestPreorderTraversal extends TestTree {
	/**
	 * Successfully produce the list of visited node labels
	 */
	@Test
	public void testSucess() {
		TraversalVisitor visitor = new TraversalVisitor();
		treeOne.performPreorderTraversal(visitor);

		String[] expected = new String[] { 
				"label: A, preorder_position: 1", 
				"label: B, preorder_position: 2",
				"label: C, preorder_position: 3" };
		assertArrayEquals(expected, visitor.getTraversal());

		visitor = new TraversalVisitor();
		treeTwo.performPreorderTraversal(visitor);
		expected = new String[] { 
				"label: A, preorder_position: 1", 
				"label: B, preorder_position: 2",
				"label: C, preorder_position: 3", 
				"label: D, preorder_position: 4" };
		assertArrayEquals(expected, visitor.getTraversal());
	}
}
