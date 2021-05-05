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
	 * Constructs the controller with a model instance.
	 * 
	 * @param model The minesweeper model
	 */
	public MinesweeperController(MinesweeperModel model) {
		this.model = model;
		board = model.getBoard();
	}
	
	/**
	 * A method that prints out the current board. This method's only purpose is for testing.
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
	 * @return a boolean that represents whether the game has been won or not
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
	 * Reveals a tile at the given coordinates
	 * 
	 * @param row A row coordinate
	 * @param col A column coordinate
	 */
	public void revealSpace(int row, int col) {
		model.revealSpace(row, col);
	}

	/**
	 * Flag the tile at the given coordinates
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
	 * Return whether or not it is currently the first move
	 * 
	 * @return a boolean representing whether or not it is the first move
	 */
	public boolean isFirstMove() {
		return model.getFirstMove();
	}

	/**
	 * Returns whether or not a previous save exists
	 * 
	 * @return a boolean representing whether or not a previous save exists
	 */
	public boolean hasSave() {
		return model.getSave();
	}

	/**
	 * Returns whether or not the game has been lost
	 * 
	 * @return a boolean representing whether the game has been lost or not
	 */
	public boolean hasLost() {
		return model.getLost();
	}
}
