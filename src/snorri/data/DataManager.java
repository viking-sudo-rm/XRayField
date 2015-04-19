package snorri.data;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.entity.Player;

import snorri.main.XRayField;

public class DataManager {

	private static HashMap<Player, DataSet> players;
	
	static {
		players = new HashMap<Player, DataSet>();
	}
	
	public static void addPlayer(Player player) {
		
		XRayField.log("Player " + player.getDisplayName() + " is now being tracked");
		
		players.put(player, new DataSet());
				
	}
	
	public static void removePlayer(Player player) {
		
		try {
			players.get(player).save(player);
			XRayField.log("finished DataManager save");
		} catch (IOException e) {
			XRayField.log(e.getStackTrace().toString());
		}
		
		players.remove(player);
		
	}
	
	public static void log(Player player, Double val) {
		players.get(player).addPoint(val);
	}
	
	public static DataSet getAllData(Player player) {
		DataSet data = new DataSet();
		data.load(players.get(player));
		try {
			data.loadLog(player);
		} catch (IOException e) { }
		return data;
	}
	
}
