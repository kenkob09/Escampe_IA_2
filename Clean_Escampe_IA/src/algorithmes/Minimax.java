/**
 * 
 */

package algorithmes;

import java.util.ArrayList;
import modeles.CoupJeu;
import modeles.Heuristique;
import modeles.PlateauJeu;
import modeles.Joueur;


public class Minimax implements AlgoRechercheArbre {

    /** La profondeur de recherche par défaut
     */
    private final static int PROFMAXDEFAUT = 5;

   
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
    private Joueur joueurMin;

    /** Le joueur Max
     * (celui dont l'algorithme de recherche adopte le point de vue) */
    private Joueur joueurMax;

    /**  Le nombre de noeuds développé par l'algorithme
     * (intéressant pour se faire une idée du nombre de noeuds développés) */
    private int nbnoeuds;

    /** Le nombre de feuilles évaluées par l'algorithme
     */
    private int nbfeuilles;


  // -------------------------------------------
  // Constructeurs
  // -------------------------------------------
    public Minimax(Heuristique h, Joueur joueurMax, Joueur joueurMin) {
        this(h,joueurMax,joueurMin,PROFMAXDEFAUT);
    }

    public Minimax(Heuristique h, Joueur joueurMax, Joueur joueurMin, int profMaxi) {
        this.h = h;
        this.joueurMin = joueurMin;
        this.joueurMax = joueurMax;
        profMax = profMaxi;
//		System.out.println("Initialisation d'un MiniMax de profondeur " + profMax);
    }

   // -------------------------------------------
  // Méthodes de l'interface AlgoJeu
  // -------------------------------------------
   public CoupJeu meilleurCoup(PlateauJeu p) {
	   
	   nbnoeuds=0;
	   
	   nbfeuilles=0;
	   	   
	   ArrayList<CoupJeu> coupsPossibles = p.coupsPossibles(joueurMax);
	   
	   CoupJeu firstCoup = coupsPossibles.get(0);
	   	   
	   nbnoeuds++;
	   
	   PlateauJeu firstP = p.copy();
	   	   
	   firstP.joue(joueurMax,firstCoup);
	   	   
	   int max =  minMax(firstP, this.h, this.profMax-1);
	   
	   CoupJeu mCoup = firstCoup;
	   	   
	   for (int i=1; i<coupsPossibles.size();i++){
		   
		   nbnoeuds++;
		   
		   CoupJeu nextCoup = coupsPossibles.get(i);
		   
		   PlateauJeu nextP = p.copy();
		   
		   nextP.joue(joueurMax,nextCoup);
		   	
		   int newVal = minMax(nextP, this.h, profMax-1);
		   		   
		   if (newVal>max){
			   
			   mCoup = nextCoup;
			   
			   max = newVal;
		   }
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

    private int minMax (PlateauJeu p, Heuristique h, int profondeur){
    	
    	
    	int min;
    	
    	if ((profondeur <= 0) || (p.finDePartie())) {	// Si profondeur atteinte
    		
    		if (p.finDePartie()){
    			
    			nbnoeuds--;
    		}
    		
    		nbfeuilles++;
    		
    		return this.h.eval(p, this.joueurMax);	
    	
    	}
    	else { // Profondeur > 0
    	
    		min = 100000;	// + Infini
    		
    		for (CoupJeu c : p.coupsPossibles(this.joueurMax)) { // Pour chaques coups possibles
    			
    			nbnoeuds++;
    			
    	    	PlateauJeu pCopy = p.copy();
    			
    			pCopy.joue(this.joueurMax, c);	// On joue le coup
    			
    			min = Math.min(min, maxMin(pCopy,h, profondeur - 1));
			}
    	}
    	
    	return min;
    	
    }
    
    private int maxMin(PlateauJeu p, Heuristique h,int profondeur){
    	
    	
    	int max;
    	
    	if ((profondeur <= 0) || (p.finDePartie())){	// Si profondeur atteinte
    		
    		if (p.finDePartie()){
    			
    			nbnoeuds--;
    		}
    		
    		nbfeuilles++;
    		
    		return this.h.eval(p, this.joueurMin);	
    		
    	}
    	else { // Profondeur > 0
    	
    		max = -1000000;	// - Infini
    		
    		for (CoupJeu c : p.coupsPossibles(this.joueurMin)) { // Pour chaques coups possibles
    		  	
    			nbnoeuds++;
    			
    			PlateauJeu pCopy = p.copy();
    			
    			pCopy.joue(this.joueurMin, c);
    			
    			max = Math.max(max, minMax(pCopy,h, profondeur - 1));
			}
    	}
    	
    	return max;
    	
    }
    
}