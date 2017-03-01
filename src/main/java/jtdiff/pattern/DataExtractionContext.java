package jtdiff.pattern;

import org.antlr.v4.runtime.Parser;

public class DataExtractionContext {
  private Parser mSourceParser;
  private Parser mTargetParser;

  public DataExtractionContext() {}

  public DataExtractionContext sourceParser(Parser parser) {
    mSourceParser = parser;
    return this;
  }

  public Parser sourceParser() {
    return mSourceParser;
  }

  public DataExtractionContext targetParser(Parser parser) {
    mTargetParser = parser;
    return this;
  }

  public Parser targetParser() {
    return mTargetParser;
  }
}