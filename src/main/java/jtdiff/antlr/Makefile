ANTLR4 = java -jar /usr/local/lib/antlr-4.6-complete.jar
SHELL = /bin/bash

run: cleanJava generateAntlr compile
	# Set the input file as an environment variable, such as:
	#   export TEST=input/K.java
	#
	cd ../../; cat jtdiff/antlr/input/$(TEST).java | java jtdiff/antlr/ParseTreeBuilder

compile:
	cd ../../; javac jtdiff/antlr/ParseTreeBuilder.java

generateAntlr:
	test -f Java8Lexer.java && echo "Skipping antlr generation. If you prefer, first run \"make -s cleanAntlr\" first." || ${ANTLR4} -package jtdiff.antlr.generated -o generated Java8.g4

cleanJava:
	rm -f *.class
	rm -f generated/*.class

cleanAntlr:
	rm -f generated/Java8BaseListener.java generated/Java8Listener.java generated/Java8Lexer.java generated/Java8Parser.java generated/Java8Lexer.tokens generated/Java8.tokens

gui: generateAntlr
	cd ../../; java org.antlr.v4.gui.TestRig jtdiff.antlr.generated.Java8 compilationUnit -gui
