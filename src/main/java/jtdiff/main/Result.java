package jtdiff.main;

public class Result {
  int cost;  // Distance
  MappingList mapping;  // Mapping from source to target tree

  public Result(int cost, MappingList mapping) {
    this.cost = cost;
    this.mapping = mapping;
  }

  public int cost() { 
    return cost;
  }

  public MappingList mapping() {
    return mapping;
  }
}