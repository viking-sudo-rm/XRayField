package snorri.main;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import snorri.math.Mathv;

public class XRayListener implements Listener {

	private static final int RANGE = 5;
	private static final int CEILING = 16;
	private static final int FLOOR = -1;
	private static final Material TREASURE = Material.DIAMOND_ORE;
	
	//TODO: move center of field to center of block
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
				
		Player player = event.getPlayer();
		World world = player.getWorld();
		Location loc = event.getFrom();
		Vector direction = loc.getDirection();
		
		Vector netForce = new Vector(0, 0, 0);
		for (int y = loc.getBlockY() - RANGE; y < loc.getBlockY() + RANGE && y < CEILING; y++) {
			if (y < FLOOR)
				continue;
			for (int x = loc.getBlockX() - RANGE; x < loc.getBlockX() + RANGE; x++) {
				for (int z = loc.getBlockZ() - RANGE; z < loc.getBlockZ() + RANGE; z++) {
					if (world.getBlockAt(x, y, z).getType().equals(TREASURE)) {
						netForce.add(Mathv.getForceOn(new Vector(loc.getX(), loc.getY(), loc.getZ()), new Vector(x, y , z)));
					}
				}
			}
		}
				
		if (netForce.equals(new Vector(0, 0, 0)))
			return;
		
		double theta = Mathv.getAngleBetween(direction, netForce);
		
		XRayField.log("" + 180 / Math.PI * theta);
		
	}
	
}
