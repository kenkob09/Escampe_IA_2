package escampe;

import java.util.Random;

import algorithmes.AlphaBeta;
import algorithmes.IDAlphaBeta;
import modeles.Probleme;

/** Joueur d'escampe basé sur l'interface IJoueur
 * 	*/

public class JoueurSuperFort implements IJoueur{

	/**	
	 * Le joueur à :
	 * 	- Une couleur
	 * 	- Un plateau de jeu
	 * 	- Différentes heuristiques
	 * */
	
	public String player;
	private EscampeBoard board; 
	private HeuristiqueEscampe hOP;
	private AlphaBeta algo;
	private IDAlphaBeta algoID;
	private final String placement_bas = "C6/B5/C5/D5/E5/F5"; //Meilleur placement bas
	private final String placement_haut = "C1/B2/C2/D2/E2/F2"; //Meilleur placement haut
	
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

		String coupJoue;
		
		//Si aucun des deux joueurs n'a fait son placement de debut de partie
		if( (board.getWhite()[0]==null)&&(board.getBlack()[0]==null) ) {
			Random rand = new Random();
			int r = rand.nextInt(100);		
			coupJoue = (r<=50) ? placement_haut : placement_bas;
		}
		
		//Si l'un des deux joueurs a deja fait son placement de debut de partie
		else if((board.getWhite()[0] == null) || (board.getBlack()[0] == null)){
			String couleur_adverse = (board.getBlack()[0] == null) ? "white" : "black";
			String bord_ami;
			
			//On prend le bord oppose au bord qui contient la licorne adverse
			if (couleur_adverse.contains("white")) {
				bord_ami = ( (EscampeBoard.get_i_from_string(board.getWhite()[0]) ) >3 )?"haut" : "bas";
			}
			else {
				bord_ami = ( (EscampeBoard.get_i_from_string(board.getBlack()[0]) ) >3 )?"haut" : "bas";
			}
			coupJoue = (bord_ami.contains("haut")) ? placement_haut : placement_bas ;
		}
		
		
		//Si on est en milieu de partie
		else {
			
			if(board.gameOver()) {	// Si la partie est fini
				return "xxxxx";
			}
			
			// Définition de l'état initial correspondant au plateau actuel
			EtatEscampe initial = new EtatEscampe(board.getWhite(), board.getBlack(), player, board.getLastLisere());
			
			String[] moves = board.possibleMoves(player);
			
			if (moves.length == 0) {	// Si il n'y a pas de coup possibles
				return "E";
			}
			
			System.out.println("Coups possibles de "+initial.getPlayer()+":");
						
			for(String m : moves) {
				System.out.print(m+ ",");
			}
			System.out.println("");
			
			//On instancie le probleme
			Probleme pb = new ProblemeEscampe( initial, "Probleme escampe");
			
			// Test avec différents Heuristiques, décommenter pour tester
			
			// Avec AlphaBeta
			//String meilleurCoup = algo.meilleurCoup(pb);
			
			// Avec IterativeAlphaBeta
			String meilleurCoup = algoID.meilleurCoup(pb);	// Itérative Deepening Alpha Beta
			
			System.out.println("Meilleur Coup : "+ meilleurCoup);
			coupJoue = meilleurCoup;
			
		}
		
		board.play(coupJoue,player);	// On joue le coup pour mettre à jour notre structure de données
		return coupJoue;	// On retroune le coup pour informer l'arbitre
	}

	@Override
	public void declareLeVainqueur(int colour) {
		// TODO Auto-generated method stub
		System.out.println("Je suis dans mon jaccuzzi, t'es dans ta jalousie");
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
		return "Ramoyashi Sensei";
	}
	
	/**
	 * Affiche le plateau actuel
	 * */
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
					System.out.print(EscampeBoard.liserePlateau[i][j]);
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