package escampe;

import algorithmes.AlphaBeta;
import modeles.Etat;
import modeles.Heuristique;
import modeles.Probleme;
import modeles.Solution;

public class JoueurSuperFort implements IJoueur{

	public String player;
	private EscampeBoard board; //TODO : Peut-il etre static avec l'algo IA?
	private Heuristique h = HeuristiquesEscampe.h;
		
/*	private static Heuristique h1 = new Heuristique() {
		
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
                return evalutation;
            } else {
                throw new Error("Cette heursitique ne peut s'appliquer que sur des EtatEscampe");
            }
		}
	};
	
	private static Heuristique h2 = new Heuristique() {
		
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
	
	private static Heuristique h3 = new Heuristique() {
		
		@Override
		public float eval(Etat e) {
			// TODO Auto-generated method stub
			return 0;
		}
	};
	*/
	//AETOILE TROP LONG ! CEST UN ALGO QUI NE SARRETE PAS TANT QUON A PAS TROUVE LA SOLUTION ? 
	//private static AEtoile algo = new AEtoile(h2);//met trop de temps*
	
	private AlphaBeta algo;
	
	@Override
	public void initJoueur(int mycolour) {
		board = new EscampeBoard();
		//board.setFromFile("\\src\\data\\plateau1.txt");
		if(mycolour == -1) {
			this.algo = new AlphaBeta(h, "blanc", "noir");
			player = "blanc";
		}
		else {
			this.algo = new AlphaBeta(h, "noir", "blanc");
			player = "noir";
		}
	}

	@Override
	public int getNumJoueur() {
		// TODO Auto-generated method stub
		return player == "blanc" ? -1 : 1;
	}

	@Override
	public String choixMouvement() {
		if(board.gameOver()) {
			return "xxxxx";
		}
		//TODO : Premier mouvement a mieux choisir
		String w = "B2/A1/B1/C2/E2/F2";
		String b = "C6/A6/B5/D5/E6/F5";
		
		String coupJoue;
		
		if((board.getWhite()[0] == null) || (board.getBlack()[0] == null)){
			coupJoue = ((player == "blanc") ?  w : b);
		}
		else {
			
			EtatEscampe initial = new EtatEscampe(board.getWhite(), board.getBlack(), player, board.getLastLisere());
			
			String[] moves = board.possibleMoves(player);
			
			if (moves.length == 0) {
				return "E";
			}
			
			
			System.out.println("Coups possibles de "+initial.getPlayer()+":");
			//System.err.println(algo.getJoueurMax());
			
			for(String m : moves) {
				System.out.print(m+ ",");
				
			}
			System.out.println("\n");
			
			Probleme pb = new ProblemeEscampe( initial, "Pb escampe");
			System.err.println(pb.successeurs(initial).size());
			
			String meilleurCoup = algo.meilleurCoup(pb);
			
			System.out.println("Meilleur Coup : "+ meilleurCoup);
			coupJoue = meilleurCoup;
			
			
			//Si le coup choisi est fatal, on renvoit xxxxx pour signaler la fin de partie
			
			if (player == "blanc") {
				if (moves[0].split("-")[1].contentEquals(board.getBlack()[0])) {
					coupJoue = "xxxxx";
				}
			}
			else {
				if (moves[0].split("-")[1].contentEquals(board.getWhite()[0])) {
					coupJoue = "xxxxx";
				}
			}
			//coupJoue = moves[0];
		}
		
		board.play(coupJoue,player);
		return coupJoue;
	}

	@Override
	public void declareLeVainqueur(int colour) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouvementEnnemi(String coup) {
		// TODO Auto-generated method stub
		if(!(coup.compareTo("E") == 0)) {
			// Le joueur ne passe pas son tour
			if(player == "blanc") {
				board.play(coup, "noir");
			}
			else {
				board.play(coup, "blanc");
			}
		}
		
		print_board();
		//System.out.print("Black :");
		//board.print_black();
		//System.out.print("White :");
		//board.print_white();
	}

	@Override
	public String binoName() {
		// TODO Auto-generated method stub
		if(player == "blanc")
			return ("kobayashi");
		else
			return ("ramos");
	}
	
	public void print_board() {
		if ( (board.getBlack()[0]!=null)&&(board.getWhite()[0]!=null)&&(!board.gameOver()) ){
			char[][] eb = board.lists_to_board();
			for(int i=0; i<6; i++) {
				for(int j=0; j<6; j++) {
					System.out.print(eb[i][j]+" ");
					/*System.out.print(board.liserePlateau[i][j]);
					System.out.print(" | ");*/
				}
				System.out.print("    ");
				for(int j=0; j<6; j++) {
					System.out.print(board.liserePlateau[i][j]);
					System.out.print("|");
				}
				System.out.println("");
			}
		}
	}
}