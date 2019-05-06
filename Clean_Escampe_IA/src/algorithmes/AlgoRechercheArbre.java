package  algorithmes;

import modeles.CoupJeu;
import modeles.PlateauJeu;

public interface AlgoRechercheArbre {

    /** Renvoie le meilleur
     * @param p
     * @return
     */
	public CoupJeu meilleurCoup(PlateauJeu p);

}
 