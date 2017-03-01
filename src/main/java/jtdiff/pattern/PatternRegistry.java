package jtdiff.pattern;

import jtdiff.antlr.ParseTreeUtil;
import jtdiff.antlr.ParseTreeWrapper;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

public class PatternRegistry {


  // attributes: {
  //   "parameter_index" : function(sourceTreeCache, targetTreeCache) {Way to compute}
  //   "method_name" : function(...) {Way to compute}
  //   "source_line_no": function(...) {Way to compute}
  //   "target_line_no": function(...) {Way to compute}
  //   "old_name": function(...) {Way to compute}
  //   "new_name": function(...) {Way to compute}
  // },

  public static final Pattern PARAMETER_CHANGE_PATTERN = new Pattern()
      .sourcePatternExtraction(
          (ctx, s, t) -> {
            ParseTree tree = s.parseTree();
            if (tree.getParent() == null ||
                !(tree.getParent() instanceof RuleContext) ||
                tree.getParent().getParent() == null ||
                !(tree.getParent().getParent() instanceof RuleContext)) {
                return "cannot extract";
            }
            return String.format(
                "%s, %s",
                ((RuleContext)tree.getParent()).getRuleIndex(),
                ((RuleContext)tree.getParent()).getParent().getRuleIndex());
          })
      .sourcePattern("56, 77")
      // parent, parent of parent
      .humanFriendlySourcePattern("variableDeclaratorId, formalParameter")
      .targetPattern("56, 77")
      .targetPatternExtraction(
          (ctx, s, t) -> {
            ParseTree tree = t.parseTree();
            if (tree.getParent() == null ||
                !(tree.getParent() instanceof RuleContext) ||
                tree.getParent().getParent() == null ||
                !(tree.getParent().getParent() instanceof RuleContext)) {
                return "cannot extract";
            }
            return String.format(
                "%s, %s",
                ((RuleContext)tree.getParent()).getRuleIndex(),
                ((RuleContext)tree.getParent().getParent()).getRuleIndex());
          })
      .humanFriendlyTargetPattern("variableDeclaratorId, formalParameter")
      .viewTemplate(
          new PatternViewTemplate(
              "{{parameter_index}}th parameter's name of " +
              "method {{method_name}} is " +
              "changed from %s to %s " +
              "(original line {{source_line_no}} and updated " +
              "line {{target_line_no}}",
              (ctx, s, t) -> {
                return ParseTreeUtil.getName(s.parseTree(),
                                             ctx.sourceParser());},
              (ctx, s, t) -> {
                return ParseTreeUtil.getName(t.parseTree(),
                                             ctx.targetParser());}
          )
      );

  public static final Pattern FAKE_PATTERN = new Pattern()
      .sourcePatternExtraction((ctx, s, t) -> { return "fake pattern"; })
      .sourcePattern("1, 2, 3, 4")
      // parent, parent of parent
      .humanFriendlySourcePattern("some, fake, pattern")
      .targetPatternExtraction((ctx, s, t) -> { return "fake pattern"; })
      .targetPattern("5, 6, 7, 8")
      .humanFriendlyTargetPattern("another, fake, pattern")
      .viewTemplate(new PatternViewTemplate("fake pattern view"));
  
  static List<Pattern> mPatterns = new ArrayList<>(
      Arrays.asList(FAKE_PATTERN,
                    PARAMETER_CHANGE_PATTERN,
                    FAKE_PATTERN));

  public static Pattern match(DataExtractionContext ctx,
                              ParseTreeWrapper nodeInOriginal,
                              ParseTreeWrapper nodeInUpdated) {
    for (Pattern pattern : mPatterns) {
      String sourcePattern = pattern.sourcePatternExtraction().extract(
        ctx, nodeInOriginal, nodeInUpdated);
      if (!sourcePattern.equals(pattern.sourcePattern())) {
        continue;
      }
      String targetPattern = pattern.targetPatternExtraction().extract(
        ctx, nodeInOriginal, nodeInUpdated);
      if (targetPattern.equals(pattern.targetPattern())) {
        return pattern;
      }
    }
    return null;
  }
}


