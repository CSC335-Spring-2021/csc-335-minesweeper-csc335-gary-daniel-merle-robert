package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Leaderboard {
	public class Player {
		private String name;
		private Integer score;
		public Player(String name, Integer score) {
			this.name = name;
			this.score = score;
		}
		public String getName() {
			return name;
		}
		public Integer getScore() {
			return score;
		}
		
	}
public class playerSorter implements Comparator<Player> {

	@Override
	public int compare(Player o1, Player o2) {
		// TODO Auto-generated method stub
		return o2.getScore().compareTo(o1.getScore());
	}
	
}
	
	private ArrayList<Player> leaderboard = new ArrayList<Player>();
	
	public Leaderboard() throws FileNotFoundException {
		Scanner readFile = new Scanner(new File("leaderboard/leaderboard"));
		while(readFile.hasNextLine()) {
			String line = readFile.nextLine();
			String [] lineArray = line.split(" ");
			Player newPlayer = new Player(lineArray[0],Integer.parseInt(lineArray[1]));
			leaderboard.add(newPlayer);
		}
		leaderboard.sort(new playerSorter());
	}
	
	public String getName(int rank) {
		return leaderboard.get(rank-1).getName();
	}
	
	public int getScore(int rank) {
		return leaderboard.get(rank-1).getScore();
	}
	
}
