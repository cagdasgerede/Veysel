package jtdiff.util;

import java.io.IOException;
import java.io.PrintWriter;

public class ImageFromDot {
  public static void generatePngFromDot(String dotRepresentation, String imageFileName) throws IOException {
    PrintWriter writer = new PrintWriter("/tmp/tmp.dot", "UTF-8");
    writer.print(dotRepresentation);
    writer.close();
    
    new ProcessBuilder("dot","-Tpng","/tmp/tmp.dot", "-o", imageFileName).start();
    new ProcessBuilder("rm","/tmp/tmp.dot").start();
  }
}
