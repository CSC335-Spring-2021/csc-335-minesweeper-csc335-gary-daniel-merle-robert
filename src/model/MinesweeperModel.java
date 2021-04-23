package model;

import java.util.Observable;
import java.util.Random;

public class MinesweeperModel extends Observable {
	private int bombCount = 20;
	private MinesweeperBoard board;

	/**
	 * Constructs the Minesweeper model, initializes the bomb
	 * locations and checks how many mines are nearby.
	 */
	public MinesweeperModel() {
		board = new MinesweeperBoard(13);
		setBombs();
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				board.setDisplayNum(i, j, board.numMinesNearby(i, j));
			}
		}
	}
	
	/**
	 * Sets the bombs to random spots on the
	 * board. Uses logic to ensure that the
	 * correct ammount of bombs are placed
	 * and that they are all at different locations.
	 */
	public void setBombs() {
		Random rand = new Random();
		// Starting Positions
		while (bombCount > 0) {
			int x = rand.nextInt(13);
			int y = rand.nextInt(13);
			if (board.getTile(x, y).hasMine)
				continue;
			board.setMine(x, y, true);
			System.out.println(x + " " + y);
			bombCount -= 1;
		}
	}
	
	/**
	 * Returns the board Array
	 * 
	 * @return Board Array
	 */
	public MinesweeperBoard getBoardArray() {
		return board;
	}
	
	/**
	 * This method is called only when it is the
	 * users first input. This is because the spot
	 * that they select must not have a mine, so this
	 * method checks to see if it does, and if it 
	 * does it moves it to a different spot.
	 * 
	 * @param x A row coordinate.
	 * @param y A column coordinate.
	 */
	public void isFirstMove(int x, int y) {
		if (board.getTile(x, y).hasMine) {
			board.setMine(x, y, false);
			Random rand = new Random();
			int count = 1;
			while (count > 0) {
				int i = rand.nextInt(13);
				int j = rand.nextInt(13);
				if (board.getTile(i, j).hasMine)
					continue;
				board.setMine(i, j, true);
				System.out.println(i + " " + j);
				count -= 1;
			}
		}
		revealSpace(x, y);
	}
	
	/**
	 * This method uses recursion to cave out the area
	 * of blank spaces done by a click. It checks to make
	 * sure that it is in range, and if not then it does
	 * nothing. It then checks if there is a bomb, which
	 * prompts the end of the game. Next, if it is a covered
	 * spot and there are 0 bombs nearby it clears itself
	 * and recursively calls again. The last condition is if
	 * it touches a bomb, which is will just reveal the spot.
	 * 
	 * @param x A row coordinate.
	 * @param y A column coordinate.
	 */
	public void revealSpace(int x, int y) {
		if (x < 0 || y < 0 || x >= board.getSize() || y >= board.getSize()) {
			return;
		} else if (board.getTile(x, y).hasMine) {
			board.reveal(x, y);
			gameLost();
		} else if (board.numMinesNearby(x, y) == 0 && board.getTile(x, y).isCovered) {
			board.reveal(x, y);
			revealSpace(x + 1, y);
			revealSpace(x - 1, y);
			revealSpace(x, y + 1);
			revealSpace(x, y - 1);
			revealSpace(x + 1, y + 1);
			revealSpace(x - 1, y + 1);
			revealSpace(x + 1, y - 1);
			revealSpace(x - 1, y - 1);
		} else {
			board.reveal(x, y);
		}
	}
	
	/**
	 * This method flags the spot on the board
	 * when the user chooses to.
	 * 
	 * @param row A row coordinate.
	 * @param col A column coordinate.
	 */
	public void flagSpace(int row, int col) {
		board.getTile(row, col).isFlagged = true;
	}
	
	/**
	 * This method is called when the user clicks
	 * a mine.
	 * TODO: Implement this method (not sure how we wanna do this)
	 */
	public void gameLost() {
		System.out.println("You lose");
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				if (board.getTile(i, j).isCovered && board.getTile(i, j).hasMine) {
					board.reveal(i, j);
				}
			}
		}
	}
	
	/**
	 * This method returns the number of mines on the board.
	 * 
	 * @return bombCount Total number of mines.
	 */
	public int getMineCount() {
		return bombCount;
	}

	
}
