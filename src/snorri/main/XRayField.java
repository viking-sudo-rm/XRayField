package snorri.main;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class XRayField extends JavaPlugin {
	
	private static final Logger log = Logger.getLogger("Minecraft");
	private static File dataFolder;
	
	public void onEnable() {
		
		getServer().getPluginManager().registerEvents(new XRayListener(), this);
		
		dataFolder = getDataFolder();
		dataFolder.mkdir();
		
	}
	
	public void onDisable() {
		
	}
	
	public static File getFolder() {
		return dataFolder;
	}
	
	public static void log(String msg) {
		log.info(msg);
	}

}
