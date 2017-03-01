package jtdiff.pattern;

import jtdiff.antlr.ParseTreeWrapper;

public class PatternViewTemplate {
  String mTemplate;
  DataExtraction[] mDataExtractions;

  public PatternViewTemplate(String template,
                             DataExtraction... dataExtractions) {
    mTemplate = template;
    mDataExtractions = dataExtractions;
  }

  public String generateView(DataExtractionContext ctx,
                             ParseTreeWrapper source,
                             ParseTreeWrapper target) {
    String[] injections = new String[mDataExtractions.length];
    int index = 0;
    for (DataExtraction dataExtraction : mDataExtractions) {
      injections[index++] = dataExtraction.extract(ctx, source, target);
    }
    return String.format(mTemplate, injections);
  }
}
