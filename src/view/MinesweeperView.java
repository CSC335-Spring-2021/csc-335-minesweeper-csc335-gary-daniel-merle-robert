package view;

import javafx.application.*;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.MinesweeperBoard;
import model.MinesweeperModel;
import model.Tile;

import java.util.Timer;
import java.util.TimerTask;
import java.text.DecimalFormat;

@SuppressWarnings("deprecation")
public class MinesweeperView extends Application implements Observer {
	
	private MinesweeperModel model;
	private MinesweeperController controller;

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
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				StackPane gameTile = gameTiles[i][j];
				Tile tile = newBoard.getTile(i, j);
				ObservableList<Node> list = gameTile.getChildren();
				for (Node node : list) {
					if (node instanceof Rectangle) {
						//Condition 1: Covered + flagged
						//Condition 2: Covered + no flag
						//Condition 3: Uncovered + has mine
						//Condition 4: Uncovered + no mine
					}
				}
			}
		}
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		this.model = new MinesweeperModel();
		this.controller = new MinesweeperController(model);
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
		for (int r = 0; r < 13; r++) {
			for (int c = 0; c < 13; c++) {
				Rectangle square = new Rectangle(44,44);
				square.setFill(Color.DARKGRAY);
				// Create stack pane and set padding and background
				StackPane tile = new StackPane(square);
				tile.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
						BorderWidths.DEFAULT)));
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
