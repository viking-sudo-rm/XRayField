package snorri.scan;

import org.bukkit.OfflinePlayer;

public class ScanResult implements Comparable<ScanResult> {

	public OfflinePlayer player;
	public double pvalue;
	
	public ScanResult(OfflinePlayer player, double pvalue) {
		this.player = player;
		this.pvalue = pvalue;
	}

	@Override
	public int compareTo(ScanResult other) {
		return (pvalue - other.pvalue > 0) ? 1 : -1;
	}
	
}
