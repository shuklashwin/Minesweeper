package edu.nyu.tandon.minesweeper.constants;

public class Constants {

	public static final String IMAGES_PATH = "src/main/resources/minesweepertiles/";
	public static final String IMAGE_EXTENSION = ".png";
	public static final int ZERO = 0;
	public static final int ONE = 1;
	public static final int EIGHT = 8;
	public static final int MINE = 9;
	public static final int DEFAULT = 10;
	public static final int FLAG = 11;
	public static final int WRONG_FLAG = 12;

	public static final int NUMBER_OF_MINES = 40;
	public static final int GRID_COLUMNS = 16;
	public static final int GRID_ROWS = 16;

	public static final double SCALE_MULTIPLIER = 1.2;
	public static final int TILE_SIZE = (int) (15 * SCALE_MULTIPLIER);

	public static final int BOARD_WIDTH = GRID_COLUMNS * TILE_SIZE + 1;
	public static final int BOARD_HEIGHT = GRID_ROWS * TILE_SIZE + 1;

	public static final int GAME_IN_PROGRESS = 0;
	public static final int GAME_LOST = -1;
	public static final int GAME_WON = 1;
	public static final int NEW_GAME = 2;

	public static final int SERVER_PORT = 8001;
	public static final String HOST = "localhost";

	public static final String GET_SAVED_GAMES = "getSavedGames";
	public static final String LOAD_GAME = "loadGame";
	public static final String HALL_OF_FAME = "HALL_OF_FAME";
	public static final int HIGH_SCORES_LIMIT = 5;

	public static final String KILL_CLIENT = "AVADA_KEDAVRA";

	public static final String CONNECT_URL = "jdbc:sqlite:identifier.sqlite";

	public static class SavedGame {

		public static final String GAME_NAME = "GAME_NAME";
		public static final String GAME_STATE = "GAME_STATE";
	}

	public static class HallOfFame {
		public static final String SCORE = "score";
		public static final String PLAYER_NAME = "name";
		public static final String DATE = "date";
	}
}
