package controller;

import model.MinesweeperBoard;
import model.MinesweeperModel;

/**
 * A controller class for the minesweeeper game. Holds a model and board instance 
 * and offers methods to interact with the game model. Supports methods to reveal 
 * a tile, flag a tile, reveal all mines on the board, determine if a save exists,
 * determine if it's the first move, and determine if the game has been won or lost.
 * 
 * @author Gary Li, Daniel S. Lee, Robert Schnell, Merle Crutchfield
 */
public class MinesweeperController {
	
	private static MinesweeperBoard board;
	private MinesweeperModel model;

	/**
	 * Constructs an instance of this class given a model.
	 * 
	 * @param model A MinesweeperModel instance
	 */
	public MinesweeperController(MinesweeperModel model) {
		this.model = model;
		board = model.getBoard();
	}
	
	/**
	 * Prints a string representation of the board. For convenience in testing only.
	 */
	public void printBoard() {
		for (int i = 1; i <= 13; i++) {
			System.out.println("  -----------------------------------------------------");
			System.out.print(Integer.toString(i) + " | ");
			for (int j = 1; j <= 13; j++) {
				if (!board.getTile(i - 1, j - 1).inBounds) {
					System.out.print("-" + " | ");
				} else if (board.getTile(i - 1, j - 1).hasMine) {
					System.out.print("x" + " | ");
				} else if (board.getTile(i - 1, j - 1).displayNum != 0) {
					System.out.print(board.getTile(i - 1, j - 1).displayNum + " | ");
				} else {
					System.out.print("o" + " | ");
				}
			}
			System.out.println();
		}
		System.out.println("  -----------------------------------------------------");
		System.out.println("    a   b   c   d   e   f   g   h   i   j   k   l   m");
	}

	/**
	 * Returns whether the game has been won.
	 * 
	 * @return True if the game has been won, false otherwise
	 */
	public boolean hasWon() {
		int tiles = 0;
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				if (board.getTile(i, j).isCovered && board.getTile(i, j).inBounds) {
					tiles += 1;
				}
			}
		}
		return tiles == model.getMineCount();
	}

	/**
	 * Calls the model's revealSpace method, which uses recursion to cave out the
	 * area of blank spaces done by a click. It checks to make sure that it is in
	 * range, and if not then it does nothing. It then checks if there is a bomb,
	 * which prompts the end of the game. Next, if it is a covered spot and there
	 * are 0 bombs nearby it clears itself and recursively calls again. The last
	 * condition is if it touches a bomb, which is will just reveal the spot.
	 * 
	 * @param row A row coordinate
	 * @param col A column coordinate
	 */
	public void revealSpace(int row, int col) {
		model.revealSpace(row, col);
	}

	/**
	 * Flags a space on the board (removes if a flag existed).
	 * 
	 * @param row A row coordinate
	 * @param col A column coordinate
	 */
	public void flagSpace(int row, int col) {
		model.flagSpace(row, col);
	}

	/**
	 * Reveals all mines in the board. This method is called when the user clicks a
	 * mine.
	 */
	public void revealMines() {
		model.revealMines();
	}

	/**
	 * Returns whether the next move will be the user's first move.
	 * 
	 * @return True if the user has not placed a move, false otherwise
	 */
	public boolean isFirstMove() {
		return model.getFirstMove();
	}

	/**
	 * Returns whether the model has been loaded from a previously saved board.
	 * 
	 * @return whether the model is loaded
	 */
	public boolean hasSave() {
		return model.getSave();
	}

	/**
	 * Returns whether the game has been lost.
	 * 
	 * @return whether the game has been lost
	 */
	public boolean hasLost() {
		return model.getLost();
	}
}
