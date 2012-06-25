package net.dkebnh.bukkit.MaintenanceMode;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class MMCommandExecutor implements CommandExecutor {

	private MaintenanceMode plugin;
	
	public MMCommandExecutor(MaintenanceMode plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		PluginDescriptionFile pdFile = plugin.getDescription();
		if (sender instanceof Player == false){
			plugin.log.infoMSG("The MaintenanceMode commands can only be used in game.");
			return true;
		}
		
		final Player player = (Player) sender;
		
		if (!player.hasPermission("maintenancemode.admin")){
			player.sendMessage(ChatColor.WHITE + "You do not have any of the required permission(s)");
			player.sendMessage(ChatColor.WHITE + " - " + ChatColor.GREEN + "maintenancemode.admin");
			return true;
		}
		
		if (true) {
				if(args.length == 1 && args[0].equalsIgnoreCase("version")) {
					player.sendMessage(ChatColor.GREEN + "The MaintenanceMode plugin is version " + pdFile.getVersion());
					return true;
				}else if (args.length == 1 && args[0].equalsIgnoreCase("help")){
					player.sendMessage(ChatColor.WHITE + "Command Help - " + ChatColor.GREEN + "MaintenanceMode");
					player.sendMessage(ChatColor.WHITE + "----------------------------------------------------");
					player.sendMessage(ChatColor.RED + "Usage: /mm version" + ChatColor.GREEN + " - Gets Plugin Version.");
					player.sendMessage(ChatColor.RED + "Usage: /mm enable" + ChatColor.GREEN + " - Enables maintenance mode.");
					player.sendMessage(ChatColor.RED + "Usage: /mm disble" + ChatColor.GREEN + " - Disables maintenance mode.");
					player.sendMessage(ChatColor.RED + "Usage: /mm msg <string>" + ChatColor.GREEN + " - Sets the Maintenance Message.");
					return true;
				}else if(args.length >= 2 && args[0].equalsIgnoreCase("msg")){
					if (args[1] != null){
						String MMmessage = args[1];
						
						for(int i = 2; i < args.length; i++){
							
							MMmessage = MMmessage + " " + args[i];
						}
						
						plugin.log.warningMSG("New message '" + MMmessage + "' set.");
						player.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "New message '" + MMmessage + "' set.");
						plugin.conf.set("maintenance-msg", MMmessage);
						plugin.saveSettings();
						return true;
					}else{
						plugin.log.warningMSG("No value given ignoring command. Message not set.");
						player.sendMessage(ChatColor.RED + "[MaintenanceMode] " + ChatColor.WHITE + "No value given ignoring command. Message not set.");
						return true;
					}
				}else if(args.length == 1 && args[0].equalsIgnoreCase("enable")){		// Sets height variable in the config file.
						plugin.MMenabled = true;
						plugin.log.warningMSG("Maintenance mode has been enabled.");
						player.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "Maintenance mode has been enabled.");
						plugin.conf.set("enabled", plugin.MMenabled);
						plugin.saveSettings();
	
						for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()){
							if (!plugin.adminList.contains(onlinePlayer.getName())){
								onlinePlayer.kickPlayer(plugin.MMkickmsg);
							}
						}
						
						return true;
				}else if(args.length == 1 && args[0].equalsIgnoreCase("disable")){		// Sets height variable in the config file.	
						plugin.MMenabled = false;
						plugin.log.warningMSG("Maintenance mode has been disabled.");
						player.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "Maintenance mode has been disabled.");
						plugin.conf.set("enabled", plugin.MMenabled);
						plugin.saveSettings();
						return true;
				}else if (args.length < 1){
					player.sendMessage(ChatColor.RED + "Usage: /mm help for more information.");
					return true;
				}else{
					player.sendMessage(ChatColor.RED + "[MaintenanceMode] " + ChatColor.WHITE + "Invalid Command. Try again.");
				}
		}
		return true;
	}
}
