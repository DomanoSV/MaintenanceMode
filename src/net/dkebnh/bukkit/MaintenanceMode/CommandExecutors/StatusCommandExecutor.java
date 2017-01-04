package net.dkebnh.bukkit.MaintenanceMode.CommandExecutors;

import net.dkebnh.bukkit.MaintenanceMode.MaintenanceMode;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

public class StatusCommandExecutor implements CommandExecutor {

    private MaintenanceMode plugin;

    public StatusCommandExecutor(MaintenanceMode plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        PluginDescriptionFile pdFile = plugin.getDescription();

        if (!sender.hasPermission("maintenancemode.status")) {
            sender.sendMessage(ChatColor.WHITE + "You do not have any of the required permission(s)");
            sender.sendMessage(ChatColor.WHITE + " - " + ChatColor.GREEN + "maintenancemode.status");
            return true;
        }

        if (true) {
            sender.sendMessage(ChatColor.WHITE + "Maintenance Status - " + ChatColor.GREEN + "MaintenanceMode " + pdFile.getVersion());
            sender.sendMessage(ChatColor.WHITE + "----------------------------------------------------");

            if (plugin.getEnabled()) {
                sender.sendMessage(ChatColor.WHITE + "MaintenanceMode (" + plugin.getSelectedMode() + ") Enabled: " + ChatColor.GREEN + "[TRUE] | False");
                sender.sendMessage(plugin.getMessage(plugin.getSelectedMode()));
            } else {
                sender.sendMessage(ChatColor.WHITE + "MaintenanceMode Enabled: " + ChatColor.GREEN + "True | [FALSE]");
            }

        }
        return true;
    }

}
