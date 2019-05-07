package escampe;

import modeles.Etat;
import modeles.Heuristique;


/**
 *  Heuristiques propre à l'escampe
 * 	*/
public class HeuristiqueEscampe implements Heuristique{

	String player;

	public HeuristiqueEscampe(String player) {
		this.player=player;
	}
	
	//Heuristique hFinal
	//Heuristique qu'on va utiliser qui regroupe evalEnd, evalDist et le fait qu'un pion ne doit pas aller sur un lisere qui pourrait mettre en danger sa licorne
	public float eval(Etat e) {
		if (e instanceof EtatEscampe) {
			EtatEscampe ee = (EtatEscampe) e;
			int res=0;
			int i_l_ami,j_l_ami;
			int i_l_ennemi,j_l_ennemi;
			int dist_min,dist_max;
			int sum_dist_max = 0;
			int sum_dist_min = 0;
			String[] pions_ami, pions_ennemi;
			String player_ami, player_ennemi;
		
			//Cas ou le joueur ami est blanc
			if(this.player.contains("blanc") ) {
				pions_ami = ee.getWhite();
				pions_ennemi = ee.getBlack();
				player_ami = "blanc";
				player_ennemi = "noir";
			}
			//Cas ou le joueur ami est noir
			else {
				pions_ennemi = ee.getWhite();
				pions_ami = ee.getBlack();
				player_ami = "noir";
				player_ennemi = "blanc";
			}
			
			//Si la licorne ami est morte
			if ( pions_ami[0].contains("ZZ") ) {
				return Integer.MIN_VALUE;
			}
			//Si la licorne ennemi est morte
			if ( pions_ennemi[0].contains("ZZ") ) {
				return Integer.MAX_VALUE;
			}
			
			//EscampeBoard correspondant a l'etat
			EscampeBoard eb = new EscampeBoard(ee.getWhite(),ee.getBlack(),ee.getLastLisere());
			
			//Liste des coups possibles de l'ami si jamais on en a besoin
			String[] possible_ami_moves = new String[0];
			String[] possible_ennemi_moves = new String[0];
			i_l_ami = EscampeBoard.get_i_from_string(pions_ami[0]);
			j_l_ami = EscampeBoard.get_j_from_string(pions_ami[0]);
			i_l_ennemi = EscampeBoard.get_i_from_string(pions_ennemi[0]);
			j_l_ennemi = EscampeBoard.get_j_from_string(pions_ennemi[0]);
			
			// On regarde la distance de chaques paladins de la licorne
			for(int i = 1; i < 6; i++) {
				int i_max_distance = Math.abs(i_l_ami - EscampeBoard.get_i_from_string(pions_ennemi[i]));
				int j_max_distance = Math.abs(j_l_ami - EscampeBoard.get_j_from_string(pions_ennemi[i]));
				int i_min_distance = Math.abs(i_l_ennemi - EscampeBoard.get_i_from_string(pions_ami[i]));
				int j_min_distance = Math.abs(j_l_ennemi - EscampeBoard.get_j_from_string(pions_ami[i]));
				dist_min = i_min_distance + j_min_distance;
				dist_max = i_max_distance + j_max_distance;
				//sum_dist_max doit etre grand, sum_dist_min doit etre petit
				if( dist_max <=3 ) {
					//Un paladin ennemi a une distance inferieur a 3 de la licorne ami a 2* plus de poids que le reste
					sum_dist_max += dist_max/2;
				}
				else {
					sum_dist_max += dist_max;
				}
				if( dist_min<=3 ) {
					//Un paladin ami a une distance inferieur a 3 de la licorne ennemi a 2* plus de poids que le reste
					sum_dist_min += dist_min/2;
				}
				else {
					sum_dist_min += dist_min;
				}
				
				//S'il existe un paladin ennemi proche de 3 cases ou moins de la licorne ami
				if (dist_max <=3) {
					
					//on recupere le lisere du paladin ennemi concerne
					int i_danger_p = EscampeBoard.get_i_from_string(pions_ennemi[i]);
					int j_danger_p = EscampeBoard.get_j_from_string(pions_ennemi[i]);
					int lisere_danger_p = EscampeBoard.liserePlateau[i_danger_p][j_danger_p];
					
					//Si le lisere du paladin ennemi correspond a la distance qui le separe de la licorne ami et que la licorne ne peut pas bouger
					if ( (dist_max == lisere_danger_p)&&(ee.getLastLisere()!= EscampeBoard.liserePlateau[i_l_ami][j_l_ami]) ) {
						
						//On regarde s'il existe un chemin entre les deux
						String paladin_danger = pions_ennemi[i];
						boolean l_ami_atteignable = false;
						
						//On regarde les mouvements possibles du paladin si jamais il peut jouer au prochain tour
						for (String m_danger : eb.possibleMovesForOnePawn(paladin_danger, player_ennemi)) {
							//S'il peut atteindre la licorne
							if (m_danger.split("-")[1].contains(pions_ami[0]) ) {
								l_ami_atteignable = true;
							}
						}
						
						//Si la licorne est atteignable par le paladin
						if (l_ami_atteignable) {
							//On regarde si les pions amis peuvent etre contraint a se deplacer sur un lisere_danger_p selon last_lisere
							//On calcule les mouvements possibles de ami seulement si il n'a jamais ete calcule
							if (possible_ami_moves.length==0) {
								possible_ami_moves = eb.possibleMoves(player_ami);
							}
							
							//On regarde s'il est possible d'eviter de deplacer un pion sur le le lisere de la mort
							boolean eviter_lisere_danger = false;
							for(String m : possible_ami_moves) {
								//Si un pion ami peut se deplacer sur une case dont le lisere est different du lisere de la mort
								if (eb.getLisereAt(m.split("-")[1]) != lisere_danger_p) {
									eviter_lisere_danger = true;
								}
							}
							
							//Si ce n'est pas possible d'eviter le lisere de la mort, ami a perdu
							if (!eviter_lisere_danger) {
								return Integer.MIN_VALUE;
							}
							
							//Si c'est possible d'eviter le lisere de la mort, la situation reste defavorable
							else {
								res = res - 100;
							}
						}
						
						//Si la licorne n'est pas atteignable par le paladin, la situation reste un peu defavorable
						else {
							res = res - 50;
						}
					}
				}
				//S'il existe un paladin ami proche de 3 cases ou moins de la licorne ennemi
				if (dist_min <=3) {
					
					//on recupere le lisere du paladin ami concerne
					int i_tropfort_p = EscampeBoard.get_i_from_string(pions_ami[i]);
					int j_tropfort_p = EscampeBoard.get_j_from_string(pions_ami[i]);
					int lisere_tropfort_p = EscampeBoard.liserePlateau[i_tropfort_p][j_tropfort_p];
					
					//Si le lisere du paladin ami correspond a la distance qui le separe de la licorne ennemi et que la licorne ne peut pas bouger
					if ( (dist_min == lisere_tropfort_p)&&(ee.getLastLisere()!= EscampeBoard.liserePlateau[i_l_ennemi][j_l_ennemi]) ) {
						
						//On regarde s'il existe un chemin entre les deux
						String paladin_tropfort = pions_ami[i];
						boolean l_ennemi_atteignable = false;
						
						//On regarde les mouvements possibles du paladin si jamais il peut jouer au prochain tour
						for (String m_tropfort : eb.possibleMovesForOnePawn(paladin_tropfort, player_ami)) {
							//S'il peut atteindre la licorne
							if (m_tropfort.split("-")[1].contains(pions_ennemi[0]) ) {
								l_ennemi_atteignable = true;
							}
						}
						
						//Si la licorne est atteignable par le paladin
						if (l_ennemi_atteignable) {
							//On regarde si les pions ennemis peuvent etre contraints a se deplacer sur un lisere_tropfort_p selon last_lisere
							//On calcule les mouvements possibles de ennemi seulement si il n'a jamais ete calcule
							if (possible_ennemi_moves.length==0) {
								possible_ennemi_moves = eb.possibleMoves(player_ennemi);
							}
							
							//On regarde s'il est possible d'eviter de deplacer un pion sur le le lisere de la victoire
							boolean eviter_lisere_tropfort = false;
							for(String m : possible_ennemi_moves) {
								
								//Si un pion ennemi peut se deplacer sur une case dont le lisere est different du lisere de la victoire
								if (eb.getLisereAt(m.split("-")[1]) != lisere_tropfort_p) {
									eviter_lisere_tropfort = true;
								}
							}
							
							//Si ce n'est pas possible d'eviter le lisere de la mort, ennemi a perdu
							if (!eviter_lisere_tropfort) {
								return Integer.MAX_VALUE;
							}
							
							//Si c'est possible d'eviter le lisere de la victoire, la situation reste favorable
							else {
								res = res + 100;
							}
						}
						
						//Si la licorne ennemi n'est pas atteignable par le paladin, la situation reste un peu favorable
						else {
							res = res + 50;
						}
					}
				}
			}
			res = res + sum_dist_max - sum_dist_min;
			return res;
        } else {
            throw new Error("Cette heursitique ne peut s'appliquer que sur des EtatEscampe");
        }
	}
	
	
	//Heuristique hDist
	public float evalDist(Etat e) {
		if (e instanceof EtatEscampe) {
			EtatEscampe ee = (EtatEscampe) e;
			int i_l_ami,j_l_ami;
			int i_l_ennemi,j_l_ennemi;
			int sum_dist_max = 0;
			int sum_dist_min = 0;
			String[] pions_ami, pions_ennemi;
			
			//Cas ou le joueur ami est blanc
			if(this.player.contains("blanc") ) {
				pions_ami = ee.getWhite();
				pions_ennemi = ee.getBlack();
			}
			//Cas ou le joueur ami est noir
			else {
				pions_ennemi = ee.getWhite();
				pions_ami = ee.getBlack();				
			}
			i_l_ami = EscampeBoard.get_i_from_string(pions_ami[0]);
			j_l_ami = EscampeBoard.get_j_from_string(pions_ami[0]);
			i_l_ennemi = EscampeBoard.get_i_from_string(pions_ennemi[0]);
			j_l_ennemi = EscampeBoard.get_j_from_string(pions_ennemi[0]);
			
			// On regarde la distance de chaques paladins de la licorne
			for(int i = 1; i < 6; i++) {
				int i_max_distance = Math.abs(i_l_ami - EscampeBoard.get_i_from_string(pions_ennemi[i]));
				int j_max_distance = Math.abs(j_l_ami - EscampeBoard.get_j_from_string(pions_ennemi[i]));
				int i_min_distance = Math.abs(i_l_ennemi - EscampeBoard.get_i_from_string(pions_ami[i]));
				int j_min_distance = Math.abs(j_l_ennemi - EscampeBoard.get_j_from_string(pions_ami[i]));
				//sum_dist_max doit etre grand, sum_dist_min doit etre petit
				if( (i_max_distance + j_max_distance)<=3 ) {
					//Un paladin ennemi a une distance inferieur a 3 de la licorne ami a 2* plus de poids que le reste
					i_max_distance = i_max_distance/2;
					j_max_distance = j_max_distance/2;
				}
				if( (i_min_distance + j_min_distance)<=3 ) {
					//Un paladin ami a une distance inferieur a 3 de la licorne ennemi a 2* plus de poids que le reste
					i_min_distance = i_min_distance/2;
					j_min_distance = j_min_distance/2;
				}
				sum_dist_max += (i_max_distance + j_max_distance);
				sum_dist_min += (i_min_distance + j_min_distance);
			}
			return sum_dist_max - sum_dist_min;
        } else {
            throw new Error("Cette heursitique ne peut s'appliquer que sur des EtatEscampe");
        }
	}
	
	
	//Heuristique hEnd
	public float evalEnd(Etat e) {
		if (e instanceof EtatEscampe) {
			EtatEscampe ee = (EtatEscampe) e;
			String[] pions_ami, pions_ennemi;
			
			//Cas ou le joueur ami est blanc
			if(this.player.contains("blanc") ) {
				pions_ami = ee.getWhite();
				pions_ennemi = ee.getBlack();
			}
			//Cas ou le joueur ami est noir
			else {
				pions_ennemi = ee.getWhite();
				pions_ami = ee.getBlack();				
			}
			
			//Si la licorne ami est morte
			if ( pions_ami[0].contains("ZZ") ) {
				return -10000000;
			}
			if ( pions_ennemi[0].contains("ZZ") ) {
				return 10000000;
			}
			return 0;
        } else {
            throw new Error("Cette heursitique ne peut s'appliquer que sur des EtatEscampe");
        }
	}
}
	
