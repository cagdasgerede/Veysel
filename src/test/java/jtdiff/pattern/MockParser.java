package jtdiff.pattern;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.atn.ATN;

public class MockParser extends Parser {
  public MockParser() {
    super(null);
  }
  public ATN getATN() { return null; }
  public String getGrammarFileName() { return ""; }
  public String[] getRuleNames() { return null; }
  public String[] getTokenNames() { return null; }
};