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
public class IDAlphaBeta {

	

    /** La profondeur de recherche par défaut
     */
    private final static int PROFMAXDEFAUT = 1;

   
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
    
    private long tempsMax;


  // -------------------------------------------
  // Constructeurs
  // -------------------------------------------
    public IDAlphaBeta(Heuristique h, String joueurMax, String joueurMin) {
        this(h,joueurMax,joueurMin,PROFMAXDEFAUT,100);
    }

    
    public IDAlphaBeta(Heuristique h, String joueurMax, String joueurMin, int profMaxi) {
        this.h = h;
        this.joueurMin = joueurMin;
        this.joueurMax = joueurMax;
        profMax = profMaxi;
//			System.out.println("Initialisation d'un MiniMax de profondeur " + profMax);
    }
    
    public IDAlphaBeta(Heuristique h, String joueurMax, String joueurMin, int profMaxi,long tempsMax) {
        this.h = h;
        this.joueurMin = joueurMin;
        this.joueurMax = joueurMax;
        this.profMax = profMaxi;
        this.tempsMax = tempsMax;
//			System.out.println("Initialisation d'un MiniMax de profondeur " + profMax);
    }

   // -------------------------------------------
  // Méthodes de l'interface AlgoJeu
  // -------------------------------------------
    public String meilleurCoup(Probleme p) {
 	   
 	   nbnoeuds=0;
 	   
 	   nbfeuilles=0;
 	   
 	   System.err.println("IDAlphaBeta.meilleurCoup() joueur, h : "+this.joueurMax+" "+ this.h.toString());
 	   
 	   //EscampeBoard eb = new EscampeBoard(p.getWhite().clone(), p.getBlack().clone(), Integer.valueOf(p.getLastLisere()));
 	   EtatEscampe ee = (EtatEscampe) p.getEtatInitial(); 
 	  
 	   // Get successeurs
 	   LinkedList<Etat> le = (LinkedList<Etat>) p.successeurs(ee);	
 	  
 	   // Initialisation
 	   
 	   float alpha = maxMinAlphaBeta(p, ee, this.h, this.profMax, -1000000, 1000000);
 	   
 	   String firstCoup = ((EtatEscampe) le.get(0)).getLastMove();
 	   //System.err.println("firstMove: "+firstCoup);
 	   String mCoup = firstCoup;
 	   // Foreach successeurs
 	   
 	   long waitedTime = 0;
 	   
 	   for(int i = 0; i < le.size(); i++) {
 		
 		   nbnoeuds++;
 		   String nextCoup = ((EtatEscampe) le.get(i)).getLastMove();
 		   
 		   long waitedTime1 = new Date().getTime();
 		   
 		   float newAlpha = Math.max(alpha,minMaxAlphaBeta(p, (EtatEscampe)le.get(i), this.h, profMax-1, alpha, 1000000));
 		   
 		   long waitedTime2 = new Date().getTime();
		   
 		   if (newAlpha>alpha){
				
	   			mCoup = nextCoup;
			   
	   			alpha = newAlpha;
	   		}
 		   //System.out.println("mCoup: "+mCoup);
// 		  System.out.println("Temps début: "+waitedTime1);
// 		  System.out.println("Temps fin: "+waitedTime2);
 		  waitedTime += (waitedTime2 - waitedTime1) + 1;
// 		   System.out.println("Temps écoulé: "+waitedTime);
 		   if(waitedTime >= tempsMax) {
 			   System.err.println("Time over");
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
       		   
       		   alpha = Math.max(beta, minMaxAlphaBeta(p, ((EtatEscampe) le.get(i)),h, profondeur - 1, alpha, beta));
      
       		   if (alpha>=beta){
      			
       			   return beta;  			   
       			
      	   		}
       	   }		
    	}
    	return alpha;
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

	public String getJoueurMin() {
		return joueurMin;
	}

	public String getJoueurMax() {
		return joueurMax;
	}
}

