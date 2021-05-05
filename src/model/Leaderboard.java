package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

/**
 *  This class is used to read and write to a text file that 
 *  will be used to store the top 10 scores for the minesweeper
 *  game.
 * @author Gary Li, Daniel S. Lee, Robert Schnell, Merle Crutchfield
 */
public class Leaderboard {
	/**
	 * This class creates a player object that will be used
	 * to help store a players name and score into the 
	 * leaderboard.
	 * @author Gary Li, Daniel S. Lee, Robert Schnell, Merle Crutchfield
	 *
	 */
	public class Player {
		private String name;
		private Integer score;
		/**
		 * Constructor to create a player object with a name
		 * and a score
		 * @param name: String that is the players name
		 * @param score: int that is the players time or score
		 */
		public Player(String name, Integer score) {
			this.name = name;
			this.score = score;
		}
		/**
		 * Getter method to return the name from a player.
		 * @return name: String
		 */
		public String getName() {
			return name;
		}
		/**
		 * Getter method to return the score from a player.
		 * @return name: int
		 */
		public Integer getScore() {
			return score;
		}

	}
	
	/**
	 * This class creates a comparator for the player objects.
	 * This allows collection methods to be used with the leaderboard
	 * allowing it to be sorted.
	 * @author Gary Li, Daniel S. Lee, Robert Schnell, Merle Crutchfield
	 *
	 */
	public class playerSorter implements Comparator<Player> {

		@Override
		public int compare(Player o1, Player o2) {
			// TODO Auto-generated method stub
			return o1.getScore().compareTo(o2.getScore());
		}

	}
	
	// Arraylist to store the players on the leaderboard
	private ArrayList<Player> leaderboard = new ArrayList<Player>();
	
	/**
	 * Constructor method that reads in the text file and adds each
	 * player to the arraylist.
	 * @throws IOException
	 */
	public Leaderboard() throws IOException {
		File file = new File("leaderboard/leaderboard.txt");
		// Creates new file if one does not exist
		if(!file.exists()) {
			file.createNewFile();
		}
		// Reads file and adds players to array list
		Scanner readFile = new Scanner(file);
		while(readFile.hasNextLine()) {
			String line = readFile.nextLine();
			String[] lineArray = line.split(" ");
			Player newPlayer = new Player(lineArray[0], Integer.parseInt(lineArray[1]));
			leaderboard.add(newPlayer);
		}
		leaderboard.sort(new playerSorter());
		readFile.close();
	}
	
	/**
	 * Getter method to return the name of a player at a given
	 * rank.
	 * @param rank: integer to specify which rank
	 * @return String that is the name of the player
	 */
	public String getName(int rank) {
		if(rank > leaderboard.size()) {
			return "";
		}
		return leaderboard.get(rank-1).getName();
	}
	
	/**
	 * Getter method to return the score of a player at a given
	 * rank.
	 * @param rank: integer to specify which rank
	 * @return int that is the score of the player
	 */
	public Integer getScore(int rank) {
		if(rank > leaderboard.size()) {
			return 0;
		}
		return leaderboard.get(rank-1).getScore();
	}
	
	/**
	 * This method adds a players score to the text file and 
	 * sorts the players. It will rewrite the entire text file and
	 * maintain only 10 players stored at a time.
	 * @param name: String that is the new players name
	 * @param score: int that is the new players score
	 * @throws IOException
	 */
	public void addScore(String name, int score) throws IOException {
		// If no name was given
		if (name.isEmpty()) {
			name = "Anon.";
		}
		// Create player object
		Player newScore = new Player(name, score);
		// Adds and sorts leaderboard
		leaderboard.add(newScore);
		leaderboard.sort(new playerSorter());
		if(leaderboard.size() > 10)
			leaderboard.remove(leaderboard.size()-1);
		// Writes players to a new file
		File file = new File("leaderboard/leaderboard.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
		for(Player players:leaderboard) {
			String playerScore = " ";
			if(players.getScore() > 0)
				playerScore = String.valueOf(players.getScore());
			String line = (players.getName() + " " + playerScore);
			writer.write(line);
			writer.newLine();
		}
		writer.close();
	}
}
