package edu.nyu.tandon.minesweeper.server;

import edu.nyu.tandon.minesweeper.constants.Constants;
import edu.nyu.tandon.minesweeper.dao.DaoImplementation;
import edu.nyu.tandon.minesweeper.staging.HighScoreInfo;
import edu.nyu.tandon.minesweeper.staging.SavedGameInfo;
import edu.nyu.tandon.minesweeper.state.GameState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {

	private static DaoImplementation dao = null;

	private static DaoImplementation getDao() {
		if (dao == null) {
			dao = new DaoImplementation();
		}
		return dao;
	}

	public void start(int port) {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("Server started at port: " + port + ".");

			while (true) {
				new ClientHandler(serverSocket.accept()).start();
			}
		} catch (IOException e) {
			System.err.println("Exception occurred while starting Server:" + e.getMessage());
			e.printStackTrace();
		}
	}

	private static class ClientHandler extends Thread {
		private final Socket clientSocket;
		private ObjectOutputStream objectOutputStream;
		private ObjectInputStream objectInputStream;

		public ClientHandler(Socket clientSocket) {
			this.clientSocket = clientSocket;
			try {
				objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
				objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public ObjectOutputStream getObjectOutputStream() {
			return objectOutputStream;
		}

		public ObjectInputStream getObjectInputStream() {
			return objectInputStream;
		}

		@Override
		public void run() {

			while (true) {

				if (clientSocket.isClosed()) {
					System.err.println("Client socket " + clientSocket + " is closed.");
					break;
				}

				try {
					Object object = getObjectInputStream().readObject();
					if (object instanceof SavedGameInfo) {
						Server.getDao().saveGame((SavedGameInfo) object);
					} else if (object instanceof HighScoreInfo) {
						Server.getDao().saveScore((HighScoreInfo) object);
					} else if (object instanceof String) {
						if (object.toString().equals(Constants.GET_SAVED_GAMES)) {
							List<String> list = Server.getDao().getSavedGames();
							getObjectOutputStream().writeObject(list);
						} // logic to fetch game name from user and load game
						else if (object.toString().equals(Constants.LOAD_GAME)) {
							String gameName = (String) getObjectInputStream().readObject();
							GameState gameState = Server.getDao().loadGame(gameName);
							getObjectOutputStream().writeObject(gameState);
						}
						// logic to get high scores.
						else if (object.toString().equals(Constants.HALL_OF_FAME)) {
							List<HighScoreInfo> hallOfFame = Server.getDao().getHighScores(Constants.HIGH_SCORES_LIMIT);
							getObjectOutputStream().writeObject(hallOfFame);
						} else if (object.toString().equals(Constants.KILL_CLIENT)) {
							clientSocket.close();
							break;
						}
					}
				} catch (IOException | ClassNotFoundException e) {
					// Handle exception
					System.err.println("Exception occurred in Server:" + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.start(Constants.SERVER_PORT);
	}
}
