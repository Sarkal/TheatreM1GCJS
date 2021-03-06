import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jus.util.IO;
import accesBD.BDConnexion;
import exceptions.ExceptionConnexion;

/**
 * ProgrammeParGroupe
 * Permet de choisir un groupe et d'afficher toutes leurs representations
 * 
 * @author Seguin Jeremy, Cano Gregory
 */
@SuppressWarnings("serial")
public class ProgrammeParGroupeServlet extends HttpServlet {
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
		String numS, nomS, dateRep, representation;
		ServletOutputStream out;
		int nbRepresentations = 0;
		
		out = res.getOutputStream();

		res.setContentType("text/html");

		out.println("<HEAD><TITLE> Consulter la liste des repr&eacute;sentations d'un groupe </TITLE></HEAD>");
		out.println("<BODY bgproperties=\"fixed\" background=\"/images/rideau.JPG\">");
		out.println("<font color=\"#FFFFFF\"><h1> Programme par groupe </h1>");

		numS = req.getParameter("numS");
		
		if (numS == null) {
			out.println("<font color=\"#FFFFFF\">Veuillez saisir le groupe 	&agrave; afficher :");
			out.println("<P>");
			out.print("<form action=\"");
			out.print("ProgrammeParGroupeServlet\" ");
			out.println("method=POST>");

			Connection c = null;
			try {
				c = BDConnexion.getConnexion("canog", "bd2013");
				String requete ;
				Statement stmt ;

				ResultSet rs ;
				stmt = c.createStatement();

				// on affiche la liste des spectacles
				requete = "select distinct numS, nomS from LesSpectacles order by numS";
				rs = stmt.executeQuery(requete);
				out.println("<SELECT name=\"numS\">");
				while (rs.next()) {
					numS = rs.getString(1);
					nomS = rs.getString(2);
					out.println("<option value=\""+ numS +"\"> "+ numS +" - "+ nomS +"</option>");
				}
				out.println("</SELECT>");
				
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
			Connection c = null;
			try {
				c = BDConnexion.getConnexion("canog", "bd2013");
				String requete;
				Statement stmt;
				ResultSet rs;

				// on récupère le nom du groupe
				stmt = c.createStatement();
				requete = "select nomS from LesSpectacles where (numS = "+ numS +")";
				rs = stmt.executeQuery(requete);
				rs.next();
				nomS = rs.getString(1);
				
				out.println("<p>Programmes du groupe "+ nomS +"</p>");
				
				// et les dates des représentation
				stmt = c.createStatement();
				requete = "select TO_CHAR(dateRep, "+ format +") from LesRepresentations where (numS = "+ numS +")";
				rs = stmt.executeQuery(requete);
				
				// et on affiche avec un lien vers les places disponibles et un bouton pour réserver
				out.println("<table>");
				while (rs.next()) {
					dateRep = rs.getString(1);
					representation = numS +"::"+ dateRep +"::"+ nomS;
					
					out.println("<tr>");
					out.println("<th>");
					out.println("<p><a href=\"/servlet/PlacesDisponiblesParRepresentation?"+
							"representation="+ representation +
							"\">"+ dateRep +"</a>");
					out.println("</th>");
					out.println("<th>");
					out.println("<form action=\"Reservation\" method=POST>");
					out.println("<input type=\"hidden\" value=\""+ representation +"\" name=\"representation\">");
					out.println("<input type=submit value=\"Réserver\">");
					out.println("</form></p>");
					out.println("<th>");
					out.println("</tr>");
					//numS::dateRep::nomS
					
					nbRepresentations++;
				}
				out.println("</table>");
				
				if (nbRepresentations == 0) {
					out.println("<p> Aucune représentation de programmée</p>");
				}
			} catch (NullPointerException e){
				out.println("<p>Null pointer exception, check connection</p>");
			} catch (ExceptionConnexion e) {
				out.println("<p>Echec requete </p>");
				out.println(e.getMessage());
			} catch (SQLException e) {
				out.println("SQLException");
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
