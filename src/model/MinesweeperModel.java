package model;

import java.util.Observable;
import java.util.Random;


public class MinesweeperModel extends Observable{
	private final String BOMB = "x";
	private final String EMPTY = " ";
	private String [][] boardArray = new String[8][8];
	private int bombCount = 10;
	
	public MinesweeperModel() {
		Random rand = new Random();
		// Starting Positions
		while (bombCount > 0) {
			int x = rand.nextInt(8);
			int y = rand.nextInt(8);
			if (boardArray[x][y] != null)
				continue;
			boardArray[x][y] = BOMB;
			// System.out.println(x + " " + y);
			bombCount -= 1;
		}
	}
	
	public String[][] getBoardArray() {
		return boardArray;
	}
	
	
}
