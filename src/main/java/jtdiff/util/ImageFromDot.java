package jtdiff.util;

import java.io.IOException;
import java.io.PrintWriter;

public class ImageFromDot {
  public static void generatePngFromDot(String dotRepresentation, String imageFileName) throws IOException, InterruptedException {
    PrintWriter writer = new PrintWriter("/tmp/tmp.dot", "UTF-8");
    writer.print(dotRepresentation);
    writer.close();
    
    Process pngGeneration = new ProcessBuilder("dot","-Tpng","/tmp/tmp.dot", "-o", imageFileName).start();
    pngGeneration.waitFor();
    Process removePng = new ProcessBuilder("rm","/tmp/tmp.dot").start();
    removePng.waitFor();
  }
}
