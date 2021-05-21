# Minesweeper
Minesweeper game in Java with a Client-Server Architecture.

## How to run:
Download the Server.jar and Minesweeper.jar files. Run Server.jar first using command `java -jar Server.jar`. Once the server is running, run `Minesweeper.jar` using command `java -jar Minesweeper.jar`.

## Design:
The application is a Maven project loosely following the MVC architecture.
All the Swing GUI components are in the `gui` package.
The `GameBoard` is structured as a Grid layout of JLabels that contain the images of different tiles. The `Tile` is a JLabel class that renders an individual tile.

The state objects in the `state` package hold the state for game in the same way. There’s a `GameState` class that holds the state of the mine field and a `TileState` that holds the state of an individual tile.

All the event listeners are inner classes in the `MineSweeper` class.

Whenever an event occurs, the `GameState` and `TileState` is updated, and the Swing components are used to just render the GameState at that moment in time.

The staging classes in the `staging` package contains Serializable classes which mimic the two database tables to hold the state of a saved game and the high scores module.

The `dao` package contains the code for the `DAOInterface` and its implementation.
The `Server` code is in the `server` package. The Server communicates with the DAO layer for saving and loading games.

I have also implemented a high scores module, which is present in the Menu bar as `Hall of Fame`. It renders a JTable that fetches the top 5 scores from the `HALL_OF_FAME` database table.

In the `utils` package, there are two classes that contain static utility methods for GUIs as well object state.

All the constants are stored in the `Constants` file.
