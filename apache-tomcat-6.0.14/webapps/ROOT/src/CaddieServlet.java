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
public class CaddieServlet extends HttpServlet {
	private Cookie [] cookies;
	private int idClient;
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
		String nomS, numS, dateRep, representation, place, noPlace, noRang ,command;
		String [] tab;
		ServletOutputStream out;
		int nbPlaces = 0;

		out = res.getOutputStream();

		res.setContentType("text/html");

		out.println("<HEAD><TITLE> Consulter la liste des places disponibles pour une repr&eacute;sentations </TITLE></HEAD>");
		out.println("<BODY bgproperties=\"fixed\" background=\"/images/rideau.JPG\">");
		out.println("<font color=\"#FFFFFF\"><h1> Votre Caddie </h1>");

		command = req.getParameter("command");
		cookies = req.getCookies();
		
		boolean cookieFound = false;
		if (cookies != null)
			for (Cookie c : cookies) {
				if (c.getName().equals("idClient")) {
					this.idClient = Integer.parseInt(c.getValue());
					cookieFound = true;
				}
			}
		if (!cookieFound) {
			Random r = new Random();
			Cookie c = new Cookie("idClient", ""+ r.nextInt(Integer.MAX_VALUE));
			res.addCookie(c);
		}
		
		if (command != null)
			tab = command.split("::");
		else
			tab = null;
		
		if (command == null || tab[0].equals("del")) {
			
			if (!cookieFound)
				out.println("<p>Votre panier est vide</p>");
			else {
				out.println("<P>");
	
				Connection c = null;
				try {
					c = BDConnexion.getConnexion("canog", "bd2013");
					String requete ;
					Statement stmt ;
					ResultSet rs ;
					
					if (tab != null && tab[0].equals("del")) {
						nomS = tab[1];
						numS = tab[2];
						dateRep = tab[3];
						noPlace = tab[4];
						noRang = tab[5];
						
						stmt = c.createStatement();
						requete = "DELETE FROM LesCaddies where (idClient = " + idClient + 
						" AND nomS = '" + nomS +"' AND numS = "+ numS +" AND dateRep = TO_DATE('" + dateRep +
						"', 'yyyy/mm/dd hh24:mi') AND noPlace = "+ noPlace +" AND noRang = "+ noRang +")";
						rs = stmt.executeQuery(requete);
						
						c.commit();
						
						out.println("<p>"+ nomS +" ("+ numS +") - "+ dateRep + " - place"+ 
								noPlace +" / rang "+ noRang +" Supprimée du caddie</p>");
					}
						
	
					
					stmt = c.createStatement();
					requete = "select numS, nomS, TO_CHAR(dateRep, "+ format +
							"), noPlace, noRang from LesCaddies " +
							"where idClient = " + idClient;
					rs = stmt.executeQuery(requete);
					
					
					out.println("<table>");
					while (rs.next()) {
						nbPlaces++;
						numS = rs.getString(1);
						nomS = rs.getString(2);
						dateRep = rs.getString(3);
						noPlace = rs.getString(4);
						noRang = rs.getString(5);
						out.println("<tr>");
						out.println("<th>");
						out.println(nomS +" ("+ numS +") - "+ dateRep + " - place"+ noPlace +" / rang "+ noRang);
						out.println("</th>");
						out.println("<th>");
						out.print("<form action=\"CaddieServlet\" method=POST>");
						out.println("<INPUT type=\"hidden\" value=\"del::"+ nomS +"::"+ numS +"::"+ dateRep + "::"+ noPlace +"::"+ noRang +"\" name=\"command\">");
						out.println("<input type=submit value=\"X\">");
						out.println("</form>");
						out.println("</th>");
						out.println("</tr>");
					}
					out.println("</table>");
					
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
				
				if (nbPlaces != 0) {
					out.print("<form action=\"CaddieServlet\" method=POST>");
					out.println("<INPUT type=\"hidden\" value=\"valider\" name=\"command\">");
					out.println("<input type=submit value=\"Valider la commande\">");
					out.println("</form>");
				}
				else
					out.println("<p>Votre panier est vide</p>");
			}

		} else {
			out.println("<p>TODO MOTHERFUCKER</p>");
			/*
			tab = command.split("::");
			numS = tab[0];
			dateRep = tab[1];
			nomS = tab[2];
			
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
						"select numZ from lesPlaces where (noPlace = "+ noPlace +" AND noRang = "+ noRang +") ))";
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
				
				
				stmt = c.createStatement();
				requete = "";
				stmt.executeQuery(requete);
				c.commit();
				
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
			*/
		}

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
