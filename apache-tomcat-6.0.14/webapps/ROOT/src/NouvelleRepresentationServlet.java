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

import modele.Categorie;

import accesBD.BDConnexion;
import exceptions.CategorieException;
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
		String numS, dateS, heureS;
		ServletOutputStream out = res.getOutputStream();

		res.setContentType("text/html");

		out.println("<HEAD><TITLE> Ajouter une nouvelle representation </TITLE></HEAD>");
		out.println("<BODY bgproperties=\"fixed\" background=\"/images/rideau.JPG\">");
		out.println("<font color=\"#FFFFFF\"><h1> Ajouter une nouvelle representation </h1>");

		numS = req.getParameter("numS");
		dateS = req.getParameter("date");
		heureS = req.getParameter("heure");
		if (numS == null || dateS == null || heureS == null) {
			out.println("<font color=\"#FFFFFF\">Veuillez saisir les informations relatives a la nouvelle representation :");
			out.println("<P>");
			out.print("<form action=\"");
			out.print("NouvelleRepresentationServlet\" ");
			out.println("method=POST>");
			out.println("Numero de spectacle :");
			out.println("<input type=text size=20 name=numS>");
			out.println("<br>");
			out.println("Date de la representation :");
			out.println("<input type=text size=20 name=date>");
			out.println("<br>");
			out.println("Heure de debut de la representation :");
			out.println("<input type=text size=20 name=heure>");
			out.println("<br>");
			out.println("<input type=submit>");
			out.println("</form>");
		} else {
			// TODO
			// Transformation des parametres vers les types adequats.
			// Ajout de la nouvelle representation.
			// Puis construction dynamique d'une page web de reponse.

			out.println("<p><i><font color=\"#FFFFFF\">A completer</i></p>");
			out.println("<p><i><font color=\"#FFFFFF\">...</i></p>");
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
