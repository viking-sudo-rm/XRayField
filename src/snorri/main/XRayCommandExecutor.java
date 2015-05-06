package snorri.main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import snorri.data.DataManager;
import snorri.data.DataSet;

public class XRayCommandExecutor implements CommandExecutor {
	
	//TODO: add a flag to specify current session vs. all data
	//TODO: have syntax/algorithm the same otherwise
	//TODO: get rid of stattracker class and just import dataset
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("xray")) {
			Player player = XRayField.getPlayer(args[1]);
			if (args[0].equals("stats")) {	
				DataSet data = DataManager.getCurrentSession(player);
				sender.sendMessage(ChatColor.GREEN + "Mean Power (1): " + data.mean());
				sender.sendMessage(ChatColor.GREEN + "Sample Size (1): " + data.size());
				sender.sendMessage(ChatColor.GREEN + "Mean Power (2): " + DataManager.getPower(player));
				sender.sendMessage(ChatColor.GREEN + "Sample Size (2): " + DataManager.getTime(player));
				return true;
			}
		}
		return false;
	}

}
