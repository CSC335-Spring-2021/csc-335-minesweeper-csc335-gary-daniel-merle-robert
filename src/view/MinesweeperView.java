package view;

import javafx.application.*;
import java.util.Observable;
import java.util.Observer;

import controller.MinesweeperController;
import javafx.application.Application;
import javafx.stage.Stage;
import model.MinesweeperModel;

public class MinesweeperView extends Application implements Observer {

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
	}
	
	public static void main(String[] args) {
		MinesweeperModel model = new MinesweeperModel();
		MinesweeperController controller = new MinesweeperController(model);
		controller.printBoard();
	}
	
}
