package jtdiff.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

public class TestYamlToBracketConverter {

  @Before
  public void setUp() throws Exception {}

  @Test
  public void test() throws Exception {
    String yaml = "A:\n" +
                  "  - B:\n" +
                  "    - X:\n" +
                  "    - Y:\n" +
                  "    - F:\n" +
                  "  - C:";
    String output = YamlToBracketConverter.convert(yaml);
    assertEquals("{A{B{X}{Y}{F}}{C}}", output);
  }

  @Test
  public void testInput1() throws Exception {
    InputStream inputStream = getInputStream(
        "./src/test/java/jtdiff/util/testData/" +
        "TestYamlToBracketConverter/input1");

    String expectedOutput = new String(Files.readAllBytes(
        Paths.get(
            "./src/test/java/jtdiff/util/testData/" +
            "TestYamlToBracketConverter/expectedOutput1")));
    String output = YamlToBracketConverter.convert(inputStream);
    // System.out.println("Expected:\n" + expectedOutput);
    // System.out.println("Returned:\n" + output);
    assertEquals(expectedOutput, output);

    inputStream.close();
  }

  @Test
  public void testInput2() throws Exception {
    InputStream inputStream = getInputStream(
        "./src/test/java/jtdiff/util/testData/" +
        "TestYamlToBracketConverter/input2");

    String expectedOutput = new String(Files.readAllBytes(
        Paths.get(
            "./src/test/java/jtdiff/util/testData/" +
            "TestYamlToBracketConverter/expectedOutput2")));
    String output = YamlToBracketConverter.convert(inputStream);
    // System.out.println("Expected:\n" + expectedOutput);
    // System.out.println("Returned:\n" + output);
    assertEquals(expectedOutput, output);

    inputStream.close();
  }

  private InputStream getInputStream(String fileName) throws Exception {
    File file = new File(fileName);
    FileInputStream fis = null;
    return new FileInputStream(file);
  }
}