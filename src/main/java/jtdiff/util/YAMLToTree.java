package jtdiff.util;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Run this by itself (under src/main/java:
 *    javac -cp .:/usr/local/lib/yamlbeans-1.09.jar jtdiff/util/YAMLToTree.java; \
 *    java -cp .:/usr/local/lib/yamlbeans-1.09.jar jtdiff.util.YAMLToTree ../../test/java/jtdiff/util/sampleTreesInYamlForTestingYamlToTree.yml
 *
 * Run this by maven:
 *    mvn exec:java -Dexec.mainClass="jtdiff.util.YAMLToTree"
 */
public class YAMLToTree {

  public static void main(String[] args) throws Exception {
    // When called with maven
    String fileLocation = "src/test/java/jtdiff/util/sampleTreesInYamlForTestingYamlToTree.yml";
    if (args.length > 0) { // Called without maven
      fileLocation = args[0];
    }
    //InputStream stream = new ByteArrayInputStream(exampleString.getBytes(StandardCharsets.UTF_8));
    InputStreamReader reader = new FileReader(fileLocation);
    for (Tree t : buildTreesFromYamlInput(reader)) {
      t.printPreorderTraversal();
      System.out.println("---");
    }
  }

  public static List<Tree> buildTreesFromYamlInput(Reader reader) throws java.io.FileNotFoundException,  YamlException{
    YamlReader yamlReader = new YamlReader(reader);
    List<Tree> treeList = new ArrayList<>();
    while (true) {
        Object o = yamlReader.read();
        if (o == null) break;
        Map treeMap = (Map) o;
        Tree tree = buildTree(treeMap);
        treeList.add(tree);
    }
    return treeList;
  }

  private static Tree buildTree(Map treeMap) {
    Map.Entry<String, List> entry = (Map.Entry<String, List>) treeMap.entrySet().iterator().next();
    String rootLabel = entry.getKey();
    List<Map> children = entry.getValue();
    TreeNode rootNode = buildSubtree(rootLabel, children);
    Tree aTree = new Tree(rootNode);
    aTree.buildCaches();
    //aTree.printPreorderTraversal();
    return aTree;
  }

  private static TreeNode buildSubtree(String rootLabel, List<Map> children) {
    TreeNode rootNode = new TreeNode(rootLabel);
    if (children != null) {
      for (Map child : children) {
        Map.Entry<String, List> entry =
            (Map.Entry<String, List>) child.entrySet().iterator().next();
        String childLabel = entry.getKey();
        Object o = entry.getValue();
        TreeNode childNode;
        List<Map> childrenOfChild = null;
        if (!(o instanceof String)) {
          childrenOfChild = (List<Map>) o;
        }
        childNode = buildSubtree(childLabel, childrenOfChild);
        rootNode.addChild(childNode);
      }
    }
    return rootNode;
  }
}