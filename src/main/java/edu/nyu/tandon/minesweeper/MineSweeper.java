package edu.nyu.tandon.minesweeper;

import edu.nyu.tandon.minesweeper.constants.Constants;
import edu.nyu.tandon.minesweeper.dao.DaoImplementation;
import edu.nyu.tandon.minesweeper.gui.GameBoard;
import edu.nyu.tandon.minesweeper.gui.HighScoresView;
import edu.nyu.tandon.minesweeper.staging.HighScoreInfo;
import edu.nyu.tandon.minesweeper.staging.SavedGameInfo;
import edu.nyu.tandon.minesweeper.state.GameState;
import edu.nyu.tandon.minesweeper.state.TileState;
import edu.nyu.tandon.minesweeper.utils.GuiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MineSweeper extends JFrame {

	private JLabel timerBar;
	private Timer timer;
	private boolean timerStarted = false;
	private GameBoard gameBoard;
	private JLabel statusBar;
	private final GameState gameState;

	private Socket socket;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;

	private DaoImplementation dao;

	public GameState getGameState() {
		return gameState;
	}

	public JLabel getTimerBar() {
		return timerBar;
	}

	public void startTimer() {
		if (timerStarted) {
			this.timer.start();
		}
	}

	public void stopTimer() {
		if (timer.isRunning()) {
			this.timer.stop();
		}
	}

	public GameBoard getGameBoard() {
		return gameBoard;
	}

	public JLabel getStatusBar() {
		return statusBar;
	}

	private Socket getSocket() {
		return socket;
	}

	private ObjectOutputStream getObjectOutputStream() throws IOException {
		if (objectOutputStream == null) {
			objectOutputStream = new ObjectOutputStream(getSocket().getOutputStream());
		}
		return objectOutputStream;
	}

	private ObjectInputStream getObjectInputStream() throws IOException {
		if (objectInputStream == null) {
			objectInputStream = new ObjectInputStream(getSocket().getInputStream());
		}
		return objectInputStream;
	}

	private DaoImplementation getDao() {
		if (dao == null) {
			dao = new DaoImplementation();
		}
		return dao;
	}

	public MineSweeper(GameState gameState) {
		this.gameState = gameState;
		System.out.println(this.gameState);
		initComponents();
		try {
			socket = new Socket(Constants.HOST, Constants.SERVER_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initComponents() {

		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menu.setDisplayedMnemonicIndex(0);
		menu.addActionListener(e -> stopTimer());

		JMenuItem newGame = new JMenuItem("New");
		newGame.setMnemonic(KeyEvent.VK_N);
		newGame.setDisplayedMnemonicIndex(0);
		newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.META_DOWN_MASK, true));
		newGame.addActionListener(new NewGameListener());

		JMenuItem saveGame = new JMenuItem("Save");
		saveGame.setMnemonic(KeyEvent.VK_S);
		saveGame.setDisplayedMnemonicIndex(0);
		saveGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.META_DOWN_MASK, true));
		saveGame.addActionListener(new SaveMenuListener());

		JMenuItem openGame = new JMenuItem("Open");
		openGame.setMnemonic(KeyEvent.VK_O);
		openGame.setDisplayedMnemonicIndex(0);
		openGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.META_DOWN_MASK, true));
		openGame.addActionListener(new OpenGameListener());

		JMenuItem highScores = new JMenuItem("Hall of Fame");
		highScores.setMnemonic(KeyEvent.VK_H);
		highScores.setDisplayedMnemonicIndex(0);
		highScores.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.META_DOWN_MASK, true));
		highScores.addActionListener(new HighScoreMenuListener());

		JMenuItem exitGame = new JMenuItem("Exit");
		exitGame.setMnemonic(KeyEvent.VK_X);
		exitGame.setDisplayedMnemonicIndex(1);
		exitGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.META_DOWN_MASK, true));
		exitGame.addActionListener(new ExitMenuListener());

		menu.add(newGame);
		menu.add(saveGame);
		menu.add(openGame);
		menu.add(highScores);
		menu.add(exitGame);

		menuBar.add(menu);

		this.setJMenuBar(menuBar);

		JPanel timerPanel = new JPanel(new FlowLayout());
		timerPanel.setPreferredSize(new Dimension(Constants.BOARD_WIDTH, Constants.TILE_SIZE * 2));

		timerBar = new JLabel("Time Remaining: " + this.getGameState().getTimer());
		timerPanel.add(timerBar);
		add(timerPanel, BorderLayout.NORTH);

		int delay = 1000; // Timer event will be fired every 1 second.
		timer = new Timer(delay, e -> {
			getGameState().decrementTimerByOneSecond();
			int timeRemaining = getGameState().getTimer();
			if (timeRemaining > 0) {
				getTimerBar().setText("Time Remaining: " + timeRemaining);
			} else {
				processPostGameLostConditions();
			}
		});

		gameBoard = new GameBoard(getGameState());
		gameBoard.addMouseListener(new TileClickEventListener());
		add(gameBoard);

		statusBar = new JLabel("Mines Left: " + getGameState().getMinesLeft());
		statusBar.setPreferredSize(new Dimension(Constants.BOARD_WIDTH, Constants.TILE_SIZE * 2));
		add(statusBar, BorderLayout.SOUTH);

		setResizable(false);
		pack();
		setTitle("Minesweeper");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void processPostGameLostConditions() {
		timer.stop();
		this.getGameState().setGameResult(Constants.GAME_LOST);
		this.getTimerBar().setText("Game Lost!");
		//Reveal all the mines as well as wrongly flagged mines.
		this.getGameState().revealAllMines();
	}

	public void processPostWinConditions() {
		timer.stop();
		this.getGameState().setGameResult(Constants.GAME_WON);
		this.getTimerBar().setText("Game Won!\tScore: " + getGameState().getTimer());
		//Reveal all the mines as well as wrongly flagged mines.
		this.getGameState().revealAllMines();

		// Store result in database.
		String playerName = JOptionPane.showInputDialog(getGameBoard(), "Enter Player Name.", "Save Score", JOptionPane.PLAIN_MESSAGE);
		getDao().saveScore(new HighScoreInfo(playerName, getGameState().getTimer()));
	}

	private void spawnNewGameWindow(GameState gameState, boolean timerStarted) {
		MineSweeper mineSweeper = new MineSweeper(gameState);
		mineSweeper.timerStarted = timerStarted;
		mineSweeper.setVisible(true);
	}

	public static void main(String[] args) {
		GuiUtils.loadAllImages();
		MineSweeper mineSweeper = new MineSweeper(new GameState());
		mineSweeper.setVisible(true);
	}

	class NewGameListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (getGameState().getGameResult() == Constants.GAME_IN_PROGRESS) {
				int response = JOptionPane.showConfirmDialog(getGameBoard(), "Do you want to save the current game?", "Save This Game?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

				if (response == 0) {
					new SaveMenuListener().actionPerformed(e);
				}
			}

			spawnNewGameWindow(new GameState(), false);
			dispose();
		}
	}

	class OpenGameListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			stopTimer();
			try {
				getObjectOutputStream().writeObject(Constants.GET_SAVED_GAMES);

				java.util.List<Object> list = (java.util.List<Object>) getObjectInputStream().readObject();
				Object[] options = list.toArray(new Object[0]);
				String gameName = (String) JOptionPane.showInputDialog(getGameBoard(), "Select a game to load.", "Open Game", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				getObjectOutputStream().writeObject(Constants.LOAD_GAME);
				getObjectOutputStream().writeObject(gameName);
				GameState openGameState = (GameState) getObjectInputStream().readObject();
				spawnNewGameWindow(openGameState, true);
			} catch (IOException | ClassNotFoundException ex) {
				ex.printStackTrace();
			}

			dispose();
			startTimer();
		}
	}

	class SaveMenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			stopTimer();
			String gameName = JOptionPane.showInputDialog(getGameBoard(), "Enter a name for this game.", "Save Game", JOptionPane.PLAIN_MESSAGE);

			try {
				getObjectOutputStream().writeObject(new SavedGameInfo(gameName, getGameState()));
				System.out.println("Game saved with name: " + gameName);
			} catch (java.io.IOException ex) {
				ex.printStackTrace();
			}
			startTimer();
		}
	}

	class HighScoreMenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			java.util.List<HighScoreInfo> scores = getDao().getHighScores(Constants.HIGH_SCORES_LIMIT);

			HighScoresView view = new HighScoresView(scores);
			view.setVisible(true);
		}
	}

	class ExitMenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int response = JOptionPane.showConfirmDialog(getGameBoard(), "Do you want to save the current game?", "Save This Game?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (response == 0) {
				new SaveMenuListener().actionPerformed(e);
			}

			dispose();
			System.exit(0);
		}
	}

	class TileClickEventListener extends MouseAdapter {

		boolean firstMove = true;

		@Override
		public void mouseClicked(MouseEvent e) {

			if (firstMove) {
				timerStarted = true;
				startTimer();
				firstMove = false;
				getGameState().setGameResult(Constants.GAME_IN_PROGRESS);
			}

			if (getGameState().isGameInProgress()) {
				int column = e.getX() / Constants.TILE_SIZE;
				int row = e.getY() / Constants.TILE_SIZE;

				if (GameState.isRowWithinBounds(row) && GameState.isColumnWithinBounds(column)) {
					TileState tileState = getGameState().getTileState(row, column);

					if (SwingUtilities.isRightMouseButton(e)) {
						if (tileState.isFlagged()) {
							tileState.unsetFlag();
							getGameState().incrementMines();
						} else {
							tileState.setFlag();
							getGameState().decrementMines();
						}
						getStatusBar().setText("Mines Left: " + getGameState().getMinesLeft());

					} else if (SwingUtilities.isLeftMouseButton(e)) {

						//If player clicks on a number tile
						if (tileState.isNumber()) {
							//Reveal just that tile
							tileState.reveal();
							getGameState().incrementNumberOfRevealedTiles();
						}

						//If player clicks on a mine tile - Game Lost.
						else if (tileState.isMine()) {
							processPostGameLostConditions();
						}

						//If player clicks on an empty tile.
						else /*if (tileState.isEmpty())*/ {
							//Reveal empty tiles until we hit a numbered tile.
							getGameState().revealEmptyTiles(row, column, tileState);
						}

						// What will be the win condition?
						if (getGameState().areAllNumberTilesRevealed()) {
							processPostWinConditions();
						}
					}
				}

				repaint();
			}
		}
	}

	@Override
	public void dispose() {
		try {
			getObjectOutputStream().writeObject(Constants.KILL_CLIENT);
			getObjectInputStream().close();
			getObjectOutputStream().close();
			getSocket().close();
			super.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
