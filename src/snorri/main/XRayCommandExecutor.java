package snorri.main;

import java.util.ArrayList;

import org.bukkit.ChatColor;
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
			Player player = XRayField.getPlayer(args[1]);
			if (args[0].equals("stats")) {
				DataSet data = flags.contains("s") ? DataManager.getCurrentSession(player) : DataManager.getAllData(player);
				sender.sendMessage(ChatColor.DARK_GREEN + "Power Statistics: ");
				sender.sendMessage(ChatColor.GREEN + "  Player: " + ChatColor.RESET + player.getDisplayName());
				sender.sendMessage(ChatColor.GREEN + "  Type: " + ChatColor.GRAY + ChatColor.ITALIC + (flags.contains("s") ? "session" : "all-time"));
				sender.sendMessage(ChatColor.GOLD + "  Mean: " + ChatColor.RESET + data.mean());
				sender.sendMessage(ChatColor.GOLD + "  SD: " + ChatColor.RESET + data.sd());
				sender.sendMessage(ChatColor.GOLD + "  Sample: " + ChatColor.RESET + data.size());
				return true;
			}
		}
		return false;
	}

}
