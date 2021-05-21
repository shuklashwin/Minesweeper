package edu.nyu.tandon.minesweeper.state;

import edu.nyu.tandon.minesweeper.constants.Constants;

import java.io.Serializable;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.stream.IntStream;

public class GameState implements Serializable {

	private static final long serialVersionUID = 5230549922091722630L;

	private int minesLeft;
	private final TileState[][] mineFieldState;
	private int timer;
	private int gameResult;
	private int numberTiles = 0;
	private int revealedTiles = 0;

	/**
	 * New game constructor.
	 */
	public GameState() {
		minesLeft = Constants.NUMBER_OF_MINES;
		mineFieldState = new TileState[Constants.GRID_ROWS][Constants.GRID_COLUMNS];
		timer = 1000;
		initializeField();
		initializeMines();
		assignNumbers();
		gameResult = Constants.NEW_GAME;
	}

	private void initializeField() {
		for (int row = 0; row < this.mineFieldState.length; row++) {
			for (int column = 0; column < this.mineFieldState[0].length; column++) {
				this.mineFieldState[row][column] = new TileState(Constants.ZERO);
			}
		}
	}

	private void initializeMines() {
		int target = Constants.NUMBER_OF_MINES;
		Random random = new Random();
		IntStream rowStream = random.ints(60, 0, Constants.GRID_ROWS);
		PrimitiveIterator.OfInt rowIterator = rowStream.iterator();
		IntStream columnStream = random.ints(60, 0, Constants.GRID_COLUMNS);
		PrimitiveIterator.OfInt columnIterator = columnStream.iterator();

		int count = 0;
		while (rowIterator.hasNext() && columnIterator.hasNext() && target > 0) {
			TileState tileState = getTileState(rowIterator.next(), columnIterator.next());
			if (!tileState.isMine()) {
				tileState.setTileType(Constants.MINE);
				target--;
				count++;
			}
		}
		System.out.println("Number of unique mines initialized: " + count);
	}

	private void assignNumbers() {
		for (int row = 0; row < this.mineFieldState.length; row++) {
			for (int column = 0; column < this.mineFieldState[0].length; column++) {
				TileState tileState = getTileState(row, column);
				if (!tileState.isMine()) {
					tileState.setTileType(countAdjacentMines(row, column));
				}
			}
		}
	}

	private int countAdjacentMines(int row, int column) {
		int adjacentMines = 0;
		for (int rowIndex = row - 1; rowIndex <= row + 1; rowIndex++) {
			for (int columnIndex = column - 1; columnIndex <= column + 1; columnIndex++) {
				if (isRowWithinBounds(rowIndex) && isColumnWithinBounds(columnIndex) && getTileState(rowIndex, columnIndex).isMine()) {
					adjacentMines++;
				}
			}
		}
		if (adjacentMines != 0) numberTiles++;
		return adjacentMines;
	}

	public static boolean isRowWithinBounds(int rowIndex) {
		return rowIndex >= 0 && rowIndex < Constants.GRID_ROWS;
	}

	public static boolean isColumnWithinBounds(int columnIndex) {
		return columnIndex >= 0 && columnIndex < Constants.GRID_COLUMNS;
	}

	public int getMinesLeft() {
		return minesLeft;
	}

	public void incrementMines() {
		this.minesLeft++;
	}

	public void decrementMines() {
		this.minesLeft--;
	}

	public int getTimer() {
		return timer;
	}

	public void decrementTimerByOneSecond() {
		if (this.timer > 0) {
			timer--;
		} else {
			timer = 0;
		}
	}

	public void incrementNumberOfRevealedTiles() {
		this.revealedTiles++;
//		System.out.println("Tiles revealed so far: " + getNumberOfRevealedTiles());
	}

	public int getNumberOfRevealedTiles() {
		return this.revealedTiles;
	}

	public int getGameResult() {
		return gameResult;
	}

	public boolean isGameInProgress() {
		return getGameResult() == Constants.GAME_IN_PROGRESS;
	}

	public void setGameResult(int gameResult) {
		this.gameResult = gameResult;
	}

	public TileState getTileState(int row, int column) {
		return this.mineFieldState[row][column];
	}

	public void revealAllMines() {
		for (int row = 0; row < Constants.GRID_ROWS; row++) {
			for (int column = 0; column < Constants.GRID_COLUMNS; column++) {
				TileState tileState = getTileState(row, column);
				if (tileState.isWronglyFlagged()) {
					tileState.setTileType(Constants.WRONG_FLAG);
					tileState.unsetFlag();
				}
				tileState.reveal();
			}
		}
	}

	public void revealEmptyTiles(int row, int column, TileState tileState) {

		if (tileState.isMine()) {
			return;
		}

		if (tileState.isNumber()) {
			tileState.reveal();
			this.incrementNumberOfRevealedTiles();
			return;
		}

		if (tileState.isEmpty()) {
			tileState.reveal();
			for (int rowIndex = row - 1; rowIndex <= row + 1; rowIndex++) {
				for (int columnIndex = column - 1; columnIndex <= column + 1; columnIndex++) {
					if (isRowWithinBounds(rowIndex) && isColumnWithinBounds(columnIndex)) {
						TileState currentTileState = getTileState(rowIndex, columnIndex);
						if (!currentTileState.isRevealed()) {
							revealEmptyTiles(rowIndex, columnIndex, currentTileState);
						}
					}
				}
			}
		}
	}

	public boolean areAllNumberTilesRevealed() {
		return this.numberTiles == this.getNumberOfRevealedTiles();
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int row = 0; row < Constants.GRID_ROWS; row++) {
			builder.append("|");
			for (int column = 0; column < Constants.GRID_COLUMNS; column++) {
				TileState tileState = getTileState(row, column);
				builder.append(tileState.getDisplayString()).append("|");
			}
			builder.append("\n");
		}
		builder.append("\n").append("Numbered tiles: ").append(this.numberTiles);
		return builder.toString();
	}

}
