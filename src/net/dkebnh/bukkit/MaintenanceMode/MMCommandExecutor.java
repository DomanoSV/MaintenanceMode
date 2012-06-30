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
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		PluginDescriptionFile pdFile = plugin.getDescription();
		
		if (!sender.hasPermission("maintenancemode.admin")){
			sender.sendMessage(ChatColor.WHITE + "You do not have any of the required permission(s)");
			sender.sendMessage(ChatColor.WHITE + " - " + ChatColor.GREEN + "maintenancemode.admin");
			return true;
		}
		
		if (true) {
				if(args.length == 1 && args[0].equalsIgnoreCase("version")) {
					sender.sendMessage(ChatColor.GREEN + "The MaintenanceMode plugin is version " + pdFile.getVersion());
					return true;
				}else if (args.length == 1 && args[0].equalsIgnoreCase("help")){
					sender.sendMessage(ChatColor.WHITE + "Command Help - " + ChatColor.GREEN + "MaintenanceMode " + pdFile.getVersion());
					sender.sendMessage(ChatColor.WHITE + "----------------------------------------------------");
					sender.sendMessage(ChatColor.GREEN +"/mm version" + ChatColor.WHITE + " - Gets Plugin Version.");
					sender.sendMessage(ChatColor.GREEN +"/mm sysinfo" + ChatColor.WHITE + " - Shows some handy System Information.");
					sender.sendMessage(ChatColor.GREEN +"/mm enable/disable" + ChatColor.WHITE + " - Enables/Disables MaintenanceMode.");
					sender.sendMessage(ChatColor.GREEN +"/mm adminadd <player>" + ChatColor.WHITE + " - Adds admin to list.");
					sender.sendMessage(ChatColor.GREEN +"/mm adminremove <player>" + ChatColor.WHITE + " - Removes admin from list.");
					sender.sendMessage(ChatColor.GREEN +"/mm msg <string>" + ChatColor.WHITE + " - Sets the Maintenance Message.");
					sender.sendMessage(ChatColor.GREEN +"/mm kickmsg <string>" + ChatColor.WHITE + " - Sets the Maintenance Message shown when kicked.");
					return true;
				}else if(args.length == 2 && args[0].equalsIgnoreCase("adminadd")){
					if (args[1] != null){
						plugin.adminList.add(args[1]);
						sender.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "Admin '" + args[1] + "' added.");
						plugin.conf.set("admins", plugin.adminList);
						plugin.saveSettings();
						return true;
					}else{
						sender.sendMessage(ChatColor.RED + "[MaintenanceMode] " + ChatColor.WHITE + "No value given ignoring command. Admin not added.");
						return true;
					}
				}else if(args.length == 2 && args[0].equalsIgnoreCase("adminremove")){
					if (args[1] != null){
						plugin.adminList.remove(args[1]);
						sender.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "Admin '" + args[1] + "' removed.");
						plugin.conf.set("admins", plugin.adminList);
						plugin.saveSettings();
						return true;
					}else{
						sender.sendMessage(ChatColor.RED + "[MaintenanceMode] " + ChatColor.WHITE + "No value given ignoring command. Admin not removed.");
						return true;
					}
				}else if(args.length >= 2 && args[0].equalsIgnoreCase("msg")){
					if (args[1] != null){
						String MMmessage = args[1];
						
						for(int i = 2; i < args.length; i++){
							
							MMmessage = MMmessage + " " + args[i];
						}
						
						plugin.MMmsg = MMmessage;
						sender.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "New message '" + MMmessage + "' set.");
						plugin.conf.set("maintenance-msg", MMmessage);
						plugin.saveSettings();
						return true;
					}else{
						sender.sendMessage(ChatColor.RED + "[MaintenanceMode] " + ChatColor.WHITE + "No value given ignoring command. Message not set.");
						return true;
					}
				}else if(args.length >= 2 && args[0].equalsIgnoreCase("kickmsg")){
						if (args[1] != null){
							String MMkickmessage = args[1];
							
							for(int j = 2; j < args.length; j++){
								
								MMkickmessage = MMkickmessage + " " + args[j];
							}
							
							plugin.MMkickmsg = MMkickmessage;
							sender.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "New kick message '" + MMkickmessage + "' set.");
							plugin.conf.set("maintenance-kick-msg", MMkickmessage);
							plugin.saveSettings();
							return true;
						}else{
							sender.sendMessage(ChatColor.RED + "[MaintenanceMode] " + ChatColor.WHITE + "No value given ignoring command. Kick message not set.");
							return true;
						}
				}else if(args.length == 1 && args[0].equalsIgnoreCase("enable")){
						plugin.MMenabled = true;
						sender.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "Maintenance mode has been enabled.");
						plugin.conf.set("enabled", plugin.MMenabled);
						plugin.saveSettings();
	
						for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()){
							if (!plugin.adminList.contains(onlinePlayer.getName())){
								onlinePlayer.kickPlayer(plugin.MMkickmsg);
							}
						}
						
						return true;
				}else if(args.length == 1 && args[0].equalsIgnoreCase("disable")){
					plugin.MMenabled = false;
					sender.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "Maintenance mode has been disabled.");
					plugin.conf.set("enabled", plugin.MMenabled);
					plugin.saveSettings();
					return true;
				}else if(args.length == 1 && args[0].equalsIgnoreCase("sysinfo")){
					 	Runtime serverRuntime = Runtime.getRuntime();
						
						sender.sendMessage(ChatColor.WHITE + "System Information - " + ChatColor.GREEN + "MaintenanceMode " + pdFile.getVersion());
						sender.sendMessage(ChatColor.WHITE + "----------------------------------------------------");
						sender.sendMessage(ChatColor.WHITE + String.format("System: %s %s (%s)", System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch")));
						sender.sendMessage(ChatColor.WHITE + "Avaliable Processors: " + serverRuntime.availableProcessors());
						sender.sendMessage(ChatColor.WHITE + "Maximum Avaliable RAM: " + ChatColor.GREEN + Math.floor(serverRuntime.maxMemory() / 1024.0 / 1024.0) + "MB");
						sender.sendMessage(ChatColor.WHITE + "Total Assigned RAM: " + ChatColor.GREEN + Math.floor(serverRuntime.totalMemory() / 1024.0 / 1024.0) + "MB");
						sender.sendMessage(ChatColor.WHITE + "Used: " + ChatColor.GREEN + Math.floor((serverRuntime.totalMemory() - serverRuntime.freeMemory()) / 1024.0 / 1024.0) + "MB" + ChatColor.WHITE + " Free: " + ChatColor.GREEN + Math.floor(serverRuntime.freeMemory() / 1024.0 / 1024.0) + "MB");
						return true;
				}else{
					sender.sendMessage(ChatColor.RED + "[MaintenanceMode] " + ChatColor.WHITE + "Usage: " + ChatColor.GREEN + "/mm help" + ChatColor.WHITE + " for more information.");
				}
		}
		return true;
	}
}
