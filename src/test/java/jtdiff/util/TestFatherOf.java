package jtdiff.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Tests <code>fatherOf</code> method of <code>Tree</code> class
 */
public class TestFatherOf extends TestTree {
	/**
	 * Usual case when father exists
	 */
	@Test
	public void testSuccess() {
		assertEquals(3, treeTwo.fatherOf(4).preorderPosition());
		assertEquals(1, treeTwo.fatherOf(3).preorderPosition());
		assertEquals(1, treeTwo.fatherOf(2).preorderPosition());
	}

	/**
	 * Father of root should be null
	 */
	@Test
	public void testRoot() {
		assertNull(treeTwo.fatherOf(1));
	}

	/**
	 * Raise error when father of a non-existing node requested
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWhenNoSuchNode() {
		treeTwo.fatherOf(5);
	}
}
