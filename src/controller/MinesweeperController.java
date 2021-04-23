package controller;

import model.MinesweeperBoard;
import model.MinesweeperModel;

public class MinesweeperController {
	private static MinesweeperBoard board;
	private static MinesweeperModel model;
	
	public MinesweeperController(MinesweeperModel model) {
		this.model = model;
		board = model.getBoardArray();
	}
	
	public void printBoard() {
		for (int i = 1; i <= 13; i++) {
			System.out.println("  -----------------------------------------------------");
			System.out.print(Integer.toString(i) + " | ");
			for (int j = 1; j <= 13; j++) {
				if (board.getTile(i-1, j-1).isCovered) {
					if (board.getTile(i-1, j-1).isFlagged)
						System.out.print("f" + " | ");
					else
						System.out.print("x" + " | ");
				} else {
					System.out.print(board.getTile(i-1, j-1).displayNum + " | ");
				}
			}
			System.out.println();
		}
		System.out.println("  -----------------------------------------------------");
		System.out.println("    a   b   c   d   e   f   g   h   i   j   k   l   m");
	}
	
	public boolean isGameOver() {
		int tiles = 0;
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				if (board.getTile(i, j).isCovered)
					tiles += 1;
			}
		}
		return tiles == model.getMineCount();
	}
	
	public void makeMove(int row, int col, String input) {
		//TODO: Implement
		if(input.equals("p")) {
			System.out.println("Primary Mouse Click");
			model.revealSpace(row, col);
			printBoard();
		}
		else if(input.equals("s")) {
			System.out.println("Right Mouse Click");
			model.flagSpace(row, col);
			printBoard();
		}
	}
}
