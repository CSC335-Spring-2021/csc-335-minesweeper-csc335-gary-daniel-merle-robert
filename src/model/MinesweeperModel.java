package model;

import java.util.Observable;
import java.util.Random;

public class MinesweeperModel extends Observable {
	private int bombCount = 20;
	private MinesweeperBoard board;
	private boolean firstMove;

	/**
	 * Constructs the Minesweeper model, initializes the bomb locations and checks
	 * how many mines are nearby. Hardcodes a 13 by 13 board of tiles.
	 */
	public MinesweeperModel() {
		board = new MinesweeperBoard(13);
		firstMove = true;
		this.bombCount = 20;
		setBombs(this.bombCount);
	}

	/**
	 * Sets the bombs to random spots on the board. Uses logic to ensure that the
	 * correct number of bombs are placed and that they are all at different
	 * locations.
	 * 
	 * @param bombCount
	 */
	public void setBombs(int bombCount) {
		Random rand = new Random();
		// Starting Positions
		while (bombCount > 0) {
			int x = rand.nextInt(13);
			int y = rand.nextInt(13);
			if (!board.getTile(x, y).inBounds) {
				continue;
			}
			if (board.getTile(x, y).hasMine) {
				continue;
			}
			board.setMine(x, y, true);
			//System.out.println(x + " " + y);
			bombCount -= 1;
		}
	}

	/**
	 * Returns the board Array
	 * 
	 * @return Board Array
	 */
	public MinesweeperBoard getBoard() {
		return board;
	}

	/**
	 * This method uses recursion to cave out the area of blank spaces done by a
	 * click. It checks to make sure that it is in range, and if not then it does
	 * nothing. It then checks if there is a bomb, which prompts the end of the
	 * game. Next, if it is a covered spot and there are 0 bombs nearby it clears
	 * itself and recursively calls again. The last condition is if it touches a
	 * bomb, which is will just reveal the spot.
	 * 
	 * @param x A row coordinate.
	 * @param y A column coordinate.
	 * @throws GameLostException If a mine is revealed
	 */
	public void revealSpace(int x, int y) throws GameLostException {
		// If first move and mine is revealed, moves it to a different spot
		if (firstMove) {
			//System.out.println("First move");
			firstMove = false;
			if (board.getTile(x, y).hasMine) {
				setBombs(1);
				board.setMine(x, y, false);
			}
			// Set display numbers for all tiles on first move
			for (int i = 0; i < 13; i++) {
				for (int j = 0; j < 13; j++) {
					board.setDisplayNum(i, j, board.numMinesNearby(i, j));
				}
			}
		}
		// Do nothing if index out of bounds
		if (x < 0 || y < 0 || x >= board.getSize() || y >= board.getSize()) {
			return;
		}
		// Do nothing is tile is out of bounds
		if (!board.getTile(x, y).inBounds) {
			return;
		}
		// If mine is revealed, throw GameLostException
		else if (board.getTile(x, y).hasMine) {
			board.reveal(x, y);
			notifyView();
			throw new GameLostException("Game lost");
		}
		// Otherwise, recursively dig out neighbors
		else if (board.numMinesNearby(x, y) == 0 && board.getTile(x, y).isCovered) {
			board.reveal(x, y);
			notifyView();
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
			notifyView();
		}
	}

	/**
	 * Notifies any observers that the model has changed.
	 */
	public void notifyView() {
		setChanged();
		notifyObservers(board);
	}

	/**
	 * This method flags the spot on the board when the user chooses to.
	 * 
	 * @param row A row coordinate.
	 * @param col A column coordinate.
	 */
	public void flagSpace(int row, int col) {
		board.getTile(row, col).isFlagged = true;
		notifyView();
	}

	/**
	 * Reveals all mines in the board. This method is called when the user clicks a
	 * mine. TODO: Implement this method (not sure how we wanna do this)
	 */
	public void revealMines() {
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				if (board.getTile(i, j).isCovered && board.getTile(i, j).hasMine) {
					board.reveal(i, j);
					notifyView();
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
