package view;

import javafx.application.*;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import java.util.Observable;
import java.util.Observer;

import controller.MinesweeperController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import model.Leaderboard;
import java.util.Timer;
import java.util.TimerTask;
import java.io.FileNotFoundException;
import model.MinesweeperBoard;
import model.MinesweeperModel;
import model.Tile;
import java.text.DecimalFormat;

@SuppressWarnings("deprecation")
public class MinesweeperView extends Application implements Observer {
	
	private MinesweeperModel model;
	private MinesweeperController controller;	
	private final int SIZE_OF_BOARD = 13;
	private StackPane[][] gameTiles;
	private Text timeDisplay;
	private static Timer timer = new Timer();
	private static boolean startOfGame = true;
	private double time = 0;
	private TimerTask task = new TimerTask() {
		@Override
		public void run() {
			time += 0.01;
			DecimalFormat f = new DecimalFormat("#0.00");
			//Needed to prevent user interface from freezing
			Platform.runLater(() -> {
				timeDisplay.setText("TIME: " + f.format(time));
			});
		}
	};

	@Override
	public void update(Observable o, Object arg) {
		MinesweeperBoard newBoard = (MinesweeperBoard) arg;
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				StackPane gameTile = gameTiles[i][j];
				Tile tile = newBoard.getTile(i, j);
				ObservableList<Node> list = gameTile.getChildren();
				for (Node node : list) {
					if (node instanceof Rectangle) {
						//Condition 1: Covered + flagged
						if(tile.isCovered && tile.isFlagged) {
							Image img = new Image("file:images/flagged_tile.png");
							((Rectangle)node).setFill(new ImagePattern(img));
						}
						//Condition 2: Covered + no flag
						else if(tile.isCovered && !tile.isFlagged) {
							Image img = new Image("file:images/covered_tile.png");
							((Rectangle)node).setFill(new ImagePattern(img));
						}
						//Condition 3: Uncovered + has mine
						else if(!tile.isCovered && tile.hasMine) {
							Image img = new Image("file:images/bombUncovered.png");
							((Rectangle)node).setFill(new ImagePattern(img));
						}
						//Condition 4: Uncovered + no mine nearby
						else if(!tile.isCovered && !tile.hasMine && tile.displayNum.equals(0)) {
							Image img = new Image("file:images/0.png");
							((Rectangle)node).setFill(new ImagePattern(img));
						}
						//Condition 5: Uncovered + 1 mine nearby
						else if(!tile.isCovered && !tile.hasMine && tile.displayNum.equals(1)) {
							Image img = new Image("file:images/1.png");
							((Rectangle)node).setFill(new ImagePattern(img));
						}
						//Condition 6: Uncovered + 2 mine nearby
						else if(!tile.isCovered && !tile.hasMine && tile.displayNum.equals(2)) {
							Image img = new Image("file:images/2.png");
							((Rectangle)node).setFill(new ImagePattern(img));
						}
						//Condition 7: Uncovered + 3 mine nearby
						else if(!tile.isCovered && !tile.hasMine && tile.displayNum.equals(3)) {
							Image img = new Image("file:images/3.png");
							((Rectangle)node).setFill(new ImagePattern(img));
						}
						//Condition 8: Uncovered + 4 mine nearby
						else if(!tile.isCovered && !tile.hasMine && tile.displayNum.equals(4)) {
							Image img = new Image("file:images/4.png");
							((Rectangle)node).setFill(new ImagePattern(img));
						}
						//Condition 9: Uncovered + 5 mine nearby
						else if(!tile.isCovered && !tile.hasMine && tile.displayNum.equals(5)) {
							Image img = new Image("file:images/5.png");
							((Rectangle)node).setFill(new ImagePattern(img));
						}
						//Condition 10: Uncovered + 6 mine nearby
						else if(!tile.isCovered && !tile.hasMine && tile.displayNum.equals(6)) {
							Image img = new Image("file:images/6.png");
							((Rectangle)node).setFill(new ImagePattern(img));
						}
						//Condition 11: Uncovered + 7 mine nearby
						else if(!tile.isCovered && !tile.hasMine && tile.displayNum.equals(7)) {
							Image img = new Image("file:images/7.png");
							((Rectangle)node).setFill(new ImagePattern(img));
						}
						//Condition 12: Uncovered + 8 mine nearby
						else if(!tile.isCovered && !tile.hasMine && tile.displayNum.equals(8)) {
							Image img = new Image("file:images/8.png");
							((Rectangle)node).setFill(new ImagePattern(img));
						}
						else {
							Image img = new Image("file:images/blackTile.png");
							((Rectangle)node).setFill(new ImagePattern(img));
						}
					}
				}
			}
		}
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		this.model = new MinesweeperModel();
		this.controller = new MinesweeperController(model);
		//TODO: Get rid of comments when model is done
		//model.addObserver(this);
		//model.notifies();
		AnchorPane mainMenu = createGameMenu(stage);
		Scene scene = new Scene(mainMenu,600,600);
		
		stage.setTitle("Minesweeper");
		stage.setScene(scene);
		stage.show();
	}
	
	private AnchorPane createGameMenu(Stage stage) {
		AnchorPane anchorPane = new AnchorPane();
		
		Image menuImage = new Image("file:images/dababy.jpg");
		ImageView imageView = new ImageView();
		imageView.setImage(menuImage);
		anchorPane.getChildren().add(imageView);
		
		Button newGameButton = new Button("New Game");
		newGameButton.setLayoutX(161.0);
		newGameButton.setLayoutY(300.0);
		newGameButton.setOpacity(0.69);
		newGameButton.setPrefHeight(41.0);
		newGameButton.setPrefWidth(278.0);
		anchorPane.getChildren().add(newGameButton);
		newGameButton.setOnAction(new NewGame(stage));
		
		Text title = new Text("Minesweeper"); 
		title.setLayoutX(62.0);
		title.setLayoutY(66.0);
		title.setFont(new Font(81.0));
		anchorPane.getChildren().add(title);
		
		return anchorPane;
	}

	private Scene launchNewGame(Stage stage) {
		AnchorPane anchorPane = new AnchorPane();
		Scene gameScene = new Scene(anchorPane,619,694);
		VBox layout = new VBox();
		// Create timer and new game button at top
		HBox topBar = new HBox();
		topBar.setPrefHeight(70.0);
		topBar.setPrefWidth(600.0);
		topBar.setSpacing(400.0);
		topBar.setStyle("-fx-background-color: LIGHTBLUE;");
		// Create timer text
		timeDisplay = new Text("TIME: 0.00");
		timeDisplay.setFont(new Font(18.0));
		topBar.getChildren().add(timeDisplay);
    
		// Create New Game Button
		Button resetButton = new Button("New Game");
		resetButton.setPrefHeight(25.0);
		resetButton.setPrefWidth(119.0);
		//A task can only be set once otherwise an exception will be thrown
		if(startOfGame) {
			//Every 10 milliseconds the run function for task is called
			timer.scheduleAtFixedRate(task, 10, 10);
			startOfGame = false;
		}
		resetButton.setOnAction(new NewGame(stage));
		topBar.getChildren().add(resetButton);
		topBar.setPadding(new Insets(25.0,25.0,25.0,25.0));
    
		// Create Grid
		gameTiles = createGameTiles();
		GridPane board = createGameBoard(gameTiles);
		layout.getChildren().add(topBar);
		layout.getChildren().add(board);
		anchorPane.getChildren().add(layout);
		
		return gameScene;
	}
  
	public Scene leaderboardMenu() throws FileNotFoundException {
		AnchorPane pane = new AnchorPane();
		Leaderboard leaderboard = new Leaderboard();
		// Create title
		Text leaderboardText = new Text("Leaderboard");
		leaderboardText.setLayoutY(55.0);
		leaderboardText.setFont(new Font(57.0));
		leaderboardText.setWrappingWidth(600.0);
		// Create VBox to store names and score
		VBox list = new VBox();
		list.setLayoutX(125.0);
		list.setLayoutY(81.0);
		list.prefHeight(500.0);
		list.prefWidth(350.0);
		for(int i =1;i<=10;i++) {
			// Create HBox for each person rank/name/score
			HBox player = new HBox();
			player.prefHeight(50.0);
			player.prefWidth(200.0);
			player.setSpacing(100.0);
			Text rank = new Text(String.valueOf(i) + ".");
			rank.setFont(new Font(21.0));
			Text name = new Text(leaderboard.getName(i));
			name.setFont(new Font(21.0));
			Text score = new Text(String.valueOf(leaderboard.getScore(i)));
			score.setFont(new Font(21.0));
			player.getChildren().add(rank);
			player.getChildren().add(name);
			player.getChildren().add(score);
			list.getChildren().add(player);
			
		}
		pane.getChildren().add(leaderboardText);
		pane.getChildren().add(list);
		Scene leaderboardScene = new Scene(pane,600,600);
		return leaderboardScene;
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
		StackPane[][] stackPanes = new StackPane[13][13];
		for (int r = 0; r < SIZE_OF_BOARD; r++) {
			for (int c = 0; c < SIZE_OF_BOARD; c++) {
				Rectangle square = new Rectangle(530/SIZE_OF_BOARD - 1, 530/SIZE_OF_BOARD - 1);
				Image img = new Image("file:images/covered_tile.png");
				square.setFill(new ImagePattern(img));
				// Create stack pane and set padding and background
				StackPane tile = new StackPane(square);
				tile.setBorder(new Border(new BorderStroke(Color.rgb(123, 123, 123), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
				tile.setOnMouseClicked(new TileClicked(r,c));
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
			//Resets time back to 0 if new game is called
			stage.setScene(launchNewGame(stage));
			time = 0;
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
			if (event.getButton() == MouseButton.PRIMARY)
            {
				controller.makeMove(row, col, "p");
            } else if (event.getButton() == MouseButton.SECONDARY)
            {
            	controller.makeMove(row, col, "s");
            }
		}
	}
}
