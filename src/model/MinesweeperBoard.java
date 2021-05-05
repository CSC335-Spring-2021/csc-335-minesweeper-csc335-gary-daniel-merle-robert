package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Scanner;

/**
 * A class representing a board containing tiles for minesweeper. Supports
 * various sizes and custom shapes.
 * 
 * @author Gary Li, Daniel S. Lee, Robert Schnell, Merle Crutchfield
 */
public class MinesweeperBoard implements Serializable {

	private Tile[][] board;
	private int size;
	public double time;
	public int bombCount;
	public String playerName;
	static final long serialVersionUID = 1L;

	/**
	 * Constructs a board given a size.
	 * 
	 * @param size The size (length and height) of the board.
	 */
	public MinesweeperBoard(int size) {
		this.size = size;
		this.board = new Tile[size][size];
		this.bombCount = 20;
		fillBoard();
	}

	/**
	 * Loads a shape into the board given a file containing "o" indicating in bounds
	 * tiles and "_" indicating out of bound tiles, with the first line indicating
	 * how many bombs to have.
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
			// First line: number of bombs
			String line = scanner.nextLine();
			this.bombCount = Integer.parseInt(line);
			// Rest: board
			while (scanner.hasNext()) {
				line = scanner.nextLine();
				String[] row = line.split(" ");
				if (row.length != size) {
					break;
				}
				for (int c = 0; c < size; c++) {
					if (row[c].equals("o")) {
						setBounds(r, c, true);
					} else if (row[c].equals("_")) {
						setBounds(r, c, false);
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

	/**
	 * Saves the current board into a file named "save_game.dat"
	 * 
	 * @param time a double representing the time that the game was stopped
	 * @param name a string representing the name of the player
	 */
	public void saveBoard(double time, String name) {
		this.time = time;
		this.playerName = name;
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream("save_game.dat"));
			oos.writeObject(this);
			oos.close();
		} catch (IOException e) {
			return;
		}
	}
}
