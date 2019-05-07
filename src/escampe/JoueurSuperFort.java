package escampe;

import algorithmes.AlphaBeta;
import algorithmes.IDAlphaBeta;
import modeles.Etat;
import modeles.Heuristique;
import modeles.Probleme;

public class JoueurSuperFort implements IJoueur{

	public String player;
	private EscampeBoard board; //TODO : Peut-il etre static avec l'algo IA?
	private HeuristiqueEscampe hOP;
	private AlphaBeta algo;
	private IDAlphaBeta algoID;
	
	@Override
	public void initJoueur(int mycolour) {
		board = new EscampeBoard();
		//board.setFromFile("\\src\\data\\plateau1.txt");
		if(mycolour == -1) {
			this.hOP = new HeuristiqueEscampe("blanc");
			this.algo = new AlphaBeta(hOP, "blanc", "noir");
			this.algoID = new IDAlphaBeta(hOP, "blanc", "noir");
			player = "blanc";
		}
		else {
			this.hOP = new HeuristiqueEscampe("noir");
			this.algo = new AlphaBeta(hOP, "noir", "blanc");
			this.algoID = new IDAlphaBeta(hOP, "noir", "blanc");
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
		
		//TODO : Premier mouvement a mieux choisir
		String w = "F1/A1/B1/C2/E2/F2";
		String b = "C6/A6/B5/D5/E6/F5";
		
		String coupJoue;
		
		if((board.getWhite()[0] == null) || (board.getBlack()[0] == null)){
			coupJoue = ((player == "blanc") ?  w : b);
		}
		else {
			
			if(board.gameOver()) {
				return "xxxxx";
			}
			
			// Définition de l'état initial correspondant au plateau actuel
			EtatEscampe initial = new EtatEscampe(board.getWhite(), board.getBlack(), player, board.getLastLisere());
			
			String[] moves = board.possibleMoves(player);
			
			// Si il n'y a pas de coup possibles
			if (moves.length == 0) {
				return "E";
			}
			
			System.out.println("Coups possibles de "+initial.getPlayer()+":");
						
			for(String m : moves) {
				System.out.print(m+ ",");
			}
			System.out.println("");
			
			//On instancie le probleme
			
			Probleme pb = new ProblemeEscampe( initial, "Probleme escampe");
			
			// Avec AlphaBeta
			String meilleurCoup = algo.meilleurCoup(pb);
			
			// Avec IterativeAlphaBeta
			//String meilleurCoup = algoID.meilleurCoup(pb);
			
			// Avec NegAlphaBeta
			//String meilleurCoup = algoNegAB.meilleurCoup(pb);
			
			System.out.println("Meilleur Coup : "+ meilleurCoup);
			coupJoue = meilleurCoup;
			
			
			//Si le coup choisi est fatal, on renvoit xxxxx pour signaler la fin de partie
			
//			if (player == "blanc") {
//				if (moves[0].split("-")[1].contentEquals(board.getBlack()[0])) {
//					return "xxxxx";
//				}
//			}
//			else {
//				if (moves[0].split("-")[1].contentEquals(board.getWhite()[0])) {
//					return "xxxxx";
//				}
//			}
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

	public AlphaBeta getAlgo() {
		return algo;
	}

	public void setAlgo(AlphaBeta algo) {
		this.algo = algo;
	}
}