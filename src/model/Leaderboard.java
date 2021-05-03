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
		return o1.getScore().compareTo(o2.getScore());
	}
	
}
	
	private ArrayList<Player> leaderboard = new ArrayList<Player>();
	
	public Leaderboard() throws IOException {
		File file = new File("leaderboard/leaderboard.txt");
		if(!file.exists()) {
			file.createNewFile();
		}
		Scanner readFile = new Scanner(file);
		while(readFile.hasNextLine()) {
			String line = readFile.nextLine();
			String [] lineArray = line.split(" ");
			Player newPlayer = new Player(lineArray[0],Integer.parseInt(lineArray[1]));
			leaderboard.add(newPlayer);
		}
		leaderboard.sort(new playerSorter());
		readFile.close();
	}
	
	public String getName(int rank) {
		if(rank > leaderboard.size()) {
			return "";
		}
		return leaderboard.get(rank-1).getName();
	}
	
	public Integer getScore(int rank) {
		if(rank > leaderboard.size()) {
			return 0;
		}
		return leaderboard.get(rank-1).getScore();
	}
	
	public void addScore(String name, int score) throws IOException {
		if(name.isEmpty()) {
			name = "Anon.";
		}
		Player newScore = new Player(name, score);
		leaderboard.add(newScore);
		leaderboard.sort(new playerSorter());
		if(leaderboard.size() > 10)
			leaderboard.remove(leaderboard.size()-1);
		File file = new File("leaderboard/leaderboard.txt");
		System.out.println(file.exists());
		BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
		for(Player players:leaderboard) {
			System.out.println("reach");
			String playerScore = " ";
			if(players.getScore() > 0)
				playerScore = String.valueOf(players.getScore());
			String line = (players.getName() + " " + playerScore);
			System.out.println(line);
			writer.write(line);
			writer.newLine();
		}
		writer.close();
	}
	
}
