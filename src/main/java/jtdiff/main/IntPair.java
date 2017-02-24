package jtdiff.main;

public class IntPair {
  int x, y;

  public IntPair(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int source() {
    return x;
  }

  public int target() {
    return y;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof IntPair) {
      IntPair ip = (IntPair) o;
      return ip.x == x && ip.y == y;
    }
    return super.equals(o);
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }
}