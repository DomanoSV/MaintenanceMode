package net.dkebnh.bukkit.MaintenanceMode;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;

public class MMLogger {

	private MaintenanceMode plugin;
	private Logger log;
	
	public MMLogger(MaintenanceMode plugin){
		this.plugin = plugin;
		this.log = Logger.getLogger("Minecraft");
	}
	
	private String FormatMessage(String message){
		PluginDescriptionFile pdFile = plugin.getDescription();
		return "[" + pdFile.getName() + "] " + message;
	}
	
	public void infoMSG(String message){
		this.log.info(this.FormatMessage(message));
	}
	
	public void warningMSG(String message){
		this.log.warning(this.FormatMessage(message));
	}
	
	public void severeMSG(String message){
		this.log.severe(this.FormatMessage(message));
	}
	
}
