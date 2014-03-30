#!/bin/bash

# -classpath .:classes:lib/Chat.jar

DEST="WEB-INF/classes/"
LIB=".:../../lib/servlet-api.jar:WEB-INF/lib/jus.util.jar"

javac -d $DEST -sourcepath src -classpath $LIB src/NouvelleRepresentationServlet.java

javac -d $DEST -sourcepath src -classpath $LIB src/ProgrammeServlet.java
