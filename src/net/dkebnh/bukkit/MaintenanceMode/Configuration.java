package net.dkebnh.bukkit.MaintenanceMode;

/**
 * Created for use for the Add5tar MC Minecraft server
 * Created by benjamincharlton on 11/04/2016.
 */
public class Configuration {

    private MaintenanceMode plugin;

    public Configuration(MaintenanceMode instance) {
        plugin = instance;
        plugin.saveDefaultConfig();
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
    }

    public void save() {
        plugin.saveConfig();
    }

    public void reload() {
        plugin.reloadConfig();
    }
}