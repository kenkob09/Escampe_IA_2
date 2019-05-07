package escampe;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import modeles.Etat;

public class EscampeBoard implements Etat {
	
	/**	Attributs	**/
	public final static char[] alphabet = {'A','B','C','D','E','F'};

	// Lisere du plateau avec Lettres en j et Chiffre en i
    public final static int[][] liserePlateau =
        {
            {1,2,2,3,1,2},
            {3,1,3,1,3,2},
            {2,3,1,2,1,3},
            {2,1,3,2,3,1},
            {1,3,1,3,1,2},
            {3,2,2,1,3,2}
        };
    
	private String[] white;
	private String[] black;
	private int last_lisere;
	private String bord_noir;

	/**	Constructeurs	**/
	public EscampeBoard (String[] w, String[] b, int last_lisere){
		this.white = new String[6];
		this.black = new String[6];
		this.white = w;
		this.black = b;
		this.last_lisere = last_lisere;
	}
	
	public EscampeBoard() {
		// TODO Auto-generated constructor stub
		this.white = new String[6];
		this.black = new String[6];
		last_lisere = 0;
	}
	
	public String[] getWhite() {
		return this.white;
	}
	
	public String[] getBlack() {
		return this.black;
	}
	
	public int getLastLisere() {
		return this.last_lisere;
	}
	
	public int getLisereAt(String c) {
		int i = get_i_from_string(c);
		int j = get_j_from_string(c);
		return liserePlateau[i][j];
	}
	
	/**Methodes de l'interface**/

	public void setFromFile(String fileName){
		String projectDir = Paths.get(".").toAbsolutePath().normalize().toString();
		String filePath = projectDir + fileName;
		Path path = Paths.get(filePath);
        int pionNoir = 0;
        int pionBlanc = 0;

        int iLine = 0;
        try {
            List<String> lines = Files.readAllLines(path);

            for (String line:lines) { // Pour chaque lignes
                if(line.charAt(0) != '%'){
                    String[] splitedLine = line.split("\\s+");
                    char[] valueLine = splitedLine[1].toCharArray();

                    for(int j = 0; j < 6; j++){
                        switch (valueLine[j]){
                            case 'B': white[0] = ""+alphabet[j]+""+(iLine+1);
                                break;
                            case 'b': white[++pionBlanc] = ""+alphabet[j]+""+(iLine+1);
                            break;
                            case 'N': black[0] = ""+alphabet[j]+""+(iLine+1);
                                break;
                            case 'n': black[++pionNoir] = ""+alphabet[j]+""+(iLine+1); 
                                break;
                            default: break;
                        }  
                    }
                    iLine++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void saveToFile(String fileName){
		String projectDir = Paths.get(".").toAbsolutePath().normalize().toString();
		String filePath = projectDir + fileName;
		
		String sauvegarde = "%\tABCDEF\n";
        char[][] board = lists_to_board();
        for(int i = 0; i < 6; i++){
            sauvegarde += "0"+ (i+1) +"\t";
            for(int j = 0; j < 6; j++){
                sauvegarde += board[i][j];
            }
			sauvegarde += "\t0" + (i+1) + "\n";
		}
		sauvegarde += "%\tABCDEF\n";

		try {

			FileWriter sauv = new FileWriter(filePath);
			sauv.write(sauvegarde);
			sauv.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isValidMove(String move, String player){
		//Pour le placement en DEBUT de partie
        if(move.length() > 5){
        	String[] pions = move.split("/");
        	//Si le joueur est noir, il commence et choisit son bord
        	if (player.contains("noir")) {
        		//Il peut choisir soit le bord haut ou bas
        		int i = get_i_from_string(pions[0]);
        		int j = get_j_from_string(pions[0]);
        		//On regarde si le joueur noir a decider de poser ses pions en haut ou en bas
        		if (i<=1) {
        			bord_noir = "haut";
        		}
        		else {
        			bord_noir = "bas";
        		}
        		//variable locale qui va servir a regarder si les pions se recouvrent
        		ArrayList<String> presence = new ArrayList<>();
        		//On verifie que tous les pions sont du meme cote et qu'ils ne sortent pas du plateau et qu'ils ne se recouvrent pas
    			for(String p : pions) {
    				//Si deux piece se recouvrent, renvoyer faux
    				if (presence.contains(p)){
    					return false;
    				}
    				presence.add(p);
    				i = get_i_from_string(p);
    				j = get_j_from_string(p);
    				if ( (bord_noir.contains(("haut")))&&((i<0)||(i>1)||(j<0)||(j>5)) ) {
    					return false;		
    				}
    				else if ( (bord_noir.contains("bas"))&&((i<4)||(i>5)||(j<0)||(j>5)) ) {
    					return false;
    				}
    			}
        	}
        	//Si le joueur est blanc
        	else {
        		//variable locale qui va servir a regarder si les pions se recouvrent
        		ArrayList<String> presence = new ArrayList<>();
        		//On verifie que tous les pions sont du meme cote et qu'ils ne sortent pas du plateau et qu'ils ne se recouvrent pas
    			for(String p : pions) {
    				//Si deux piece se recouvrent, renvoyer faux
    				if (presence.contains(p)){
    					return false;
    				}
    				presence.add(p);
    				int i = get_i_from_string(p);
    				int j = get_j_from_string(p);
    				bord_noir = (get_i_from_string(black[0])>3) ? "haut" : "bas";
    				if ( (bord_noir.contains("bas") )&&((i<0)||(i>1)||(j<0)||(j>5)) ) {
    					return false;		
    				}
    				else if ( (bord_noir.contains("haut") )&&((i<4)||(i>5)||(j<0)||(j>5)) ) {
    					return false;
    				}
    			}        		
        	}        	
        }
		
        //Pour le deplacement en MILIEU de partie
        else {  
        	String[] possible_moves = possibleMoves(player);
        	for(int i=0; i<possible_moves.length;i++) {
        		if (possible_moves[i].contains(move) ) {
        			return true;
        		}
        	}
        	return false;
        }
        return false;
	}

	public String[] possibleMoves(String player){
		//Les pions qu'on va regarder 
		String[] pions;
		if (player.contains("blanc")) {
			pions = white;
		}
		else {
			pions = black;
		}
		//On recupere les pions deplacables
		ArrayList<String> pions_deplacables = new ArrayList<>();
		//Si le lisere est egal a 0, i.e. si on est en debut de partie, alors le joueur peut deplacer n'importe quel pion
		if (last_lisere==0) {
			for (int i=0; i<6; i++) {
				//On met dans une liste la position des pions
				pions_deplacables.add(pions[i]);
			}
		}
		else {
			for (int i=0; i<6; i++) {
	            int pion_i = get_i_from_string(pions[i]);
	            int pion_j = get_j_from_string(pions[i]);
	        	//On verifie le respect du lisere
                if (liserePlateau[pion_i][pion_j]==last_lisere){
    				pions_deplacables.add(pions[i]);
                }
			}
		}
		//ArrayList des differents coups possibles qu'on va remplir par la suite
		LinkedList<String> possible_moves = new LinkedList<>();
		//Pour chaque pions deplacables, on regarde ses differentes cases atteignables
		for (String p : pions_deplacables) {
			//On met dans une ArrayList la position du pion ainsi que une direction "nul" qui represente la direction de la ou on vient dans l'exploration des cases (donc nul au depart)
			ArrayList<String> pion = new ArrayList<>();
			//On distingue les licornes aux paladins
			if (p.contains(pions[0])) {
				pion.add(p+"/l/nul");
			}
			else {
				pion.add(p+"/p/nul");
			}
			ArrayList<String> cases_atteignables = explore_adjacents_rec (pion, player, 0, liserePlateau[get_i_from_string(p)][get_j_from_string(p)]);
			//On recupere les coups possibles
			for (String c : cases_atteignables) {
				String cases = c.split("/")[0];
				String move = p+"-"+cases;
				//On ajoute pas les mouvements deja presents
				if (!possible_moves.contains(move)) {
					//On recupere les mouvements qui prennent la licorne ennemie pour les placer au debut du tableau
					if ( (cases.contains(white[0]))||(cases.contains(black[0])) ) {
						possible_moves.addFirst(move);
					}
					else {
						possible_moves.add(move);
					}
				}
			}
		}	
		//On convertit l'array en un tableau
		String[] possible_moves_tab = new String[possible_moves.size()];
		for (int i=0; i<possible_moves.size();i++) {
			possible_moves_tab[i]=possible_moves.get(i);
		}
		return possible_moves_tab;
	}
	
	public ArrayList<String> explore_adjacents_rec (ArrayList<String> cases, String player, int n, int lisere){
		//Si on a atteint le nombre de mouvements, on renvoie la liste des positions des cases atteignables
		if (n==lisere) {
			ArrayList<String> cases_atteignables = new ArrayList<>();
			for (String c: cases) {
				cases_atteignables.add(c.split("/")[0]);
			}
			//System.out.println("finish");
			return cases_atteignables;
		}
		else {
			//Sinon, on explore une case plus loin
			return explore_adjacents_rec( explore_adjacents(cases, player, n+1, lisere), player, n+1, lisere); 
		}
	}
	
	public ArrayList<String> explore_adjacents (ArrayList<String> cases, String player, int n, int lisere) {		
		
		//Tableau des differentes directions
		String[] directions = {"haut","bas","droite","gauche"};
		//ArrayList qui sera retourne
		ArrayList<String> res = new ArrayList<>();
		
		//On parcourt les cases a la frontiere
		for (String c : cases) {
		
			//On decompose chaque case de la liste en ses differentes composantes (position,type du pion,direction par laquelle il vient)
			String[] composantes = c.split("/");
			String pos = composantes[0];
			String p_type = composantes[1];
			String come_from = composantes[2];
			int start_i = get_i_from_string(pos);
			int start_j = get_j_from_string(pos);
			
			for (String d : directions) {
			
				//Pour chaque case, on va explorer les cases adjacentes sauf celle de laquelle on vient
				if (!d.contains(come_from) ) {
					if (d.contains("haut")) {
						//Si on ne sort pas du tableau
						if (start_i-1>=0) {
							//Si la case n'est pas occupee, alors on l'ajoute dans le resultat
							if (!is_occupied(start_i-1,start_j,p_type,player,n,lisere)){
								String indice = String.valueOf(start_i-1 +1);//+1 car c'est un indice
								String alpha = String.valueOf(alphabet[start_j]);
								String new_case = alpha+indice;
								res.add(new_case+"/"+p_type+"/bas");
							}	
						}
					}
					if (d.contains("bas")) {
						if (start_i+1<=5) {
							if (!is_occupied(start_i+1,start_j,p_type,player,n,lisere)){
								String indice = String.valueOf(start_i+1 +1);
								String alpha = String.valueOf(alphabet[start_j]);
								String new_case = alpha+indice;
								res.add(new_case+"/"+p_type+"/haut");
							}	
						}
					}				
					if (d.contains("droite")) {
						if (start_j+1<=5) {
							if (!is_occupied(start_i,start_j+1,p_type,player,n,lisere)){
								String indice = String.valueOf(start_i +1);
								String alpha = String.valueOf(alphabet[start_j+1]);
								String new_case = alpha+indice;
								res.add(new_case+"/"+p_type+"/gauche");
							}	
						}
					}
					if (d.contains("gauche")) {
						if (start_j-1>=0) {
							if (!is_occupied(start_i,start_j-1,p_type,player,n,lisere)){
								String indice = String.valueOf(start_i +1);
								String alpha = String.valueOf(alphabet[start_j-1]);
								String new_case = alpha+indice;
								res.add(new_case+"/"+p_type+"/droite");
							}	
						}
					}
				}
			}
		}
		return res;
	}
	
	
	//Fonction qui regarde si la case est occupee en fonction d'un type de pion et d'un joueur et du nombre de mouvements effectue
	public boolean is_occupied (int i, int j, String p_type, String player, int n, int lisere) {
		char[][] board = lists_to_board();
		if (player.contains("noir")) {
			if ( (board[i][j]=='-')) {
				return false;
			}
			//Si c'est un paladin, il peut prendre la licorne adverse
			if (p_type.contains("p")&&(board[i][j]=='B')&&(n==lisere)) {
				return false;
			}
		}
		else if (player.contains("blanc")) {
			if ( (board[i][j]=='-')) {
				return false;
			}
			if (p_type.contains("p")&&(board[i][j]=='N')&&(n==lisere)) {
				return false;
			}
		}
		return true;
	}
	

	public void play(String move, String player){
		//pour le placement en debut de partie
        if(move.length() > 5){ 
            String[] pions = move.split("/");
            
            if(player.contains("blanc") ){
                    white = pions;
            }
            else {
                    black = pions;
            }
        }
        //pour un deplacement normal
        else{
    		char[][] board = lists_to_board();
            String[] change = move.split("-");
            String start = change[0];
            String end = change[1];
            int end_i = get_i_from_string(end);
            int end_j = get_j_from_string(end);
            if(player.contains("blanc") ){
            	//On regarde si la licorne adverse est sur la case d'arrivee
            	if (board[end_i][end_j]=='N') {
            		//Dans ce cas, elle est morte
            		black[0]="ZZ";
            	}
                int pion = 0;
                while(!white[pion].contentEquals(start)){
                    pion++;
                }
                white[pion] = end;
            }
            else {
            	//On regarde si la licorne adverse est sur la case d'arrivee
            	if (board[end_i][end_j]=='B') {
            		//Dans ce cas, elle est morte
            		white[0]="ZZ";
            	}
                int pion = 0;
                while(!black[pion].contentEquals(start)){
                	pion++;               
                }
                black[pion] = end;
            }
            //On met a jour last_lisere
            last_lisere = liserePlateau[end_i][end_j];
        }
	}
	
	public char[][] lists_to_board(){
		//Initialisation
		char[][] board = new char[6][6];
		for(int i=0; i<6; i++){
			for(int j=0; j<6; j++){
				board[i][j]='-';
			}
		}
		//Positionnement des licornes
		board[get_i_from_string(this.white[0])][get_j_from_string(this.white[0])] = 'B';
		board[get_i_from_string(this.black[0])][get_j_from_string(this.black[0])] = 'N';
		
		//Affectation des pions blancs
		for(int i=1; i<6; i++){
			board[get_i_from_string(this.white[i])][get_j_from_string(this.white[i])] = 'b';
			board[get_i_from_string(this.black[i])][get_j_from_string(this.black[i])] = 'n';
		}
		return board;
	}
	
	
	public boolean gameOver(){
		//On regarde si l'une des deux positions des licornes est �gale � ZZ
		return ( (white[0].contains("ZZ"))||(black[0].contains("ZZ")) );
	}
	
	public static int get_j_from_string(String s){
		char j = s.charAt(0);
		for (int w=0; w<6;w++){
			if (alphabet[w]==j){
				return w;
			}
		}
		//si la lettre n'est pas entre A et F
		return -1;
	}
	
	public static int get_i_from_string(String s){
		char i = s.charAt(1);
		return (Character.getNumericValue(i)-1);
	}
	
	public void print_white () {
		for (int i=0; i<white.length; i++) {
			System.out.print(white[i]+";");
		}
		System.out.println("");
	}
	
	public void print_black () {
		for (int i=0; i<black.length; i++) {
			System.out.print(black[i]+";");
		}
		System.out.println("");
	}
	
	public void print_board() {
		if ( (black[0]!=null)&&(white[0]!=null)&&(!gameOver()) ){
			char[][] eb = lists_to_board();
			for(int i=0; i<6; i++) {
				for(int j=0; j<6; j++) {
					System.out.print(eb[i][j]+" ");
					/*System.out.print(board.liserePlateau[i][j]);
					System.out.print(" | ");*/
				}
				System.out.print("    ");
				for(int j=0; j<6; j++) {
					System.out.print(liserePlateau[i][j]);
					System.out.print("|");
				}
				System.out.println("");
			}
		}
	}
	
	
	
/**Fonctions personnelles necessaire au jeu	**/
	
	
	//Fonction qui servira pour enumerer les successeurs lors de la recherche de chemin
	public void simulate_play (String move, String[] w, String[] b, String player, int lastLisere) {
		//pour le placement en debut de partie
        if(move.length() > 5){ 
            String[] pions = move.split("/");
            
            if(player.contentEquals("blanc")){
                    w = pions;
            }
            else {
                    b = pions;
            }
        }
        //pour un deplacement normal
        else{
    		char[][] board = given_lists_to_board(w,b);
            String[] change = move.split("-");
            String start = change[0];
            String end = change[1];
            int end_i = get_i_from_string(end);
            int end_j = get_j_from_string(end);
            if(player.contentEquals("blanc")){
            	//On regarde si la licorne adverse est sur la case d'arrivee
            	if (board[end_i][end_j]=='N') {
            		//Dans ce cas, elle est morte
            		b[0]="ZZ";
            	}
                int pion = 0;
                while(!w[pion].equals(start)){
                    pion++;
                }
                w[pion] = end;
            }
            else {
            	//On regarde si la licorne adverse est sur la case d'arrivee
            	if (board[end_i][end_j]=='B') {
            		//Dans ce cas, elle est morte
            		w[0]="ZZ";
            	}
                int pion = 0;
                while(!b[pion].contentEquals(start)){
                	pion++;               
                }
                b[pion] = end;
            }
        }
	}
	
	//fonction qui renvoie un plateau 6*6 en fonction d'un etat de jeu passé en parametres
	public static char[][] given_lists_to_board(String[] w, String[] b){
		//Initialisation
		char[][] board = new char[6][6];
		for(int i=0; i<6; i++){
			for(int j=0; j<6; j++){
				board[i][j]='-';
			}
		}
		//Positionnement des licornes
		board[get_i_from_string(w[0])][get_j_from_string(w[0])] = 'B';
		board[get_i_from_string(b[0])][get_j_from_string(b[0])] = 'N';
		
		//Affectation des pions blancs
		for(int i=1; i<6; i++){
			board[get_i_from_string(w[i])][get_j_from_string(w[i])] = 'b';
			board[get_i_from_string(b[i])][get_j_from_string(b[i])] = 'n';
		}
		return board;
	}
	

	//Fonction qui calcule les coups possibles pour un pion donne, sachant qu'il est sur un lisere egal a lastlisere
	public String[] possibleMovesForOnePawn(String pawn, String player){
		String[] pions;
		if (player.contains("blanc")) {
			pions = this.white;
		}
		else {
			pions = this.black;
		}
		//ArrayList des differents coups possibles qu'on va remplir par la suite
		LinkedList<String> possible_moves = new LinkedList<>();
		//On met dans une ArrayList la position du pion ainsi que une direction "nul" qui represente la direction de la ou on vient dans l'exploration des cases (donc nul au depart)
		ArrayList<String> pion = new ArrayList<>();
		//On distingue les licornes aux paladins
		if (pawn.contains(pions[0])) {
			pion.add(pawn+"/l/nul");
		}
		else {
			pion.add(pawn+"/p/nul");
		}
		//ArrayList des differentes cases atteignables
		ArrayList<String> cases_atteignables = explore_adjacents_rec (pion, player, 0, liserePlateau[get_i_from_string(pawn)][get_j_from_string(pawn)]);
		//On recupere les coups possibles
		for (String c : cases_atteignables) {
			String cases = c.split("/")[0];
			String move = pawn+"-"+cases;
			//On ajoute pas les mouvements deja presents
			if (!possible_moves.contains(move)) {
				//On recupere les mouvements qui prennent la licorne ennemie pour les placer au debut du tableau
				if ( (cases.contains(white[0]))||(cases.contains(black[0])) ) {
					possible_moves.addFirst(move);
				}
				else {
					possible_moves.add(move);
				}
			}
		}
		//On convertit l'array en un tableau
		String[] possible_moves_tab = new String[possible_moves.size()];
		for (int i=0; i<possible_moves.size();i++) {
			possible_moves_tab[i]=possible_moves.get(i);
		}
		return possible_moves_tab;
	}


	
	
	/**	Tests 	**/	
	public static void main (String[] args){
		
		String s = "";
		String[] ss = s.split("/");
		
		System.err.println(ss.length );
		//Tests
	
		EscampeBoard eb = new EscampeBoard();
	    //Definition du chemin actuel
	    String projectDir = Paths.get(".").toAbsolutePath().normalize().toString();
	 
	    // Test setFromFile
	    eb.setFromFile("\\src\\data\\plateau1.txt");	    
	    
	    // Test saveToFile
	    eb.saveToFile("\\src\\data\\sauvegarde.txt");
	    
	    //Test to display the board
	    eb.print_board();
	    
	    // Test isValideMove
	    System.out.println("");
	    boolean verdict = eb.isValidMove("A1-A2", "blanc");
	    System.out.print("Is A1-A2 a valid move for white ? -> ");
	    System.out.println(verdict);
	    boolean verdict2 = eb.isValidMove("C2-A2", "blanc");
	    System.out.print("Is C2-A2 a valid move for white ? -> ");
	    System.out.println(verdict2);
	    System.out.println("");
	    
	    System.out.println("blanc plays B2-B3");
	    eb.play("B2-B3","blanc");
	    eb.print_board();
	    
	    System.out.println("");
	    boolean verdict3 = eb.isValidMove("B5-B2", "noir");
	    System.out.print("Is B5-B2 a valid move for black ? -> ");
	    System.out.println(verdict3);
	    System.out.println("");
        
	    // On cherche tous les moves
        String[] pm = eb.possibleMoves("noir");
        System.out.println("");
        System.out.println("Liste des coups possibles pour NOIR : ");
        for(String m : pm) {
            System.out.print(m+",");
        }
        
        // On test la fonction qui genere les mouvements possibles pour un seul pion (moins gourmande)
        String[] pmp = eb.possibleMovesForOnePawn("B5", "noir");
        System.out.println("");
        System.out.println("Liste des coups possibles pour B5 : ");
        for(String m : pmp) {
            System.out.print(m+",");
        }
        System.out.println("");
	}
}
