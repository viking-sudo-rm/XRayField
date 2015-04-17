package snorri.main;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class XRayField extends JavaPlugin {
	
	private static final Logger log = Logger.getLogger("Minecraft");
	
	public void onEnable() {
		
		getServer().getPluginManager().registerEvents(new XRayListener(), this);
		
	}
	
	public void onDisable() {
		
	}
	
	public static void log(String msg) {
		log.info(msg);
	}

}
