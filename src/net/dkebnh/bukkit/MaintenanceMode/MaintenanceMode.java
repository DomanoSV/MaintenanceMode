package net.dkebnh.bukkit.MaintenanceMode;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MaintenanceMode extends JavaPlugin {
	
	protected MMLogger log;
	
	private File confFile;
	public YamlConfiguration conf;
	boolean MMenabled = false;
	String MMmsg = "The server is undergoing scheduled maintenance, please check back later.";
	String MMkickmsg = "We are now performing server maintenance, please check back later.";
	List<String> adminList = Arrays.asList("Player1","Player2","Player3");
    
    public void onEnable(){
		File dFolder = getDataFolder();		
		if(!dFolder.exists()) dFolder.mkdirs();		
		confFile = new File(dFolder, "config.yml");        
	
		this.getServer().getWorldContainer();
		
		if (confFile.exists()) {
			conf = YamlConfiguration.loadConfiguration(confFile);
			MMenabled = conf.getBoolean("enabled");
			MMmsg = conf.getString("maintenance-msg");
			MMkickmsg = conf.getString("maintenance-kick-msg");
			adminList = conf.getStringList("admins");

		}else{        	
			conf = new YamlConfiguration();        	
			conf.set("enabled", false);       
			conf.set("maintenance-msg", "The server is undergoing scheduled maintenance, please check back later."); 
			conf.set("maintenance-kick-msg", "We are now performing server maintenance, please check back later.");
			conf.set("admins", adminList);
			saveSettings();
		}
		
		this.log = new MMLogger(this);
		this.getCommand("mm").setExecutor(new MMCommandExecutor(this));
		this.getServer().getPluginManager().registerEvents(new MMPlayerLoginListener(this), this);
	}
	
	public void onDisable(){
 
	}
	
	public boolean saveSettings() {
		if (!confFile.exists()) {			
			confFile.getParentFile().mkdirs();		
		}try{			
			conf.save(confFile);			
			return true;		
			}catch (IOException e){
				e.printStackTrace();		
			}			
		return false;
	}
	
}
