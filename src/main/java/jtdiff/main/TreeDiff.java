package jtdiff.main;

import jtdiff.util.Tree;
import jtdiff.util.TreeNode;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/* This file has the implementation of the algorithm described in
 * "The Tree-to-Tree Correction Problem" by Kuo-Chung Tai published
 * at the Journal of the ACM , 26(3):422-433, July 1979.
 *
 * We follow the naming of the variables and functions from the paper
 * even though sometimes it may be against some Java conventions.
 * The algorithm is at section 5 in the paper. There is one
 * missing piece in the algorithm provided in the paper which is
 * MIN_M(i, 1) and MIN_M(1, j) values. We added the computation
 * of these in the implementation below.
 *
 * Usage:
 *     Tree source = ...;
 *     Tree target = ...;
 *     Result result = TreeDiff.computeDiff(source, target);
 *     // result contains the min cost of the diff and
 *     // the mapping between tree node preorder positions.
 *     List<String> modifications = MappingUtil
 *        .produceHumanFriendlyMapping(result.mapping(), source, target);
 *
 */
public class TreeDiff {
  private static final int INFINITE = Integer.MAX_VALUE;

  /**
   * Returns the distance between the given trees and the list of pairs
   * where each pair (x, y) shows which node at the preorder position x
   * in the source tree is mapped to which node at the preorder
   * position y in the target tree. If x is ALPHA, then it shows
   * the node at the preorder position y in the target tree is
   * inserted. If y is ALPHA, then it shows the node at the preorder
   * position x in the souce tree is deleted.
   *
   * @param sourceTree the source tree (Tree)
   * @param targetTree the target tree (Tree)
   * @return Result
   */
  public static Result computeDiff(Tree sourceTree, Tree targetTree) {
    // E, mappingForE
    DictionaryPair<KeyForE> dictionaryPairForE =
        computeE(sourceTree, targetTree);
    // MIN_M, mappingForMinM
    DictionaryPair<IntPairAsKey> dictionaryPair =
        computeMIN_M(dictionaryPairForE, sourceTree, targetTree);
    // D, mappingForD
    dictionaryPair = computeD(sourceTree, targetTree, dictionaryPair);
    Map<IntPairAsKey, Integer> D = dictionaryPair.costs;
    Map<IntPairAsKey, MappingList> mappingForD = dictionaryPair.mapping;
    MappingList mapping =
        mappingForD.get(keyForD(sourceTree.size(), targetTree.size()));
    return new Result(
        D.get(keyForD(sourceTree.size(), targetTree.size())),
        mapping);
  }

  /**
   * Returns the cost of transforming a to b
   *
   * @param a the label of the source node
   * @param b the label of the target node
   * @return integer
   */
  protected static int r(TreeNode a, TreeNode b) {
    if (a != Constants.ALPHA &&
        b != Constants.ALPHA &&
        a.label().equals(b.label())) { // No change
      return 0;
    }
    return 1; // Insert, Delete, Change
  }

  // DO NOT SUBMIT (profiling/optimizing the code)
  //protected static String keyForE(int s, int u, int i, int t, int v, int j) {
  //  return String.format("%d:%d:%d, %d:%d:%d", s, u, i, t, v, j);
  //}

  /**
   * Returns the E mapping. Check the paper to understand what
   * the mapping mean.
   *
   * @param sourceTree the source tree (Tree)
   * @param targetTree the target tree (Tree)
   * @return (dict, dict)
   *       The first dict is in the format {'i:j:k, p:q:r' => cost} where 
   *       i, j, k, p, q, r are integers. The second dict is in the format
   *       {'i:j:k, p:q:r' => mapping} where mapping is a list of
   *       (x, y) pairs showing which node at the preorder position x
   *       in the source tree is mapped to which node at the preorder
   *       position y in the target tree. If x is ALPHA, then it shows
   *       the node at the preorder position y in the target tree is
   *       inserted. If y is ALPHA, then it shows the node at the preorder
   *       position x in the souce tree is deleted.
   */
  protected static DictionaryPair computeE(Tree sourceTree, Tree targetTree) {
    Map<KeyForE, Integer> E = new HashMap<>();
    Map<KeyForE, MappingList> mappingForE = new HashMap<>();
    DictionaryPair<KeyForE> dictionaryPair =
        new DictionaryPair<>(E, mappingForE);
    for (int i = 1; i <= sourceTree.size(); i++) {
      //System.out.println("source: " + i + " out of " + sourceTree.size());
      for (int j = 1; j <= targetTree.size(); j++) {
        //System.out.println("target: " + j + " out of " + targetTree.size());
        for (int u : sourceTree.ancestors(i)) {
          for (int s : sourceTree.ancestors(u)) {
            for (int v : targetTree.ancestors(j)) {
              for (int t : targetTree.ancestors(v)) {
                KeyForE key = new KeyForE(s, u, i, t, v, j);
                if ((s == u && u == i) && (t == v && v == j)) {
                  E.put(key, r(sourceTree.nodeAt(i), targetTree.nodeAt(j)));
                  mappingForE.put(key, new MappingList(i, j));
                }
                else if ((s == u && u == i) || (t < v && v == j)) {
                  int f_j = targetTree.fatherOf(j).preorderPosition();
                  KeyForE dependentKey = new KeyForE(s, u, i, t, f_j, j - 1);
                  E.put(key, E.get(dependentKey) + r(Constants.ALPHA, targetTree.nodeAt(j)));
                  // Insertion
                  mappingForE.put(
                    key, mappingForE.get(dependentKey).clone().add(Constants.ALPHA_INT, j));
                }
                else if ((s < u && u == i) || (t == v && v == j)) {
                  int f_i = sourceTree.fatherOf(i).preorderPosition();
                  KeyForE dependentKey = new KeyForE(s, f_i, i - 1, t, v, j);
                  E.put(key, E.get(dependentKey) + r(sourceTree.nodeAt(i), Constants.ALPHA));
                  // Deletion
                  mappingForE.put(
                    key, mappingForE.get(dependentKey).clone().add(i, Constants.ALPHA_INT));
                }
                else {
                  TreeNode xNode = sourceTree.childOnPathFromDescendant(u, i);
                  int x = xNode.preorderPosition();
                  TreeNode yNode = targetTree.childOnPathFromDescendant(v, j);
                  int y = yNode.preorderPosition();
                  KeyForE dependentKey1 = new KeyForE(s, x, i, t, v, j);
                  KeyForE dependentKey2 = new KeyForE(s, u, i, t, y, j);
                  KeyForE dependentKey3 = new KeyForE(s, u, x - 1, t, v, y - 1);
                  KeyForE dependentKey4 = new KeyForE(x, x, i, y, y, j);
                  int minDistance = Collections.min(
                      Arrays.asList(
                          E.get(dependentKey1),
                          E.get(dependentKey2),
                          E.get(dependentKey3) + E.get(dependentKey4)));
                  E.put(key, minDistance);
                  // Remember the mapping.
                  if (E.get(key).equals(E.get(dependentKey1))) {
                    mappingForE.put(key, mappingForE.get(dependentKey1).clone());
                  }
                  else if (E.get(key).equals(E.get(dependentKey2))) {
                    mappingForE.put(key, mappingForE.get(dependentKey2).clone());
                  }
                  else {
                    MappingList newMapping =
                        mappingForE
                            .get(dependentKey3)
                            .clone()
                            .extendWith(mappingForE.get(dependentKey4));
                    mappingForE.put(key, newMapping);
                  }
                }
              }
            }
          }
        }
      }
    }
    System.out.printf("dictionary after computeE: %s\n", dictionaryPair);
    return dictionaryPair;
  }

  // Returns the key for MIN_M map
  protected static IntPairAsKey keyForMIN_M(int s, int t) {
    // return String.format("%d:%d", s, t);
    return new IntPairAsKey(s, t);
  }

  /** 
   * Returns the MIN_M mapping. Check out the article to see
   * what the mapping mean
   *
   * @param E computed by computeE (dict)
   * @param sourceTree the source tree (Tree)
   * @param targetTree the target tree (Tree)
   * @return (dict, dict)
   *        The first dict is the MIN_M map (key to cost). The second
   *        dict is (key to list of integer pairs) the transformation mapping
   *        where a pair (x, y) shows which node at the preorder position x
   *        in the source tree is mapped to which node at the preorder
   *        position y in the target tree. If x is ALPHA, then it shows
   *        the node at the preorder position y in the target tree is
   *        inserted. If y is ALPHA, then it shows the node at the preorder
   *        position x in the souce tree is deleted.
   */
  protected static DictionaryPair computeMIN_M(
      DictionaryPair dictionaryPair, Tree sourceTree, Tree targetTree) {
    Map<KeyForE, Integer> E = dictionaryPair.costs;
    Map<KeyForE, MappingList> mappingForE = dictionaryPair.mapping;

    Map<IntPairAsKey, Integer> MIN_M = new HashMap<>();
    MIN_M.put(keyForMIN_M(1, 1), 0);
    Map<IntPairAsKey, MappingList> mappingForMinM = new HashMap<>();
    mappingForMinM.put(keyForMIN_M(1, 1), new MappingList(1, 1));
    DictionaryPair<IntPairAsKey> newDictionaryPair =
        new DictionaryPair<>(MIN_M, mappingForMinM);

    // This part is missing in the paper
    for (int j = 2; j < targetTree.size(); j++) {
      MIN_M.put(
          keyForMIN_M(1, j),
          MIN_M.get(keyForMIN_M(1, j - 1)) +
              r(Constants.ALPHA, targetTree.nodeAt(j)));
      mappingForMinM.put(
          keyForMIN_M(1, j),
          mappingForMinM.get(keyForMIN_M(1, j - 1))
                        .clone()
                        .add(Constants.ALPHA_INT, j));
    }

    // This part is missing in the paper
    for (int i = 2; i < sourceTree.size(); i++) {
      MIN_M.put(keyForMIN_M(i, 1),
                MIN_M.get(keyForMIN_M(i - 1, 1)) +
                    r(sourceTree.nodeAt(i), Constants.ALPHA));
      mappingForMinM.put(
          keyForMIN_M(i, 1),
          mappingForMinM.get(keyForMIN_M(i - 1, 1))
                        .clone()
                        .add(i, Constants.ALPHA_INT));
    }

    for (int i = 2; i <= sourceTree.size(); i++) {
      for (int j = 2; j <= targetTree.size(); j++) {
        IntPairAsKey keyForMIN_M_i_j = keyForMIN_M(i, j);
        MIN_M.put(keyForMIN_M_i_j, INFINITE);
        int f_i = sourceTree.fatherOf(i).preorderPosition();
        int f_j = targetTree.fatherOf(j).preorderPosition();
        
        for (int s : sourceTree.ancestors(f_i)) {
          for (int t : targetTree.ancestors(f_j)) {
            KeyForE dependentKeyForE =
                new KeyForE(s, f_i, i - 1, t, f_j, j - 1);
            IntPairAsKey dependentKeyForM = keyForMIN_M(s, t);
            int temp = MIN_M.get(dependentKeyForM) +
                       E.get(dependentKeyForE) -
                       r(sourceTree.nodeAt(s), targetTree.nodeAt(t));
            MIN_M.put(keyForMIN_M_i_j,
                      Math.min(temp, MIN_M.get(keyForMIN_M_i_j)));
            if (temp == MIN_M.get(keyForMIN_M_i_j)) {
              mappingForMinM.put(
                  keyForMIN_M_i_j,
                  mappingForMinM
                      .get(dependentKeyForM)
                      .clone()
                      .extendUniquelyWith(
                          mappingForE.get(dependentKeyForE)));
            }
          }
        }

        MIN_M.put(
            keyForMIN_M_i_j,
            MIN_M.get(keyForMIN_M_i_j) +
            r(sourceTree.nodeAt(i), targetTree.nodeAt(j)));
        mappingForMinM.get(keyForMIN_M_i_j).add(i, j);
      }
    }
    System.out.printf("dictionary after computeMIN_M: %s\n", newDictionaryPair);
    return newDictionaryPair;
  }

  // Returns the key for D map
  protected static IntPairAsKey keyForD(int i, int j) {
    //return String.format("%d, %d", i, j);
    return new IntPairAsKey(i, j);
  }

  /**
   * Returns the D mapping. Check out the article to see
   * what the mapping mean
   *
   * @param sourceTree the source tree (Tree)
   * @param targetTree the target tree (Tree)
   * @param MIN_M the MIN_M map (dict)
   * @param mappingForM the transformation details for MIN_M
   * @return (dict, dict)
   *        The first dict is the D mapping (key to cost).
   *        The second dict is (key to list of integer pairs) the transformation mapping
   *        where a pair (x, y) shows which node at the preorder position x
   *        in the source tree is mapped to which node at the preorder
   *        position y in the target tree. If x is ALPHA, then it shows
   *        the node at the preorder position y in the target tree is
   *        inserted. If y is ALPHA, then it shows the node at the preorder
   *        position x in the souce tree is deleted.
   */
  protected static DictionaryPair<IntPairAsKey> computeD(
      Tree sourceTree, Tree targetTree, DictionaryPair dictionaryPair) {
    Map<IntPairAsKey, Integer> MIN_M = dictionaryPair.costs;
    Map<IntPairAsKey, MappingList> mappingForMinM = dictionaryPair.mapping;
    Map<IntPairAsKey, Integer> D = new HashMap<>();
    D.put(keyForD(1, 1), 0); 
    Map<IntPairAsKey, MappingList> mappingForD = new HashMap<>();
    mappingForD.put(keyForD(1, 1), new MappingList(1, 1));
    DictionaryPair<IntPairAsKey> newDictionaryPair =
        new DictionaryPair<>(D, mappingForD);

    for (int i = 2; i <= sourceTree.size(); i++) {
      D.put(keyForD(i, 1),
            D.get(keyForD(i - 1, 1)) + r(sourceTree.nodeAt(i), Constants.ALPHA));
    }

    for (int j = 2; j <= targetTree.size(); j++) {
      D.put(keyForD(1, j),
            D.get(keyForD(1, j - 1)) + r(Constants.ALPHA, targetTree.nodeAt(j)));
      mappingForD.put(keyForD(1, j),
        mappingForD.get(keyForD(1, j - 1)).clone().add(Constants.ALPHA_INT, j));
    }

    for (int i = 2; i <= sourceTree.size(); i++) {
      for (int j = 2; j <= targetTree.size(); j++) {
        int option1 =
            D.get(keyForD(i, j - 1)) + r(Constants.ALPHA, targetTree.nodeAt(j));
        int option2 =
            D.get(keyForD(i - 1, j)) + r(sourceTree.nodeAt(i), Constants.ALPHA);
        int option3 = MIN_M.get(keyForMIN_M(i, j));
        D.put(keyForD(i, j),
              Collections.min(Arrays.asList(option1, option2, option3)));

        if (D.get(keyForD(i, j)) == option1) {
          mappingForD.put(
            keyForD(i,  j),
            mappingForD.get(keyForD(i, j - 1)).clone().add(Constants.ALPHA_INT, j));
        }
        else if (D.get(keyForD(i, j)) == option2) {
          mappingForD.put(
            keyForD(i,  j),
            mappingForD.get(keyForD(i - 1, j)).clone().add(i, Constants.ALPHA_INT));
        }
        else {
          mappingForD.put(
            keyForD(i,  j),
            mappingForMinM.get(keyForMIN_M(i, j)));
        }
      }
    }
    System.out.printf("dictionary after computeD: %s\n", newDictionaryPair);
    return newDictionaryPair;
  }
}

class DictionaryPair<T> {
  Map<T, Integer> costs;
  Map<T, MappingList> mapping;
  public DictionaryPair(
      Map<T, Integer> costs,
      Map<T, MappingList> mapping) {
    this.costs = costs;
    this.mapping = mapping;
  }

  public String toString() {
    return String.format("costs size: %d; mapping size: %d",
                         costs.size(),
                         mapping.size());
  }
}

class KeyForE {
  int mA, mB, mC, mP, mQ, mR;

  public KeyForE(int a, int b, int c, int p, int q, int r) {
    mA = a;
    mB = b;
    mC = c;
    mP = p;
    mQ = q;
    mR = r;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof KeyForE) {
      KeyForE e = (KeyForE) o;
      return mA == e.mA && mB == e.mB && mC == e.mC &&
             mP == e.mP && mQ == e.mQ && mR == e.mR;
    }
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 37 * result + mA;
    result = 37 * result + mB;
    result = 37 * result + mC;
    result = 37 * result + mP;
    result = 37 * result + mQ;
    return 37 * result + mR;
  }
}

class IntPairAsKey {
  int mA, mB;

  public IntPairAsKey(int a, int b) {
    mA = a;
    mB = b;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof IntPairAsKey) {
      IntPairAsKey k = (IntPairAsKey) o;
      return mA == k.mA && mB == k.mB;
    }
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    return 37 * 37 + 37 * mA + mB;
  }
}