package net.dkebnh.bukkit.MaintenanceMode.Listeners;

import net.dkebnh.bukkit.MaintenanceMode.MaintenanceMode;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class PingListener implements Listener {
    private MaintenanceMode plugin;

    public PingListener(MaintenanceMode plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPing(ServerListPingEvent event) {
        if (plugin.getEnabled()) {
            event.setMotd(plugin.getMOTD());
        }
    }
}
