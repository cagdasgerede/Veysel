package jtdiff.antlr;

import jtdiff.antlr.generated.Java8Lexer;
import jtdiff.antlr.generated.Java8Parser;

import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import java.io.InputStream;

/**
 * Builds a cache with the parse tree of the given Java source code in addition
 * to some indices.
 *
 * Run:
 *   mvn exec:java -Dexec.mainClass="jtdiff.antlr.ParseTreeCache" -Dexec.args="32"
 *
 * Then enter, for instance:
 *   public interface I {
 *     public boolean m(int param);
 *   }
 *   (Ctrl+D / EOF)
 *
 *
 * Above 32 corresponds to the node for the parameter name "param".
 */
public class ParseTreeCache {
  private Map<Integer, ParseTreeWrapper> mPreorderPositionToParseTree =
      new HashMap<>();
  private Map<Integer, ParseTreeWrapper> mPostorderPositionToParseTree =
      new HashMap<>();
  private String mYamlSerialization;
  private Java8Parser mParser;

  public static void main(String[] args) throws Exception {
    ParseTreeCache cache = new ParseTreeCache();
    cache.build(System.in);
    System.out.println(cache.yamlSerialization());
    System.out.println(cache.getPreorderPositionToParseTreeMap());
    if (args.length > 0) {
      // Map the given int (preorder position) to the ParseTree root
      ParseTreeWrapper t = cache.getParseTreeAtPreorderPosition(
          Integer.valueOf(args[0]));
      System.out.println("Name of the node: " + t);

      ParseTree parseTree = t.parseTree();
      // Print the path to the root from the node
      System.out.println("Path to the root:");
      while (parseTree.getParent() != null) {
        parseTree = parseTree.getParent();
        System.out.println(ParseTreeUtil.getName(parseTree, cache.parser()));
        System.out.println(((RuleContext) parseTree).getRuleIndex());
      }
    }
  }

  public ParseTreeCache build(InputStream inputStream) throws Exception {
    ANTLRInputStream input = new ANTLRInputStream(inputStream);
    Java8Lexer lexer = new Java8Lexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    mParser = new Java8Parser(tokens);
    ParseTree tree = mParser.compilationUnit();
    ParseTreeWalker walker = new ParseTreeWalker();

    TokenStream tokenStream = mParser.getTokenStream();
    YamlSerializer serializer = new YamlSerializer(
        tokenStream,
        mParser,
        mPreorderPositionToParseTree,
        mPostorderPositionToParseTree);
    walker.walk(serializer, tree);

    mYamlSerialization = serializer.serialization();
    return this;
  }

  public String yamlSerialization() {
    return mYamlSerialization;
  }

  public ParseTreeWrapper getParseTreeAtPreorderPosition(int position) {
    return mPreorderPositionToParseTree.get(position);
  }

  public ParseTreeWrapper getParseTreeAtPostorderPosition(int position) {
    return mPostorderPositionToParseTree.get(position);
  }

  public Map<Integer, ParseTreeWrapper> getPreorderPositionToParseTreeMap() {
    return mPreorderPositionToParseTree;
  }

  public Map<Integer, ParseTreeWrapper> getPostorderPositionToParseTreeMap() {
    return mPostorderPositionToParseTree;
  }

  public Java8Parser parser() {
    return mParser;
  }
}