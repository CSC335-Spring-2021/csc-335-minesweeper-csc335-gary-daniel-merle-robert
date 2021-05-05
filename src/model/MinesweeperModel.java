package model;

import java.util.Observable;
import java.util.Random;

/**
 * A model to represent a minesweeper game. A model contains a MinesweeperBoard
 * class containing the underlying tiles, and enforces game logic by setting
 * bombs and "digging" tiles (by revealing the space and relevant neighbors).
 * 
 * @author Gary Li, Daniel S. Lee, Robert Schnell, Merle Crutchfield
 */
public class MinesweeperModel extends Observable {
	private MinesweeperBoard board;
	private boolean firstMove;
	private boolean save;
	private boolean gameLost;
	private Random rand;

	/**
	 * Constructs the Minesweeper model, initializes the bomb locations and checks
	 * how many mines are nearby. Hardcodes a 13 by 13 board of tiles.
	 */
	public MinesweeperModel() {
		board = new MinesweeperBoard(13);
		firstMove = true;
		save = false;
		gameLost = false;
		rand = new Random();
		// setBombs(board.bombCount);
	}

	/**
	 * Constructs the Minesweeper model given a custom shape. The shape must refer
	 * to the name of a text file (excluding ".txt") in the ./shapes/ folder. See
	 * MinesweeperBoard.loadShapeFromFile() for more details.
	 * 
	 * @param shape A string referring to a file containing a custom shape for the
	 *              board.
	 */
	public MinesweeperModel(String shape) {
		board = new MinesweeperBoard(13);
		board.loadShapeFromFile("shapes/" + shape + ".txt");
		firstMove = true;
		save = false;
		gameLost = false;
		rand = new Random();
		// setBombs(board.bombCount);
	}

	/**
	 * Constructs a model given a previously constructed minesweeper board.
	 * 
	 * @param board A MinesweeperBoard instance
	 */
	public MinesweeperModel(MinesweeperBoard board) {
		this.board = board;
		firstMove = false;
		save = true;
	}

	/**
	 * Sets the bombs to random spots on the board. Uses logic to ensure that the
	 * correct number of bombs are placed and that they are all at different
	 * locations.
	 * 
	 * @param bombCount The number of bombs to place
	 */
	public void setBombs(int bombCount) {
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
			// System.out.println(x + " " + y);
			bombCount -= 1;
		}
	}

	/**
	 * Sets the seed of the random number generator for bombs. Used for testing.
	 * 
	 * @param seed A seed
	 */
	public void setSeed(long seed) {
		rand.setSeed(seed);
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
	 */
	public void revealSpace(int x, int y) {
		revealSpaceHelper(x, y);
		notifyView();
	}

	/**
	 * This method sets a square of bombs around a location to either be true or
	 * false. This is so that it ensures that the users first input is always not a
	 * number but instead clears out space.
	 * 
	 * @param x   A row coordinate.
	 * @param y   A column coordinate.
	 * @param ans Boolean for setting bomb.
	 */
	public void bombSquare(int x, int y, boolean ans) {
		board.getTile(x, y).setHasMine(ans);
		if (x > 0) {
			board.getTile(x - 1, y).setHasMine(ans);
			if (y < board.getSize() - 1)
				board.getTile(x - 1, y + 1).setHasMine(ans);
			if (y > 0)
				board.getTile(x - 1, y - 1).setHasMine(ans);
		}
		if (y > 0) {
			board.getTile(x, y - 1).setHasMine(ans);
			if (x < board.getSize() - 1)
				board.getTile(x + 1, y - 1).setHasMine(ans);
		}
		if (x < board.getSize() - 1)
			board.getTile(x + 1, y).setHasMine(ans);
		if (y < board.getSize() - 1)
			board.getTile(x, y + 1).setHasMine(ans);
		if (x < board.getSize() - 1 && y < board.getSize() - 1)
			board.getTile(x + 1, y + 1).setHasMine(ans);
	}

	/**
	 * Private inner class that implements revealSpace without notifying observers
	 * (runtime optimization).
	 * 
	 * @param x A row coordinate.
	 * @param y A column coordinate.
	 */
	public void revealSpaceHelper(int x, int y) {
		// If first move and mine is revealed, moves it to a different spot
		if (firstMove) {
			// System.out.println("First move");
			firstMove = false;
			bombSquare(x, y, true);
			setBombs(board.bombCount);
			bombSquare(x, y, false);
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
		if (board.getTile(x, y).isFlagged) {
			return;
		}
		// If mine is revealed
		else if (board.getTile(x, y).hasMine) {
			board.reveal(x, y);
			gameLost = true;
		}
		// Otherwise, recursively dig out neighbors
		else if (board.numMinesNearby(x, y) == 0 && board.getTile(x, y).isCovered) {
			board.getTile(x, y).isCovered = true;
			board.reveal(x, y);
			revealSpaceHelper(x + 1, y);
			revealSpaceHelper(x - 1, y);
			revealSpaceHelper(x, y + 1);
			revealSpaceHelper(x, y - 1);
			revealSpaceHelper(x + 1, y + 1);
			revealSpaceHelper(x - 1, y + 1);
			revealSpaceHelper(x + 1, y - 1);
			revealSpaceHelper(x - 1, y - 1);
		} else {
			board.getTile(x, y).isCovered = true;
			board.reveal(x, y);
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
		if (board.getTile(row, col).isFlagged) {
			board.getTile(row, col).isFlagged = false;
		} else {
			board.getTile(row, col).isFlagged = true;
		}
		notifyView();
	}

	/**
	 * Reveals all mines in the board. This method can be called when the user
	 * clicks a mine.
	 */
	public void revealMines() {
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				if (board.getTile(i, j).isCovered && board.getTile(i, j).hasMine) {
					board.reveal(i, j);
				}
			}
		}
		notifyView();
	}

	/**
	 * Returns the number of mines on the board.
	 * 
	 * @return bombCount Total number of mines.
	 */
	public int getMineCount() {
		return board.bombCount;
	}

	/**
	 * Returns whether the next move will be the user's first move.
	 * 
	 * @return True if the user has not placed a move, false otherwise
	 */
	public boolean getFirstMove() {
		return firstMove;
	}

	/**
	 * Saves the game by serializing the board into a file named "save_game.dat"
	 */
	public void saveGame(double time, String name) {
		board.saveBoard(time, name);
	}

	/**
	 * Returns whether the model has been loaded from a previously saved board.
	 * 
	 * @return whether the model is loaded
	 */
	public boolean getSave() {
		return save;
	}

	/**
	 * If the game has been previously saved, returns the time of the game when the
	 * board was saved.
	 * 
	 * @return The previous time
	 */
	public double getTime() {
		return board.time;
	}

	/**
	 * Returns whether the game has been lost.
	 * 
	 * @return whether the game has been lost
	 */
	public boolean getLost() {
		return gameLost;
	}

	/**
	 * If the game has been previously saved, returns the name of the player
	 * associated with the loaded board.
	 * 
	 * @return The name of the player
	 */
	public String getName() {
		return board.playerName;
	}
}
