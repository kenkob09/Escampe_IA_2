package modeles;

import java.util.Collection;

/**
 * Un probleme se caractérise par
 *	- un nom
 * 	- un etat initial,
 * 	- une méthode qui selon le problème posé determine si un
 *    état est un etat terminal (ou but)
 * 	- une méthode qui pour un état donné donne tous les
 *    successeurs possibles
 *    
 * @author Philippe Chatalic
 * @author Alexandre Allauzen
 */
public abstract class Probleme {

   /* ********* Attributs  *********** */
	
	/**
	 * Le nom du problème
	 */
	private String nomDuProbleme;

	/**
	 * L'état initial
	 */
	private Etat etatInitial;
	
  /* ********* Constructeurs *********** */
	
	/**
	 * Le constructeur de Probleme
	 * @param eInit l'état initial du problème
	 * @param nom le nom donné au problème
	 */
	public Probleme(Etat eInit,String nom){
		etatInitial = eInit;
		nomDuProbleme = new String(nom);
	}
	
  /* ********* Accesseur *********** */
	
	/**
	 * @return l'état initial du problème
	 */
	public Etat getEtatInitial(){
		return etatInitial;
	}
	
	/**
	 * Le nom du Probleme
	 */
	public String getNom(){
		return nomDuProbleme;
	}


	public String toString() {
		String s = "";
		if (! nomDuProbleme.equals(""))
			s = s + "Probleme : " + nomDuProbleme + "\n";
		s = s + "Etat Initial : n"+ etatInitial;
		return s;
	}

  /* ********* Autres méthodes *********** */
        
	/**
	 * @param e l'état considéré
	 * @return vrai si l'état est Terminal pour ce problème
	 * @throws Exception 
	 */
	public abstract boolean isTerminal(Etat e);

	/**
	 * @param e l'état considéré
	 * @return L'ensemble des états atteignables à partir de l'état
	 *         par un opérateur de changement d'état
	 *         L'utilisation d'une Collection permet de choisir librement
	 *         La classe la mieux adaptée en fonction du problème
	 */
	public abstract Collection <Etat> successeurs(Etat e);

}

