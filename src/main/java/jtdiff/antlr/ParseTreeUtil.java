package jtdiff.antlr;

import org.antlr.v4.runtime.Parser;

import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ParseTreeUtil {
	public static String getName(ParserRuleContext ctx, Parser parser) {
	  int ruleIndex = ctx.getRuleIndex();
    List<String> ruleNames = Arrays.asList(parser.getRuleNames());
    String ruleName = Integer.toString(ruleIndex);
    if (ruleIndex >= 0 && ruleIndex < ruleNames.size()) {
      ruleName = ruleNames.get(ruleIndex);
    }
    return ruleName;
	}

  public static String getName(TerminalNode node) {
    Token symbol = ((TerminalNode)node).getSymbol();
    if (symbol == null) {
      return null;
    }
    return symbol.getText();
  }

  public static String getName(ParseTree tree, Parser parser) {
    if (tree instanceof ParserRuleContext) {
      return getName((ParserRuleContext) tree, parser);
    } else if (tree instanceof TerminalNode) {
      return getName((TerminalNode) tree);
    } else {
      return null;
    }
  }
} 