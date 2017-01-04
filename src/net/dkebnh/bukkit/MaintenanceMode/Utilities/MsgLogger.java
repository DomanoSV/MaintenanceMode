package net.dkebnh.bukkit.MaintenanceMode.Utilities;

import java.util.logging.Logger;

import net.dkebnh.bukkit.MaintenanceMode.MaintenanceMode;

import org.bukkit.plugin.PluginDescriptionFile;

public class MsgLogger {

    private MaintenanceMode plugin;
    private Logger log;

    public MsgLogger(MaintenanceMode plugin) {
        this.plugin = plugin;
        this.log = Logger.getLogger("Minecraft");
    }

    private String FormatMessage(String message) {
        PluginDescriptionFile pdFile = plugin.getDescription();
        return "[" + pdFile.getName() + "] " + message;
    }

    private String UpdaterFormatMessage(String message) {
        return "[DKE UpdateChecker] " + message;
    }

    public void infoMSG(String message) {
        this.log.info(this.FormatMessage(message));
    }

    public void warningMSG(String message) {
        this.log.warning(this.FormatMessage(message));
    }

    public void severeMSG(String message) {
        this.log.severe(this.FormatMessage(message));
    }

    public void infoMSGUpdater(String message) {
        this.log.info(this.UpdaterFormatMessage(message));
    }
}

