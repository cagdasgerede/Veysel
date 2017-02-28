package jtdiff.util;

import jtdiff.main.Constants;
import jtdiff.main.MappingList;
import jtdiff.util.Tree;
import jtdiff.util.TreeNode;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TestDiffToDot {
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
  }

  @Test
  public void testDiffToDot() {
    MappingList mapping = new MappingList();
    mapping.add(1, 1);
    mapping.add(2, 3);
    mapping.add(3, 4);
    mapping.add(Constants.ALPHA_INT, 2);
    String dot = DiffToDot.generateDotFromDiff(mTreeOne, mTreeTwo, mapping);
    String expected = new StringBuilder()
        .append("digraph G {\n")
        .append("subgraph { rank = same; \"Source_A_1\"; \"Target_A_1\" };\n")
        .append("\"Source_A_1\" -> \"Source_B_2\";\n")
        .append("\"Source_B_2\" -> \"Source_D_3\";\n")
        .append("\"Target_A_1\" -> \"Target_B_2\";\n")
        .append("\"Target_A_1\" -> \"Target_C_3\";\n")
        .append("\"Target_C_3\" -> \"Target_D_4\";\n")
        .append("\"Source_A_1\" -> \"Target_A_1\" " +
                "[style=dotted color=green constraint=false];\n")
        .append("\"Source_B_2\" -> \"Target_C_3\" " +
                "[style=dotted color=gray constraint=false];\n")
        .append("\"Source_D_3\" -> \"Target_D_4\" " +
                "[style=dotted color=green constraint=false];\n")
        .append("\"Target_B_2\" [color=orange];\n")
        .append("}")
        .toString();
    assertEquals(expected, dot);
  }
}