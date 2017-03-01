package jtdiff.pattern;

import jtdiff.antlr.ParseTreeWrapper;

public interface DataExtraction {
  public String extract(DataExtractionContext ctx,
                        ParseTreeWrapper source,
                        ParseTreeWrapper target);
}