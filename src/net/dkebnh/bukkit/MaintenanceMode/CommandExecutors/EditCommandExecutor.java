package net.dkebnh.bukkit.MaintenanceMode.CommandExecutors;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import net.dkebnh.bukkit.MaintenanceMode.MaintenanceMode;

public class EditCommandExecutor implements CommandExecutor {

    private String editMode = "default";
    private MaintenanceMode plugin;

    public EditCommandExecutor(MaintenanceMode plugin) {
        this.plugin = plugin;
    }


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        PluginDescriptionFile pdFile = plugin.getDescription();

        if (!sender.hasPermission("maintenancemode.edit")) {
            sender.sendMessage(ChatColor.WHITE + "You do not have any of the required permission(s)");
            sender.sendMessage(ChatColor.WHITE + " - " + ChatColor.GREEN + "maintenancemode.edit");
            return true;
        }

        if (true) {
            if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                sender.sendMessage(ChatColor.WHITE + "Edit Command Help - " + ChatColor.GREEN + "MaintenanceMode " + pdFile.getVersion());
                sender.sendMessage(ChatColor.WHITE + "----------------------------------------------------");
                sender.sendMessage(ChatColor.GREEN + "/mmedit mode <mode>" + ChatColor.WHITE + " - Select the mode you wish to edit.");
                sender.sendMessage(ChatColor.GREEN + "/mmedit kick <mode>" + ChatColor.WHITE + " - Sets kickOnEnable for selected mode.");
                sender.sendMessage(ChatColor.GREEN + "/mmedit kickmsg <mode>" + ChatColor.WHITE + " - Sets kickMessage for selected mode.");
                sender.sendMessage(ChatColor.GREEN + "/mmedit message <mode>" + ChatColor.WHITE + " - Sets message for selected mode.");
                sender.sendMessage(ChatColor.GREEN + "/mmedit motd <mode>" + ChatColor.WHITE + " - Sets motd for selected mode.");
                sender.sendMessage(ChatColor.GREEN + "Current Selected Mode: " + ChatColor.WHITE + plugin.getSelectedMode());
                return true;
            } else if (args.length >= 1 && args[0].equalsIgnoreCase("mode")) {
                if (args.length == 1) {
                    sender.sendMessage(editMode);
                    return true;
                }

                if (plugin.getConfig().contains("modes." + args[1].toLowerCase())) {
                    this.editMode = args[1].toLowerCase();
                    sender.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "You can now edit the mode selected '" + this.editMode + "' safetly.");
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "[MaintenanceMode] " + ChatColor.WHITE + "The mode you are looking for doesn`t exist.");
                    return true;
                }
            } else if (args.length > 1 && args[0].equalsIgnoreCase("kick")) {
                if (args[1] != null) {
                    try {
                        String check = args[1].toLowerCase();

                        if (check.equalsIgnoreCase("true")) {
                            sender.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "kickOnEnable: " + check + ", set for selected mode '" + this.editMode + "'.");
                            plugin.getConfig().set("modes." + this.editMode + ".kickOnEnable", true);
                            plugin.conf.save();
                        } else if (check.equalsIgnoreCase("false")) {
                            sender.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "kickOnEnable: " + check + ", set for selected mode '" + this.editMode + "'.");
                            plugin.getConfig().set("modes." + this.editMode + ".kickOnEnable", false);
                            plugin.conf.save();
                        } else {
                            sender.sendMessage(ChatColor.RED + "[MaintenanceMode] " + ChatColor.WHITE + "Invalid. kickOnEnable not set.");
                        }
                    } catch (Exception e) {
                        sender.sendMessage(ChatColor.RED + "[MaintenanceMode] " + ChatColor.WHITE + "Invalid. kickOnEnable not set.");
                    }
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "[MaintenanceMode] " + ChatColor.WHITE + "No value given ignoring command. kickOnEnable not set.");
                    return true;
                }
            } else if (args.length > 1 && args[0].equalsIgnoreCase("kickmsg")) {
                if (args[1] != null) {
                    String message = args[1];

                    for (int j = 2; j < args.length; j++) {

                        message = message + " " + args[j];
                    }

                    sender.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "kickMessage: '" + message + "', set for selected mode '" + this.editMode + "'.");
                    plugin.getConfig().set("modes." + this.editMode + "kickMessage", message);
                    plugin.conf.save();
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "[MaintenanceMode] " + ChatColor.WHITE + "No value given ignoring command. kickMessage not set.");
                    return true;
                }
            } else if (args.length > 1 && args[0].equalsIgnoreCase("message")) {
                if (args[1] != null) {
                    String message = args[1];

                    for (int i = 2; i < args.length; i++) {

                        message = message + " " + args[i];
                    }

                    sender.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "message: '" + message + "', set for selected mode '" + this.editMode + "'.");
                    plugin.getConfig().set("modes." + this.editMode + ".message", message);
                    plugin.conf.save();
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "[MaintenanceMode] " + ChatColor.WHITE + "No value given ignoring command. Message not set.");
                    return true;
                }
            } else if (args.length > 1 && args[0].equalsIgnoreCase("motd")) {
                if (args[1] != null) {
                    String message = args[1];

                    for (int i = 2; i < args.length; i++) {

                        message = message + " " + args[i];
                    }

                    sender.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "motd: '" + message + "', set for selected mode '" + this.editMode + "'.");
                    plugin.getConfig().set("modes." + this.editMode + ".motd", message);
                    plugin.conf.save();
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "[MaintenanceMode] " + ChatColor.WHITE + "No value given ignoring command. Motd not set.");
                    return true;
                }
            } else {

            }
        }
        return false;
    }
}
