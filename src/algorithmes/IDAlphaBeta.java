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

/** Classe Iterative deepening Alpha Beta
 * 	Note : Nous utilisons des états successeurs ce qui permet
 * 	d'éviter le besoin d'alterné joueurMin, joueurMax à chaque itération*/

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
        this(h,joueurMax,joueurMin,PROFMAXDEFAUT,1000);
    }

    
    public IDAlphaBeta(Heuristique h, String joueurMax, String joueurMin, int profMaxi) {
        this.h = h;
        this.joueurMin = joueurMin;
        this.joueurMax = joueurMax;
        profMax = profMaxi;

    }
    
    public IDAlphaBeta(Heuristique h, String joueurMax, String joueurMin, int profMaxi,long tempsMax) {
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
 	   
    	nbnoeuds=0;
 	   
    	nbfeuilles=0;
 	   
    	//System.err.println("IDAlphaBeta.meilleurCoup() joueur, h : "+this.joueurMax+" "+ this.h.toString());
 	   
	   //EscampeBoard eb = new EscampeBoard(p.getWhite().clone(), p.getBlack().clone(), Integer.valueOf(p.getLastLisere()));
	   
	   // Récupération de l'état initial
	   EtatEscampe ee = (EtatEscampe) p.getEtatInitial(); 
	  
	   // Get successeurs
	   LinkedList<Etat> le = (LinkedList<Etat>) p.successeurs(ee);	
	  
	   // Initialisation
	   // On récupère la première valeur de alpha afin de la comparer avec les autres
	   float alpha = maxMinAlphaBeta(p, ee, this.h, this.profMax, -1000000, 1000000);
	   System.err.println(alpha);
	   String firstCoup = ((EtatEscampe) le.get(0)).getLastMove();
	   //System.err.println("bon joueur: "+ee.getPlayer());
	   //System.err.println("firstMove: "+firstCoup);
	   String mCoup = firstCoup;
	   
	   long waitedTime = 0;
	   
	   // Foreach successeurs
 	   for(int i = 0; i < le.size(); i++) {
 		   //System.err.println("joueur inverse: "+((EtatEscampe) le.get(0)).getPlayer());
 		   nbnoeuds++;
 		   
 		   String nextCoup = ((EtatEscampe) le.get(i)).getLastMove();
 		   
 		   long waitedTime1 = new Date().getTime();
 		   
 		   float newAlpha = minMaxAlphaBeta(p, (EtatEscampe)le.get(i), this.h, profMax-1, alpha, 1000000);
 		   
 		   System.out.println("nA: "+newAlpha + "pour: "+ ((EtatEscampe) le.get(i)).getLastMove());
 		   
 		   long waitedTime2 = new Date().getTime();
		  
 		   // Si le nouveau coup à une meilleur heuristique
 		   
 		   if (newAlpha>alpha){
			
 			   // On remplace le coup
	   			mCoup = nextCoup;
			   
	   			alpha = newAlpha;
	   		
 		   }
 		   // Si on a le temps, on va plus profondément
 		   waitedTime += (waitedTime2 - waitedTime1) + 1;
 		  
 		   //System.out.println("Temps écoulé: "+waitedTime);
 		  
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
		//System.err.println("h: "+this.h.eval(ee));
		return this.h.eval(ee);	
    	
	}
	else { // Profondeur > 0
    	
		LinkedList<Etat> le =  (LinkedList<Etat>) p.successeurs(ee);
		
    	for(int i = 0; i < le.size(); i++) {
    		//System.err.println("bon joueur: "+((EtatEscampe) le.get(0)).getPlayer());
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
    		//System.err.println("h: "+this.h.eval(ee));
    		return this.h.eval(ee);	
    	}
    	else { // Profondeur > 0
    		
    		LinkedList<Etat> le =  (LinkedList<Etat>) p.successeurs(ee);
    		
        	for(int i = 0; i < le.size(); i++) {
       		   nbnoeuds++;
 
       		   alpha = Math.max(alpha, minMaxAlphaBeta(p, ((EtatEscampe) le.get(i)),h, profondeur - 1, alpha, beta));
       		  
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

