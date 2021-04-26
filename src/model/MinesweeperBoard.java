package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * A class representing a board containing tiles for minesweeper. Supports
 * various sizes and custom shapes.
 */
public class MinesweeperBoard {

	private Tile[][] board;
	private int size;

	/**
	 * Constructs a board given a size.
	 * 
	 * @param size The size (length and height) of the board.
	 */
	public MinesweeperBoard(int size) {
		this.size = size;
		this.board = new Tile[size][size];
		fillBoard();
	}

	/**
	 * Loads a shape into the board given a file containing "o" indicating in bounds
	 * tiles and "_" indicating out of bound tiles.
	 * 
	 * Custom shapes can be given to board via the "inBounds" field of Tile objects,
	 * which indicate whether a Tile is considered part of the board (i.e. clickable
	 * and could contain a mine). This method loads in a custom shape via a file
	 * that contains an "o" or "_" for every Tile within the grid, indicating in
	 * bounds or out of bounds respectively.
	 * 
	 * The contents of the file must match the dimensions of the board. Does nothing
	 * if the file does not exist.
	 * 
	 * @param fn The filename to a shape file
	 */
	public void loadShapeFromFile(String fn) {
		try {
			Scanner scanner = new Scanner(new File(fn));
			int r = 0;
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				String[] row = line.split(" ");
				if (row.length != size) {
					break;
				}
				for (int c = 0; c < size; c++) {
					if (row[c].equals("o")) {
						board[r][c].inBounds = true;
					} else if (row[c].equals("_")) {
						board[r][c].inBounds = false;
					} else {
						break;
					}
				}
				r++;
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the tile at a given coordinate.
	 * 
	 * @param r A row coordinate
	 * @param c A column coordinate
	 * @return A Tile object
	 */
	public Tile getTile(int r, int c) {
		return board[r][c];
	}

	/**
	 * Returns the size of the board.
	 * 
	 * @return Size of board
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Places or removes a mine at a given coordinate in the board.
	 * 
	 * @param r       A row coordinate
	 * @param c       A column coordinate
	 * @param hasMine Whether a mine should be placed (true) or removed (false)
	 */
	public void setMine(int r, int c, boolean hasMine) {
		board[r][c].setHasMine(hasMine);
	}

	/**
	 * Places or removes a flag at a given coordinate
	 * 
	 * @param r    A row coordinate
	 * @param c    A column coordinate
	 * @param flag Whether a flag should be placed.
	 */
	public void setFlagged(int r, int c, boolean flag) {
		board[r][c].isFlagged = flag;
	}

	/**
	 * Reveals a tile, possibly revealing a mine.
	 * 
	 * @param r A row coordinate
	 * @param c A column coordinate
	 */
	public void reveal(int r, int c) {
		board[r][c].isCovered = false;
	}

	/**
	 * Sets a tile as in bounds or out of bounds.
	 * 
	 * @param r        A row coordinate
	 * @param c        A column coordinate
	 * @param inBounds Whether the tile is in bounds (true) or out (false)
	 */
	public void setBounds(int r, int c, boolean inBounds) {
		board[r][c].inBounds = inBounds;
	}

	/**
	 * Sets the number indicating how many mines are adjacent to a tile to display
	 * to the user.
	 * 
	 * @param r          A row coordinate
	 * @param c          A column coordinate
	 * @param displayNum The number of nearby mines to display
	 */
	public void setDisplayNum(int r, int c, Integer displayNum) {
		board[r][c].displayNum = displayNum;
	}

	/**
	 * Returns the number of mines that are in the 8 adjacent squares of a tile.
	 * 
	 * @param r A row coordinate
	 * @param c A column coordinate
	 * @return The number of mines
	 */
	public Integer numMinesNearby(int r, int c) {
		Integer numMines = 0;
		// Check all eight directions
		// Up
		if (inBounds(r - 1, c) && board[r - 1][c].hasMine) {
			numMines++;
		}
		// Down
		if (inBounds(r + 1, c) && board[r + 1][c].hasMine) {
			numMines++;
		}
		// Left
		if (inBounds(r, c - 1) && board[r][c - 1].hasMine) {
			numMines++;
		}
		// Right
		if (inBounds(r, c + 1) && board[r][c + 1].hasMine) {
			numMines++;
		}
		// Up left
		if (inBounds(r - 1, c - 1) && board[r - 1][c - 1].hasMine) {
			numMines++;
		}
		// Up right
		if (inBounds(r - 1, c + 1) && board[r - 1][c + 1].hasMine) {
			numMines++;
		}
		// Down left
		if (inBounds(r + 1, c - 1) && board[r + 1][c - 1].hasMine) {
			numMines++;
		}
		// Down right
		if (inBounds(r + 1, c + 1) && board[r + 1][c + 1].hasMine) {
			numMines++;
		}
		return numMines;
	}

	/**
	 * Returns whether a coordinate pair is in bounds.
	 * 
	 * @param r A row coordinate
	 * @param c A column coordinate
	 * @return Where a coordinate is within the bounds of the board
	 */
	private boolean inBounds(int r, int c) {
		if (r < 0 || r >= size || c < 0 || c >= size) {
			return false;
		}
		return board[r][c].inBounds;
	}

	/**
	 * Fills the board with tiles. All tiles are in bounds by default.
	 */
	private void fillBoard() {
		for (int r = 0; r < size; r++) {
			for (int c = 0; c < size; c++) {
				board[r][c] = new Tile(true);
			}
		}
	}

}
