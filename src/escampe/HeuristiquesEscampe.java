package escampe;

import modeles.Etat;
import modeles.Heuristique;

public class HeuristiquesEscampe {

	
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
	
	public static Heuristique h2 = new Heuristique() {
		
		@Override
		public float eval(Etat e) {
			if (e instanceof EtatEscampe) {
				EtatEscampe ee = (EtatEscampe) e;
				int ret = 0;
				int iOrigin,jOrigin;
				String[] pions;
				
				if(ee.getPlayer() == "blanc") {
					pions = ee.getWhite();
					}
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
}
