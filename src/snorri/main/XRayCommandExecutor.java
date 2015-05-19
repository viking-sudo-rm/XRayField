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

	// TODO: streamline getData method?s?
	// TODO: move some ttest stuff to the data classes

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
	
	private DataSet getData(OfflinePlayer player, String flags) {
		if (! player.isOnline() || flags.contains("a")) {
			return DataManager.getOfflineData(player);
		}
		if (flags.contains("s")) {
			return DataManager.getCurrentSession((Player) player);
		}
		return DataManager.getAllData((Player) player);
	}
	
	private String getPlayerName(OfflinePlayer player) {
		return player.isOnline() ? ((Player) player).getDisplayName() : player.getName();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String flags = getFlags(args);
		args = getArgs(args);
		if (cmd.getName().equals("xray")) {
			
			if (args.length < 2)
				return false;
			
			OfflinePlayer player = XRayField.getPlayer(args[1]);
			if (player == null)
				return false;
			
			
			DataSet data = getData(player, flags);
			String playerName = getPlayerName(player);
			
			if (args[0].equals("stats")) {
				sender.sendMessage(ChatColor.DARK_GREEN + "Power Statistics: ");
				sender.sendMessage(ChatColor.GREEN + "  Player: " + ChatColor.RESET + playerName);
				sender.sendMessage(ChatColor.GREEN + "  Type: " + ChatColor.GRAY + ChatColor.ITALIC + data.getMode().toString());
				sender.sendMessage(ChatColor.GOLD + "  Mean: " + ChatColor.RESET + data.mean());
				sender.sendMessage(ChatColor.GOLD + "  SD: " + ChatColor.RESET + data.sd());
				sender.sendMessage(ChatColor.GOLD + "  Sample: " + ChatColor.RESET + data.size());
				return true;
			}
			
			if (args[0].equals("test")) {
				sender.sendMessage(ChatColor.DARK_GREEN + "X-Ray Test: ");
				sender.sendMessage(ChatColor.GREEN + "  Player: " + ChatColor.RESET + playerName);
				if (args.length == 2)
					sender.sendMessage(ChatColor.GREEN + "  Against: " + ChatColor.GRAY + ChatColor.ITALIC + "trusted");
				else
					sender.sendMessage(ChatColor.GREEN + "  Against: " + ChatColor.RESET + getPlayerName(XRayField.getPlayer(args[2])));
				sender.sendMessage(ChatColor.GREEN + "  Type: " + ChatColor.GRAY + ChatColor.ITALIC + data.getMode().toString());
				
				double pvalue;
				try {
					pvalue = data.tTest(Double.parseDouble(args[2]));
				} catch (Exception e) {
					DataSet otherData = (args.length == 2) ? XRaySettings.getTrustedData() : getData(XRayField.getPlayer(args[2]), flags);
					pvalue = data.tTest(otherData);
				}
				
				sender.sendMessage(ChatColor.GOLD + "  P-Value: " + ChatColor.RESET + pvalue);
				sender.sendMessage(ChatColor.GOLD + "  Score: " + ChatColor.RESET + -Math.log10(pvalue));
				return true;
			}
			
			if (args[0].equals("trust")) {
				
				//TODO: make /xray trust show who is trusted
				
				if (XRaySettings.isTrusted(XRayField.getPlayer(playerName))) {
					
					if (flags.contains("r")) {
						XRaySettings.revokeTrusted(player);
						sender.sendMessage(ChatColor.RESET + playerName + ChatColor.GREEN + " is no longer a trusted miner");
						return true;
					}
						
					sender.sendMessage(ChatColor.RESET + playerName + ChatColor.GREEN + " is already a trusted miner");
					sender.sendMessage(ChatColor.GREEN + "Revoke that status with " + ChatColor.RED + "/xray -r trust " + playerName);
					return true;
				}
				XRaySettings.addTrusted(XRayField.getPlayer(playerName));
				sender.sendMessage(ChatColor.RESET + playerName + ChatColor.GREEN + " is now a trusted miner");
				return true;
			}
			
		}
		return false;
	}

}
