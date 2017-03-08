package jtdiff.util;

import jtdiff.main.Constants;
import jtdiff.main.IntPair;
import jtdiff.main.MappingList;
import jtdiff.util.Tree;

import java.util.ArrayList;
import java.util.List;

public class DiffToDot {
  public static String generateDotFromDiff(
      Tree sourceTree, Tree targetTree, MappingList diffMapping,
      boolean isPreorder, int emptyLabelValue) {
    String sourceTreeRootLabel = sourceTree.nodeAt(1, isPreorder).label();
    String targetTreeRootLabel = targetTree.nodeAt(1, isPreorder).label();
    String sourceTreeLabelPrefix = "Source_";
    String targetTreeLabelPrefix = "Target_";

    DotGenerator dotGenerator = new DotGenerator(
        sourceTreeLabelPrefix, sourceTreeRootLabel,
        targetTreeLabelPrefix, targetTreeRootLabel);
    sourceTree.visit(dotGenerator);
    dotGenerator.switchToTargetTree();
    targetTree.visit(dotGenerator);
    
    decorateEditOperationsForDot(
        dotGenerator,
        sourceTree, sourceTreeLabelPrefix,
        targetTree, targetTreeLabelPrefix,
        diffMapping,
        isPreorder,
        emptyLabelValue);
    
    return dotGenerator.finalDotRepresentation();
  }

  private static void decorateEditOperationsForDot(
      DotGenerator dotGenerator,
      Tree sourceTree,
      String sourceTreeLabelPrefix,
      Tree targetTree,
      String targetTreeLabelPrefix,
      MappingList mapping,
      boolean isPreorder,
      int emptyLabelValue) {
    for (IntPair pair : mapping.pairs()) {
      int s = pair.source();
      int t = pair.target();
      TreeNode sourceNode = null;
      TreeNode targetNode = null;
      if (s != emptyLabelValue) {
        sourceNode = sourceTree.nodeAt(s, isPreorder);
      }
      if (t != emptyLabelValue) {
        targetNode = targetTree.nodeAt(t, isPreorder);
      }

      if (s == emptyLabelValue) {
        dotGenerator.addInsertion(
            dotGenerator.getNodeLabel(
                targetTreeLabelPrefix,
                targetNode));
      }
      else if(t == emptyLabelValue) {
        dotGenerator.addDeletion(
            dotGenerator.getNodeLabel(
                sourceTreeLabelPrefix,
                sourceNode));
      } else {
        String color = "gray";
        if (sourceNode.label().equals(targetNode.label())) {
          color = "green";
        }

        dotGenerator.addDottedLine(
            dotGenerator.getNodeLabel(
                sourceTreeLabelPrefix,
                sourceNode),
            dotGenerator.getNodeLabel(
                targetTreeLabelPrefix,
                targetNode),
            color);
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
      String.format("subgraph { rank = same; \"%s\"; \"%s\" };",
                    getNodeLabel(
                      mSourceTreeLabelPrefix,
                      mSourceTreeRootNodeLabel,
                      1),
                    getNodeLabel(
                      mTargetTreeLabelPrefix,
                      mTargetTreeRootNodeLabel,
                      1)));
  }

  @Override
  public void enter(TreeNode node) {
    TreeNode fatherNode = node.father();
    if (fatherNode != null) {
      mDotRepresentation.add(
        String.format("\"%s\" -> \"%s\";",
                      getNodeLabel(
                        mCurrentTreeLabelPrefix,
                        fatherNode),
                      getNodeLabel(
                        mCurrentTreeLabelPrefix,
                        node)));
    }
  }

  @Override
  public void exit(TreeNode node) {}

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
  public void addDottedLine(String sourceLabel,
                            String targetLabel,
                            String color) {
    mDotRepresentation.add(
        String.format(
            "\"%s\" -> \"%s\" [style=dotted color=%s constraint=false];",
            sourceLabel, 
            targetLabel,
            color));
  }

  public void addDeletion(String nodeLabel) {
    mDotRepresentation.add(
        String.format("\"%s\" [color=red];", nodeLabel));
  }

  public void addInsertion(String nodeLabel) {
    mDotRepresentation.add(
        String.format("\"%s\" [color=orange];", nodeLabel));
  }

  public String getNodeLabel(
      String prefix, String label, int preorderPosition) {
    return String.format(
        "%s%s_%s",
        prefix,
        label,
        preorderPosition);  // Add unique position information so that
                            // Graphviz can distinguish different nodes
                            // with the same (grammar rule) name.
  }

  public String getNodeLabel(
      String prefix, TreeNode node) {
    return getNodeLabel(prefix, node.label(), node.preorderPosition());
  }
}



