package view;

import javafx.application.*;
import java.util.Observable;
import java.util.Observer;

import controller.MinesweeperController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.MinesweeperModel;
import java.util.Timer;
import java.util.TimerTask;
import java.text.DecimalFormat;

@SuppressWarnings("deprecation")
public class MinesweeperView extends Application implements Observer {
	//Test
	private MinesweeperModel model;
	private MinesweeperController controller;
	
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
		Scene gameScene = new Scene(anchorPane,600,600);
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
		GridPane board = createGameBoard();
		layout.getChildren().add(topBar);
		layout.getChildren().add(board);
		anchorPane.getChildren().add(layout);
		
		return gameScene;
	}

	private GridPane createGameBoard() {
		GridPane board = new GridPane();
		board.setPrefHeight(530.0);
		board.setStyle("-fx-background-color: LIGHTBLUE; -fx-grid-lines-visible: true;");
		board.setPadding(new Insets(0.0,10.0,35.0,35.0));
		for(int i = 0; i < 12;i++) {
			ColumnConstraints con = new ColumnConstraints();
			con.setPrefWidth(530/12);
			con.setHalignment(HPos.CENTER);
			RowConstraints row = new RowConstraints();
			row.setPrefHeight(530/12);
			row.setValignment(VPos.CENTER);
			board.getColumnConstraints().add(con);
			board.getRowConstraints().add(row);
		}
		
		for(int r = 0; r < 13;r++) {
			for(int c = 0; c < 13;c++) {
				Rectangle tile = new Rectangle(44,44);
				tile.setOnMouseClicked(new EventHandler<MouseEvent>() {
					
					@Override
					public void handle(MouseEvent event) {
						if (event.getButton() == MouseButton.PRIMARY)
			            {
							tile.setFill(Color.BLACK);
			            } else if (event.getButton() == MouseButton.SECONDARY)
			            {
			                //fill
			            }
					}
					
				});
				tile.setFill(Color.DARKGRAY);
				board.add(tile, r, c);
			}
		}
		return board;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
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
}
