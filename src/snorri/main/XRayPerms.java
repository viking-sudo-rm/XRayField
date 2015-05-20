package snorri.main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class XRayPerms {
	
	private static boolean can(Player player, String perm, boolean def) {
		
		if (! Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx")) 
			return def;
		
		return PermissionsEx.getPermissionManager().has(player, perm);
		
	}
	
	public static boolean canAccessData(Player player) {
		return can(player, "xray.stats", true);
	}
	
	public static boolean canGiveTrust(Player player) {
		return can(player, "xray.trust", false);
	}
	
	public static boolean canHack(Player player) {
		return can(player, "xray.hack", false);
	}

}
