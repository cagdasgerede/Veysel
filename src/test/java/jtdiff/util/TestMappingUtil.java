package jtdiff.util;

import jtdiff.main.Constants;
import jtdiff.main.Result;
import jtdiff.main.TreeDiff;
import jtdiff.main.MappingList;
import jtdiff.util.Tree;
import jtdiff.util.TreeNode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TestMappingUtil {
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
  public void testProduceHumanFriendlyMapping1() {
    MappingList mapping = new MappingList();
    mapping.add(1, 1);
    mapping.add(2, 3);
    mapping.add(3, 4);
    mapping.add(Constants.ALPHA_INT, 2);

    List<String> description = MappingUtil.produceHumanFriendlyMapping(
        mapping, mTreeOne, mTreeTwo);
    List<String> expected = Arrays.asList(
        "No change for A (@1 and @1)",
        "Change from B (@2) to C (@3)",
        "No change for D (@3 and @4)",
        "Insert B (@2)");
    assertEquals(new HashSet(expected), new HashSet(description));
  }

  @Test
  public void testProduceHumanFriendlyMapping2() {
    MappingList mapping = new MappingList();
    mapping.add(1, 1);
    mapping.add(2, 3);
    mapping.add(3, 4);
    mapping.add(Constants.ALPHA_INT, 2);
    mapping.add(Constants.ALPHA_INT, 5);

    List<String> description = MappingUtil.produceHumanFriendlyMapping(
        mapping, mTreeOne, mTreeThree);
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
    MappingList mapping = new MappingList();
    mapping.add(1, 1);
    mapping.add(2, 2);
    mapping.add(3, 3);
    mapping.add(4, 4);
    mapping.add(Constants.ALPHA_INT, 5);

    List<String> description = MappingUtil.produceHumanFriendlyMapping(
        mapping, mTreeTwo, mTreeThree);
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
    MappingList mapping = new MappingList();
    mapping.add(1, 1);
    mapping.add(2, 2);
    mapping.add(3, 3);
    mapping.add(4, 4);
    mapping.add(5 ,5);

    List<String> description = MappingUtil.produceHumanFriendlyMapping(
        mapping, mTreeThree, mTreeFour);
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
    MappingList mapping = new MappingList();
    mapping.add(1, 1);
    mapping.add(2, 2);
    mapping.add(3, 3);
    mapping.add(4, 4);

    List<String> description = MappingUtil.produceHumanFriendlyMapping(
        mapping, mTreeTwo, mTreeTwo);
    List<String> expected = Arrays.asList(
        "No change for A (@1 and @1)",
        "No change for B (@2 and @2)",
        "No change for C (@3 and @3)",
        "No change for D (@4 and @4)");
    assertEquals(new HashSet(expected), new HashSet(description));
  }

  @Test
  public void testProduceEditOperationList() {
    MappingList mapping = new MappingList();
    mapping.add(1, 1);  // No change for A
    mapping.add(2, 3);  // Change from B to C
    mapping.add(3, 4);  // No change for D
    mapping.add(Constants.ALPHA_INT, 2);  // Insert B

    List<EditOperation> editList = MappingUtil.produceEditOperationList(
        mapping, mTreeOne, mTreeTwo);
    assertEquals(4, editList.size());

    EditOperation expectedOp1 = new EditOperation()
        .type(EditOperation.Type.NO_CHANGE)
        .sourceNodePosition(1)
        .targetNodePosition(1)
        .sourceNodeLabel("A")
        .targetNodeLabel("A");
    EditOperation expectedOp2 = new EditOperation()
        .type(EditOperation.Type.CHANGE)
        .sourceNodePosition(2)
        .targetNodePosition(3)
        .sourceNodeLabel("B")
        .targetNodeLabel("C");
    EditOperation expectedOp3 = new EditOperation()
        .type(EditOperation.Type.NO_CHANGE)
        .sourceNodePosition(3)
        .targetNodePosition(4)
        .sourceNodeLabel("D")
        .targetNodeLabel("D");
    EditOperation expectedOp4 = new EditOperation()
        .type(EditOperation.Type.INSERT)
        .targetNodePosition(2)
        .targetNodeLabel("B");
    assertTrue(editList.contains(expectedOp1));
    assertTrue(editList.contains(expectedOp2));
    assertTrue(editList.contains(expectedOp3));
    assertTrue(editList.contains(expectedOp4));
  }
}