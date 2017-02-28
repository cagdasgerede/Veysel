package jtdiff.antlr;

import jtdiff.antlr.generated.Java8Parser;
import jtdiff.antlr.generated.Java8BaseListener;


import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Arrays;
import java.util.List;

/**
 * Same as doing:
 *    System.out.println(tree.toStringTree(parser));
 *
 * Implementation is inspired by getNodeText method of
 * org/antlr/v4/runtime/tree/Trees.java class.
 *
 * The LISP serializer generates a string representation for the tree in
 * the following format:
 *    (A (B (C D)) (E))
 * where
 *   - A has two children, B and E;
 *   - B has two children: C and D;
 *   - E has no children.
 */
public class LISPSerializer extends Java8BaseListener {
  TokenStream mTokenStream;
  StringBuilder mStringBuilder;
  Java8Parser mParser;
  boolean startRuleProcessed = false;

  public LISPSerializer(
      TokenStream tokenStream, Java8Parser parser) {
    mTokenStream = tokenStream;
    mStringBuilder = new StringBuilder();
    mParser = parser;
  }

  public String serialization() {
    return mStringBuilder.toString();
  }

  @Override
  public void enterEveryRule(ParserRuleContext ctx) {    
    int ruleIndex = ctx.getRuleIndex();
    List<String> ruleNames = Arrays.asList(mParser.getRuleNames());
    String ruleName = Integer.toString(ruleIndex);
    if (ruleIndex >= 0 && ruleIndex < ruleNames.size()) {
      ruleName = ruleNames.get(ruleIndex);
    }
    if (startRuleProcessed) {
      mStringBuilder.append(" ");
    }
    startRuleProcessed = true;
    mStringBuilder.append("(").append(ruleName);
  }

  @Override
  public void exitEveryRule(ParserRuleContext ctx) {
    mStringBuilder.append(")");
  }

  @Override
  public void visitTerminal(TerminalNode node) {
    Token symbol = ((TerminalNode)node).getSymbol();
    if (symbol != null) {
      String s = symbol.getText();
      mStringBuilder.append(" ").append(s);
      return;
    }
    System.out.println(
      "Inside LISPSerializer: Terminal symbol is null!!!: " + node.toString());
  }

  @Override
  public void visitErrorNode(ErrorNode node) {
    System.out.println("Inside LISPSerializer: ERROR NODE: " + node.toString());
  }
}