package model;

import java.util.Observable;

import controller.MinesweeperController;

public class MinesweeperModel extends Observable{
private static String[][] board;
	
	public MinesweeperModel(MinesweeperController controller) {
		board = controller.getBoardArray();
	}
	
	public void printBoard() {
		for (int i = 1; i <= 8; i++) {
			System.out.println("  ---------------------------------");
			System.out.print(Integer.toString(i) + " | ");
			for (int j = 1; j <= 8; j++) {
				if (board[i-1][j-1] != null) {
					System.out.print(board[i-1][j-1] + " | ");
				} else {
					System.out.print("  | ");
				}
			}
			System.out.println();
		}
		System.out.println("  ---------------------------------");
		System.out.println("    a   b   c   d   e   f   g   h");
	}
}
