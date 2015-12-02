package snorri.main;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import snorri.data.DataSet;
import snorri.scan.ScanResult;
import snorri.scan.XRayScan;

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
	
	private String getPlayerName(OfflinePlayer player) {
		return player.isOnline() ? ((Player) player).getDisplayName() : player.getName();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		String flags = getFlags(args);
		args = getArgs(args);
		
		if (cmd.getName().equals("xray")) {
			
			XRayScan scan = new XRayScan(flags);
			
			if (args.length == 0)
				return false;
			
			if (args[0].equals("scan")) {
				
				if (sender instanceof Player && ! XRayPerms.canAccessData((Player) sender)) {
					sender.sendMessage(ChatColor.RED + "You do not have permission to access mining data");
					return true;
				}
				
				ArrayList<ScanResult> results;
				if (args.length == 2)
					results = scan.checkPlayers(Double.parseDouble(args[1]));
				else
					results = scan.checkPlayers();
								
				sender.sendMessage(ChatColor.DARK_GREEN + "Top Suspects:");
				if (results.size() == 0)
					sender.sendMessage(ChatColor.GRAY + "  " + ChatColor.ITALIC + "none");
				else {
					double pvalue;
					ChatColor suspicion;
					for (int i = 0; i < results.size() && i < 5; i++) {
						pvalue = results.get(i).pvalue;
						suspicion = (pvalue < XRaySettings.getAlpha("alert")) ? ChatColor.RED : ChatColor.GREEN;
						sender.sendMessage("  " + ChatColor.GOLD + (i + 1) + ". " + ChatColor.RESET + getPlayerName(results.get(i).player) + "  " + suspicion + pvalue);
					}
				}
				
				return true;
			}
			
			OfflinePlayer player = null;
			DataSet data = null;
			String playerName = null;
			
			if (args.length < 2) {
				if (! args[0].equals("trust"))
					return false;
			}
			else {
				player = XRayField.getPlayer(args[1]);
				if (player == null)
					return false;
				
				
				data = scan.getData(player);
				playerName = getPlayerName(player);
			}
			
			if (args[0].equals("stats")) {
				
				//TODO: generalize this?
				if (sender instanceof Player && ! XRayPerms.canAccessData((Player) sender)) {
					sender.sendMessage(ChatColor.RED + "You do not have permission to access mining data");
					return true;
				}
				
				sender.sendMessage(ChatColor.DARK_GREEN + "Power Statistics:");
				sender.sendMessage(ChatColor.GREEN + "  Player: " + ChatColor.RESET + playerName);
				sender.sendMessage(ChatColor.GREEN + "  Type: " + ChatColor.GRAY + ChatColor.ITALIC + data.getMode().toString());
				sender.sendMessage(ChatColor.GOLD + "  Mean: " + ChatColor.RESET + data.mean());
				sender.sendMessage(ChatColor.GOLD + "  SD: " + ChatColor.RESET + data.sd());
				sender.sendMessage(ChatColor.GOLD + "  Sample: " + ChatColor.RESET + data.size());
				
				return true;
			}
			
			if (args[0].equals("test")) {
				
				if (sender instanceof Player && ! XRayPerms.canAccessData((Player) sender)) {
					sender.sendMessage(ChatColor.RED + "You do not have permission to access mining data");
					return true;
				}
				
				sender.sendMessage(ChatColor.DARK_GREEN + "X-Ray Test:");
				sender.sendMessage(ChatColor.GREEN + "  Player: " + ChatColor.RESET + playerName);
				if (args.length == 2)
					sender.sendMessage(ChatColor.GREEN + "  Against: " + ChatColor.GRAY + ChatColor.ITALIC + "trusted");
				else
					sender.sendMessage(ChatColor.GREEN + "  Against: " + ChatColor.RESET + getPlayerName(XRayField.getPlayer(args[2])));
				sender.sendMessage(ChatColor.GREEN + "  Type: " + ChatColor.GRAY + ChatColor.ITALIC + data.getMode().toString());
				
				if (args.length == 2 && XRaySettings.getTrustedData().size() == 0) {
					sender.sendMessage(ChatColor.RED + "  There is no trusted mining data to compare against");
					sender.sendMessage(ChatColor.GREEN + "  Manually set a null with " + ChatColor.RED + "/xray test " + ChatColor.RESET + playerName + ChatColor.RED + " <null>");
					return true;
				}
				
				//TODO: have "score" feature inside the scan class
				double pvalue;
				try {
					pvalue = scan.checkPlayer(player, Double.parseDouble(args[2])).pvalue;
				} catch (Exception e) {
					pvalue = ((args.length == 2) ? scan.checkPlayer(player) : scan.checkPlayer(player, XRayField.getPlayer(args[2]))).pvalue;
				}
				
				if (pvalue == 10d) {
					sender.sendMessage(ChatColor.RED + "  There is not sufficient data to perform this test");
					return true;
				}
				
				sender.sendMessage(ChatColor.GOLD + "  P-Value: " + ChatColor.RESET + pvalue);
				sender.sendMessage(ChatColor.GOLD + "  Score: " + ChatColor.RESET + -Math.log10(pvalue));
				return true;
			}
			
			if (args[0].equals("trust")) {
				
				if (sender instanceof Player && ! XRayPerms.canGiveTrust((Player) sender)) {
					sender.sendMessage(ChatColor.RED + "You do not have permission to change trusted miner status");
					return true;
				}
				
				if (args.length == 1) {
					sender.sendMessage(ChatColor.DARK_GREEN + "Trusted Miners:");
					boolean noOne = true;
					for (String name : XRaySettings.getTrustedNames()) {
						noOne = false;
						sender.sendMessage("  " + name);
					}
					if (noOne) {
						sender.sendMessage("" + ChatColor.GRAY + ChatColor.ITALIC + "  none");
						sender.sendMessage(ChatColor.GREEN + "Use " + ChatColor.RED  + "/xray trust <player>" + ChatColor.GREEN + " to add some");
					}
					return true;
				}
				
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
