package snorri.main;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class XRayField extends JavaPlugin {
	
	private static final Logger log = Logger.getLogger("Minecraft");
	private static File dataFolder;
	
	private static XRayField plugin;
	
	public void onEnable() {
		
		plugin = this;
		
		getServer().getPluginManager().registerEvents(new XRayListener(), this);
		getCommand("xray").setExecutor(new XRayCommandExecutor());
		
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
	
	@SuppressWarnings("deprecation")
	public static Player getPlayer(String username) {
		return plugin.getServer().getPlayer(username);
	}

}
