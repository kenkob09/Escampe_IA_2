/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modeles;

import modeles.Etat;

/**
 *
 * @author pc
 */
public interface Heuristique {

    /**
     * @param e
     * @return la valeur de l'heuristique pour le noeud
     */
        public float eval(Etat e);

}
