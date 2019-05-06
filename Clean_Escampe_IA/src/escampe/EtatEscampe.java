package escampe;

import modeles.Etat;

public class EtatEscampe implements Etat{

	private String[] white;
	private String[] black;
	private String player;
	private int lastLisere;
	private String lastMove;
	
	public EtatEscampe(String[] white, String[] black, String player, int lastLisere, String lastMove) {
		super();
		this.white = white;
		this.black = black;
		this.player = player;
		this.lastLisere = lastLisere;
		this.lastMove = lastMove;
	}


	public EtatEscampe(String[] white, String[] black, String player, int lastLisere) {
		super();
		this.white = white;
		this.black = black;
		this.player = player;
		this.lastLisere = lastLisere;
	}


	public String[] getWhite() {
		return white;
	}



	public void setWhite(String[] white) {
		this.white = white;
	}



	public String[] getBlack() {
		return black;
	}



	public void setBlack(String[] black) {
		this.black = black;
	}



	public String getPlayer() {
		return player;
	}



	public void setPlayer(String player) {
		this.player = player;
	}


	public int getLastLisere() {
		return lastLisere;
	}


	public void setLastLisere(int lastLisere) {
		this.lastLisere = lastLisere;
	}


	public EtatEscampe(EtatEscampe ee) {
		this.white = ee.white;
		this.black = ee.black;
		this.player = ee.player;
		this.lastLisere = ee.lastLisere;
	}

	public String getLastMove() {
		return lastMove;
	}


	public float cout(EtatEscampe e2) {
		////////////////////////////////////////////////////////////////////////////////////////////////////Pas sur que ca coute 1
		return 1;
	}
	
}