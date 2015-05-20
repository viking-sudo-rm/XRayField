package snorri.main;

import java.util.ArrayList;
import java.util.List;
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
			result.load(DataManager.getAllData(player));
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
		List<String> trustedIds = getTrustedIds();
		trustedIds.add(repr);
		config.set("trusted", trustedIds);
	}
	
	public static void revokeTrusted(OfflinePlayer player) {
		List<String> trustedIds = getTrustedIds();
		for (int i = 0; i < trustedIds.size(); i++) {
			if (trustedIds.get(i).equals(player.getUniqueId().toString())) {
				trustedIds.remove(i);
				i--;
			}
		}
		config.set("trusted", trustedIds);
	}

	public static ArrayList<String> getTrustedNames() {
		
		//SIGH: if only java had generators :/
		//implementing an iterator here would be too tedious
		
		ArrayList<String> result = new ArrayList<String>();
		for (String id : getTrustedIds()) {
			result.add(XRayField.getPlayer(UUID.fromString(id)).getName());
		}
		
		return result;
	}
		
}
