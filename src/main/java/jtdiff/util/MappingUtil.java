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
      MappingList mapping, Tree sourceTree, Tree targetTree,
      boolean isPreorder, int emptyPositionValue) {
    List<String> humandFriendlyMapping = new ArrayList<>();
    for (IntPair ip : mapping.pairs()) {
      int i = ip.source(), j = ip.target();
      if (i == emptyPositionValue) {
        TreeNode targetNode = targetTree.nodeAt(j, isPreorder);
        int position;
        if (isPreorder) {
          position = targetNode.preorderPosition();
        } else {
          position = targetNode.postorderPosition();
        }

        humandFriendlyMapping.add(
            String.format(
                "Insert %s (@%d)",
                targetNode.label(),
                position));
      } else if (j == emptyPositionValue) {
        TreeNode sourceNode = sourceTree.nodeAt(i, isPreorder);
        int position;
        if (isPreorder) {
          position = sourceNode.preorderPosition();
        } else {
          position = sourceNode.postorderPosition();
        }

        humandFriendlyMapping.add(
          String.format(
              "Delete %s (@%d)",
              sourceNode.label(),
              position));
      } else {
        TreeNode sourceNode = sourceTree.nodeAt(i, isPreorder);
        TreeNode targetNode = targetTree.nodeAt(j, isPreorder);
        int sourcePosition, targetPosition;
        if (isPreorder) {
          sourcePosition = sourceNode.preorderPosition();
          targetPosition = targetNode.preorderPosition();
        } else {
          sourcePosition = sourceNode.postorderPosition();
          targetPosition = targetNode.postorderPosition();
        }

        if (sourceNode.label().equals(targetNode.label())) {
          humandFriendlyMapping.add(
              String.format("No change for %s (@%d and @%d)",
                            sourceNode.label(),
                            sourcePosition,
                            targetPosition));
        }
        else {
          humandFriendlyMapping.add(
              String.format("Change from %s (@%d) to %s (@%d)",
                            sourceNode.label(),
                            sourcePosition,
                            targetNode.label(),
                            targetPosition));
        }
      }
    }
    return humandFriendlyMapping;
  }

  public static List<EditOperation> produceEditOperationList(
      MappingList mapping, Tree sourceTree, Tree targetTree,
      boolean isPreorder, int emptyPositionValue) {
    List<EditOperation> editOperations = new ArrayList<>();
    for (IntPair ip : mapping.pairs()) {
      EditOperation editOperation = new EditOperation();
      editOperations.add(editOperation);

      int i = ip.source(), j = ip.target();
      if (i == emptyPositionValue) {
        TreeNode targetNode = targetTree.nodeAt(j, isPreorder);
        int position;
        if (isPreorder) {
          position = targetNode.preorderPosition();
        } else {
          position = targetNode.postorderPosition();
        }

        editOperation
            .type(EditOperation.Type.INSERT)
            .targetNodeLabel(targetNode.label())
            .targetNodePosition(position);
      } else if (j == emptyPositionValue) {
        TreeNode sourceNode = sourceTree.nodeAt(i, isPreorder);
        int position;
        if (isPreorder) {
          position = sourceNode.preorderPosition();
        } else {
          position = sourceNode.postorderPosition();
        }

        editOperation
            .type(EditOperation.Type.DELETE)
            .sourceNodeLabel(sourceNode.label())
            .sourceNodePosition(position);
      } else {
        TreeNode sourceNode = sourceTree.nodeAt(i, isPreorder);
        TreeNode targetNode = targetTree.nodeAt(j, isPreorder);
        int sourcePosition, targetPosition;
        if (isPreorder) {
          sourcePosition = sourceNode.preorderPosition();
          targetPosition = targetNode.preorderPosition();
        } else {
          sourcePosition = sourceNode.postorderPosition();
          targetPosition = targetNode.postorderPosition();
        }

        if (sourceNode.label().equals(targetNode.label())) {
          editOperation
              .type(EditOperation.Type.NO_CHANGE)
              .sourceNodeLabel(sourceNode.label())
              .sourceNodePosition(sourcePosition)
              .targetNodeLabel(targetNode.label())
              .targetNodePosition(targetPosition);
        }
        else {
          editOperation
              .type(EditOperation.Type.CHANGE)
              .sourceNodeLabel(sourceNode.label())
              .sourceNodePosition(sourcePosition)
              .targetNodeLabel(targetNode.label())
              .targetNodePosition(targetPosition);
        }
      }
    }
    return editOperations;
  }
}