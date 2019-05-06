package escampe;

import java.util.Collection;
import java.util.LinkedList;
import modeles.Etat;
import modeles.Probleme;
//import sun.awt.SunHints.Value;
//import problemes.tsp.EtatTSP;


/**
 * 	┏━━╮┏┓┏┓╭━┓┏━┓
	┃┏╮┃┃┃┃┃┃╭┛┃┗┓
	┃┃┃╰┛┃┃┃┃╰┓┃┗┓
	┗┛╰━━┛┗┛╰━┛┗━┛﻿
	@author ramos*/


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
		///////////////////////////////////////////////TODO : Pas besoin de copier ?
		///// Pas sur que la copie serve puisque l'on copie le même tableau, game over fait simplement une vérification, elle ne modifie rien
		EscampeBoard eb = new EscampeBoard(ee.getWhite(),ee.getBlack(),ee.getLastLisere());
		return eb.gameOver();
	}

	@Override
	public Collection<Etat> successeurs(Etat e) {
		LinkedList<Etat> toRet = new LinkedList<Etat>();
        if ((e instanceof EtatEscampe) && (!this.isTerminal(e))) {
            EtatEscampe etat = (EtatEscampe) e;
            //On fait des copies pour eviter les effets de bords
            EscampeBoard eb = new EscampeBoard(etat.getWhite().clone(),etat.getBlack().clone(),new Integer(etat.getLastLisere()));
            
            for(String m : eb.possibleMoves(etat.getPlayer())) {
            	
            	/*
            	System.out.println("joueur : "+etat.getPlayer());
            	System.out.println("lastlisere : "+eb.getLastLisere());
            	System.out.println("movement : "+m);
            	System.out.print("black : ");
            	eb.print_black();
            	System.out.print("blanc : ");
            	eb.print_white();*/
            	            	
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