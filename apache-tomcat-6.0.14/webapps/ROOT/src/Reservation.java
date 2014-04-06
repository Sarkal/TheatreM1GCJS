import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jus.util.IO;
import accesBD.BDConnexion;
import exceptions.ExceptionConnexion;


@SuppressWarnings("serial")
public class Reservation extends HttpServlet {
	private static final String format = "'yyyy/mm/dd HH24:MI'";

	/**
	 * HTTP GET request entry point.
	 * 
	 * @param req
	 *            an HttpServletRequest object that contains the request the
	 *            client has made of the servlet
	 * @param res
	 *            an HttpServletResponse object that contains the response the
	 *            servlet sends to the client
	 * 
	 * @throws ServletException
	 *             if the request for the GET could not be handled
	 * @throws IOException
	 *             if an input or output error is detected when the servlet
	 *             handles the GET request
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		String nomS, numS, dateRep, representation, place, noPlace, noRang;
		String [] tab;
		ServletOutputStream out;

		out = res.getOutputStream();

		res.setContentType("text/html");

		out.println("<HEAD><TITLE> Consulter la liste des places disponibles pour une repr&eacute;sentations </TITLE></HEAD>");
		out.println("<BODY bgproperties=\"fixed\" background=\"/images/rideau.JPG\">");
		out.println("<font color=\"#FFFFFF\"><h1> Places disponibles </h1>");

		representation = req.getParameter("representation");
		place = req.getParameter("place");
		
		if (representation == null || place == null) {
			out.println("<font color=\"#FFFFFF\"> Choisir la repr&eacute;sentation :");
			out.println("<P>");
			out.print("<form action=\"");
			out.print("Reservation\" ");
			out.println("method=POST>");

			Connection c = null;
			try {
				c = BDConnexion.getConnexion("canog", "bd2013");
				String requete ;
				Statement stmt ;

				ResultSet rs ;
				stmt = c.createStatement();
				requete = "select nomS, numS, TO_CHAR(dateRep, "+ format +") from LesRepresentations natural join LesSpectacles";
				rs = stmt.executeQuery(requete);
				out.println("<select name=\"representation\">");
				while (rs.next()) {
					nomS = rs.getString(1);
					numS = rs.getString(2);
					dateRep = rs.getString(3);
					out.println("<option value=\""+ numS +"::"+ dateRep + "::"+ nomS +"\"> "+ nomS +" - "+ dateRep +"</option>");
				}
				out.println("</select>");
				
				if (place == null && representation != null) {
					tab = representation.split("::");
					numS = tab[0];
					dateRep = tab[1];
					nomS = tab[2];
					
 					stmt = c.createStatement();
					requete = "select noPlace, noRang from LesPlaces minus select noPlace, noRang" +
							" from LesTickets where (numS = "+ numS +
							" AND dateRep = TO_DATE('"+ dateRep +"', "+ format +"))";

					rs = stmt.executeQuery(requete);
					
					
					out.println("<br />");
					out.println("<input type=submit>");
					out.println("</form>");
					
					
					out.print("<form action=\"");
					out.print("Reservation\" ");
					out.println("method=POST>");
					
					out.println("<p>Choix de la place pour "+ nomS +" - "+ dateRep +"</p>");
					out.println("<INPUT type=\"hidden\" value=\""+ representation +"\" name=\"representation\">");
					
					out.println("<select name=\"place\">");
					while (rs.next()) {
						noPlace = rs.getString(1);
						noRang = rs.getString(2);
						out.println("<option value=\""+ noPlace +"::"+ noRang +"\"> place "+ noPlace +" - rang "+ noRang +"</option>");
					}
					out.println("</select>");
				}
				
			} catch (NullPointerException e){
				out.println("Null pointer exception, check connection");
			} catch (ExceptionConnexion e) {
				out.println("<p>Echec requete </p>");
				out.println(e.getMessage());
			} catch (SQLException e) {
				IO.println("SQLException");
			} finally {
				if (c != null)
					try {
						c.close();
					} catch (SQLException e) {
						IO.println("SQLException");
					}
			}
			
			out.println("<br />");
			out.println("<input type=submit>");
			out.println("</form>");

		} else {
			tab = representation.split("::");
			numS = tab[0];
			dateRep = tab[1];
			nomS = tab[2];
			
			tab = place.split("::");
			noRang = tab[0];
			noPlace = tab[1];
			
			Connection c = null;
			String noS, noD, prix;
			int noSerie, noDossier;
			try {
				c = BDConnexion.getConnexion("canog", "bd2013");
				String requete;
				Statement stmt;
				ResultSet rs;
				
				// on recupere le numero de serie le plus grand pour avoir le suivant
				stmt = c.createStatement();
				requete = "select max(noSerie) from lesTickets";
				rs = stmt.executeQuery(requete);
				rs.next();
				noS = rs.getString(1);
				noSerie = Integer.parseInt(noS);
				noSerie++;
				
				
				// idem pour le noDossier
				stmt = c.createStatement();
				requete = "select max(noDossier) from LesDossiers";
				rs = stmt.executeQuery(requete);
				rs.next();
				noD = rs.getString(1);
				noDossier = Integer.parseInt(noD);
				noDossier++;
				
				// on recupere le pris de la place voulue
				stmt = c.createStatement();
				requete = "select prix from LesCategories where nomC in (" +
						"select nomC from LesZones where numZ in (" +
						"select numZ from lesPlaces where (noPlace = 1 AND noRang = 1) ))";
				rs = stmt.executeQuery(requete);
				rs.next();
				prix = rs.getString(1);
				
				
				// on ajoute le dossier dans la base
				stmt = c.createStatement();
				requete = "INSERT INTO LesDossiers VALUES ('" + 
						noDossier +"', '"+ prix +"')";
				stmt.executeQuery(requete);
				
				
				// on ajoute le nouveau ticket dans la base
				stmt = c.createStatement();
				requete = "INSERT INTO LesTickets VALUES ('" + noSerie +
						"', '"+ numS +"', TO_DATE('" + dateRep +
						"', 'yyyy/mm/dd hh24:mi'), '"+ noPlace +"', '"+ noRang +"', " +
						"SYSDATE, '"+ noDossier +"')";
				stmt.executeQuery(requete);
				
				c.commit();
				
				out.println("<p>Place " + noPlace + " rang "+ noRang +" réservee avec succès pour le concert de "+ nomS +" le " + dateRep +"</p>");
				
				
			} catch (NullPointerException e){
				out.println("Null pointer exception, check connection");
				out.println(e.getMessage());
			} catch (ExceptionConnexion e) {
				out.println("<p>Echec requete </p>");
				out.println(e.getMessage());
			} catch (SQLException e) {
				IO.println("SQLException");
				out.println(e.getMessage());
			} finally {
				if (c != null)
					try {
						c.close();
					} catch (SQLException e) {
						IO.println("SQLException");
					}
			}
		}

		out.println("<hr><p><font color=\"#FFFFFF\"><a href=\"/admin/admin.html\">Page d'administration</a></p>");
		out.println("<hr><p><font color=\"#FFFFFF\"><a href=\"/index.html\">Page d'accueil</a></p>");
		out.println("</BODY>");
		out.close();

	}

	/**
	 * HTTP POST request entry point.
	 * 
	 * @param req
	 *            an HttpServletRequest object that contains the request the
	 *            client has made of the servlet
	 * @param res
	 *            an HttpServletResponse object that contains the response the
	 *            servlet sends to the client
	 * 
	 * @throws ServletException
	 *             if the request for the POST could not be handled
	 * @throws IOException
	 *             if an input or output error is detected when the servlet
	 *             handles the POST request
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doGet(req, res);
	}

	/**
	 * Returns information about this servlet.
	 * 
	 * @return String information about this servlet
	 */

	public String getServletInfo() {
		return "Ajoute une representation a une date donnee pour un spectacle existant";
	}

}
