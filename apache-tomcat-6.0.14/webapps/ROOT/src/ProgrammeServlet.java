/*
 * @(#)ProgrammeServlet.java	1.0 2007/10/31
 * 
 * Copyright (c) 2007 Sara Bouchenak.
 */
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
 * Proramme Servlet.
 *
 * This servlet dynamically returns the theater program.
 *
 * @author <a href="mailto:Sara.Bouchenak@imag.fr">Sara Bouchenak</a>
 * @version 1.0, 31/10/2007
 */
@SuppressWarnings("serial")
public class ProgrammeServlet extends HttpServlet {

	/**
	 * HTTP GET request entry point.
	 *
	 * @param req	an HttpServletRequest object that contains the request 
	 *			the client has made of the servlet
	 * @param res	an HttpServletResponse object that contains the response 
	 *			the servlet sends to the client
	 *
	 * @throws ServletException   if the request for the GET could not be handled
	 * @throws IOException	   if an input or output error is detected 
	 *					   when the servlet handles the GET request
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{
		ServletOutputStream out = res.getOutputStream();
		String nomS, numS;

		res.setContentType("text/html");

		out.println("<HEAD><TITLE> Programme de la saison </TITLE></HEAD>");
		out.println("<BODY bgproperties=\"fixed\" background=\"/images/rideau.JPG\">");
		out.println("<font color=\"#FFFFFF\"><h1> Programme de la saison </h1>");

		try {
			Connection c = BDConnexion.getConnexion("canog", "bd2013");
			String requete ;
			Statement stmt ;
			
			ResultSet rs ;
			stmt = c.createStatement();
			
			// on récupère la liste des dpectacles
			// et on les affiche avec un lien vers la liste des programmes
			requete = "select distinct nomS, numS from LesSpectacles order by noms";
			rs = stmt.executeQuery(requete);
			out.println("<p> Noms : </p>");
			while (rs.next()) {
				nomS = rs.getString(1);
				numS = rs.getString(2);
				out.println("<p><a href=\"/servlet/ProgrammeParGroupeServlet?numS="
						+ numS+"\">"+ numS +" - "+ nomS +"</a></p>");
			}
		} catch (ExceptionConnexion e) {
			out.println("<p>Echec requete </p>");
			out.println(e.getMessage());
		} catch (SQLException e) {
			IO.println("SQLException");
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
			throws ServletException, IOException
			{
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
