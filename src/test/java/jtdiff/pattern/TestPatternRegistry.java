package jtdiff.pattern;

import jtdiff.antlr.ParseTreeCache;
import jtdiff.antlr.ParseTreeWrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class TestPatternRegistry {
  PatternViewTemplate mPatternViewTemplate;

  @Before
  public void setUp() {}

  @Test
  public void testParameterChangePatternViewGeneration() throws Exception {
    String originalCode =
        "public interface I {\n" +
        "  public boolean m(int apples);\n" +
        "}";
    String updatedCode =
        "public interface I {\n" +
        "  public boolean m(int oranges);\n" +
        "}";
    InputStream sourceInputStream =
        new ByteArrayInputStream(originalCode.getBytes());
    ParseTreeCache sourceParseTreeCache =
        new ParseTreeCache().build(sourceInputStream);

    InputStream targetInputStream =
        new ByteArrayInputStream(updatedCode.getBytes());
    ParseTreeCache targetParseTreeCache =
        new ParseTreeCache().build(targetInputStream);

    DataExtractionContext ctx = new DataExtractionContext()
        .sourceParser(sourceParseTreeCache.parser())
        .targetParser(targetParseTreeCache.parser());
    ParseTreeWrapper nodeInOriginalCode =
        sourceParseTreeCache.getParseTreeAt(32);
    ParseTreeWrapper nodeInUpdatedCode =
        targetParseTreeCache.getParseTreeAt(32);
    String returnedView = PatternRegistry.PARAMETER_CHANGE_PATTERN
        .viewTemplate()
        .generateView(ctx, nodeInOriginalCode, nodeInUpdatedCode);

    String expectedView =
        "{{parameter_index}}th parameter's name of " +
        "method {{method_name}} is " +
        "changed from apples to oranges " +
        "(original line {{source_line_no}} and updated " +
        "line {{target_line_no}}";
    // System.out.println("Expected:\n" + expectedView);
    // System.out.println("Returnde:\n" + returnedView);
    assertEquals(expectedView, returnedView);
  }

  @Test
  public void testParameterChangePatternSourcePatternExtraction()
  throws Exception {
    String originalCode =
        "public interface I {\n" +
        "  public boolean m(int apples);\n" +
        "}";
    String updatedCode =
        "public interface I {\n" +
        "  public boolean m(int oranges);\n" +
        "}";
    InputStream sourceInputStream =
        new ByteArrayInputStream(originalCode.getBytes());
    ParseTreeCache sourceParseTreeCache =
        new ParseTreeCache().build(sourceInputStream);

    InputStream targetInputStream =
        new ByteArrayInputStream(updatedCode.getBytes());
    ParseTreeCache targetParseTreeCache =
        new ParseTreeCache().build(targetInputStream);

    DataExtractionContext ctx = new DataExtractionContext()
        .sourceParser(sourceParseTreeCache.parser())
        .targetParser(targetParseTreeCache.parser());
    ParseTreeWrapper nodeInOriginalCode =
        sourceParseTreeCache.getParseTreeAt(32);
    ParseTreeWrapper nodeInUpdatedCode =
        targetParseTreeCache.getParseTreeAt(32);
    String sourcePattern = PatternRegistry.PARAMETER_CHANGE_PATTERN
        .sourcePatternExtraction()
        .extract(ctx, nodeInOriginalCode, nodeInUpdatedCode);
    // See the output of ParseTreeCache for the numbers.
    assertEquals("56, 77", sourcePattern);
  }

  @Test
  public void testPatternMatch() throws Exception {
    String originalCode =
        "public interface I {\n" +
        "  public boolean m(int apples);\n" +
        "}";
    String updatedCode =
        "public interface I {\n" +
        "  public boolean m(int oranges);\n" +
        "}";
    InputStream sourceInputStream =
        new ByteArrayInputStream(originalCode.getBytes());
    ParseTreeCache sourceParseTreeCache =
        new ParseTreeCache().build(sourceInputStream);

    InputStream targetInputStream =
        new ByteArrayInputStream(updatedCode.getBytes());
    ParseTreeCache targetParseTreeCache =
        new ParseTreeCache().build(targetInputStream);

    DataExtractionContext ctx = new DataExtractionContext()
        .sourceParser(sourceParseTreeCache.parser())
        .targetParser(targetParseTreeCache.parser());
    ParseTreeWrapper nodeInOriginalCode =
        sourceParseTreeCache.getParseTreeAt(32);
    ParseTreeWrapper nodeInUpdatedCode =
        targetParseTreeCache.getParseTreeAt(32);

    Pattern pattern = PatternRegistry.match(
        ctx, nodeInOriginalCode, nodeInUpdatedCode);
    assertEquals(PatternRegistry.PARAMETER_CHANGE_PATTERN, pattern);
  }

  @Test
  public void testNoPatternMatch() throws Exception {
    String originalCode =
        "public interface I {\n" +
        "  public boolean m(int apples);\n" +
        "}";
    String updatedCode =
        "public interface I {\n" +
        "  public boolean m(int oranges);\n" +
        "}";
    InputStream sourceInputStream =
        new ByteArrayInputStream(originalCode.getBytes());
    ParseTreeCache sourceParseTreeCache =
        new ParseTreeCache().build(sourceInputStream);

    InputStream targetInputStream =
        new ByteArrayInputStream(updatedCode.getBytes());
    ParseTreeCache targetParseTreeCache =
        new ParseTreeCache().build(targetInputStream);

    DataExtractionContext ctx = new DataExtractionContext()
        .sourceParser(sourceParseTreeCache.parser())
        .targetParser(targetParseTreeCache.parser());
    ParseTreeWrapper nodeInOriginalCode =
        sourceParseTreeCache.getParseTreeAt(3);
    ParseTreeWrapper nodeInUpdatedCode =
        targetParseTreeCache.getParseTreeAt(5);

    Pattern pattern = PatternRegistry.match(
        ctx, nodeInOriginalCode, nodeInUpdatedCode);
    assertNull(pattern);
  }
}