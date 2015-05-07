package snorri.data;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import snorri.main.XRayField;

public class DataManager {

	private static HashMap<Player, DataSet> workData;
	//private static HashMap<Player, StatTracker> stats;
		
	static {
		workData = new HashMap<Player, DataSet>();
		//stats = new HashMap<Player, StatTracker>();
	}
	
	public static void addPlayer(Player player) {
		
		XRayField.log(player.getDisplayName() + " is now being tracked");
		
		workData.put(player, new DataSet());
		//stats.put(player, new StatTracker());
		
		/*double initialWork = 0;
		try {
			DataSet all = new DataSet();
			all.loadLog(player);
			initialWork = all.get(all.size() - 1);
			stats.get(player).setWork(initialWork);
		} catch (Exception e) { 
			XRayField.log("No mining data has been recorded for " + player.getDisplayName() + " yet");
		}*/
						
	}
	
	public static void removePlayer(Player player) {
		
		try {
			workData.get(player).save(player);
			XRayField.log("Saved mining data for " + player.getDisplayName() + " to disk");
		} catch (IOException e) {
			XRayField.log(e.getStackTrace().toString());
		}
		
		workData.remove(player);
		//stats.remove(player);
		
	}
	
	public static void writeToDisk(Player player) {
		
		//TODO: test if getting rid of this fixes the ratio problem
		
		if (workData.get(player).size() < 1024)
			return;
		
		try {
			workData.get(player).save(player);
			workData.put(player,  new DataSet());
		} catch (IOException e) { }
		
	}
	
	public static void updateStats(Player player, double workTerm) {
		//stats.get(player).doWork(workTerm);
		//stats.get(player).tick();
		workData.get(player).addPoint(workTerm);
	}
	
	/*public static double getPower(Player player) {
		return stats.get(player).getPower();
	}
	
	public static int getTime(Player player) {
		return stats.get(player).getTime();
	}*/
	
	public static DataSet getCurrentSession(Player player) {
		return workData.get(player);
	}
	
	public static DataSet getOfflineData(OfflinePlayer player) {
		DataSet data = new DataSet();
		try {
			data.loadLog(player);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public static DataSet getAllData(Player player) {
		DataSet data = new DataSet();
		if (workData.keySet().contains(player))
			data.load(getCurrentSession(player));
		try {
			data.loadLog(player);
		} catch (IOException e) { }
		return data;
	}
	
}
