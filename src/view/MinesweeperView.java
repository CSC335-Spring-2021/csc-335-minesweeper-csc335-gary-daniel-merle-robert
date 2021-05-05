package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import controller.MinesweeperController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Leaderboard;
import model.MinesweeperBoard;
import model.MinesweeperModel;
import model.Tile;

/**
 * A view class for the minesweeper game. Holds a model and controller instance
 * as well as methods to setup up the graphical user interface and to update the 
 * board when changes are made. Handles multiple events when a player attempts 
 * to make a move or when the player interacts with different scenes and buttons.
 * 
 * @author Gary Li, Daniel S. Lee, Robert Schnell, Merle Crutchfield
 */
@SuppressWarnings("deprecation")
public class MinesweeperView extends Application implements Observer {

	private MinesweeperModel model;
	private MinesweeperController controller;
	private final int SIZE_OF_BOARD = 13;
	private String playerName = "";
	private Leaderboard leaderboard;
	private StackPane[][] gameTiles;
	private Text timeDisplay;
	private static Timer timer;
	private double time = 0;
	private boolean renderBlack = true;
	private TimerTask task;

	/**
	 * Updates the board to match the model when the model makes change to it.
	 * 
	 * @param o An observable object which notifies observers about changes to the board.
	 * 
	 * @param arg A MinesweeperBoard object that represents the board of the model.
	 */
	@Override
	public void update(Observable o, Object arg) {
		MinesweeperBoard newBoard = (MinesweeperBoard) arg;
		for (int i = 0; i < SIZE_OF_BOARD; i++) {
			for (int j = 0; j < SIZE_OF_BOARD; j++) {
				StackPane gameTile = gameTiles[i][j];
				Tile tile = newBoard.getTile(i, j);
				ObservableList<Node> list = gameTile.getChildren();
				for (Node node : list) {
					if (node instanceof Rectangle) {
						// Condition 0: Tile out of bounds
						if (!tile.inBounds) {
							if (renderBlack) {
								Image img = new Image("file:images/blackTile.png");
								((Rectangle) node).setFill(new ImagePattern(img));
							}
						}
						// Condition 1: Covered + flagged
						else if (tile.isCovered && tile.isFlagged) {
							Image img = new Image("file:images/flagged_tile-2.png");
							((Rectangle) node).setFill(new ImagePattern(img));
						}
						// Condition 2: Covered + no flag
						else if (tile.isCovered && !tile.isFlagged) {
							Image img = new Image("file:images/covered_tile.png");
							((Rectangle) node).setFill(new ImagePattern(img));
						}
						// Condition 3: Uncovered + has mine
						else if (!tile.isCovered && tile.hasMine) {
							Image img = new Image("file:images/bombUncovered-2.png");
							((Rectangle) node).setFill(new ImagePattern(img));
						}
						// Condition 4: Uncovered + mines nearby
						else if (!tile.isCovered && !tile.hasMine) {
							Image img = new Image("file:images/" + Integer.toString(tile.displayNum) + ".png");
							((Rectangle) node).setFill(new ImagePattern(img));
						}
					}
				}
			}
		}
		renderBlack = false;
	}
	
	/**
	 * The method that is called when the JavaFX application is started. 
	 * 
	 * @param stage where all visual parts of the JavaFX application are displayed
	 */
	@Override
	public void start(Stage stage) throws Exception {
		AnchorPane mainMenu = createGameMenu(stage);
		Scene scene = new Scene(mainMenu, 600, 600);
		stage.setTitle("Minesweeper");
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * This method creates the main menu that will be shown when the game
	 * is launched.
	 * 
	 * @param stage where all visual parts of the JavaFX application are displayed
	 * 
	 * @return AnchorPane that will be added to the scene
	 */
	private AnchorPane createGameMenu(Stage stage) {
		AnchorPane anchorPane = new AnchorPane();
		// Adds background image
		Image menuImage = new Image("file:images/devilsweeper.png");
		ImageView imageView = new ImageView();
		imageView.setImage(menuImage);
		anchorPane.getChildren().add(imageView);
		// Adds New game button
		Button newGameButton = new Button("New Game");
		newGameButton.setLayoutX(161.0);
		newGameButton.setLayoutY(345.0);
		newGameButton.setOpacity(0.00);
		newGameButton.setPrefHeight(41.0);
		newGameButton.setPrefWidth(278.0);
		anchorPane.getChildren().add(newGameButton);
		newGameButton.setOnAction(new NewGamemodeMenu(stage));
		// Adds leaderboard button
		Button leaderboardButton = new Button("Leaderboard");
		leaderboardButton.setLayoutX(161.0);
		leaderboardButton.setLayoutY(404.0);
		leaderboardButton.setOpacity(0.00);
		leaderboardButton.setPrefHeight(41.0);
		leaderboardButton.setPrefWidth(278.0);
		anchorPane.getChildren().add(leaderboardButton);
		leaderboardButton.setOnAction(new loadLeaderboard(stage));
		// Adds load game button
		Button loadGameButton = new Button("Load Game");
		loadGameButton.setLayoutX(161.0);
		loadGameButton.setLayoutY(464.0);
		loadGameButton.setOpacity(0.00);
		loadGameButton.setPrefHeight(41.0);
		loadGameButton.setPrefWidth(278.0);
		anchorPane.getChildren().add(loadGameButton);
		loadGameButton.setOnAction(new LoadGame(stage));

		return anchorPane;
	}
	
	/**
	 * This method creates the main game menu, where the user will be 
	 * presented with the minesweeper board and will be able to play.
	 * 
	 * @param stage where all visual parts of the JavaFX application are displayed
	 * 
	 * @param model the minesweeper model
	 * 
	 * @return Scene a scene representing the main game
	 * 
	 */
	private Scene launchNewGame(Stage stage, MinesweeperModel model) {
		// Initialize model and controller
		this.model = model;
		this.controller = new MinesweeperController(model);
		
		AnchorPane anchorPane = new AnchorPane();
		Scene gameScene = new Scene(anchorPane, 619, 694);
		// VBox to create a vertical layout
		VBox layout = new VBox();
		// Create timer and new game button at top
		// HBox to store timer and back button at the top of screen
		HBox topBar = new HBox();
		topBar.setPrefHeight(70.0);
		topBar.setPrefWidth(600.0);
		topBar.setSpacing(300.0);
		// Set background of the top bar
		BackgroundImage image = new BackgroundImage(new Image("file:images/game_bar.png"), 
				BackgroundRepeat.NO_REPEAT,
		        BackgroundRepeat.NO_REPEAT,
		        BackgroundPosition.CENTER,
		        null);
		topBar.setBackground(new Background(image));
		// Create timer text
		if(controller.hasSave()) {
			playerName = model.getName();
			DecimalFormat f = new DecimalFormat("#0.00");
			time = model.getTime();
			timeDisplay = new Text("TIME: " + f.format(time));
		}
		else {
			timeDisplay = new Text("TIME: 0.00");
		}
		timeDisplay.setFont(new Font(18.0));
		topBar.getChildren().add(timeDisplay);

		// Create New Game Button
		Button resetButton = new Button("New Game");
		resetButton.setPrefHeight(30.0);
		resetButton.setPrefWidth(250.0);
		resetButton.setOpacity(0.0);
		resetButton.setOnAction(new NewGamemodeMenu(stage));
		topBar.getChildren().add(resetButton);
		topBar.setPadding(new Insets(25.0, 25.0, 25.0, 25.0));
		
		// Create Grid
		gameTiles = createGameTiles();
		GridPane board = createGameBoard(gameTiles);
		layout.getChildren().add(topBar);
		layout.getChildren().add(board);
		anchorPane.getChildren().add(layout);

		this.model.addObserver(this);
		this.model.notifyView();


		return gameScene;
	}
	
	/**
	 * This method creates the leaderboard scene to display the top 10
	 * players and their scores.
	 * 
	 * @param stage where all visual parts of the JavaFX application are displayed
	 * 
	 * @return Scene a scene representing the leaderboard
	 * 
	 * @throws IOException signals that an input/output exception has occurred
	 */
	private Scene leaderboardMenu(Stage stage) throws IOException {
		AnchorPane anchorPane = new AnchorPane();
		// Initialize leaderboard and set background
		leaderboard = new Leaderboard();
		Image menuImage = new Image("file:images/leaderboard_menu.png");
		ImageView imageView = new ImageView();
		imageView.setImage(menuImage);
		anchorPane.getChildren().add(imageView);
		// Create VBox to store names and score
		double layoutY = 125.0;
		for (int i = 1; i <= 10; i++) {
			// Create HBox for each person rank/name/score
			String name = leaderboard.getName(i);
			String score = " ";
			if(leaderboard.getScore(i) > 0)
				score = String.valueOf(leaderboard.getScore(i));
			String playerScore = name + " " + score;
			Text player = new Text(playerScore);
			player.setLayoutX(127.0);
			player.setLayoutY(layoutY);
			player.setFont(new Font(30));
			player.setFill(Color.WHITE);
			anchorPane.getChildren().add(player);
			layoutY += 49.0;
		}
		// Add a back button to go to main menu
		Button back = new Button();
		back.setLayoutX(14.0);
		back.setLayoutY(14.0);
		back.setPrefHeight(58.0);
		back.setPrefWidth(90.0);
		back.setOpacity(0);
		// Event to go back to main menu
		back.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				try {
					start(stage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		anchorPane.getChildren().add(back);
		Scene leaderboardScene = new Scene(anchorPane, 600, 600);
		return leaderboardScene;
	}
	/**
	 * This method creates a scene that will allow a player to choose
	 * from 3 different gamemodes.
	 * 
	 * @param stage where all visual parts of the JavaFX application are displayed
	 * 
	 * @return Scene a scene representing the game mode menu
	 */
	private Scene gamemodeMenu(Stage stage) {
		AnchorPane anchorPane = new AnchorPane();
		// Create background
		Image menuImage = new Image("file:images/game_selection.png");
		ImageView imageView = new ImageView();
		imageView.setImage(menuImage);
		anchorPane.getChildren().add(imageView);
		//Regular gamemode button
		Button regularButton = new Button("Regular");
		regularButton.setLayoutX(161.0);
		regularButton.setLayoutY(220.0);
		regularButton.setOpacity(0.0);
		regularButton.setPrefHeight(45.0);
		regularButton.setPrefWidth(278.0);
		anchorPane.getChildren().add(regularButton);
		regularButton.setOnAction(new StartGame(stage));
		// Triangle gamemode button
		Button triangleButton = new Button("Triangle");
		triangleButton.setLayoutX(161.0);
		triangleButton.setLayoutY(300.0);
		triangleButton.setOpacity(0.0);
		triangleButton.setPrefHeight(45.0);
		triangleButton.setPrefWidth(278.0);
		anchorPane.getChildren().add(triangleButton);
		triangleButton.setOnAction(new StartGame(stage, "triangle"));
		// Donut gamemode button
		Button donutButton = new Button("Donut");
		donutButton.setLayoutX(161.0);
		donutButton.setLayoutY(385.0);
		donutButton.setOpacity(0.0);
		donutButton.setPrefHeight(41.0);
		donutButton.setPrefWidth(278.0);
		anchorPane.getChildren().add(donutButton);
		donutButton.setOnAction(new StartGame(stage, "donut"));
		// Back button to go to main menu
		Button back = new Button();
		back.setLayoutX(12.0);
		back.setLayoutY(5.0);
		back.setPrefHeight(58.0);
		back.setPrefWidth(90.0);
		back.setOpacity(0.0);
		back.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				try {
					start(stage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		anchorPane.getChildren().add(back);
		
		Scene gamemodeScene = new Scene(anchorPane, 600, 600);
		return gamemodeScene;
	}
	
	/**
	 * Creates a gridpane that will represent the 13x13 grid for the 
	 * minesweeper game.
	 * 
	 * @param gameTiles A nested array of stackpanes to act as game tiles.
	 * 
	 * @return GridPane A gridpane represented a game board.
	 */
	private GridPane createGameBoard(StackPane[][] gameTiles) {
		// Create gridpane and set background and insets
		GridPane gameBoard = new GridPane();
		gameBoard.setBackground(
				new Background(new BackgroundFill(Color.rgb(189, 189, 189), CornerRadii.EMPTY, Insets.EMPTY)));
		gameBoard.setPadding(new Insets(8, 8, 8, 8));
		// Add tiles as children
		for (int r = 0; r < 13; r++) {
			for (int c = 0; c < 13; c++) {
				gameBoard.add(gameTiles[r][c], c, r);
			}
		}
		return gameBoard;
	}
	/**
	 * Creates an 13 by 13 array of stackpanes to represent all tiles within the
	 * board. Each stackpane contains rectangles and event handlers to respond to mouse
	 * clicks.
	 * 
	 * @return An 13 by 13 nested array of stackpanes.
	 */
	private StackPane[][] createGameTiles() {
		StackPane[][] stackPanes = new StackPane[SIZE_OF_BOARD][SIZE_OF_BOARD];
		for (int r = 0; r < SIZE_OF_BOARD; r++) {
			for (int c = 0; c < SIZE_OF_BOARD; c++) {
				Rectangle square = new Rectangle(44, 44);
				Image img = new Image("file:images/covered_tile.png");
				square.setFill(new ImagePattern(img));
				// Create stack pane and set padding and background
				StackPane tile = new StackPane(square);
				tile.setBorder(new Border(new BorderStroke(Color.rgb(123, 123, 123), BorderStrokeStyle.SOLID,
						CornerRadii.EMPTY, BorderWidths.DEFAULT)));
				tile.setOnMouseClicked(new TileClicked(r, c));
				// Set mouse click handler, add to list
				stackPanes[r][c] = tile;
			}
		}
		return stackPanes;
	}

	/**
	 * Event handler for when a new game is started.
	 * 
	 * Upon click, a new game is created and the time is set to 0.
	 * 
	 * @author Gary Li, Daniel S. Lee, Robert Schnell, Merle Crutchfield
	 */
	private class StartGame implements EventHandler<ActionEvent> {
		private Stage stage;
		private String shape;
		
		/**
		 * Initializes StartGame with the current stage
		 * 
		 * @param stage where all visual parts of the JavaFX application are displayed
		 */
		public StartGame(Stage stage) {
			this.stage = stage;
			this.shape = "";
		}
		
		/**
		 * Initializes StartGame with the current stage and the current game mode
		 * 
		 * @param stage where all visual parts of the JavaFX application are displayed
		 * @param shape a string that represents the game mode
		 */
		public StartGame(Stage stage,String shape) {
			this.stage = stage;
			this.shape = shape;
		}
		
		/**
		 * Handles the event when a new game is started
		 * 
		 * @param event An action event object.
		 */
		@Override
		public void handle(ActionEvent event) {
			// Resets time back to 0 if new game is called
			TextInputDialog getName = new TextInputDialog();
			getName.setHeaderText("Enter your name");
			// show the text input dialog
        	getName.showAndWait();
            // set the text of the label
            playerName = getName.getEditor().getText();
            if(shape.isEmpty()) {
            	stage.setScene(launchNewGame(stage, new MinesweeperModel()));
            }
            else {
            	renderBlack = true;
            	stage.setScene(launchNewGame(stage, new MinesweeperModel(shape)));
            }
			time = 0;
			stage.setOnCloseRequest(new GameClosed());
		}
	}
	
	/**
	 * Event handler for when the game window is closed.
	 * 
	 * When the game is closed, the old save file is deleted and a new one is created
	 * if the game is not over and a move has been made. Information such as time and 
	 * player name are saved.
	 * 
	 * @author Gary Li, Daniel S. Lee, Robert Schnell, Merle Crutchfield
	 */
	private class GameClosed implements EventHandler<WindowEvent> {
		/**
		 * Handles the event when the game window is closed
		 * 
		 * @param event A window event object.
		 */
		@Override
		public void handle(WindowEvent event) {
			// If file exists
			File save = new File("save_game.dat");
			if (save.isFile() && !save.isDirectory()) {
				save.delete();
			}
			if (controller.hasWon() || controller.hasLost() || controller.isFirstMove()) {
				return;
			}
			if(!(timer == null)) {
				timer.cancel();
				timer.purge();
			}
			model.saveGame(time, playerName);
		}
	}
	
	/**
	 * Event handler that sets the window to the game mode menu screen
	 * 
	 * Upon click, the screen changes to the game mode menu
	 * 
	 * @author Gary Li, Daniel S. Lee, Robert Schnell, Merle Crutchfield
	 */
	private class NewGamemodeMenu implements EventHandler<ActionEvent> {
		private Stage stage;

		/**
		 * Initializes NewGamemodeMenu with the current stage
		 * 
		 * @param stage where all visual parts of the JavaFX application are displayed
		 */
		public NewGamemodeMenu(Stage stage) {
			this.stage = stage;
		}
		
		/**
		 * Handles the event when new game is clicked
		 * 
		 * @param event A action event object.
		 */
		@Override
		public void handle(ActionEvent event) {
			if(!(timer == null)) {
				timer.cancel();
				timer.purge();
			}
			stage.setScene(gamemodeMenu(stage));
		}
	}

	/**
	 * Event handler that sets the window to the leaderboard
	 * 
	 * Upon click, the screen changes to the leaderboard
	 * 
	 * @author Gary Li, Daniel S. Lee, Robert Schnell, Merle Crutchfield
	 *
	 */
	private class loadLeaderboard implements EventHandler<ActionEvent> {
		private Stage stage;

		/**
		 * Initializes loadLeaderboard with the current stage
		 * 
		 * @param stage where all visual parts of the JavaFX application are displayed
		 */
		public loadLeaderboard(Stage stage) {
			this.stage = stage;
		}

		/**
		 * Handles the event when leaderboard is clicked
		 * 
		 * @param event A action event object.
		 */
		@Override
		public void handle(ActionEvent event) {
			try {
				stage.setScene(leaderboardMenu(stage));
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Event handler for when a tile is clicked on the gameBoard.
	 * 
	 * This event handler supports both left and right mouse button inputs.
	 * If the left button is clicked then the tile is revealed and if the 
	 * right button is clicked then the tile is flagged.
	 * 
	 * @author Gary Li, Daniel S. Lee, Robert Schnell, Merle Crutchfield
	 */
	private class TileClicked implements EventHandler<MouseEvent> {
		private int row;
		private int col;

		/**
		 * Initializes the event handler with the coordinates of the tile it is
		 * associated with.
		 * 
		 * @param row A row coordinate
		 * @param col A column coordinate
		 */
		public TileClicked(int row, int col) {
			this.row = row;
			this.col = col;
		}

		/**
		 * Handles the event when tile is clicked
		 * 
		 * @param event A mouse event object.
		 */
		@Override
		public void handle(MouseEvent event) {
			if (event.getButton() == MouseButton.PRIMARY) {
				//Timer only starts when primary mouse button is clicked
				if(controller.isFirstMove() || controller.hasSave()) {
					timer = new Timer();
					// A task can only be set once otherwise an exception will be thrown
					task = new TimerTask() {
						@Override
						public void run() {
							time += 0.01;
							DecimalFormat f = new DecimalFormat("#0.00");
							// Needed to prevent user interface from freezing
							Platform.runLater(() -> {
								timeDisplay.setText("TIME: " + f.format(time));
							});
						}
					};
					timer.scheduleAtFixedRate(task, 10, 10);
				}
				
				controller.revealSpace(row, col);
				if (controller.hasWon()) {
					timer.cancel();
					timer.purge();
					disable();
					try {
						leaderboard = new Leaderboard();
						try {
							leaderboard.addScore(playerName, (int)time);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					Alert alert = new Alert(AlertType.INFORMATION, "You Won!");
					alert.showAndWait();
				}
				else if (controller.hasLost()) {
					timer.cancel();
					timer.purge();
					disable();
					controller.revealMines();
					Alert alert = new Alert(AlertType.INFORMATION, "You Lost!");
					alert.showAndWait();
				}
			} else if (event.getButton() == MouseButton.SECONDARY) {
				controller.flagSpace(row, col);
			}
		}
		
		/**
		 * Disables the tiles when the game has ended
		 */
		private void disable() {
			for (int i = 0; i < SIZE_OF_BOARD; i++) {
				for (int j = 0; j < SIZE_OF_BOARD; j++) {
					StackPane gameTile = gameTiles[i][j];
					gameTile.setDisable(true);
				}
			}
		}
	}
	
	/**
	 * Event handler for when load game is clicked on the menu.
	 * 
	 * Upon click, checks if save exists and if it does loads the save
	 * 
	 * @author Gary Li, Daniel S. Lee, Robert Schnell, Merle Crutchfield
	 */
	private class LoadGame implements EventHandler<ActionEvent> {
		private Stage stage;
		
		/**
		 * Initializes LoadGame with the current stage
		 * 
		 * @param stage where all visual parts of the JavaFX application are displayed
		 */
		public LoadGame(Stage stage) {
			this.stage = stage;
		}
		
		/**
		 * Handles the event when load game is clicked
		 * 
		 * @param event A mouse event object.
		 */
		@Override
		public void handle(ActionEvent event) {
			File save = new File("save_game.dat");
			try {
				ObjectInputStream oos = new ObjectInputStream(new FileInputStream(save));
				MinesweeperBoard board = (MinesweeperBoard) oos.readObject();
				oos.close();
				MinesweeperModel model = new MinesweeperModel(board);
				stage.setScene(launchNewGame(stage, model));
				stage.setOnCloseRequest(new GameClosed());
				
			} catch (IOException | ClassNotFoundException e) {
				Alert alert = new Alert(AlertType.INFORMATION, "Save file not found!");
				alert.showAndWait();
			}
		}
	}
}
