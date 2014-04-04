#!/bin/bash

# -classpath .:classes:lib/Chat.jar

DEST="WEB-INF/classes/"
LIB=".:../../lib/servlet-api.jar:WEB-INF/lib/jus.util.jar"

javac -d $DEST -sourcepath src -classpath $LIB $CATALINA_HOME/webapps/ROOT/src/NouvelleRepresentationServlet.java

javac -d $DEST -sourcepath src -classpath $LIB $CATALINA_HOME/webapps/ROOT/src/ProgrammeServlet.java

javac -d $DEST -sourcepath src -classpath $LIB $CATALINA_HOME/webapps/ROOT/src/ProgrammeParGroupeServlet.java

javac -d $DEST -sourcepath src -classpath $LIB $CATALINA_HOME/webapps/ROOT/src/PlacesDisponiblesParRepresentation.java
