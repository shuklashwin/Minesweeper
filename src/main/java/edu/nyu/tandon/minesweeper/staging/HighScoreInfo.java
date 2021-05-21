package edu.nyu.tandon.minesweeper.staging;

import java.io.Serializable;

public class HighScoreInfo implements Serializable {

	private static final long serialVersionUID = 5230549922091722632L;

	private final String playerName;
	private final int score;
	private String date;

	public HighScoreInfo(String playerName, int score) {
		this.playerName = playerName;
		this.score = score;
	}

	public HighScoreInfo(String playerName, int score, String date) {
		this.playerName = playerName;
		this.score = score;
		this.date = date;
	}

	public String getPlayerName() {
		return playerName;
	}

	public int getScore() {
		return score;
	}

	@Override
	public String toString() {
		return "HighScoreInfo{" +
				"playerName='" + playerName + '\'' +
				", score=" + score +
				'}';
	}

	public String[] getAsArray() {
		return new String[]{playerName, String.valueOf(score), date};
	}
}
