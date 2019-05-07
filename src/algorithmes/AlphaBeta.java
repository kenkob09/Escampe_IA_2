/**
aaaaa
 * 
 */

package algorithmes;

import java.util.ArrayList;
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

public class AlphaBeta {
	
    /** La profondeur de recherche par défaut
     */
    private final static int PROFMAXDEFAUT = 10;

   
    // -------------------------------------------
    // Attributs
    // -------------------------------------------
 
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
    public AlphaBeta(Heuristique h, String joueurMax, String joueurMin) {
        this(h,joueurMax,joueurMin,PROFMAXDEFAUT);
    }

    public AlphaBeta(Heuristique h, String joueurMax, String joueurMin, int profMaxi) {
        this.h = h;
        this.joueurMin = joueurMin;
        this.joueurMax = joueurMax;
        profMax = profMaxi;
//		System.out.println("Initialisation d'un MiniMax de profondeur " + profMax);
    }

   // -------------------------------------------
  // Méthodes de l'interface AlgoJeu
  // -------------------------------------------
    public String meilleurCoup(Probleme p) {
 	   
 	   nbnoeuds=0;
 	   
 	   nbfeuilles=0;
 	   
 	   //EscampeBoard eb = new EscampeBoard(p.getWhite().clone(), p.getBlack().clone(), Integer.valueOf(p.getLastLisere()));
 	   EtatEscampe ee = (EtatEscampe) p.getEtatInitial(); 
 	  
 	   // Get successeurs
 	   LinkedList<Etat> le = (LinkedList<Etat>) p.successeurs(ee);	
 	  
 	   // Initialisation
 	   float alpha = maxMinAlphaBeta(p, ee, this.h, this.profMax-1, -1000000, 1000000);
 	   String firstCoup = ((EtatEscampe) le.get(0)).getLastMove();
 	   System.err.println("firstMove: "+firstCoup);
 	   String mCoup = firstCoup;
 	   // Foreach successeurs
 	   for(int i = 1; i < le.size(); i++) {
 		   nbnoeuds++;
 		   String nextCoup = ((EtatEscampe) le.get(i)).getLastMove();
 		   
 		   float newAlpha = maxMinAlphaBeta(p, (EtatEscampe)le.get(i), this.h, profMax-1, alpha, 1000000);
		   
 		   if (newAlpha>alpha){
				
	   			mCoup = nextCoup;
			   
	   			alpha = newAlpha;
	   		}
 		   //System.out.println("mCoup: "+mCoup);
 	   }
	   System.out.println("Nombre de feuilles développés par la recherche : "+nbfeuilles);
 	   
 	   System.out.println("Nombre de noeuds développés par la recherche : "+nbnoeuds);
 	   
 	   return mCoup;
 }
    
private float minMaxAlphaBeta (Probleme p, EtatEscampe ee, Heuristique h, int profondeur, float alpha, float beta){
	
	EscampeBoard eb = new EscampeBoard(ee.getWhite().clone(), ee.getBlack().clone(), Integer.valueOf(ee.getLastLisere()));
   
    
	if ((profondeur <= 0) || (eb.gameOver())) {	// Si profondeur atteinte
		
		if (eb.gameOver()){
    			
			nbnoeuds--;
		}
		nbfeuilles++;
		
		return this.h.eval(ee);	
    	
	}
	else { // Profondeur > 0
    	LinkedList<Etat> le =  (LinkedList<Etat>) p.successeurs(ee);
		
    	for(int i = 1; i < le.size(); i++) {
   		   nbnoeuds++;
   		
   		   beta = Math.min(beta, maxMinAlphaBeta(p, ((EtatEscampe) le.get(i)),h, profondeur - 1, alpha, beta));
  		   
   		   if (alpha>=beta){
  			
   			   return alpha;  			   
   			
  	   		}
   	   }		
	}
	return beta;
}
    
private float maxMinAlphaBeta (Probleme p, EtatEscampe ee, Heuristique h,int profondeur, float alpha, float beta){
	
	EscampeBoard eb = new EscampeBoard(ee.getWhite().clone(), ee.getBlack().clone(), Integer.valueOf(ee.getLastLisere()));
	
	
	if ((profondeur <= 0) || (eb.gameOver())) {	// Si profondeur atteinte
		//System.err.println("Etat Final");
		if (eb.gameOver()){
			
			nbnoeuds--;
		}
		nbfeuilles++;
		
		return this.h.eval(ee);	
	}
	else { // Profondeur > 0
		LinkedList<Etat> le =  (LinkedList<Etat>) p.successeurs(ee);
		
    	for(int i = 1; i < le.size(); i++) {
   		   nbnoeuds++;
   		
   		   alpha = Math.max(alpha, minMaxAlphaBeta(p, ((EtatEscampe) le.get(i)),h, profondeur - 1, alpha, beta));
  
   		   if (alpha>=beta){
  			
   			   return beta;  			   
   			
  	   		}
   	   }		
	}
	return alpha;
}

  
   public String meilleurCoup(EtatEscampe p) {
	   
	   nbnoeuds=0;
	   
	   nbfeuilles=0;
	   
	   EscampeBoard eb = new EscampeBoard(p.getWhite().clone(), p.getBlack().clone(), Integer.valueOf(p.getLastLisere()));
	   
	   String[] coupsPossibles = eb.possibleMoves(joueurMax);
	   //Collection<Etat> etatSuccesseur = ProblemeEscampe.successeurs(p);
	   String firstCoup = coupsPossibles[0];
	   System.out.println("Fisrt Coup: "+firstCoup);
	   //System.err.println(joueurMax);
	   nbnoeuds++;
	   
		String[] w = p.getWhite().clone();
    	String[] b = p.getBlack().clone();
    	String pl = new String(joueurMax);
    	int lastL = Integer.valueOf(p.getLastLisere());
    	System.out.println("lastLisere: "+lastL);
    	eb.simulate_play(firstCoup, w, b, pl, lastL);
	   	EtatEscampe copyF = new EtatEscampe(w, b, pl, lastL);
	   	float alpha =  minMaxAlphaBeta(copyF, this.h, this.profMax-1, -1000000, 1000000);
	   
	   	String mCoup = firstCoup;
	   
	   	for (int i=1; i<coupsPossibles.length;i++){
		  
		   
	   		nbnoeuds++;
		   
	   		String nextCoup = coupsPossibles[i];
		   
			String[] white = p.getWhite().clone();
        	String[] black = p.getBlack().clone();
        	String player = new String(joueurMax);
        	int lastLisere = Integer.valueOf(p.getLastLisere());
        	
        	eb.simulate_play(nextCoup, white, black, player, lastLisere);
        	
			EtatEscampe pCopy = new EtatEscampe(white, black, player, lastLisere);
			
			float newAlpha = minMaxAlphaBeta(pCopy, this.h, profMax-1, alpha, 1000000);
		   
			if (newAlpha>alpha){
			   
				mCoup = nextCoup;
			   
				alpha = newAlpha;
			}
			//System.out.println("mCoup: "+mCoup);
	   }
	   System.out.println("Nombre de feuilles développés par la recherche : "+nbfeuilles);
	   
	   System.out.println("Nombre de noeuds développés par la recherche : "+nbnoeuds);
	   
	   return mCoup;
    }
     
   
  // -------------------------------------------
  // Méthodes publiques
  // -------------------------------------------
    public String toString() {
        return "MiniMax(ProfMax="+profMax+")";
    }



  // -------------------------------------------
  // Méthodes internes
  // -------------------------------------------

    
    private float minMaxAlphaBeta (EtatEscampe p, Heuristique h, int profondeur, float alpha, float beta){
    	
    	EscampeBoard eb = new EscampeBoard(p.getWhite().clone(), p.getBlack().clone(), new Integer(p.getLastLisere()));
    	
    	if ((profondeur <= 0) || (eb.gameOver())) {	// Si profondeur atteinte
    		
    		if (eb.gameOver()){
    			
    			nbnoeuds--;
    		}
    		
    		nbfeuilles++;
    		
    		return this.h.eval(p);	
    	
    	}
    	else { // Profondeur > 0
    		
    		for (String c : eb.possibleMoves(this.joueurMax)) { // Pour chaques coups possibles
    			
    			nbnoeuds++;
    			
    			String[] white = p.getWhite().clone();
            	String[] black = p.getBlack().clone();
            	String player = new String(joueurMax);
            	int lastLisere = Integer.valueOf(p.getLastLisere());
            	
            	eb.simulate_play(c, white, black, player, lastLisere);
            	
    			EtatEscampe pCopy = new EtatEscampe(white, black, player, lastLisere);
    			
    			beta = Math.min(beta, maxMinAlphaBeta(pCopy,h, profondeur - 1, alpha, beta));	
    	    			
    			if (alpha>=beta) {
    				
    				
    				return alpha;
    				
    			}
    			
			}
    		
    	}
    	
    	return beta;
    }
    
    private float maxMinAlphaBeta (EtatEscampe p, Heuristique h,int profondeur, float alpha, float beta){
    	
    	EscampeBoard eb = new EscampeBoard(p.getWhite().clone(), p.getBlack().clone(), new Integer(p.getLastLisere()));
    	
    	
    	if ((profondeur <= 0) || (eb.gameOver())) {	// Si profondeur atteinte
    		
    		if (eb.gameOver()){
    			
    			nbnoeuds--;
    		}
    		
    		nbfeuilles++;
    		
    		return this.h.eval(p);	
    	}
    	else { // Profondeur > 0
    		
    		for (String c : eb.possibleMoves(this.joueurMin)) { // Pour chaques coups possibles
    		  	
    			nbnoeuds++;
    		 	String[] white = p.getWhite().clone();
            	String[] black = p.getBlack().clone();
            	String player = new String(joueurMin);
            	int lastLisere = Integer.valueOf(p.getLastLisere());
            	
            	eb.simulate_play(c, white, black, player, lastLisere);
            	
    			//EtatEscampe pCopy = p.copy();
    			
    			//pCopy.joue(this.joueurMin, c);
    			
            	EtatEscampe pCopy = new EtatEscampe(white, black, player, lastLisere);
    			alpha = Math.max(alpha, minMaxAlphaBeta(pCopy,h, profondeur - 1,alpha,beta));
    			
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