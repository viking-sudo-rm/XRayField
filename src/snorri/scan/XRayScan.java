package snorri.scan;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import snorri.data.DataManager;
import snorri.data.DataSet;
import snorri.main.XRayField;
import snorri.main.XRaySettings;

public class XRayScan {
	
	//TODO: make this the standard for all commands

	private String flags;
	
	public XRayScan(String flags) {
		this.flags = flags;
	}
	
	//TODO: would be hard do use make truly iterable
	public Iterable<OfflinePlayer> getAllPlayers() {
		
		Set<Player> playerSet = DataManager.getOnlinePlayers();
		//ArrayList<OfflinePlayer> onlinePlayers = new ArrayList<OfflinePlayer>();
		//for (Player p : playerSet)
		//	onlinePlayers.add((OfflinePlayer) p);
		
		if (flags.contains("s"))
			return new ArrayList<OfflinePlayer>(playerSet);
		
		ArrayList<OfflinePlayer> offlinePlayers = new ArrayList<OfflinePlayer>();
		File folder = new File(XRayField.getFolder().getAbsolutePath() + "/log");
		File[] files = folder.listFiles();
				
		for (File file : files) {
			String stringID = file.getName();
			stringID = stringID.substring(0,stringID.lastIndexOf('.'));
			offlinePlayers.add(Bukkit.getOfflinePlayer(UUID.fromString(stringID)));
		}
		
		if (! flags.contains("a"))
			offlinePlayers.addAll(playerSet);
				
		return offlinePlayers;
	}
		
	@Deprecated
	public DataSet getData(OfflinePlayer player, String flags) {
		if (flags.contains("a")) {
			return DataManager.getOfflineData(player);
		}
		if (flags.contains("s")) {
			return DataManager.getCurrentSession((Player) player);
		}
		return DataManager.getAllData(player);
	}
	
	public DataSet getData(OfflinePlayer player) {
		if (flags.contains("a")) {
			return DataManager.getOfflineData(player);
		}
		if (flags.contains("s")) {
			return DataManager.getCurrentSession((Player) player);
		}
		return DataManager.getAllData(player);
	}
	
	public ArrayList<ScanResult> checkPlayers() {
		ArrayList<ScanResult> result = new ArrayList<ScanResult>();
		for (OfflinePlayer player : getAllPlayers()) {
			result.add(checkPlayer(player));
		}
		Collections.sort(result);
		return result;
	}
	
	public ArrayList<ScanResult> checkPlayers(double nullHypothesis) {
		ArrayList<ScanResult> result = new ArrayList<ScanResult>();
		for (OfflinePlayer player : getAllPlayers()) {
			result.add(checkPlayer(player, nullHypothesis));
		}
		Collections.sort(result);
		return result;
	}
	
	public ScanResult checkPlayer(OfflinePlayer player) {
		DataSet data = getData(player);
		DataSet nullHypo = XRaySettings.getTrustedData();
		return new ScanResult(player, data.tTest(nullHypo));
	}
	
	public ScanResult checkPlayer(OfflinePlayer player, OfflinePlayer otherPlayer) {
		DataSet data = getData(player);
		DataSet nullHypo = getData(otherPlayer);
		return new ScanResult(player, data.tTest(nullHypo));
	}
	
	public ScanResult checkPlayer(OfflinePlayer player, double nullHypo) {
		DataSet data = getData(player);
		return new ScanResult(player, data.tTest(nullHypo));
	}
	
}
