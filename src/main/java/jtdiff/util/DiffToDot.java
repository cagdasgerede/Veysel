package jtdiff.util;

import jtdiff.main.Constants;
import jtdiff.main.IntPair;
import jtdiff.main.MappingList;
import jtdiff.util.Tree;

import java.util.ArrayList;
import java.util.List;

public class DiffToDot {
  public static String generateDotFromDiff(Tree sourceTree, Tree targetTree, MappingList diffMapping) {
    String sourceTreeRootLabel = sourceTree.nodeAt(1).label();
    String targetTreeRootLabel = targetTree.nodeAt(1).label();
    String sourceTreeLabelPrefix = "Source";
    String targetTreeLabelPrefix = "Target";

    DotGenerator dotGenerator = new DotGenerator(
        sourceTreeLabelPrefix, sourceTreeRootLabel,
        targetTreeLabelPrefix, targetTreeRootLabel);
    sourceTree.performPreorderTraversal(dotGenerator);
    dotGenerator.switchToTargetTree();
    targetTree.performPreorderTraversal(dotGenerator);
    
    decorateEditOperationsForDot(
        dotGenerator,
        sourceTree, sourceTreeLabelPrefix,
        targetTree, targetTreeLabelPrefix,
        diffMapping);
    
    return dotGenerator.finalDotRepresentation();
  }

  private static void decorateEditOperationsForDot(
      DotGenerator dotGenerator,
      Tree sourceTree, String sourceTreeLabelPrefix,
      Tree targetTree, String targetTreeLabelPrefix,
      MappingList mapping) {
    for (IntPair pair : mapping.pairs()) {
      int s = pair.source();
      int t = pair.target();
      TreeNode sourceNode = null;
      TreeNode targetNode = null;
      if (s != Constants.ALPHA_INT) {
        sourceNode = sourceTree.nodeAt(s);
      }
      if (t != Constants.ALPHA_INT) {
        targetNode = targetTree.nodeAt(t);
      }

      if (s == Constants.ALPHA_INT) {
        dotGenerator.addInsertion(
            targetTreeLabelPrefix + targetNode.label());
      }
      else if(t == Constants.ALPHA_INT) {
        dotGenerator.addDeletion(
            sourceTreeLabelPrefix + sourceNode.label());
      } else {
        dotGenerator.addDottedLine(
            sourceTreeLabelPrefix, sourceNode.label(),
            targetTreeLabelPrefix, targetNode.label());
      }
    }
  }
}

class DotGenerator extends Visitor {
  List<String> mDotRepresentation = new ArrayList<String>();
  String mSourceTreeLabelPrefix, mSourceTreeRootNodeLabel,
      mTargetTreeLabelPrefix, mTargetTreeRootNodeLabel,
      mCurrentTreeLabelPrefix;

  public DotGenerator(
      String sourceTreeLabelPrefix,
      String sourceTreeRootNodeLabel,
      String targetTreeLabelPrefix,
      String targetTreeRootNodeLabel) {
    mDotRepresentation.add("digraph G {");
    mSourceTreeLabelPrefix = sourceTreeLabelPrefix;
    mSourceTreeRootNodeLabel = sourceTreeRootNodeLabel;
    mTargetTreeLabelPrefix = targetTreeLabelPrefix;
    mTargetTreeRootNodeLabel = targetTreeRootNodeLabel;

    // Used to prepend the node labels in both trees so
    // that we can distinguish the node labels between 2 trees
    mCurrentTreeLabelPrefix = mSourceTreeLabelPrefix;

    // Root nodes of both trees should be in the same level
    // in the drawing
    mDotRepresentation.add(
      String.format("subgraph { rank = same; %s; %s };",
                    mSourceTreeLabelPrefix + mSourceTreeRootNodeLabel,
                    mTargetTreeLabelPrefix + mTargetTreeRootNodeLabel));
  }

  public void visit(TreeNode node) {
    TreeNode fatherNode = node.father();
    if (fatherNode != null) {
      mDotRepresentation.add(
        String.format("%s%s -> %s%s;",
                      mCurrentTreeLabelPrefix,
                      fatherNode.label(),
                      mCurrentTreeLabelPrefix,
                      node.label()));
    }
  }

  public void switchToTargetTree() {
    mCurrentTreeLabelPrefix = mTargetTreeLabelPrefix;
  }
  
  public String finalDotRepresentation() {
    mDotRepresentation.add("}");
    return String.join("\n", mDotRepresentation);
  }
  /**
   * Add a dotted line representing a change
   *
   * @source - name of the source node
   * @target - name of the target node
   */
  public void addDottedLine(String sourceTreeLabelPrefix, String sourceLabel,
                            String targetTreeLabelPrefix, String targetLabel) {
    String color = "gray";
    if (sourceLabel.equals(targetLabel)) {
      color = "green";
    }
    mDotRepresentation.add(
        String.format("%s -> %s [style=dotted color=%s constraint=false];",
                      sourceTreeLabelPrefix + sourceLabel, 
                      targetTreeLabelPrefix + targetLabel,
                      color));
  }

  public void addDeletion(String nodeLabel) {
    mDotRepresentation.add(
        String.format("%s [color=red];", nodeLabel));
  }

  public void addInsertion(String nodeLabel) {
    mDotRepresentation.add(
        String.format("%s [color=orange];", nodeLabel));
  }
}