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

/*
 * @(#)NouvelleRepresentationServlet.java	1.0 2007/10/31
 * 
 * Copyright (c) 2007 Sara Bouchenak.
 */

/**
 * NouvelleRepresentation Servlet.
 * 
 * This servlet dynamically adds a new date a show.
 * 
 * @author <a href="mailto:Sara.Bouchenak@imag.fr">Sara Bouchenak</a>
 * @version 1.0, 31/10/2007
 */

@SuppressWarnings("serial")
public class NouvelleRepresentationServlet extends HttpServlet {

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
		String numS, nomS, jourS, moisS, anneeS, heureS, minutesS;
		ServletOutputStream out;
		GregorianCalendar gc;
		int year;

		
		out = res.getOutputStream();
		gc = new GregorianCalendar();
		year = gc.get(Calendar.YEAR);

		res.setContentType("text/html");

		out.println("<HEAD><TITLE> Ajouter une nouvelle representation </TITLE></HEAD>");
		out.println("<BODY bgproperties=\"fixed\" background=\"/images/rideau.JPG\">");
		out.println("<font color=\"#FFFFFF\"><h1> Ajouter une nouvelle representation </h1>");

		numS = req.getParameter("numS");
		jourS = req.getParameter("jourS");
		moisS = req.getParameter("moisS");
		anneeS = req.getParameter("anneeS");
		heureS = req.getParameter("heureS");
		minutesS = req.getParameter("minutesS");
		
		if (numS == null || jourS == null || moisS == null || anneeS == null || heureS == null || minutesS == null) {
			out.println("<font color=\"#FFFFFF\">Veuillez saisir les informations relatives a la nouvelle representation :");
			out.println("<P>");
			out.print("<form action=\"");
			out.print("NouvelleRepresentationServlet\" ");
			out.println("method=POST>");

			out.println("<table>");
			out.println("<tr align=left>");

			out.println("<th>");
			out.println("Rep : ");
			out.println("</th>");
			
			out.println("<th>");
			Connection c = null;
			try {
				c = BDConnexion.getConnexion("canog", "bd2013");
				String requete ;
				Statement stmt ;

				ResultSet rs ;
				stmt = c.createStatement();

				// on récupère et on affiche la liste des numeros de spectacles
				requete = "select distinct numS, nomS from LesSpectacles order by numS";
				rs = stmt.executeQuery(requete);
				out.println("<SELECT name=\"numS\">");
				while (rs.next()) {
					numS = rs.getString(1);
					nomS = rs.getString(2);
					out.println("<option value=\""+ numS +"\"> "+ numS +" - "+ nomS +"</option>");
					IO.println("<option value=\""+ numS +"\"> "+ numS +" - "+ nomS +"</option>");
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
			// on affiche aussi toutes les informations à récupérer pour le nouveau spectacle
			out.println("</th>");
			
			out.println("</tr>");
			out.println("<tr align=left>");

			out.println("<th>");
			out.println("Date  :");
			out.println("</th>");
			out.println("<th>");
			
			out.println("<SELECT name=\"jourS\">");
			for (int i = 1; i <= 31; i++)
				out.println("<OPTION>"+i);
			out.println("</SELECT>");

			out.println("<SELECT name=\"moisS\" >");
			for (int i = 1; i <= 12; i++)
				out.println("<OPTION>"+i);
			out.println("</SELECT>");

			out.println("<SELECT name=\"anneeS\" >");
			for (int i = year; i < year + 20; i++)
				out.println("<OPTION>"+i);
			out.println("</SELECT>");
			out.println("</th>");

			out.println("</tr>");
			out.println("<tr align=left>");
			
			out.println("<th>");
			out.println("Heure : ");
			out.println("</th>");
			out.println("<th>");
			
			out.println("<SELECT name=\"heureS\" >");
			for (int i = 0; i < 24; i++)
				out.println("<OPTION>"+i);
			out.println("</SELECT>");

			out.println(":");

			out.println("<SELECT name=\"minutesS\" >");
			for (int i = 0; i < 60; i++)
				out.println("<OPTION>"+i);
			out.println("</SELECT>");
			
			out.println("</th>");
			
			out.println("</tr>");

			out.println("</table>");
			
			out.println("<input type=submit>");
			out.println("</form>");

		} else {
			Connection c = null;
			try {
				c = BDConnexion.getConnexion("canog", "bd2013");
				String requete, date;
				Statement stmt;

				// on insère la nouvelle représentation dans la table
				date = anneeS +"/" + moisS + "/" + jourS + " " + heureS + ":" + minutesS;
				stmt = c.createStatement();
				requete = "INSERT INTO LesRepresentations VALUES "
						+ "('" + numS + "', TO_DATE('" + date + "', 'yyyy/mm/dd hh24:mi'))";
				stmt.executeQuery(requete);
				c.commit();
				
				out.println("<p>Spectacle numéro " + numS + " ajouté avec succes à la date " + date + "</p>");
				
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
