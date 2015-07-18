package snorri.main;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import snorri.data.DataManager;
import snorri.math.Mathv;

public class XRayListener implements Listener {

	private static final int FLOOR = -1;
	
	//TODO: move center of field to center of block
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
				
		Player player = event.getPlayer();
		World world = player.getWorld();
		Location loc = event.getFrom();
		
		int range = XRaySettings.getSearchRange();
		
		Vector netForce = new Vector(0, 0, 0);
		
		for (String blockType : XRaySettings.getSourceBlocks()) {
			for (int y = loc.getBlockY() - range; y < loc.getBlockY() + range && y < XRaySettings.getCeiling(blockType); y++) {
				if (y < FLOOR)
					continue;
				for (int x = loc.getBlockX() - range; x < loc.getBlockX() + range; x++) {
					for (int z = loc.getBlockZ() - range; z < loc.getBlockZ() + range; z++) {
						if (world.getBlockAt(x, y, z).getType().equals(Material.getMaterial(blockType))) {
							netForce.add(Mathv.getForceOn(new Vector(loc.getX(), loc.getY(), loc.getZ()), new Vector(x, y , z), XRaySettings.getFlux(blockType)));
						}
					}
				}
			}
		}
				
		if (netForce.equals(new Vector(0, 0, 0)))
			return;
		
		DataManager.updateStats(player, Mathv.getWorkTerm(netForce, player.getVelocity()));
		
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		DataManager.addPlayer(event.getPlayer());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		DataManager.removePlayer(event.getPlayer());
	}
	
}
