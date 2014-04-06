P=WEB-INF/classes/
DEST=WEB-INF/classes/
LIB=.:../../lib/servlet-api.jar:WEB-INF/lib/jus.util.jar
COMMAND=javac -d $(DEST) -sourcepath src -classpath $(LIB) $(CATALINA_HOME)/webapps/ROOT/
SERVLETS=$(P)NouvelleRepresentationServlet.class $(P)ProgrammeServlet.class
SERVLETS+=$(P)ProgrammeParGroupeServlet.class $(P)PlacesDisponiblesParRepresentation.class
SERVLETS+=$(P)Reservation.class $(P)RepresentationServlet.class

all : $(SERVLETS)

$(P)NouvelleRepresentationServlet.class : src/NouvelleRepresentationServlet.java
	$(COMMAND)$^

$(P)ProgrammeServlet.class : src/ProgrammeServlet.java
	$(COMMAND)$^

$(P)ProgrammeParGroupeServlet.class : src/ProgrammeParGroupeServlet.java
	$(COMMAND)$^

$(P)PlacesDisponiblesParRepresentation.class : src/PlacesDisponiblesParRepresentation.java
	$(COMMAND)$^

$(P)Reservation.class : src/Reservation.java
	$(COMMAND)$^

$(P)RepresentationServlet.class : src/RepresentationServlet.java
	$(COMMAND)$^


