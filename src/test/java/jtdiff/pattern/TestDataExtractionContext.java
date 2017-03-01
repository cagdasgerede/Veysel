package jtdiff.pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TestDataExtractionContext {
  DataExtractionContext mDataExtractionContext;

  @Before
  public void setUp() {
    mDataExtractionContext = new DataExtractionContext();
  }

  @Test
  public void testSourceParser() {
    MockParser parser = new MockParser();
    mDataExtractionContext.sourceParser(parser);
    assertTrue(parser == mDataExtractionContext.sourceParser());
  }

  @Test
  public void testTargetParser() {
    MockParser parser = new MockParser();
    mDataExtractionContext.targetParser(parser);
    assertTrue(parser == mDataExtractionContext.targetParser());
  }
}