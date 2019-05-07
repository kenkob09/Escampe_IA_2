package algorithmes;
 
import java.util.Date;
import java.util.LinkedList;
import escampe.EscampeBoard;
import escampe.EtatEscampe;
import modeles.Etat;
import modeles.Heuristique;
import modeles.Probleme;
 
/** Classe Iterative deepening Alpha Beta*/

public class IDAlphaBeta{
	
    
 
 
    private final static int PROFMAXDEFAUT = 1;
     
    private int profMax = PROFMAXDEFAUT;
    private Heuristique h;
    private int nbnoeuds;
    private int nbfeuilles;
    
    // La duree autorisee pour le deroulement de l'algorithme
    private long tempsMax;
    private long waitedTime;
    private long actualTime;
    
    private String joueurMin;
    private String joueurMax;
    
    /** Constructeurs, définition du temps par défault à 1000ms*/
    public IDAlphaBeta(Heuristique h, String joueurMax, String joueurMin) {
        this(h,joueurMax,joueurMin,PROFMAXDEFAUT, 6000);
    }

    public IDAlphaBeta(Heuristique h, String joueurMax, String joueurMin, int profMaxi) {
        this.h = h;
        this.joueurMin = joueurMin;
        this.joueurMax = joueurMax;
        this.profMax = profMaxi;
    }

    public IDAlphaBeta(Heuristique h, String joueurMax, String joueurMin, int profMaxi,long tempsMax) {
        this.h = h;
        this.joueurMin = joueurMin;
        this.joueurMax = joueurMax;
        this.profMax = profMaxi;
        this.tempsMax = tempsMax;
 
    }

    /** Recherche du meilleur coup pour un problème
     * 
     * @param Probleme p, le problème à résoudre
     * */
    
    public String meilleurCoup(Probleme p) {
    	
    	//Initialisation de alpha et beta
    	float alpha = Integer.MIN_VALUE;
    	float beta = Integer.MAX_VALUE;
    	
    	//On initialise le nombre de noeuds et de feuilles developpes par la recherche
    	nbnoeuds=0;
    	nbfeuilles=0;
    	waitedTime = 0;
    	actualTime = new Date().getTime();
    	
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
    		
    		//long waitedTime1 = new Date().getTime();
    		
    		nbnoeuds++;
    		
    		String nextCoup = ((EtatEscampe) le.get(i)).getLastMove();
    		float newAlpha = minMaxIDAlphaBeta2(p, (EtatEscampe)le.get(i), this.h, profMax-1, alpha, 1000000);
    		
    		long actualTime2 = new Date().getTime();
    		
    		if (newAlpha>alpha){
    			mCoup = nextCoup;
	   			alpha = newAlpha;
	   		}
    		
    		// Estimation du temps 
            long waitedTime1 = (actualTime2 - actualTime) + 1;
    		
            //System.out.println("Temps écoulé: "+waitedTime);
            
            // Si on a le temps, on va plus profondement
	        if(waitedTime1 >= tempsMax) {
               System.err.println("Time over, waitedTime: "+waitedTime1+ " ms.");
               profMax = 0;
               return mCoup;
	        }
           
	        else { // dans les temps
               profMax++;
               //System.out.println("Itérative avec prof: "+profMax);
            }       
    	}
    	System.out.println("Nombre de feuilles développés par la recherche : "+nbfeuilles);
    	System.out.println("Nombre de noeuds développés par la recherche : "+nbnoeuds);
    	
    	profMax = 0;
    	
    	System.out.print("Temps de la recherche: "+waitedTime+" ms.\n");
    	
    	return mCoup;
    }
	    
    //Evaluation pour ennemi
	private float minMaxIDAlphaBeta2 (Probleme p, EtatEscampe ee, Heuristique h, int profondeur, float alpha, float beta){
		
		EscampeBoard eb = new EscampeBoard(ee.getWhite().clone(), ee.getBlack().clone(), Integer.valueOf(ee.getLastLisere()));
	    
		long actualTime2 = new Date().getTime();
		long waitedTime1 = (actualTime2 - actualTime) + 1;
		
		
		// Si profondeur atteinte ou que l'etat est terminal
		if ((profondeur <= 0) || (eb.gameOver()) || waitedTime1 > this.tempsMax ) {	
			if (eb.gameOver()){
				//l'etat est donc une feuille et non un noeud
				nbnoeuds--;
			}
			if(this.waitedTime > this.tempsMax) {
				System.err.println("Time Over");
			}
			nbfeuilles++;
			return this.h.eval(ee);	
	    	
		}
		
		// Sinon, on regarde les etats successeurs
		else { 
	    	LinkedList<Etat> le =  (LinkedList<Etat>) p.successeurs(ee);
	    	for(int i = 1; i < le.size(); i++) {
	    		
	    		//long waitedTime1 = new Date().getTime();
	    		
	   		   	nbnoeuds++;
	   		   	//Evaluation la moins favorable
	   		   	beta = Math.min(beta, maxMinIDAlphaBeta2(p, ((EtatEscampe) le.get(i)),h, profondeur - 1, alpha, beta));
	   		   	
	   		   	//long waitedTime2 = new Date().getTime();
	   		   	
	   		   	//Coupe alpha
	   		   	if (alpha>=beta){
	   		   		return alpha; 
	   		   	}
	   		   	
	   		   	//waitedTime += (waitedTime2 - waitedTime1) + 1;
	   		   	
   	   		}		
		}
		return beta;
	}
	    
	//Evaluation pour ami
	private float maxMinIDAlphaBeta2 (Probleme p, EtatEscampe ee, Heuristique h,int profondeur, float alpha, float beta){
		
		EscampeBoard eb = new EscampeBoard(ee.getWhite().clone(), ee.getBlack().clone(), Integer.valueOf(ee.getLastLisere()));
		
		long actualTime2 = new Date().getTime();
		long waitedTime1 = (actualTime2 - actualTime) + 1;
		
		// Si profondeur atteinte ou que l'etat est terminal
		if ((profondeur <= 0) || (eb.gameOver()) || waitedTime1 > this.tempsMax) {	
			if (eb.gameOver()){
				nbnoeuds--;
			}
			if(this.waitedTime > this.tempsMax) {
				System.err.println("Time Over");
			}
			nbfeuilles++;
			return this.h.eval(ee);	
		}
		
		else { 
			LinkedList<Etat> le =  (LinkedList<Etat>) p.successeurs(ee);
	    	for(int i = 1; i < le.size(); i++) {
	    		
	    		
	    		//long waitedTime1 = new Date().getTime();
	    		
	    		nbnoeuds++;
	    		alpha = Math.max(alpha, minMaxIDAlphaBeta2(p, ((EtatEscampe) le.get(i)),h, profondeur - 1, alpha, beta));
	   		   
	    		//long waitedTime2 = new Date().getTime();
	   		   
	    		if (alpha>=beta){
	    			return beta;  			   
	    		}
	    		//waitedTime += (waitedTime2 - waitedTime1) + 1;
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