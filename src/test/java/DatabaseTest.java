import edu.nyu.tandon.minesweeper.constants.Constants;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class DatabaseTest {

	private void getSavedGamesTest() {
		try {
			Socket client = new Socket(Constants.HOST, Constants.SERVER_PORT);
			OutputStream outputStream = client.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(Constants.GET_SAVED_GAMES);

			ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream());
			List<String> list = (List<String>) objectInputStream.readObject();
			System.out.println(list);

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		DatabaseTest d = new DatabaseTest();
		d.getSavedGamesTest();
	}
}
