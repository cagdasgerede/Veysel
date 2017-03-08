package jtdiff.util;

import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

/**
 * Tests <code>visit</code> method of 
 * <code>Tree</code> class
 */
public class TestVisit extends TestTree {
	/**
	 * Successfully produce the list of visited node labels
	 */
	@Test
	public void testSucess() {
		TraversalVisitor visitor = new TraversalVisitor();
		treeOne.visit(visitor);

		String[] expected = new String[] { 
				"label: A, preorder_position: 1, postorder_position: 3", 
				"label: B, preorder_position: 2, postorder_position: 2",
				"label: C, preorder_position: 3, postorder_position: 1" };
		assertArrayEquals(expected, visitor.getTraversal());

		visitor = new TraversalVisitor();
		treeTwo.visit(visitor);
		expected = new String[] { 
				"label: A, preorder_position: 1, postorder_position: 4",
				"label: B, preorder_position: 2, postorder_position: 1",
				"label: C, preorder_position: 3, postorder_position: 3", 
				"label: D, preorder_position: 4, postorder_position: 2" };
		assertArrayEquals(expected, visitor.getTraversal());
	}
}
