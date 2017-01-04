package net.dkebnh.bukkit.MaintenanceMode.Listeners;

import net.dkebnh.bukkit.MaintenanceMode.MaintenanceMode;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class PlayerLoginListener implements Listener {
    private MaintenanceMode plugin;

    private boolean hasPermission(Player player) {
        if (player.hasPermission("maintenancemode.pardon.mode." + plugin.getSelectedMode())) {
            return true;
        } else if (player.hasPermission("maintenancemode.pardon.allmodes")) {
            return true;
        } else if (player.isOp()) {
            return true;
        }
        return false;
    }

    public PlayerLoginListener(MaintenanceMode plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)

    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        if (plugin.getEnabled()) {
            if (plugin.getKickEnabled(plugin.getSelectedMode())) {
                if (!hasPermission(player)) {
                    event.setKickMessage(plugin.getMessage(plugin.getSelectedMode()));
                    event.setResult(Result.KICK_WHITELIST);
                }
            }
        }
    }
}
