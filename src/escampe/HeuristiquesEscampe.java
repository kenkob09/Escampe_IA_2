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
}
