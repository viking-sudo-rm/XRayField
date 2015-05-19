package snorri.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

public class XRayField extends JavaPlugin {
	
	private static final Logger log = Logger.getLogger("Minecraft");
	private static File dataFolder;
	
	private static XRayField plugin;
	
	public void onEnable() {
		
		plugin = this;
		
		getServer().getPluginManager().registerEvents(new XRayListener(), this);
		getCommand("xray").setExecutor(new XRayCommandExecutor());
		
		createDefaultConfig();
		XRaySettings.setConfig(getConfig());
		
		dataFolder = getDataFolder();
		dataFolder.mkdir();
		
	}
	
	public void createDefaultConfig() {
		File configFile = new File(getDataFolder(), "config.yml");
		if (! configFile.exists()) {
			log("Initializing default config file");
			configFile.getParentFile().mkdirs();
			copy(getResource("config.yml"), configFile);
		}
	}
	
	public void saveConfig() {
		try {
			getConfig().save(new File(getDataFolder(), "config.yml"));
			log("Saving config file");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void copy(InputStream in, File file) {
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void onDisable() {
		
		saveConfig();
		
	}
	
	public static File getFolder() {
		return dataFolder;
	}
	
	public static void log(String msg) {
		log.info(msg);
	}
	
	@SuppressWarnings("deprecation")
	public static OfflinePlayer getPlayer(String username) {
		return plugin.getServer().getOfflinePlayer(username);
	}
	
	public static OfflinePlayer getPlayer(UUID id) {
		return plugin.getServer().getOfflinePlayer(id);
	}

}
