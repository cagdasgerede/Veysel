package jtdiff.main;

import jtdiff.util.DiffToDot;
import jtdiff.util.ImageFromDot;
import jtdiff.util.MappingUtil;
import jtdiff.util.Tree;
import jtdiff.util.YAMLToTree;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogManager;

/**
 * Run this by itself (under src/main/java:
 *    javac -cp .:/usr/local/lib/yamlbeans-1.09.jar jtdiff/main/Demo.java; \
 *    java -Djava.util.logging.config.file=jtdiff/main/demo.logging.properties -cp .:/usr/local/lib/yamlbeans-1.09.jar jtdiff.main.Demo ../../main/java/jtdiff/main/demoTreesInYaml.yml
 *
 * Run this by maven (under the root folder):
 *    mvn compile; \
 *    mvn exec:java -Dexec.mainClass="jtdiff.main.Demo"
 *
 * Log file is available at (As defined in src/main/java/jtdiff/main/demo.logging.properties):
 *    /tmp/koroglu_demo.log
 */
public class Demo {
  private final static Logger LOGGER = Logger.getLogger(Demo.class.getName());
  private final static String LOCATION_FOR_DEMO_TREES_IN_YAML =
      "src/main/java/jtdiff/main/demoTreesInYaml.yml";
  private final static String LOCATION_FOR_PNG_IMAGE = "/tmp/demoDiffImage.png";
  private final static String LOCATION_FOR_LOG_PROPERTIES_FILE_FOR_MAVEN =
      "src/main/java/jtdiff/main/demo.logging.properties";

  public static void main(String[] args) throws Exception {
    LOGGER.setLevel(Level.INFO);
    LOGGER.info("Info Log");

    if (args.length == 0) { // Started by maven
        // It is tricky to configure the logging.properties file location
        // for maven. Therefore
        Properties preferences = new Properties();
        try {
            java.io.FileInputStream configFile =
                new java.io.FileInputStream(
                    LOCATION_FOR_LOG_PROPERTIES_FILE_FOR_MAVEN);
            LogManager.getLogManager().readConfiguration(configFile);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(
                "WARNING: Could not open configuration file");
            System.out.println(
                "WARNING: Logging not configured (console output only)");
            return;
        }
    }

    // When called with maven (Yaml file location
    // is different when called from maven vs. java. So we
    // pass an explicit argument for that when executed from java)
    String yamlFileLocation = LOCATION_FOR_DEMO_TREES_IN_YAML;
    if (args.length > 0) { // Started without maven
      yamlFileLocation = args[0];
    }
    InputStreamReader reader = new FileReader(yamlFileLocation);
    List<Tree> trees = YAMLToTree.buildTreesFromYamlInput(reader);
    Iterator<Tree> iter = trees.iterator();
    Tree sourceTree = iter.next();
    Tree targetTree = iter.next();
    
    Result result = TreeDiff.computeDiff(sourceTree, targetTree);
    LOGGER.info("Cost: " + result.cost);
    LOGGER.info("Difference: " + MappingUtil.produceHumanFriendlyMapping(
        result.mapping, sourceTree, targetTree));

    String dot = DiffToDot.generateDotFromDiff(
        sourceTree, targetTree, result.mapping);
    LOGGER.info("Dot representation:\n" + dot);

    String pngLocation = LOCATION_FOR_PNG_IMAGE;
    LOGGER.info(String.format(
        "Creating a png image at \"%s\" from the dot...", pngLocation));
    ImageFromDot.generatePngFromDot(dot, pngLocation);
    LOGGER.info("Starting the image viewer...");
    new ProcessBuilder("eog", pngLocation).start();
  }
}