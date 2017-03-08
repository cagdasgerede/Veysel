package jtdiff.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

// import java.io.File;
// import java.io.FileInputStream;
// import java.io.InputStream;
// import java.nio.file.Files;
// import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

public class TestAPTEDWrapper {

  @Before
  public void setUp() throws Exception {}

  @Test
  public void testDiff() {
    Result result = APTEDWrapper.diff("{A{B{D}}}", "{A{B}{C{D}}}");
    assertEquals(2, result.cost());
    assertEquals(4, result.mapping().pairs().size());

    result.mapping().contains(3, 4);
    result.mapping().contains(2, 3);
    result.mapping().contains(1, 2);
    result.mapping().contains(0, 1);
  }

  // @Test
  // public void testInput1() throws Exception {
  //   InputStream inputStream = getInputStream(
  //       "./src/test/java/jtdiff/util/testData/" +
  //       "TestYamlToBracketConverter/input1");

  //   String expectedOutput = new String(Files.readAllBytes(
  //       Paths.get(
  //           "./src/test/java/jtdiff/util/testData/" +
  //           "TestYamlToBracketConverter/expectedOutput1")));
  //   String output = YamlToBracketConverter.convert(inputStream);
  //   //System.out.println("Expected:\n" + expectedOutput);
  //   //System.out.println("Returned:\n" + output);
  //   assertEquals(expectedOutput, output);

  //   inputStream.close();
  // }

  // private InputStream getInputStream(String fileName) throws Exception {
  //   File file = new File(fileName);
  //   FileInputStream fis = null;
  //   return new FileInputStream(file);
  // }
}