package jtdiff.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TestEditOperation {
  EditOperation mEditOperation;

  @Before
  public void setUp() {
    mEditOperation = new EditOperation();
  }

  @Test
  public void testType() {
    mEditOperation.type(EditOperation.Type.INSERT);
    assertEquals(EditOperation.Type.INSERT, mEditOperation.type());

    mEditOperation.type(EditOperation.Type.DELETE);
    assertEquals(EditOperation.Type.DELETE, mEditOperation.type());

    mEditOperation.type(EditOperation.Type.CHANGE);
    assertEquals(EditOperation.Type.CHANGE, mEditOperation.type());

    mEditOperation.type(EditOperation.Type.NO_CHANGE);
    assertEquals(EditOperation.Type.NO_CHANGE, mEditOperation.type());
  }

  @Test
  public void testSourceNodePosition() {
    mEditOperation.sourceNodePosition(42);
    assertEquals(42, mEditOperation.sourceNodePosition());
  }

  @Test
  public void testTargetNodePosition() {
    mEditOperation.targetNodePosition(24);
    assertEquals(24, mEditOperation.targetNodePosition());
  }

  @Test
  public void testSourceNodeLabel() {
    mEditOperation.sourceNodeLabel("source");
    assertEquals("source", mEditOperation.sourceNodeLabel());
  }

  @Test
  public void testTargetNodeLabel() {
    mEditOperation.targetNodeLabel("target");
    assertEquals("target", mEditOperation.targetNodeLabel());
  }

  @Test
  public void testToString() {
    mEditOperation
        .type(EditOperation.Type.INSERT)
        .sourceNodePosition(42)
        .sourceNodeLabel("source")
        .targetNodePosition(24)
        .targetNodeLabel("target");
    assertEquals("<INSERT, 42, source, 24, target>",
                 mEditOperation.toString());
  }
}