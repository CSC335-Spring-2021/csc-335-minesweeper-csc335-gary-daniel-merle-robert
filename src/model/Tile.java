package model;

/**
 * Class representing a tile within minesweeper board. Tiles can contain mines
 * or be empty.
 */
public class Tile {

	// Whether a tile is part of the board (used for custom shapes)
	public boolean inBounds;
	// Whether a tile has been uncovered by the user
	public boolean isCovered;
	public boolean isFlagged;
	public boolean hasMine;
	// Integer describing how many mines are nearby
	public Integer displayNum;

	/**
	 * Constructs an instance of this class given inputs representing whether the
	 * tile is in bounds (part of the board).
	 * 
	 * @param inBounds Whether the mine is in bounds
	 */
	public Tile(boolean inBounds) {
		this.inBounds = inBounds;
		isCovered = true;
		isFlagged = false;
		hasMine = false;
		displayNum = null;
	}

	/**
	 * Sets whether this tile contains a mine.
	 * 
	 * @param hasMine Boolean indicating presence of mine.
	 */
	public void setHasMine(boolean hasMine) {
		this.hasMine = hasMine;
	}
}
