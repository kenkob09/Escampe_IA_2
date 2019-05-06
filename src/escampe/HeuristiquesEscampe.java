package escampe;

import modeles.Etat;
import modeles.Heuristique;

public class HeuristiquesEscampe {

	//Heuristique bidon
	public static Heuristique h = new Heuristique() {
		
		@Override
		public float eval(Etat e) {
			// TODO Auto-generated method stub
			if (e instanceof EtatEscampe) {
				EtatEscampe ee = (EtatEscampe) e;
				int evalutation = 0; 
				for(String p : ee.getWhite()) {
					evalutation += EscampeBoard.get_i_from_string(p) + EscampeBoard.get_j_from_string(p);
				}
				for(String p : ee.getBlack()) {
					evalutation -= EscampeBoard.get_i_from_string(p) + EscampeBoard.get_j_from_string(p);
				}
                return (float)evalutation;
            } else {
                throw new Error("Cette heursitique ne peut s'appliquer que sur des EtatEscampe");
            }
		}
	};
	
	//Heuristique mashallah --
	//Heuristique qui fait la somme des distances des pions amies de la licorne amie
	public static Heuristique h2 = new Heuristique() {
		
		@Override
		public float eval(Etat e) {
			if (e instanceof EtatEscampe) {
				EtatEscampe ee = (EtatEscampe) e;
				int ret = 0;
				int iOrigin,jOrigin;
				String[] pions;
				
				//Cas ou le joueur ami est blanc
				if(ee.getPlayer().contains("blanc") ) {
					pions = ee.getWhite();
					}
				//Cas ou le joueur ami est noir
				else {
					pions = ee.getBlack();
				}
				
				iOrigin = EscampeBoard.get_i_from_string(pions[0]);
				jOrigin = EscampeBoard.get_j_from_string(pions[0]);
				
				// On regarde la distance de chaques pions de la licorne
				for(int i = 1; i < 6; i++) {
					int iDistance = Math.abs(iOrigin - EscampeBoard.get_i_from_string(pions[i]));
					int jDistance = Math.abs(jOrigin - EscampeBoard.get_j_from_string(pions[i]));
					// Plus ils sont eloigne, plus la licorde est en danger
					ret += (iDistance + jDistance);
				}
				return ret;
            } else {
                throw new Error("Cette heursitique ne peut s'appliquer que sur des EtatEscampe");
            }
		}
	};
	
	//Heuristique mashallah +
	//Heuristique qui fait (somme des distances qui separent la licorne amie aux paladins ennemie) 
	// 					 - (somme des distances qui separent la licorne ennemie aux paladins amis)
	public static Heuristique h3 = new Heuristique() {
		
		@Override
		public float eval(Etat e) {
			if (e instanceof EtatEscampe) {
				EtatEscampe ee = (EtatEscampe) e;
				int i_l_amie,j_l_amie;
				int i_l_ennemie,j_l_ennemie;
				int sum_dist_max = 0;
				int sum_dist_min = 0;
				String[] pions_amie, pions_ennemie;
				
				//Cas ou le joueur ami est blanc
				if(ee.getPlayer().contains("blanc") ) {
					pions_amie = ee.getWhite();
					pions_ennemie = ee.getBlack();
				}
				//Cas ou le joueur ami est noir
				else {
					pions_ennemie = ee.getWhite();
					pions_amie = ee.getBlack();				
				}
				i_l_amie = EscampeBoard.get_i_from_string(pions_amie[0]);
				j_l_amie = EscampeBoard.get_j_from_string(pions_amie[0]);
				i_l_ennemie = EscampeBoard.get_i_from_string(pions_ennemie[0]);
				j_l_ennemie = EscampeBoard.get_j_from_string(pions_ennemie[0]);
				
				// On regarde la distance de chaques paladins de la licorne
				for(int i = 1; i < 6; i++) {
					int i_max_distance = Math.abs(i_l_amie - EscampeBoard.get_i_from_string(pions_ennemie[i]));
					int j_max_distance = Math.abs(j_l_amie - EscampeBoard.get_j_from_string(pions_ennemie[i]));
					int i_min_distance = Math.abs(i_l_ennemie - EscampeBoard.get_i_from_string(pions_amie[i]));
					int j_min_distance = Math.abs(j_l_ennemie - EscampeBoard.get_j_from_string(pions_amie[i]));
					//sum_dist_max doit etre grand, sum_dist_min doit etre petit
					if( (i_max_distance + j_max_distance)<=3 ) {
						//Un paladin a une distance inferieur a 3 a plus de poids que le reste
						i_max_distance = i_max_distance*2;
						j_max_distance = j_max_distance*2;
					}
					if( (i_min_distance + j_min_distance)<=3 ) {
						//Un paladin a une distance inferieur a 3 a plus de poids que le reste
						i_min_distance = i_min_distance*2;
						j_min_distance = j_min_distance*2;
					}
					sum_dist_max += (i_max_distance + j_max_distance);
					sum_dist_max += (i_min_distance + j_min_distance);
				}
				return sum_dist_max - sum_dist_min;
            } else {
                throw new Error("Cette heursitique ne peut s'appliquer que sur des EtatEscampe");
            }
		}
	};
	
	//Heuristique mashallah classique
	//Heuristique qui renvoie +infini si c'est gagne pour amie, -infini si c'est perdu pour amie
	public static Heuristique hend = new Heuristique() {
		
		@Override
		public float eval(Etat e) {
			if (e instanceof EtatEscampe) {
				EtatEscampe ee = (EtatEscampe) e;
				String[] pions_amie, pions_ennemie;
				
				//Cas ou le joueur ami est blanc
				if(ee.getPlayer().contains("blanc") ) {
					pions_amie = ee.getWhite();
					pions_ennemie = ee.getBlack();
				}
				//Cas ou le joueur ami est noir
				else {
					pions_ennemie = ee.getWhite();
					pions_amie = ee.getBlack();				
				}
				
				//Si la licorne amie est morte
				if ( pions_amie[0].contains("ZZ") ) {
					return -10000000;
				}
				if ( pions_ennemie[0].contains("ZZ") ) {
					return +10000000;
				}
				return 0;
            } else {
                throw new Error("Cette heursitique ne peut s'appliquer que sur des EtatEscampe");
            }
		}
	}; 
	
	//Heuristique mashallah ++ 
	public static Heuristique h4 = new Heuristique() {
		
		@Override
		public float eval(Etat e) {
			if (e instanceof EtatEscampe) {
				EtatEscampe ee = (EtatEscampe) e;
				int i_l_amie,j_l_amie;
				int i_l_ennemie,j_l_ennemie;
				int sum_dist_max = 0;
				int sum_dist_min = 0;
				String[] pions_amie, pions_ennemie;
				
				//Cas ou le joueur ami est blanc
				if(ee.getPlayer().contains("blanc") ) {
					pions_amie = ee.getWhite();
					pions_ennemie = ee.getBlack();
				}
				//Cas ou le joueur ami est noir
				else {
					pions_ennemie = ee.getWhite();
					pions_amie = ee.getBlack();				
				}
				i_l_amie = EscampeBoard.get_i_from_string(pions_amie[0]);
				j_l_amie = EscampeBoard.get_j_from_string(pions_amie[0]);
				i_l_ennemie = EscampeBoard.get_i_from_string(pions_ennemie[0]);
				j_l_ennemie = EscampeBoard.get_j_from_string(pions_ennemie[0]);
				
				// On regarde la distance de chaques pions de la licorne
				for(int i = 1; i < 6; i++) {
					int i_max_distance = Math.abs(i_l_amie - EscampeBoard.get_i_from_string(pions_ennemie[i]));
					int j_max_distance = Math.abs(j_l_amie - EscampeBoard.get_j_from_string(pions_ennemie[i]));
					int i_min_distance = Math.abs(i_l_ennemie - EscampeBoard.get_i_from_string(pions_amie[i]));
					int j_min_distance = Math.abs(j_l_ennemie - EscampeBoard.get_j_from_string(pions_amie[i]));
					//sum_dist_max doit etre grand, sum_dist_min doit etre petit
					sum_dist_max += (i_max_distance + j_max_distance);
					sum_dist_max +=  (i_min_distance + j_min_distance);
				}
				return sum_dist_max - sum_dist_min;
            } else {
                throw new Error("Cette heursitique ne peut s'appliquer que sur des EtatEscampe");
            }
		}
	};
	
}
