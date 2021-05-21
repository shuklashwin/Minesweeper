package edu.nyu.tandon.minesweeper.gui;

import edu.nyu.tandon.minesweeper.constants.Constants;
import edu.nyu.tandon.minesweeper.state.GameState;
import edu.nyu.tandon.minesweeper.state.TileState;
import edu.nyu.tandon.minesweeper.utils.GuiUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author Ashwin Shukla (as13351@nyu.edu)
 */
public class GameBoard extends JPanel {

	private Tile[][] mineField;
	private GameState gameState;

	public GameBoard(GameState gameState) {
		this.gameState = gameState;
		initComponents();
	}

	private GameState getGameState() {
		return gameState;
	}

	private void initComponents() {
		GridLayout gridLayout = new GridLayout(Constants.GRID_ROWS, Constants.GRID_COLUMNS);
		gridLayout.setHgap(0);
		gridLayout.setVgap(0);
		setLayout(gridLayout);

		setPreferredSize(new Dimension(Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT));

		mineField = new Tile[Constants.GRID_ROWS][Constants.GRID_COLUMNS];
		for (int row = 0; row < Constants.GRID_ROWS; row++) {
			for (int column = 0; column < Constants.GRID_COLUMNS; column++) {
				Tile tile = new Tile(Constants.DEFAULT);
				mineField[row][column] = tile;
				add(tile);
			}
		}

	}

	public Tile getMineFieldTile(int row, int column) {
		return mineField[row][column];
	}

	@Override
	protected void paintComponent(Graphics g) {

		for (int row = 0; row < Constants.GRID_ROWS; row++) {
			for (int column = 0; column < Constants.GRID_COLUMNS; column++) {
				TileState tileState = this.getGameState().getTileState(row, column);

				Tile tile = this.getMineFieldTile(row, column);
				tile.setIcon(GuiUtils.getImageIcon(tileState.getTileDisplayValue()));
			}
		}
	}

}