package edu.nyu.tandon.minesweeper.dao;

import edu.nyu.tandon.minesweeper.staging.HighScoreInfo;
import edu.nyu.tandon.minesweeper.staging.SavedGameInfo;
import edu.nyu.tandon.minesweeper.state.GameState;

import java.sql.Connection;
import java.util.List;

public interface DaoInterface {

	Connection getConnection();

	int saveGame(SavedGameInfo savedGameInfo);

	GameState loadGame(String name);

	List<String> getSavedGames();

	int saveScore(HighScoreInfo highScoreInfo);

	List<HighScoreInfo> getHighScores(int limit);
}
