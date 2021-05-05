package model;

import java.io.Serializable;

/**
 * Class representing a tile within minesweeper board. Tiles can contain mines
 * or be empty.
 * @author Gary Li, Daniel S. Lee, Robert Schnell, Merle Crutchfield
 */
public class Tile implements Serializable {

	// Whether a tile is part of the board (used for custom shapes)
	public boolean inBounds;
	// Whether a tile has been uncovered by the user
	public boolean isCovered;
	public boolean isFlagged;
	public boolean hasMine;
	// Integer describing how many mines are nearby
	public Integer displayNum;
	private static final long serialVersionUID = 1L;
	
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
		displayNum = 0;
	}

	/**
	 * Sets whether this tile contains a mine.
	 * 
	 * @param hasMine Boolean indicating presence of mine.
	 */
	public void setHasMine(boolean hasMine) {
		this.hasMine = hasMine;
		displayNum = -1;
	}
}
