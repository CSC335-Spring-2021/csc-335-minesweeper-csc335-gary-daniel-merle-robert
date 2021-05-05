import javafx.application.Application;
import view.MinesweeperView;

/**
 * Creates a GUI based minesweeper game.
 * 
 * @author Gary Li, Daniel S. Lee, Robert Schnell, Merle Crutchfield
 */
public class Minesweeper {
	
	/**
	 * Launches the Minesweeper game
	 * 
	 * @param args None required.
	 */
	public static void main(String[] args) {
		Application.launch(MinesweeperView.class);
	}
}