package controller;

import model.GameLostException;
import model.MinesweeperBoard;
import model.MinesweeperModel;

public class MinesweeperController {
	private static MinesweeperBoard board;
	private MinesweeperModel model;

	/**
	 * 
	 * @param model
	 */
	public MinesweeperController(MinesweeperModel model) {
		this.model = model;
		board = model.getBoard();
	}

	/**
	 * 
	 */
	public void printBoard() {
		for (int i = 1; i <= 13; i++) {
			System.out.println("  -----------------------------------------------------");
			System.out.print(Integer.toString(i) + " | ");
			for (int j = 1; j <= 13; j++) {
				if (board.getTile(i - 1, j - 1).isCovered) {
					if (board.getTile(i - 1, j - 1).isFlagged)
						System.out.print("f" + " | ");
					else
						System.out.print("x" + " | ");
				} else {
					System.out.print(board.getTile(i - 1, j - 1).displayNum + " | ");
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
	 * @return
	 */
	public boolean isGameOver() {
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
	 * 
	 * @param row
	 * @param col
	 * @throws GameLostException
	 */
	public void revealSpace(int row, int col) throws GameLostException {
		model.revealSpace(row, col);
	}

	/**
	 * 
	 * @param row
	 * @param col
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
	
	public boolean isFirstMove() {
		return model.getFirstMove();
	}
	
	public boolean hasSave() {
		return model.getSave();
	}
	
	public boolean isLost() {
		return model.getLost();
	}
}
