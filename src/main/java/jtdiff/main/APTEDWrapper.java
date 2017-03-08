package jtdiff.main;


import distance.APTED; // apted.jar
import util.LblTree;  // apted.jar

import java.util.LinkedList;

public class APTEDWrapper {
  public static Result diff(String ts1, String ts2) {
    APTED rted = new APTED(1, 1, 1);
    LblTree lt1, lt2;
    lt1 = LblTree.fromString(ts1);
    lt2 = LblTree.fromString(ts2);
    double distance = rted.nonNormalizedTreeDist(lt1, lt2);
    
    MappingList mapping = new MappingList();
    LinkedList<int[]> editMapping = rted.computeEditMapping();
    for (int[] nodeAlignment : editMapping) {
      mapping.add(nodeAlignment[0], nodeAlignment[1]);
    }

    return new Result((int)distance, mapping);
  }
}