package jtdiff.main;

import jtdiff.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/* File containings unit tests for testing min cost tree diff computation.
 * Run the test by executing "python test_treediff.py -v" at the command line.
 */
public class TestTreeDiff {
  Tree mTreeOne, mTreeTwo, mTreeThree, mTreeFour;

  @Before
  public void setUp() throws Exception {
    TreeNode aNode = new TreeNode("A");
    TreeNode bNode = new TreeNode("B");
    aNode.addChild(bNode);
    TreeNode dNode = new TreeNode("D");
    bNode.addChild(dNode);
    mTreeOne = new Tree(aNode);
    mTreeOne.buildCaches();

    aNode = new TreeNode("A");
    bNode = new TreeNode("B");
    TreeNode cNode = new TreeNode("C");
    dNode = new TreeNode("D");
    aNode.addChild(bNode);
    aNode.addChild(cNode);
    cNode.addChild(dNode);
    mTreeTwo = new Tree(aNode);
    mTreeTwo.buildCaches();

    aNode = new TreeNode("A");
    bNode = new TreeNode("B");
    cNode = new TreeNode("C");
    dNode = new TreeNode("D");
    TreeNode eNode = new TreeNode("E");
    aNode.addChild(bNode);
    aNode.addChild(cNode);
    cNode.addChild(dNode);
    dNode.addChild(eNode);
    mTreeThree = new Tree(aNode);
    mTreeThree.buildCaches();
    
    aNode = new TreeNode("A");
    bNode = new TreeNode("B");
    cNode = new TreeNode("CC");
    dNode = new TreeNode("D");
    eNode = new TreeNode("E");
    aNode.addChild(bNode);
    aNode.addChild(cNode);
    cNode.addChild(dNode);
    dNode.addChild(eNode);
    mTreeFour = new Tree(aNode);
    mTreeFour.buildCaches();
  }

  @Test
  public void testDistance() {
    assertEquals(2, TreeDiff.computeDiff(mTreeOne, mTreeTwo).cost);
    assertEquals(3, TreeDiff.computeDiff(mTreeOne, mTreeThree).cost);
    assertEquals(1, TreeDiff.computeDiff(mTreeTwo, mTreeThree).cost);
    assertEquals(1, TreeDiff.computeDiff(mTreeThree, mTreeFour).cost);
    assertEquals(0, TreeDiff.computeDiff(mTreeTwo, mTreeTwo).cost);
  }

  @Test
  public void testMapping1() {
    MappingList expectedMapping = new MappingList();
    expectedMapping.add(1, 1);
    expectedMapping.add(2, 3);
    expectedMapping.add(3, 4);
    expectedMapping.add(TreeDiff.ALPHA_INT, 2);

    Result result = TreeDiff.computeDiff(mTreeOne, mTreeTwo);
    assertEquals(expectedMapping, result.mapping);    
  }

  @Test
  public void testMapping2() {
    MappingList expectedMapping = new MappingList();
    expectedMapping.add(1, 1);
    expectedMapping.add(2, 3);
    expectedMapping.add(3, 4);
    expectedMapping.add(TreeDiff.ALPHA_INT, 2);
    expectedMapping.add(TreeDiff.ALPHA_INT, 5);

    Result result = TreeDiff.computeDiff(mTreeOne, mTreeThree);
    assertEquals(expectedMapping, result.mapping);    
  }

  @Test
  public void testMapping3() {
    MappingList expectedMapping = new MappingList();
    expectedMapping.add(1, 1);
    expectedMapping.add(2, 2);
    expectedMapping.add(3, 3);
    expectedMapping.add(4, 4);
    expectedMapping.add(TreeDiff.ALPHA_INT, 5);

    Result result = TreeDiff.computeDiff(mTreeTwo, mTreeThree);
    assertEquals(expectedMapping, result.mapping);    
  }

  @Test
  public void testMapping4() {
    MappingList expectedMapping = new MappingList();
    expectedMapping.add(1, 1);
    expectedMapping.add(2, 2);
    expectedMapping.add(3, 3);
    expectedMapping.add(4, 4);
    expectedMapping.add(5, 5);

    Result result = TreeDiff.computeDiff(mTreeThree, mTreeFour);
    assertEquals(expectedMapping, result.mapping);    
  }

  @Test
  public void testMapping5() {
    MappingList expectedMapping = new MappingList();
    expectedMapping.add(1, 1);
    expectedMapping.add(2, 2);
    expectedMapping.add(3, 3);
    expectedMapping.add(4, 4);

    Result result = TreeDiff.computeDiff(mTreeTwo, mTreeTwo);
    assertEquals(expectedMapping, result.mapping);    
  }

  @Test
  public void testProduceHumanFriendlyMapping1() {
    Result result = TreeDiff.computeDiff(mTreeOne, mTreeTwo);
    List<String> description = TreeDiff.produceHumanFriendlyMapping(
        result.mapping, mTreeOne, mTreeTwo);
    List<String> expected = Arrays.asList(
        "No change for A (@1 and @1)",
        "Change from B (@2) to C (@3)",
        "No change for D (@3 and @4)",
        "Insert B (@2)");
    assertEquals(new HashSet(expected), new HashSet(description));
  }

  @Test
  public void testProduceHumanFriendlyMapping2() {
    Result result = TreeDiff.computeDiff(mTreeOne, mTreeThree);
    List<String> description = TreeDiff.produceHumanFriendlyMapping(
        result.mapping, mTreeOne, mTreeThree);
    List<String> expected = Arrays.asList(
        "No change for A (@1 and @1)",
        "Change from B (@2) to C (@3)",
        "No change for D (@3 and @4)",
        "Insert B (@2)",
        "Insert E (@5)");
    assertEquals(new HashSet(expected), new HashSet(description));
  }

  @Test
  public void testProduceHumanFriendlyMapping3() {
    Result result = TreeDiff.computeDiff(mTreeTwo, mTreeThree);
    List<String> description = TreeDiff.produceHumanFriendlyMapping(
        result.mapping, mTreeTwo, mTreeThree);
    List<String> expected = Arrays.asList(
        "No change for A (@1 and @1)",
        "No change for B (@2 and @2)",
        "No change for C (@3 and @3)",
        "No change for D (@4 and @4)",
        "Insert E (@5)");
    assertEquals(new HashSet(expected), new HashSet(description));
  }

  @Test
  public void testProduceHumanFriendlyMapping4() {
    Result result = TreeDiff.computeDiff(mTreeThree, mTreeFour);
    List<String> description = TreeDiff.produceHumanFriendlyMapping(
        result.mapping, mTreeThree, mTreeFour);
    List<String> expected = Arrays.asList(
        "No change for A (@1 and @1)",
        "No change for B (@2 and @2)",
        "Change from C (@3) to CC (@3)",
        "No change for D (@4 and @4)",
        "No change for E (@5 and @5)");
    assertEquals(new HashSet(expected), new HashSet(description));
  }

  @Test
  public void testProduceHumanFriendlyMapping5() {
    Result result = TreeDiff.computeDiff(mTreeTwo, mTreeTwo);
    List<String> description = TreeDiff.produceHumanFriendlyMapping(
        result.mapping, mTreeTwo, mTreeTwo);
    List<String> expected = Arrays.asList(
        "No change for A (@1 and @1)",
        "No change for B (@2 and @2)",
        "No change for C (@3 and @3)",
        "No change for D (@4 and @4)");
    assertEquals(new HashSet(expected), new HashSet(description));
  }
}