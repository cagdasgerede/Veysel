package jtdiff.pattern;

public class Pattern {
	String mSourcePatternHumanFriendly;
  String mSourcePattern;
  DataExtraction mSourcePatternExtraction;

  String mTargetPatternHumanFriendly;
  String mTargetPattern; 
  DataExtraction mTargetPatternExtraction;

  PatternViewTemplate mViewTemplate;

  public String humanFriendlySourcePattern() {
    return mSourcePatternHumanFriendly;
  }

  public Pattern humanFriendlySourcePattern(String pattern) {
    mSourcePatternHumanFriendly = pattern;
    return this;
  }

  public String sourcePattern() {
    return mSourcePattern;
  }

  public Pattern sourcePattern(String pattern) {
    mSourcePattern = pattern;
    return this;
  }

  public DataExtraction sourcePatternExtraction() {
    return mSourcePatternExtraction;
  }

  public Pattern sourcePatternExtraction(DataExtraction extraction) {
    mSourcePatternExtraction = extraction;
    return this;
  }

  public String humanFriendlyTargetPattern() {
    return mTargetPatternHumanFriendly;
  }

  public Pattern humanFriendlyTargetPattern(String humanFriendlyTargetPattern) {
    mTargetPatternHumanFriendly = humanFriendlyTargetPattern;
    return this;
  }

  public String targetPattern() {
    return mTargetPattern;
  }

  public Pattern targetPattern(String pattern) {
    mTargetPattern = pattern;
    return this;
  }

  public DataExtraction targetPatternExtraction() {
    return mTargetPatternExtraction;
  }

  public Pattern targetPatternExtraction(DataExtraction extraction) {
    mTargetPatternExtraction = extraction;
    return this;
  }

  public PatternViewTemplate viewTemplate() {
    return mViewTemplate;
  }

  public Pattern viewTemplate(PatternViewTemplate template) {
    mViewTemplate = template;
    return this;
  }
}