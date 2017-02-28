package jtdiff.antlr;

import jtdiff.antlr.generated.Java8Parser;
import jtdiff.antlr.generated.Java8BaseListener;

import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Arrays;
import java.util.List;

public class YamlSerializer extends Java8BaseListener {
  TokenStream mTokenStream;
  StringBuilder mStringBuilder;
  Java8Parser mParser;
  int mIndentCount = 0;
  Map<Integer, ParseTreeWrapper> mPreorderPositionToParseTree;
  int mPreorderPosition = 1;

  public YamlSerializer(
      TokenStream tokenStream,
      Java8Parser parser,
      Map preorderPositionToParseTree) {
    mTokenStream = tokenStream;
    mStringBuilder = new StringBuilder();
    mParser = parser;
    mPreorderPositionToParseTree = preorderPositionToParseTree;
  }

  public String serialization() {
    return mStringBuilder.toString();
  }

  @Override
  public void enterEveryRule(ParserRuleContext ctx) {
    String ruleName = ParseTreeUtil.getName(ctx, mParser);

    indent(mIndentCount, mStringBuilder);
    if (mIndentCount > 0) {
      mStringBuilder.append("- ");
    }
    mIndentCount += 2;
    mStringBuilder.append("'")  // Escape for Yaml
                  .append(ruleName)
                  .append("'")  // Escape for Yaml
                  .append(":\n");

    mPreorderPositionToParseTree.put(
        mPreorderPosition, 
        new ParseTreeWrapper(ctx, ruleName, mPreorderPosition));
    mPreorderPosition++;
  }

  @Override
  public void exitEveryRule(ParserRuleContext ctx) {
    mIndentCount -= 2;
  }

  @Override
  public void visitTerminal(TerminalNode node) {
    String name = ParseTreeUtil.getName(node);
    if (name == null) {
      System.out.println(
      "Inside YamlSerializer: Terminal symbol is null!!!: " + node.toString());
      return;
    }

    indent(mIndentCount, mStringBuilder);
    mStringBuilder.append("- ")
                  .append("'")  // Escape for Yaml
                  .append(name)
                  .append("'")  // Escape for Yaml
                  .append(":\n");

    mPreorderPositionToParseTree.put(
        mPreorderPosition,
        new ParseTreeWrapper(node, name, mPreorderPosition));
    mPreorderPosition++;
  }

  @Override
  public void visitErrorNode(ErrorNode node) {
    System.out.println("Inside YamlSerializer: ERROR NODE: " + node.toString());
  }

  private static void indent(int indentCount, StringBuilder builder) {
    while (indentCount-- > 0) {
      builder.append(" ");
    }
  }
}