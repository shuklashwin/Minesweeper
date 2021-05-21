package edu.nyu.tandon.minesweeper.staging;

import edu.nyu.tandon.minesweeper.state.GameState;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

public class SavedGameInfo implements Serializable {

	private static final long serialVersionUID = 5230549922091722633L;

	private final String gameName;
	private final Calendar savedDate;
	private final GameState gameState;

	public SavedGameInfo(String gameName, GameState gameState) {
		this.gameName = gameName;
		this.gameState = gameState;
		this.savedDate = Calendar.getInstance();
	}

	public String getGameName() {
		return gameName;
	}

	public GameState getGameState() {
		return gameState;
	}

	@Override
	public String toString() {
		return gameName + " - " + savedDate.getDisplayName(Calendar.DATE, Calendar.SHORT, Locale.US);
	}

}

