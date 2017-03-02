package jtdiff.util;

import jtdiff.main.Constants;
import jtdiff.main.IntPair;
import jtdiff.main.MappingList;

import java.util.ArrayList;
import java.util.List;

public class MappingUtil {
  /*
   * Produces a list of humand friendly descriptions for mapping
   * between two trees
   * Example:
   *   ['No change for A (@1)', 'Change from B (@2) to C (@3)',
   *    'No change for D (@3)', 'Insert B (@2)']
   *
   * @returns list of strings
   */
  public static List<String> produceHumanFriendlyMapping(
      MappingList mapping, Tree sourceTree, Tree targetTree) {
    List<String> humandFriendlyMapping = new ArrayList<>();
    for (IntPair ip : mapping.pairs()) {
      int i = ip.source(), j = ip.target();
      if (i == Constants.ALPHA_INT) {
        TreeNode targetNode = targetTree.nodeAt(j);
        humandFriendlyMapping.add(
            String.format(
                "Insert %s (@%d)",
                targetNode.label(),
                targetNode.preorderPosition()));
      } else if (j == Constants.ALPHA_INT) {
        TreeNode sourceNode = sourceTree.nodeAt(i);
        humandFriendlyMapping.add(
          String.format(
              "Delete %s (@%d)",
              sourceNode.label(),
              sourceNode.preorderPosition()));
      } else {
        TreeNode sourceNode = sourceTree.nodeAt(i);
        TreeNode targetNode = targetTree.nodeAt(j);
        if (sourceNode.label().equals(targetNode.label())) {
          humandFriendlyMapping.add(
              String.format("No change for %s (@%d and @%d)",
                            sourceNode.label(),
                            sourceNode.preorderPosition(),
                            targetNode.preorderPosition()));
        }
        else {
          humandFriendlyMapping.add(
              String.format("Change from %s (@%d) to %s (@%d)",
                            sourceNode.label(),
                            sourceNode.preorderPosition(),
                            targetNode.label(),
                            targetNode.preorderPosition()));
        }
      }
    }
    return humandFriendlyMapping;
  }

  public static List<EditOperation> produceEditOperationList(
      MappingList mapping, Tree sourceTree, Tree targetTree) {
    List<EditOperation> editOperations = new ArrayList<>();
    for (IntPair ip : mapping.pairs()) {
      EditOperation editOperation = new EditOperation();
      editOperations.add(editOperation);

      int i = ip.source(), j = ip.target();
      if (i == Constants.ALPHA_INT) {
        TreeNode targetNode = targetTree.nodeAt(j);
        editOperation
            .type(EditOperation.Type.INSERT)
            .targetNodeLabel(targetNode.label())
            .targetNodePosition(targetNode.preorderPosition());
      } else if (j == Constants.ALPHA_INT) {
        TreeNode sourceNode = sourceTree.nodeAt(i);
        editOperation
            .type(EditOperation.Type.DELETE)
            .sourceNodeLabel(sourceNode.label())
            .sourceNodePosition(sourceNode.preorderPosition());
      } else {
        TreeNode sourceNode = sourceTree.nodeAt(i);
        TreeNode targetNode = targetTree.nodeAt(j);

        if (sourceNode.label().equals(targetNode.label())) {
          editOperation
              .type(EditOperation.Type.NO_CHANGE)
              .sourceNodeLabel(sourceNode.label())
              .sourceNodePosition(sourceNode.preorderPosition())
              .targetNodeLabel(targetNode.label())
              .targetNodePosition(targetNode.preorderPosition());
        }
        else {
          editOperation
              .type(EditOperation.Type.CHANGE)
              .sourceNodeLabel(sourceNode.label())
              .sourceNodePosition(sourceNode.preorderPosition())
              .targetNodeLabel(targetNode.label())
              .targetNodePosition(targetNode.preorderPosition());
        }
      }
    }
    return editOperations;
  }
}