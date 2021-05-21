package edu.nyu.tandon.minesweeper.dao;

import edu.nyu.tandon.minesweeper.constants.Constants;
import edu.nyu.tandon.minesweeper.staging.HighScoreInfo;
import edu.nyu.tandon.minesweeper.staging.SavedGameInfo;
import edu.nyu.tandon.minesweeper.state.GameState;
import edu.nyu.tandon.minesweeper.utils.ObjectUtils;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DaoImplementation implements DaoInterface {

	private Connection connection;
	private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");

	public DaoImplementation() {
		getConnection();
	}

	@Override
	public Connection getConnection() {
		if (this.connection == null) {
			try {
				this.connection = DriverManager.getConnection(Constants.CONNECT_URL);
				System.out.println("Connection established to DB with URL: " + Constants.CONNECT_URL);
			} catch (SQLException throwable) {
				System.err.println(throwable.getMessage());
			}
		}
		return this.connection;
	}

	@Override
	public int saveGame(SavedGameInfo savedGameInfo) {
		String statement = "INSERT INTO SAVED_GAMES (GAME_NAME, GAME_STATE, SAVED_DATE) VALUES (?, ?, ?)";
		System.out.println("Executing insert query: " + statement);
		try (PreparedStatement preparedStatement = this.getConnection().prepareStatement(statement)) {
			preparedStatement.setString(1, savedGameInfo.getGameName());
			preparedStatement.setObject(2, ObjectUtils.toByteStream(savedGameInfo.getGameState()));
			preparedStatement.setLong(3, System.currentTimeMillis());

			int numberOfRowsProcessed = preparedStatement.executeUpdate();
			System.out.println("Number of rows inserted: " + numberOfRowsProcessed);
			return numberOfRowsProcessed;
		} catch (SQLException throwable) {
			System.err.println("Exception occurred while saving game:" + throwable.getMessage());
		}
		return 0;
	}

	@Override
	public GameState loadGame(String name) {
		String query = "SELECT GAME_STATE FROM SAVED_GAMES WHERE GAME_NAME LIKE '%" + name + "%'";
		System.out.println("Executing fetch query: " + query);
		GameState savedGameState = null;
		try (Statement statement = this.getConnection().createStatement()) {
			ResultSet resultSet = statement.executeQuery(query);

			int count = 0;
			while (resultSet.next()) {
				byte[] bytes = (byte[]) resultSet.getObject(Constants.SavedGame.GAME_STATE);
				savedGameState = (GameState) ObjectUtils.toObject(bytes);
				count++;
			}
			System.out.println("Number of rows fetched: " + count);
		} catch (SQLException throwable) {
			System.err.println(throwable.getMessage());
		}

		return savedGameState;
	}

	@Override
	public List<String> getSavedGames() {
		String query = "SELECT GAME_NAME FROM SAVED_GAMES ORDER BY SAVED_DATE DESC";
		System.out.println("Executing fetch query: " + query);

		List<String> list = new ArrayList<>();
		try (Statement statement = this.getConnection().createStatement()) {
			ResultSet resultSet = statement.executeQuery(query);

			int count = 0;
			while (resultSet.next()) {
				String gameName = resultSet.getString(Constants.SavedGame.GAME_NAME);
				list.add(gameName);
				count++;
			}
			System.out.println("Number of rows fetched: " + count);
		} catch (SQLException throwable) {
			System.err.println(throwable.getMessage());
		}

		return list;
	}

	@Override
	public int saveScore(HighScoreInfo highScoreInfo) {
		String statement = "INSERT INTO HALL_OF_FAME (name, score, date) VALUES (?, ?, ?)";
		System.out.println("Executing insert query: " + statement);
		try (PreparedStatement preparedStatement = this.getConnection().prepareStatement(statement)) {
			preparedStatement.setString(1, highScoreInfo.getPlayerName());
			preparedStatement.setInt(2, highScoreInfo.getScore());
			preparedStatement.setLong(3, System.currentTimeMillis());

			int numberOfRowsProcessed = preparedStatement.executeUpdate();
			System.out.println("Number of rows inserted: " + numberOfRowsProcessed);
			return numberOfRowsProcessed;
		} catch (SQLException throwable) {
			System.err.println("Exception occurred while saving score:" + throwable.getMessage());
		}
		return 0;
	}

	@Override
	public List<HighScoreInfo> getHighScores(int limit) {
		String query = "SELECT name, score, date FROM HALL_OF_FAME ORDER BY score DESC LIMIT " + limit;
		System.out.println("Executing fetch query: " + query);

		List<HighScoreInfo> list = new ArrayList<>();
		try (Statement statement = this.getConnection().createStatement()) {
			ResultSet resultSet = statement.executeQuery(query);

			int count = 0;
			while (resultSet.next()) {
				String playerName = resultSet.getString(Constants.HallOfFame.PLAYER_NAME);
				int score = resultSet.getInt(Constants.HallOfFame.SCORE);
				long savedTimestamp = resultSet.getLong(Constants.HallOfFame.DATE);
				String date = dateFormat.format(new Date(savedTimestamp));
				list.add(new HighScoreInfo(playerName, score, date));
				count++;
			}
			System.out.println("Number of rows fetched: " + count);
		} catch (SQLException throwable) {
			System.err.println(throwable.getMessage());
		}

		return list;
	}
}
