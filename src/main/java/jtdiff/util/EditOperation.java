package jtdiff.util;

public class EditOperation {
  public enum Type {
    INSERT,
    DELETE,
    CHANGE,
    NO_CHANGE
  };
  Type mType;

  public final int UNDEFINED = -1;
  int mSourceNodePosition = UNDEFINED;
  int mTargetNodePosition = UNDEFINED;
  String mSourceNodeLabel = null;
  String mTargetNodeLabel = null;

  public EditOperation type(Type type) {
    mType = type;
    return this;
  }

  public Type type() {
    return mType;
  }

  public int sourceNodePosition() {
    return mSourceNodePosition;
  }

  public EditOperation sourceNodePosition(int position) {
    mSourceNodePosition = position;
    return this;
  }

  public int targetNodePosition() {
    return mTargetNodePosition;
  }

  public EditOperation targetNodePosition(int position) {
    mTargetNodePosition = position;
    return this;
  }

  public String sourceNodeLabel() {
    return mSourceNodeLabel;
  }

  public EditOperation sourceNodeLabel(String sourceNodeLabel) {
    mSourceNodeLabel = sourceNodeLabel;
    return this;
  }

  public String targetNodeLabel() {
    return mTargetNodeLabel;
  }

  public EditOperation targetNodeLabel(String targetNodeLabel) {
    mTargetNodeLabel = targetNodeLabel;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof EditOperation) {
      EditOperation e = (EditOperation) o;
      return e.mType == mType &&
             e.mSourceNodePosition == mSourceNodePosition &&
             e.mSourceNodeLabel == mSourceNodeLabel &&
             e.mTargetNodePosition == mTargetNodePosition &&
             e.mTargetNodeLabel == mTargetNodeLabel;
    }
    return super.equals(o);
  }

  @Override
  public String toString() {
    return String.format(
        "<%s, %s, %s, %s, %s>",
        mType,
        mSourceNodePosition,
        mSourceNodeLabel,
        mTargetNodePosition,
        mTargetNodeLabel);
  }
}