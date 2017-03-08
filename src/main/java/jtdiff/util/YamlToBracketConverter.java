package jtdiff.util;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Encodes a tree in bracket notation.
 *
 * Example:
 *   Let's say we have a tree as illustrated in a yaml encoding below:
 *
 *    A:
 *      - B:
 *        - X:
 *        - Y:
 *        - F:
 *      - C:
 *
 * This class encodes the tree into the following:
 *
 *   {A{B{X}{Y}{F}}{C}}
 * 
 */
public class YamlToBracketConverter {

  public static String convert(String yaml) throws Exception {
    Reader reader = new StringReader(yaml);
    return convert(reader);
  }

  public static String convert(InputStream stream) throws Exception {
    Reader reader = new InputStreamReader(stream);
    return convert(reader);
  }

  private static String convert(Reader reader) throws Exception {
    YamlReader yamlReader = new YamlReader(reader);
    StringBuffer buffer = new StringBuffer();
    while (true) {
      Object o = yamlReader.read();
      if (o == null) break;
        Map treeMap = (Map) o;
        Map.Entry<String, List> entry =
            (Map.Entry<String, List>) treeMap.entrySet().iterator().next();
        processNode(entry, buffer);
    }
    return buffer.toString();
  }

  private static void processNode(Map.Entry<String, List> node,
                                  StringBuffer buffer) {
    String label = node.getKey();
    if (label.equals("{")) {
      label = "\\opencurl";
    } else if (label.equals("}")) {
      label = "\\closecurl";
    }

    buffer.append("{")
          .append(label);

    Object o = node.getValue();
    if (!(o instanceof String)) {
      List<Map> children = (List<Map>) o;
      for (Map child : children) {
        Map.Entry<String, List> childNode =
            (Map.Entry<String, List>) child.entrySet().iterator().next();
        processNode(childNode, buffer);
      }
    }
    buffer.append("}");
  }
}