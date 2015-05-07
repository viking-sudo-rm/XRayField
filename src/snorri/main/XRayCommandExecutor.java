package snorri.main;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import snorri.data.DataManager;
import snorri.data.DataSet;

public class XRayCommandExecutor implements CommandExecutor {

	// TODO: add a flag to specify current session vs. all data
	// TODO: have syntax/algorithm the same otherwise
	// TODO: get rid of stattracker class and just import dataset

	private String getFlags(String[] args) {
		String result = "";
		for (String arg : args) {
			if (arg.charAt(0) == '-') {
				for (int i = 1; i < arg.length(); i++) {
					result = result + arg.charAt(i);
				}
			}
		}
		return result;
	}

	private String[] getArgs(String[] args) {
		ArrayList<String> result = new ArrayList<String>();
		for (String arg : args) {
			if (arg.charAt(0) != '-') {
				result.add(arg);
			}
		}
		return (String[]) result.toArray(args);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String flags = getFlags(args);
		args = getArgs(args);
		if (cmd.getName().equals("xray")) {
			OfflinePlayer player = XRayField.getPlayer(args[1]);
			if (player == null)
				return false;
			if (args[0].equals("stats")) {
				DataSet data;
				String playerName, mode;
				if (! player.isOnline() || flags.contains("a")) {
					data = DataManager.getOfflineData(player);
					playerName = player.getName();
					mode = "offline";
				}
				else if (flags.contains("s")) {
					data = DataManager.getCurrentSession((Player) player);
					playerName = ((Player) player).getDisplayName();
					mode = "session";
				}
				else {
					data = DataManager.getAllData((Player) player);
					playerName = ((Player) player).getDisplayName();
					mode = "all-time";
				}
				sender.sendMessage(ChatColor.DARK_GREEN + "Power Statistics: ");
				sender.sendMessage(ChatColor.GREEN + "  Player: " + ChatColor.RESET + playerName);
				sender.sendMessage(ChatColor.GREEN + "  Type: " + ChatColor.GRAY + ChatColor.ITALIC + mode);
				sender.sendMessage(ChatColor.GOLD + "  Mean: " + ChatColor.RESET + data.mean());
				sender.sendMessage(ChatColor.GOLD + "  SD: " + ChatColor.RESET + data.sd());
				sender.sendMessage(ChatColor.GOLD + "  Sample: " + ChatColor.RESET + data.size());
				return true;
			}
		}
		return false;
	}

}
