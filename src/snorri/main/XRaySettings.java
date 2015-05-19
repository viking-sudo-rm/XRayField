package snorri.main;

import java.util.List;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import snorri.data.DataManager;
import snorri.data.DataMode;
import snorri.data.DataSet;

public class XRaySettings {

	private static FileConfiguration config;

	public static DataSet getTrustedData() {
		DataSet result = new DataSet();
		result.setMode(DataMode.ALL_TIME);
		for (String id : getTrustedIds()) {
			OfflinePlayer player = XRayField.getPlayer(UUID.fromString(id));
			try {
				result.loadLog(player);
			} catch (IOException e) { }
			if (player.isOnline())
				result.load(DataManager.getCurrentSession(player.getPlayer()));
		}
		return result;
	}
	
	private static List<String> getTrustedIds() {
		return config.getStringList("trusted");
	}
	
	public static void setConfig(FileConfiguration c) {
		config = c;
		//XRayField.log("" + getTrustedIds().size());
	}
	
	public static boolean isTrusted(OfflinePlayer player) {
		return getTrustedIds().contains(player.getUniqueId().toString());
	}
	
	public static void addTrusted(OfflinePlayer player) {
		String repr = player.getUniqueId().toString();
		List<String> players = getTrustedIds();
		players.add(repr);
		config.set("trusted", players);
	}
		
}