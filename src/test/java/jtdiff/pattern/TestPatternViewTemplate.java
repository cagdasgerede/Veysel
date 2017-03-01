package jtdiff.pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TestPatternViewTemplate {
  PatternViewTemplate mPatternViewTemplate;

  @Before
  public void setUp() {}

  @Test
  public void testInitialization() {
    String template = "template";
    DataExtraction extraction1 = (c, s, t) -> { return ""; };
    DataExtraction extraction2 = (c, s, t) -> { return ""; };
    mPatternViewTemplate = new PatternViewTemplate(
        template,
        extraction1,
        extraction2);
    assertEquals(template, mPatternViewTemplate.mTemplate);
    assertTrue(extraction1 == mPatternViewTemplate.mDataExtractions[0]);
    assertTrue(extraction2 == mPatternViewTemplate.mDataExtractions[1]);
  }

  @Test
  public void testGenerateView() {
    String template = "My favorite fruits are %s and %s.";
    DataExtraction extraction1 = (c, s, t) -> { return "apples"; };
    DataExtraction extraction2 = (c, s, t) -> { return "oranges"; };
    mPatternViewTemplate = new PatternViewTemplate(
        template,
        extraction1,
        extraction2);
    assertEquals(
        "My favorite fruits are apples and oranges.",
        mPatternViewTemplate.generateView(null, null, null));
  }
}