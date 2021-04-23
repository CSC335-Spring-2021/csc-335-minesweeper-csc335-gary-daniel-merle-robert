package model;

/**
 * Exception indicating that the game has been lost (i.e. a mine has been
 * revealed).
 */
public class GameLostException extends Exception {

	/**
	 * Contructs an instance of this class given an error message.
	 * 
	 * @param message an error message
	 */
	public GameLostException(String message) {
		super(message);
	}

}
