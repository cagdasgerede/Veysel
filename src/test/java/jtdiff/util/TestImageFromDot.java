package jtdiff.util;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestImageFromDot {
  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testImageFromDot() throws IOException, InterruptedException {
    String dot = new StringBuilder()
        .append("digraph G {\n")
        .append("subgraph { rank = same; SourceA; TargetA }\n")
        .append("SourceA -> SourceB\n")
        .append("SourceB -> SourceD\n")
        .append("TargetA -> TargetB\n")
        .append("TargetA -> TargetC\n")
        .append("TargetC -> TargetD\n")
        .append("SourceA -> TargetA [style=dotted color=\"green\" constraint=false]\n")
        .append("SourceB -> TargetC [style=dotted color=\"gray\" constraint=false]\n")
        .append("SourceD -> TargetD [style=dotted color=\"green\" constraint=false]\n")
        .append("TargetB [color=\"orange\"]\n")
        .append("}")
        .toString();
    String fileName = "/tmp/testImageFromDot.png";
    ImageFromDot.generatePngFromDot(dot, fileName);
    File f = new File(fileName);
    
    assertTrue(f.exists() && !f.isDirectory());

    // Delete the png file
    try {
      Process p = new ProcessBuilder("rm", fileName).start();
      p.waitFor();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // This was useful for examining what happens in the travis ci's file system.
  // during this test was running.
  private void debug(String path) {
    File folder = new File(path);
    File[] listOfFiles = folder.listFiles();

    for (int i = 0; i < listOfFiles.length; i++) {
      if (listOfFiles[i].isFile()) {
        System.out.println("File " + listOfFiles[i].getName());
      } else if (listOfFiles[i].isDirectory()) {
        System.out.println("Directory " + listOfFiles[i].getName());
      }
    }
  }
}