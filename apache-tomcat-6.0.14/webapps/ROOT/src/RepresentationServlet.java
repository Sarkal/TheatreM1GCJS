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
 * RepresentationServlet
 * Affiche toutes les representations de tous les groupes
 * 
 * @author Seguin Jeremy, Cano Gregory
 */
@SuppressWarnings("serial")
public class RepresentationServlet extends HttpServlet {
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
		ServletOutputStream out = res.getOutputStream();
		Connection c;

		res.setContentType("text/html");

		out.println("<HEAD><TITLE> Programme de la saison </TITLE></HEAD>");
		out.println("<BODY bgproperties=\"fixed\" background=\"/images/rideau.JPG\">");
		out.println("<font color=\"#FFFFFF\"><h1> Programme de la saison </h1>");

		c = null;
		try {
			c = BDConnexion.getConnexion("canog", "bd2013");
			String requete;
			Statement stmt;
			ResultSet rs ;

			stmt = c.createStatement();

			// on récupère la liste de tous les spectacles de la saison
			requete = "select nomS, dateRep from LesRepresentations natural join LesSpectacles";

			rs = stmt.executeQuery(requete);
			out.println("<table><tr><th>Nom</th> <th>Date</th></tr>");
			while (rs.next()) {
				out.println("<tr><th>" + rs.getString(1) + "</th> <th>" + rs.getDate(2));
			}
			out.println("</table>");

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

		out.println("<hr><p><font color=\"#FFFFFF\"><a href=\"/index.html\">Accueil</a></p>");
		out.println("</BODY>");
		out.close();
	}

	/**
	 * HTTP POST request entry point.
	 *
	 * @param req	an HttpServletRequest object that contains the request 
	 *			the client has made of the servlet
	 * @param res	an HttpServletResponse object that contains the response 
	 *			the servlet sends to the client
	 *
	 * @throws ServletException   if the request for the POST could not be handled
	 * @throws IOException	   if an input or output error is detected 
	 *					   when the servlet handles the POST request
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
		return "Retourne le programme du theatre";
	}
}
