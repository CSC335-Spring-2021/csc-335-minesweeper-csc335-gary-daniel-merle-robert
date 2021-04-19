import javafx.application.Application;
import view.MinesweeperView;

public class Minesweeper {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args[0].equals("-text")) {
			MinesweeperView.main(args);
		} else if(args[0].equals("-window")) { // Launches GUI
			Application.launch(MinesweeperView.class);
		} else {
			System.exit(0); // exits application if other args
		}
	}
}
