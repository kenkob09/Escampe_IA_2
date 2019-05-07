package escampe;

import java.util.Collection;
import java.util.LinkedList;
import modeles.Etat;
import modeles.Probleme;

/** Classe Iterative deepening Alpha Beta
 * 
 * @author Kobayashi_Ramos
 * @see
 * @version 1*/

public class ProblemeEscampe extends Probleme{
	
	public ProblemeEscampe(Etat eInit, String nom) {
		super(eInit, nom);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean isTerminal(Etat e) {
		if (!(e instanceof EtatEscampe)) {
            return false;
        }
		EtatEscampe ee = (EtatEscampe) e;
		return ee.getBlack()[0].contains("ZZ")||ee.getWhite()[0].contains("ZZ");
	}

	@Override
	public Collection<Etat> successeurs(Etat e) {
		LinkedList<Etat> toRet = new LinkedList<Etat>();
        if ((e instanceof EtatEscampe) && (!this.isTerminal(e))) {
            EtatEscampe etat = (EtatEscampe) e;
            //On fait des copies pour eviter les effets de bords
            EscampeBoard eb = new EscampeBoard(etat.getWhite().clone(),etat.getBlack().clone(),new Integer(etat.getLastLisere()));
            
            for(String m : eb.possibleMoves(etat.getPlayer())) {
            	
            	//On fait des copies pour eviter les effets de bords
            	String[] white = etat.getWhite().clone();
            	String[] black = etat.getBlack().clone();
            	String player = new String(etat.getPlayer());
            	int lastLisere = new Integer(etat.getLastLisere());
            	
            	//On recupere l'etat resultant d'un coup en modifiant directement les variables d'etat et en simulant un coup
            	eb.simulate_play(m, white, black, player, lastLisere);
            	
            	//On recupere le lisere du coup pour le mettre a jour
            	lastLisere = eb.getLisereAt(m.split("-")[1]);
            	
            	//On met a jour le joueur
            	player = (player.contentEquals("blanc")) ? "noir":"blanc";
            	
            	toRet.add(new EtatEscampe(white,black,player,lastLisere, m));
            }
        }
        return toRet;
	}
}