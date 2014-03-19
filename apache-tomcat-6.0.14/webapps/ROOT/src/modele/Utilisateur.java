package modele;

/**
 * 
 * @author fauvet
 *
 */
public class Utilisateur {
	
	private String login;
	private String mdp;
	
	/**
	 * construction d'un utilisateur avec son login
	 * @param l le login
	 */
	public Utilisateur (String l) {
		this.login = l;
	}
	
	/**
	 * construction d'un utilisateur avec des valeurs pour chacun des attributs
	 * @param l le login
	 * @param n le nom
	 * @param p le prenom
	 * @param m le mot de passe / application
	 * @param c l'adresse email
	 */
	public Utilisateur (String l, String m) {
		this.login = l;
		this.mdp = m;
	}
	
	public String getmdp() {
		return this.mdp;
	}
	public String getLogin() {
		return this.login;
	}
	
	public void setmdp(String m) {
		this.mdp = m;
	}
	public void setLogin(String l) {
		this.login =l;
	}
}
