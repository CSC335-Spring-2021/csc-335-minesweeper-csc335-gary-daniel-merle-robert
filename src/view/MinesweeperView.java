package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import model.GameLostException;
import model.Leaderboard;
import model.MinesweeperBoard;
import model.MinesweeperModel;
import model.Tile;

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
	private TimerTask task;

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
							Image img = new Image("file:images/blackTile.png");
							((Rectangle) node).setFill(new ImagePattern(img));
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
	}

	@Override
	public void start(Stage stage) throws Exception {
		AnchorPane mainMenu = createGameMenu(stage);
		Scene scene = new Scene(mainMenu, 600, 600);
		stage.setTitle("Minesweeper");
		stage.setScene(scene);
		stage.show();
	}

	private AnchorPane createGameMenu(Stage stage) {
		AnchorPane anchorPane = new AnchorPane();

		Image menuImage = new Image("file:images/devilsweeper.png");
		ImageView imageView = new ImageView();
		imageView.setImage(menuImage);
		anchorPane.getChildren().add(imageView);

		Button newGameButton = new Button("New Game");
		newGameButton.setLayoutX(161.0);
		newGameButton.setLayoutY(345.0);
		newGameButton.setOpacity(0.00);
		newGameButton.setPrefHeight(41.0);
		newGameButton.setPrefWidth(278.0);
		anchorPane.getChildren().add(newGameButton);
		newGameButton.setOnAction(new NewGame(stage));
		
		Button leaderboardButton = new Button("Leaderboard");
		leaderboardButton.setLayoutX(161.0);
		leaderboardButton.setLayoutY(404.0);
		leaderboardButton.setOpacity(0.00);
		leaderboardButton.setPrefHeight(41.0);
		leaderboardButton.setPrefWidth(278.0);
		anchorPane.getChildren().add(leaderboardButton);
		leaderboardButton.setOnAction(new loadLeaderboard(stage));
		
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

	private Scene launchNewGame(Stage stage, MinesweeperModel model) {
		this.model = model;
		this.controller = new MinesweeperController(model);
		
		AnchorPane anchorPane = new AnchorPane();
		Scene gameScene = new Scene(anchorPane, 619, 694);
		VBox layout = new VBox();
		// Create timer and new game button at top
		HBox topBar = new HBox();
		topBar.setPrefHeight(70.0);
		topBar.setPrefWidth(600.0);
		topBar.setSpacing(400.0);
		topBar.setStyle("-fx-background-color: LIGHTBLUE;");
		// Create timer text
		if(controller.hasSave()) {
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
		resetButton.setPrefHeight(25.0);
		resetButton.setPrefWidth(119.0);
		
		resetButton.setOnAction(new NewGame(stage));
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

	private Scene leaderboardMenu(Stage stage) throws FileNotFoundException {
		AnchorPane anchorPane = new AnchorPane();
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
			String score = String.valueOf(leaderboard.getScore(i));
			String playerScore = name + " " + score;
			Text player = new Text(playerScore);
			player.setLayoutX(127.0);
			player.setLayoutY(layoutY);
			player.setFont(new Font(30));
			player.setFill(Color.WHITE);
			anchorPane.getChildren().add(player);
			layoutY += 49.0;
		}
		Button back = new Button();
		back.setLayoutX(14.0);
		back.setLayoutY(14.0);
		back.setPrefHeight(58.0);
		back.setPrefWidth(90.0);
		back.setOpacity(0);
		back.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					start(stage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		anchorPane.getChildren().add(back);
		Scene leaderboardScene = new Scene(anchorPane, 600, 600);
		return leaderboardScene;
	}
	
	private Scene gamemodeMenu(Stage stage) {
		AnchorPane anchorPane = new AnchorPane();
		// Create title
		Text leaderboardText = new Text("Gamemode");
		leaderboardText.setLayoutY(55.0);
		leaderboardText.setFont(new Font(57.0));
		leaderboardText.setWrappingWidth(600.0);
		
		Button regularButton = new Button("Regular");
		regularButton.setLayoutX(161.0);
		regularButton.setLayoutY(300.0);
		regularButton.setOpacity(0.69);
		regularButton.setPrefHeight(41.0);
		regularButton.setPrefWidth(278.0);
		anchorPane.getChildren().add(regularButton);
		regularButton.setOnAction(new NewGame(stage));
		
		Button triangleButton = new Button("Triangle");
		triangleButton.setLayoutX(161.0);
		triangleButton.setLayoutY(357.0);
		triangleButton.setOpacity(0.69);
		triangleButton.setPrefHeight(41.0);
		triangleButton.setPrefWidth(278.0);
		anchorPane.getChildren().add(triangleButton);
		triangleButton.setOnAction(new loadLeaderboard(stage));
		
		Button donutButton = new Button("Donut");
		donutButton.setLayoutX(161.0);
		donutButton.setLayoutY(416.0);
		donutButton.setOpacity(0.69);
		donutButton.setPrefHeight(41.0);
		donutButton.setPrefWidth(278.0);
		anchorPane.getChildren().add(donutButton);
		
		Scene gamemodeScene = new Scene(anchorPane, 600, 600);
		return gamemodeScene;
	}
	

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

	private class NewGame implements EventHandler<ActionEvent> {
		private Stage stage;

		public NewGame(Stage stage) {
			this.stage = stage;
		}
		@Override
		public void handle(ActionEvent event) {
			// Resets time back to 0 if new game is called
			TextInputDialog getName = new TextInputDialog();
			getName.setHeaderText("Enter your name");
			// show the text input dialog
        	getName.showAndWait();
            // set the text of the label
            playerName = getName.getEditor().getText();
			stage.setScene(launchNewGame(stage, new MinesweeperModel()));
			time = 0;
			stage.setOnCloseRequest(new GameClosed());
		}
	}
	
	private class GameClosed implements EventHandler<WindowEvent> {
		@Override
		public void handle(WindowEvent event) {
			// If file exists
			File save = new File("save_game.dat");
			if (save.isFile() && !save.isDirectory()) {
				save.delete();
			}
			if (controller.isGameOver() || controller.isLost()) {
				System.out.println("yup");
				return;
			}
			model.saveGame(time);
		}
		
	}
	
	private class loadLeaderboard implements EventHandler<ActionEvent> {
		private Stage stage;
		
		public loadLeaderboard(Stage stage) {
			this.stage = stage;
		}

		@Override
		public void handle(ActionEvent arg0) {
			try {
				stage.setScene(leaderboardMenu(stage));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

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

		@Override
		public void handle(MouseEvent event) {
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
			if (event.getButton() == MouseButton.PRIMARY) {
				try {
					controller.revealSpace(row, col);
					if (controller.isGameOver()) {
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
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						Alert alert = new Alert(AlertType.INFORMATION, "You Won!");
						alert.showAndWait();
					}
				} catch (GameLostException e) {
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
		
		private void disable() {
			for (int i = 0; i < SIZE_OF_BOARD; i++) {
				for (int j = 0; j < SIZE_OF_BOARD; j++) {
					StackPane gameTile = gameTiles[i][j];
					gameTile.setDisable(true);
				}
			}
		}
	}
	
	private class LoadGame implements EventHandler<ActionEvent> {
		private Stage stage;
		
		public LoadGame(Stage stage) {
			this.stage = stage;
		}
		
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
