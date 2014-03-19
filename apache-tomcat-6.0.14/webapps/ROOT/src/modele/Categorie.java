package modele;

public class Categorie {

	private String categorie;
	private float prix;
	
	public Categorie (String c, float p) {
		this.categorie = c;
		this.prix = p;
	}

	public String getCategorie () {
		return this.categorie;
	}
	
	public float getPrix () {
		return this.prix;
	}
	
	public void setCategorie (String c) {
		this.categorie = c;
	}
	
	public void setPrix (float p) {
		this.prix = p;
	}
}
