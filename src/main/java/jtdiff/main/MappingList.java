package jtdiff.main;

import java.util.ArrayList;

/**
 * Records which tree node in the source tree at a preorder position is mapped to
 * which other node at a preorder position in the target tree.
 */
public class MappingList {
  ArrayList<IntPair> mList = new ArrayList<>();

  public MappingList() {}

  public MappingList(int x, int y) {
    add(x, y);
  }

  public MappingList add(int x, int y) {
    mList.add(new IntPair(x, y));
    return this;
  }

  public MappingList clone() {
    MappingList m = new MappingList();
    for (IntPair pair : mList) {
      m.mList.add(pair);
    }
    return m;
  }

  public boolean contains(IntPair ip) {
    for (IntPair pair : mList) {
      if (pair.equals(ip)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof MappingList) {
      ArrayList<IntPair> list = ((MappingList) o).mList;
      if (mList.size() != list.size()) {
        return false;
      }
      for (IntPair ip : list) {
        if (!mList.contains(ip)) {
          return false;
        }
      }
      return true;
    }
    return super.equals(o);
  }

  public MappingList extendWith(MappingList list) {
    for (IntPair ip : list.mList) {
      mList.add(ip);
    }
    return this;
  }

  public MappingList extendUniquelyWith(MappingList list) {
    for (IntPair ip : list.mList) {
      if (mList.contains(ip)) {
        continue;
      }
      mList.add(ip);
    }
    return this;
  }

  public ArrayList<IntPair> pairs() {
    return mList;
  }

  public String toString() {
    return mList.toString();
  }
}