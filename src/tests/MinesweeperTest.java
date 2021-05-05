package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.junit.jupiter.api.Test;

import controller.MinesweeperController;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Leaderboard;
import model.MinesweeperBoard;
import model.MinesweeperModel;

/**
 * A test class for the methods in the controller, model, board, tile,
 * and leaderboard class.
 * 
 * @author Gary Li, Daniel S. Lee, Robert Schnell, Merle Crutchfield
 */
public class MinesweeperTest {

	/*
	 * Tests and achieves 90% statement coverage on the following classes:
	 * MinesweeperController, MinesweeperModel, MinesweeperBoard, and Tile.
	 */
	@Test
	void testGeneral() {

		MinesweeperModel model = new MinesweeperModel();
		MinesweeperController controller = new MinesweeperController(model);
		MinesweeperBoard board = model.getBoard();
		// Test isFirstMove, hasWon, hasLost, flagSpace
		assertTrue(controller.isFirstMove());
		assertFalse(controller.hasWon());
		assertFalse(controller.hasLost());
		controller.flagSpace(0, 0);
		assertTrue(board.getTile(0, 0).isFlagged);
		controller.flagSpace(0, 0);
		assertFalse(board.getTile(0, 0).isFlagged);
		// Set bombs
		model.setSeed((long) 10);
		controller.revealSpace(1, 2);
		assertFalse(board.getTile(6, 3).isCovered || board.getTile(1, 0).isCovered);
		assertEquals((Integer) board.getTile(1, 0).displayNum, (Integer) 1);
		controller.printBoard();
		// Save and load game
		model.saveGame(999, "Daniel-Test");
		File save = new File("save_game.dat");
		try {
			ObjectInputStream oos = new ObjectInputStream(new FileInputStream(save));
			board = (MinesweeperBoard) oos.readObject();
			oos.close();
			model = new MinesweeperModel(board);
			controller = new MinesweeperController(model);

		} catch (IOException | ClassNotFoundException e) {
			Alert alert = new Alert(AlertType.INFORMATION, "Save file not found!");
			alert.showAndWait();
		}
		assertTrue(model.getTime() == 999.0);
		assertTrue(controller.hasSave());
		assertTrue(model.getName().equals("Daniel-Test"));
		// Click on mine (lose game)
		controller.revealSpace(2, 0);
		assertTrue(controller.hasLost());
		controller.revealMines();
		assertFalse(board.getTile(11, 1).isCovered);
	}

	/*
	 * Tests and achieves 90% statement coverage on the following classes:
	 * MinesweeperController, MinesweeperModel, MinesweeperBoard, and Tile.
	 */
	@Test
	void testModelShape() {
		MinesweeperModel model = new MinesweeperModel("triangle");
		MinesweeperController controller = new MinesweeperController(model);
		MinesweeperBoard board = model.getBoard();
		assertFalse(board.getTile(0, 0).inBounds);
		assertEquals(board.getSize(), 13);
		// controller.printBoard();
	}
	/*
	 * This test recieves over 90% however it is required that the original leadboard
	 * text file be deleted or removed before and after the test. 
	 */
	@Test
	void testLeaderboard() throws IOException {
		Leaderboard board = new Leaderboard();
		board.addScore("Testcase", 1);
		assertEquals((int)board.getScore(1), 1);
		assertEquals(board.getName(1),"Testcase");
		assertEquals(board.getName(99),"");
		assertEquals((int)board.getScore(99),0);
		board.addScore("", 999);
		Leaderboard board2 = new Leaderboard();
		for(int i = 0;i<15;i++) {
			board2.addScore("test",(int)Math.random()* 100 + 1);
		}
	}

}
