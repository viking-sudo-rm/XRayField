package snorri.data;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.entity.Player;

import snorri.main.XRayField;

public class DataManager {

	private static HashMap<Player, DataSet> workData;
	private static HashMap<Player, StatTracker> stats;
	
	//save stats
	
	static {
		workData = new HashMap<Player, DataSet>();
		stats = new HashMap<Player, StatTracker>();
	}
	
	public static void addPlayer(Player player) {
		
		XRayField.log(player.getDisplayName() + " is now being tracked");
		
		workData.put(player, new DataSet());
		
		DataSet all = new DataSet();
		double initialWork = 0;
		try {
			all.loadLog(player);
			initialWork = all.get(all.size() - 1);
		} catch (Exception e) { 
			XRayField.log("No mining data has been recorded for " + player.getDisplayName() + " yet");
		}
		
		stats.put(player, new StatTracker(initialWork));
				
	}
	
	public static void removePlayer(Player player) {
		
		try {
			workData.get(player).save(player);
			XRayField.log("Saved mining data for " + player.getDisplayName() + " to disk");
		} catch (IOException e) {
			XRayField.log(e.getStackTrace().toString());
		}
		
		workData.remove(player);
		stats.remove(player);
		
	}
	
	public static void writeToDisk(Player player) {
		
		if (workData.get(player).size() < 1024)
			return;
		
		try {
			workData.get(player).save(player);
			workData.put(player,  new DataSet());
		} catch (IOException e) { }
		
	}
	
	public static void logWork(Player player) {
		workData.get(player).addPoint(stats.get(player).getWork());
	}
	
	public static void updateStats(Player player, double workTerm) {
		stats.get(player).doWork(workTerm);
		stats.get(player).tick();
	}
	
	public static double getPower(Player player) {
		return stats.get(player).getPower();
	}
	
	public static DataSet getAllData(Player player) {
		DataSet data = new DataSet();
		data.load(workData.get(player));
		try {
			data.loadLog(player);
		} catch (IOException e) { }
		return data;
	}
	
}
