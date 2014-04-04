import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jus.util.IO;
import accesBD.BDConnexion;
import exceptions.ExceptionConnexion;


@SuppressWarnings("serial")
public class PlacesDisponiblesParRepresentation extends HttpServlet {

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
		String nomS, dateRep, representation;
		String [] tab;
		ServletOutputStream out;
		GregorianCalendar gc;
		int year;

		
		out = res.getOutputStream();
		gc = new GregorianCalendar();
		year = gc.get(Calendar.YEAR);

		res.setContentType("text/html");

		out.println("<HEAD><TITLE> Consulter la liste des places disponibles pour une repr&eacute;sentations </TITLE></HEAD>");
		out.println("<BODY bgproperties=\"fixed\" background=\"/images/rideau.JPG\">");
		out.println("<font color=\"#FFFFFF\"><h1> Places disponibles </h1>");

		representation = req.getParameter("representation");
		
		if (representation == null) {
			out.println("<font color=\"#FFFFFF\"> Choisir la repr&eacute;sentation :");
			out.println("<P>");
			out.print("<form action=\"");
			out.print("PlacesDisponiblesParRepresentation\" ");
			out.println("method=POST>");

			Connection c = null;
			try {
				c = BDConnexion.getConnexion("canog", "bd2013");
				String requete ;
				Statement stmt ;

				ResultSet rs ;
				stmt = c.createStatement();
				requete = "select nomS, dateRep from LesRepresentations natural join LesSpectacles";
				rs = stmt.executeQuery(requete);
				out.println("<SELECT name=\"representation\">");
				while (rs.next()) {
					nomS = rs.getString(1);
					dateRep = rs.getString(2);
					out.println("<option value=\""+ nomS +"::"+ dateRep +"\"> "+ nomS +" - "+ dateRep +"</option>");
					IO.println("<option value=\""+ nomS +"::"+ dateRep +"\"> "+ nomS +" - "+ nomS +"</option>");
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

				tab = representation.split("::");
				nomS = tab[0];
				dateRep = tab[1];
				
				out.println("Paces pour "+ nomS +" le "+ dateRep);
				stmt = c.createStatement();
				requete = "select * from LesRepresentations";

				rs = stmt.executeQuery(requete);
				// TODO requete SQL
				/*
				while (rs.next()) {
					out.println("<p>" + rs.getString(1) + "</p>");
				}
				*/
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
