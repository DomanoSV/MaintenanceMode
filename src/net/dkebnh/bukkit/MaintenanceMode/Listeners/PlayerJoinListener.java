package net.dkebnh.bukkit.MaintenanceMode.Listeners;

import net.dkebnh.bukkit.MaintenanceMode.MaintenanceMode;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private MaintenanceMode plugin;

    public PlayerJoinListener(MaintenanceMode plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)

    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Will tell InformationCenter Admins or Server OPs if there is an update available upon logon.
        if (player.isOp() || player.hasPermission("informationcenter.admin")) {
            if (plugin.updateChecker.updateNeeded()) {
                player.sendMessage(ChatColor.GREEN + "[DKE UpdateChecker]" + ChatColor.WHITE + " A new version of InformationCenter is avaliable, Version " + plugin.updateChecker.getVersion() + ", you can get it from:");
                player.sendMessage(ChatColor.BLUE + plugin.updateChecker.getLink());
            }
        }

        if (plugin.getEnabled()) {
            if (plugin.getKickEnabled(plugin.getSelectedMode())) {
                player.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "MaintenanceMode has been Enabled. Kicking players with no access permissions.");
            }
            player.sendMessage(plugin.getMessage(plugin.getSelectedMode()));
        }
    }
}

