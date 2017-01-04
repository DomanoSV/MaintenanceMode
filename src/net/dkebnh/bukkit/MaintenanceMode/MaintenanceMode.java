package net.dkebnh.bukkit.MaintenanceMode;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import net.dkebnh.bukkit.MaintenanceMode.CommandExecutors.MMCommandExecutor;
import net.dkebnh.bukkit.MaintenanceMode.CommandExecutors.EditCommandExecutor;
import net.dkebnh.bukkit.MaintenanceMode.CommandExecutors.StatusCommandExecutor;
import net.dkebnh.bukkit.MaintenanceMode.Utilities.MsgLogger;
import net.dkebnh.bukkit.MaintenanceMode.Utilities.UpdateChecker;
import net.dkebnh.bukkit.MaintenanceMode.Listeners.PingListener;
import net.dkebnh.bukkit.MaintenanceMode.Listeners.PlayerJoinListener;
import net.dkebnh.bukkit.MaintenanceMode.Listeners.PlayerLoginListener;
import net.dkebnh.bukkit.MaintenanceMode.QueryServer.QueryServer;

import org.bukkit.plugin.java.JavaPlugin;

public class MaintenanceMode extends JavaPlugin {

    public MsgLogger log;
    public UpdateChecker updateChecker;
    public Configuration conf;
    public static final String CONFIG_FILE = "server.properties";
    public String ServerMotd = "A minecraft server.";
    private QueryServer server;

    public void onEnable() {
        conf = new Configuration(this);
        this.log = new MsgLogger(this);

        try {
            Properties props = new Properties();
            props.load(new FileReader(CONFIG_FILE));
            ServerMotd = props.getProperty("motd", "A minecraft server.");
        } catch (IOException ex) {
            this.log.severeMSG("Unable to load MOTD from server.properties configuration file.");
        }

        this.updateChecker = new UpdateChecker(this, "http://dev.bukkit.org/server-mods/maintenancemode/files.rss");
        if (this.updateChecker.updateNeeded()) {
            log.infoMSGUpdater("A new version of MaintenanceMode is available, Version " + this.updateChecker.getVersion() + ", you can get it from: " + this.updateChecker.getLink());
        }

        this.getCommand("mm").setExecutor(new MMCommandExecutor(this));
        this.getCommand("mmedit").setExecutor(new EditCommandExecutor(this));
        this.getCommand("mmstatus").setExecutor(new StatusCommandExecutor(this));
        this.getServer().getPluginManager().registerEvents(new PlayerLoginListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PingListener(this), this);

        try {
            String queryServerHost = this.getHost();
            int queryServerPort = this.getPort();
            if (queryServerHost.equals("")) {
                queryServerHost = "ANY";
            }

            if (this.getQueryServerEnabled()) {
                server = new QueryServer(this, queryServerHost, queryServerPort);

                if (server == null) {
                    throw new IllegalStateException("Could not enable MaintenanceMode's Query Server!");
                } else {
                    server.start();
                }
            }

        } catch (IOException ex) {
            log.severeMSG("Error starting MaintenanceMode's Query Server please check your configuration file.");
        }
    }

    public void onDisable() {
        try {
            if (server != null) {
                server.getListener().close();
            }
        } catch (IOException ex) {
            log.severeMSG("Unable to stop MaintenanceMode's Query Server.");
        }
        log.infoMSG("Successfully disabled and saved config.");
    }

    public Configuration getConfiguration() {
        return conf;
    }

    public void saveConf() {
        conf.save();
    }

    public void reloadConf() {
        conf.reload();
    }

    public boolean getEnabled() {
        return getConfig().getBoolean("enabled");
    }

    public String getSelectedMode() {
        return getConfig().getString("selectedmode");
    }

    public boolean getQueryServerEnabled() {
        return getConfig().getBoolean("queryserver.enabled");
    }

    public String getHost() {
        return getConfig().getString("queryserver.host");
    }

    public int getPort() {
        return getConfig().getInt("queryserver.port");
    }

    public boolean getKickEnabled(String mode) {
        String node = "modes." + mode + ".kickOnEnable";
        if (getConfig().contains(node)) {
            return getConfig().getBoolean(node);
        } else {
            return false;
        }
    }

    public String getKickMessage(String mode) {
        String node = "modes." + mode + ".kickMessage";
        if (getConfig().contains(node)) {
            String kickMessage = getConfig().getString(node);
            return kickMessage;
        } else {
            return "nullMessage";
        }
    }

    public String getMessage(String mode) {
        String node = "modes." + mode + ".message";
        if (getConfig().contains(node)) {
            String message = getConfig().getString(node);
            return message;
        } else {
            return "nullMessage";
        }
    }

    public String getMOTD() {
        String motd = "A minecraft server.";

        if (this.getEnabled()) {
            String node = "modes." + this.getSelectedMode() + ".motd";
            if (getConfig().contains(node)) {
                motd = getConfig().getString(node);
            }
        } else {
            motd = ServerMotd;
        }
        return motd;
    }
}