package algorithmes;
 
import java.util.ArrayList;
 
import java.util.Date;
import java.util.LinkedList;
import escampe.EscampeBoard;
import escampe.EtatEscampe;
import escampe.IJoueur;
import escampe.ProblemeEscampe;
import modeles.Etat;
import modeles.Heuristique;
import modeles.Etat;
import modeles.Heuristique;
import modeles.Probleme;
 
/** Classe Iterative deepening Alpha Beta*/

public class IDAlphaBeta2{
	
    
    // -------------------------------------------
    // Attributs
    // -------------------------------------------
	
    /** La profondeur de recherche par défaut
     */
    private final static int PROFMAXDEFAUT = 1;
 
    /**  La profondeur de recherche utilisée pour l'algorithme
     */
    private int profMax = PROFMAXDEFAUT;

     /**  L'heuristique utilisée par l'algorithme
      */
    private Heuristique h;

    /** Le joueur Min
     *  (l'adversaire) */
    private String joueurMin;

    /** Le joueur Max
     * (celui dont l'algorithme de recherche adopte le point de vue) */
    private String joueurMax;

    /**  Le nombre de noeuds développé par l'algorithme
     * (intéressant pour se faire une idée du nombre de noeuds développés) */
    private int nbnoeuds;

    /** Le nombre de feuilles évaluées par l'algorithme
     */
    private int nbfeuilles;


  // -------------------------------------------
  // Constructeurs
  // -------------------------------------------
    
    public IDAlphaBeta2(Heuristique h, String joueurMax, String joueurMin) {
        this(h,joueurMax,joueurMin,PROFMAXDEFAUT);
    }

    public IDAlphaBeta2(Heuristique h, String joueurMax, String joueurMin, int profMaxi) {
        this.h = h;
        this.joueurMin = joueurMin;
        this.joueurMax = joueurMax;
        profMax = profMaxi;
    }

    public IDAlphaBeta2(Heuristique h, String joueurMax, String joueurMin, int profMaxi,long tempsMax) {
        this.h = h;
        this.joueurMin = joueurMin;
        this.joueurMax = joueurMax;
        this.profMax = profMaxi;
        this.tempsMax = tempsMax;
 
    }
    
  // -------------------------------------------
  // Méthodes de l'interface AlgoJeu
  // -------------------------------------------
    
    
    public String meilleurCoup(Probleme p) {
    	
    	//Initialisation de alpha et beta
    	float alpha = Integer.MIN_VALUE;
    	float beta = Integer.MAX_VALUE;
    	
    	//On initialise le nombre de noeuds et de feuilles developpes par la recherche
    	nbnoeuds=0;
    	nbfeuilles=0;
 	   
    	// On recupere l'etat initial
    	EtatEscampe ee = (EtatEscampe) p.getEtatInitial(); 
 	  
    	// On recupere les successeurs successeurs
    	LinkedList<Etat> le = (LinkedList<Etat>) p.successeurs(ee);	
 	  
    	// Initialisation
    	EtatEscampe firstEtatSucc = (EtatEscampe) le.get(0);
    	String firstCoupSucc = firstEtatSucc.getLastMove();
    	String mCoup = firstCoupSucc;
    	
    	// On calcule le alpha du premier successeur
    	alpha = minMaxIDAlphaBeta2(p, firstEtatSucc, this.h, this.profMax-1, alpha, beta);
 	  
    	// On recupere le meilleur coup. On commence l'indexation a 1 car on a deja explore le premier coup
    	for(int i = 1; i < le.size(); i++) {
    		nbnoeuds++;
    		String nextCoup = ((EtatEscampe) le.get(i)).getLastMove();
    		float newAlpha = minMaxIDAlphaBeta2(p, (EtatEscampe)le.get(i), this.h, profMax-1, alpha, 1000000);
    		if (newAlpha>alpha){
    			mCoup = nextCoup;
	   			alpha = newAlpha;
	   		}
    	}
    	System.out.println("Nombre de feuilles développés par la recherche : "+nbfeuilles);
    	System.out.println("Nombre de noeuds développés par la recherche : "+nbnoeuds);
    	return mCoup;
    }
	    
    //Evaluation pour ennemi
	private float minMaxIDAlphaBeta2 (Probleme p, EtatEscampe ee, Heuristique h, int profondeur, float alpha, float beta){
		
		EscampeBoard eb = new EscampeBoard(ee.getWhite().clone(), ee.getBlack().clone(), Integer.valueOf(ee.getLastLisere()));
	    
		// Si profondeur atteinte ou que l'etat est terminal
		if ((profondeur <= 0) || (eb.gameOver())) {	
			if (eb.gameOver()){
				//l'etat est donc une feuille et non un noeud
				nbnoeuds--;
			}
			nbfeuilles++;
			return this.h.eval(ee);	
	    	
		}
		
		// Sinon, on regarde les etats successeurs
		else { 
	    	LinkedList<Etat> le =  (LinkedList<Etat>) p.successeurs(ee);
	    	for(int i = 1; i < le.size(); i++) {
	   		   nbnoeuds++;
	   		   //Evaluation la moins favorable
	   		   beta = Math.min(beta, maxMinIDAlphaBeta2(p, ((EtatEscampe) le.get(i)),h, profondeur - 1, alpha, beta));
	  		   //Coupe alpha
	   		   if (alpha>=beta){
	   			   return alpha; 
	   		   }
	   	   	}		
		}
		return beta;
	}
	    
	//Evaluation pour ami
	private float maxMinIDAlphaBeta2 (Probleme p, EtatEscampe ee, Heuristique h,int profondeur, float alpha, float beta){
		
		EscampeBoard eb = new EscampeBoard(ee.getWhite().clone(), ee.getBlack().clone(), Integer.valueOf(ee.getLastLisere()));
		
		// Si profondeur atteinte ou que l'etat est terminal
		if ((profondeur <= 0) || (eb.gameOver())) {	
			if (eb.gameOver()){
				nbnoeuds--;
			}
			nbfeuilles++;
			return this.h.eval(ee);	
		}
		
		else { 
			LinkedList<Etat> le =  (LinkedList<Etat>) p.successeurs(ee);
	    	for(int i = 1; i < le.size(); i++) {
	   		   nbnoeuds++;
	   		   alpha = Math.max(alpha, minMaxIDAlphaBeta2(p, ((EtatEscampe) le.get(i)),h, profondeur - 1, alpha, beta));
	   		   if (alpha>=beta){
	   			   return beta;  			   
	   		   }
	    	}		
		}
		return alpha;
	}

	public String getJoueurMin() {
		return joueurMin;
	}

	public String getJoueurMax() {
		return joueurMax;
	}
}