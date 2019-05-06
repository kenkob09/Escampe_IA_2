package algorithmes;

import java.util.LinkedList;

import escampe.EscampeBoard;
import escampe.EtatEscampe;
import modeles.Etat;
import modeles.Heuristique;
import modeles.Probleme;

public class NegAlphaBeta {
	
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
    
    /**  Le nombre de noeuds développé par l'algorithme
     * (intéressant pour se faire une idée du nombre de noeuds développés) */
    private int nbnoeuds;

    /** Le nombre de feuilles évaluées par l'algorithme
     */
    private int nbfeuilles;

    
	public NegAlphaBeta(int profMax, Heuristique h) {
		super();
		this.profMax = profMax;
		this.h = h;
	}

	public NegAlphaBeta(Heuristique h) {
		this(PROFMAXDEFAUT,h);
	}

	/**
	 * Calcul le meilleur coup du problème grace à négAlphaBeta
	 * prend en compte le niveau de l'état afin de ne pas calculer 
	 * l'heuristique des états ennemies
	 * 
	 * @param p Le problème
	 * */
	
	public String meilleurCoup(Probleme p) {
		  nbnoeuds=0;
		  nbfeuilles=0;
		  
		  //EscampeBoard eb = new EscampeBoard(p.getWhite().clone(), p.getBlack().clone(), Integer.valueOf(p.getLastLisere()));
		  EtatEscampe ee = (EtatEscampe) p.getEtatInitial(); 
	  
		  // Get successeurs
		  LinkedList<Etat> le = (LinkedList<Etat>) p.successeurs(ee);	
		  
		  // Initialisation
		  float alpha = negAlphaBeta(p, ee, this.h, -1000000, 1000000, 1,profMax-1);
		  String firstCoup = ((EtatEscampe) le.get(0)).getLastMove();
		  //System.err.println("firstMove: "+firstCoup);
		  String mCoup = firstCoup;
		  // Foreach successeurs
		  for(int i = 1; i < le.size(); i++) {
			  nbnoeuds++;
			  String nextCoup = ((EtatEscampe) le.get(i)).getLastMove();
			   
			  float newAlpha = negAlphaBeta(p, (EtatEscampe) le.get(i), this.h, alpha, 1000000, -1,profMax-1);
	   
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
	
	public float negAlphaBeta(Probleme p, EtatEscampe ee, Heuristique h, float alpha, float beta, int rangNoeud, int prof) {
		
		EscampeBoard eb = new EscampeBoard(ee.getWhite().clone(), ee.getBlack().clone(), Integer.valueOf(ee.getLastLisere()));
		
		if(eb.gameOver()|| prof <= 0) {
			nbfeuilles++;
			nbnoeuds--;
			return (rangNoeud * this.h.eval(ee));
		}
		else { // Profondeur > 0
	    	LinkedList<Etat> le =  (LinkedList<Etat>) p.successeurs(ee);
			
	    	for(int i = 1; i < le.size(); i++) {
	   		   nbnoeuds++;
	   		
	   		   alpha = Math.max(alpha, -negAlphaBeta(p, (EtatEscampe) le.get(i), this.h, -beta, -alpha, -rangNoeud, prof-1));
	  		   
	   		   if (alpha>=beta){
	  			
	   			   return beta;  			   
	   			
	  	   		}
	   	   }		
		}
		return alpha;
	}

}
