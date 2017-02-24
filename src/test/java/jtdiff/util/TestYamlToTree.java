package jtdiff.util;

import static org.junit.Assert.assertEquals;

import com.esotericsoftware.yamlbeans.YamlException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.junit.Before;
import org.junit.Test;


public class TestYamlToTree {
  Tree mExpectedTreeOne, mExpectedTreeTwo;

  @Before
  public void setUp() throws Exception {
    // Tree 1
    TreeNode aNode = new TreeNode("A");
    TreeNode bNode = new TreeNode("B");
    TreeNode dNode = new TreeNode("D");
    TreeNode zNode = new TreeNode("Z");
    TreeNode abcNode = new TreeNode("ABC");
    TreeNode eNode = new TreeNode("E");
    TreeNode gNode = new TreeNode("G");
    aNode.addChild(bNode);
    aNode.addChild(eNode);
    aNode.addChild(gNode);
    bNode.addChild(dNode);
    dNode.addChild(zNode);
    zNode.addChild(abcNode);
    mExpectedTreeOne = new Tree(aNode);
    mExpectedTreeOne.buildCaches();

    // Tree 2
    aNode = new TreeNode("A");
    bNode = new TreeNode("B");
    TreeNode cNode = new TreeNode("C");
    dNode = new TreeNode("D");
    aNode.addChild(bNode);
    aNode.addChild(cNode);
    cNode.addChild(dNode);
    mExpectedTreeTwo = new Tree(aNode);
    mExpectedTreeTwo.buildCaches();
  }

  @Test
  public void testImageFromDot() throws FileNotFoundException, YamlException {
    String fileLocation = "src/test/java/jtdiff/util/sampleTreesInYamlForTestingYamlToTree.yml";
    InputStreamReader reader = new FileReader(fileLocation);
    List<Tree> trees = YAMLToTree.buildTreesFromYamlInput(reader);

    assertEquals(2, trees.size());
    Iterator<Tree> iter = trees.iterator();
    Tree tree1 = iter.next();
    assertEquals(mExpectedTreeOne, tree1);
    Tree tree2 = iter.next();
    assertEquals(mExpectedTreeTwo, tree2);
  }
}