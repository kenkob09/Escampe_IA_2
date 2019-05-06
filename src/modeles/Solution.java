package modeles;

import java.util.LinkedList;

/**
 * Un chemin solution pour un problème de recherche dans un espace
 * d'états est représenté par une liste chainée d'états dont le dernier
 * est un état terminal pour le problème considéré.
 * 
 * @author Philippe Chatalic
 */
public class Solution extends LinkedList<Etat> {

    // Constructeurs -----------------------------------------------------------
    /**
     * Cree une solution vide
     */
    public Solution() {
        super();
    }
    /**
     * Cree une solution ne contenant qu'un seul état
     * @param e l'unique état de la solution
     */
    public Solution(Etat e) {
        super();
        this.add(e);
    }

    // Autres méthodes -----------------------------------------------------------
    /**
     * Affichage d'un chemin solution (un état par ligne)
     */
    public void affiche() {
        for (Etat e : this) {
            System.out.println(e);
        }
    }
}
