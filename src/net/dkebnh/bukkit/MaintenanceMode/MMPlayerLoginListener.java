package net.dkebnh.bukkit.MaintenanceMode;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class MMPlayerLoginListener implements Listener {

	private MaintenanceMode plugin;
	
	public MMPlayerLoginListener(MaintenanceMode plugin){
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	
	public void onPlayerLogin(PlayerLoginEvent event){
		String playerName = event.getPlayer().getName();
		
		if (plugin.MMenabled){
			if (!plugin.adminList.contains(playerName)){
				event.setKickMessage(plugin.MMmsg);
				event.setResult(Result.KICK_WHITELIST);
			}
		}
	}

}
