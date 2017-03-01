package jtdiff.pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TestPattern {
  Pattern mPattern;

  @Before
  public void setUp() {
    mPattern = new Pattern();
  }

  @Test
  public void testHumandFriendlySourcePattern() {
    mPattern.humanFriendlySourcePattern("source");
    assertEquals("source", mPattern.humanFriendlySourcePattern());
  }

  @Test
  public void testSourcePattern() {
    mPattern.sourcePattern("123");
    assertEquals("123", mPattern.sourcePattern());
  }

  @Test
  public void testSourcePatternExtraction() {
    DataExtraction extraction = (ctx, s, t) -> { return ""; };
    mPattern.sourcePatternExtraction(extraction);
    assertTrue(extraction == mPattern.sourcePatternExtraction());
  }

  @Test
  public void testHumandFriendlyTargetPattern() {
    mPattern.humanFriendlyTargetPattern("target");
    assertEquals("target", mPattern.humanFriendlyTargetPattern());
  }

  @Test
  public void testTargetPattern() {
    mPattern.targetPattern("321");
    assertEquals("321", mPattern.targetPattern());
  }

  @Test
  public void testTargetPatternExtraction() {
    DataExtraction extraction = (ctx, s, t) -> { return ""; };
    mPattern.targetPatternExtraction(extraction);
    assertTrue(extraction == mPattern.targetPatternExtraction());
  }

  @Test
  public void testViewTemplate() {
    PatternViewTemplate template = new PatternViewTemplate(
        "template");
    mPattern.viewTemplate(template);
    assertTrue(template == mPattern.viewTemplate());
  }
}