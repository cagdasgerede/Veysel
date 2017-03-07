cheap:
	mvn exec:java -Dexec.mainClass="jtdiff.main.Demo2" -Dexec.args="src/test/data/demo2_original.java src/test/data/demo2_updated.java" 

expensive:
	mvn exec:java -Dexec.mainClass="jtdiff.main.Demo2" -Dexec.args="src/test/data/demo_original.java src/test/data/demo_updated.java" 

