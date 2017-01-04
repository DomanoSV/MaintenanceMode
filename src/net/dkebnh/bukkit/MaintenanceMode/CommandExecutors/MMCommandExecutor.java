package net.dkebnh.bukkit.MaintenanceMode.CommandExecutors;

import net.dkebnh.bukkit.MaintenanceMode.MaintenanceMode;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.plugin.PluginDescriptionFile;

public class MMCommandExecutor implements CommandExecutor {

    private MaintenanceMode plugin;

    public MMCommandExecutor(MaintenanceMode plugin) {
        this.plugin = plugin;
    }

    private boolean hasPermission(Player player, String mode) {
        if (player.hasPermission("maintenancemode.pardon." + mode)) {
            return true;
        } else if (player.hasPermission("maintenancemode.pardon.allmodes")) {
            return true;
        } else if (player.isOp()) {
            return true;
        }
        return false;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        PluginDescriptionFile pdFile = plugin.getDescription();

        if (!sender.hasPermission("maintenancemode.admin")) {
            sender.sendMessage(ChatColor.WHITE + "You do not have any of the required permission(s)");
            sender.sendMessage(ChatColor.WHITE + " - " + ChatColor.GREEN + "maintenancemode.admin");
            return true;
        }

        if (true) {
            if (args.length == 1 && args[0].equalsIgnoreCase("version")) {
                sender.sendMessage(ChatColor.GREEN + "The MaintenanceMode plugin is version " + pdFile.getVersion());
                return true;
            } else if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                sender.sendMessage(ChatColor.WHITE + "Command Help - " + ChatColor.GREEN + "MaintenanceMode " + pdFile.getVersion());
                sender.sendMessage(ChatColor.WHITE + "----------------------------------------------------");
                sender.sendMessage(ChatColor.GREEN + "/mmstatus" + ChatColor.WHITE + " - Shows MaintenanceMode's current status.");
                sender.sendMessage(ChatColor.GREEN + "/mm version" + ChatColor.WHITE + " - Gets Plugin Version.");
                sender.sendMessage(ChatColor.GREEN + "/mm enable/disable" + ChatColor.WHITE + " - Enables/Disables MaintenanceMode.");
                sender.sendMessage(ChatColor.GREEN + "/mm reload" + ChatColor.WHITE + " - Reloads the configuration file.");
                sender.sendMessage(ChatColor.GREEN + "/mm optimize world <world>" + ChatColor.WHITE + " - Optimizes selected world.");
                sender.sendMessage(ChatColor.GREEN + "/mm help 2" + ChatColor.WHITE + " - Help page 2.");
                return true;
            } else if (args.length == 2 && args[0].equalsIgnoreCase("help") && args[1].equalsIgnoreCase("2")) {
                sender.sendMessage(ChatColor.WHITE + "Command Help - 2 - " + ChatColor.GREEN + "MaintenanceMode " + pdFile.getVersion());
                sender.sendMessage(ChatColor.WHITE + "----------------------------------------------------");
                sender.sendMessage(ChatColor.GREEN + "/mm optimize whitelist" + ChatColor.WHITE + " - Cleans whitelist. 30 days by default.");
                sender.sendMessage(ChatColor.GREEN + "/mm optimize whitelist <days>" + ChatColor.WHITE + " - removes players from whitelist");
                sender.sendMessage(ChatColor.WHITE + "who haven't been on for the selected days.");
                sender.sendMessage(ChatColor.GREEN + "/mm sysinfo" + ChatColor.WHITE + " - Displays some server Information.");
                return true;
            } else if (args.length >= 1 && args[0].equalsIgnoreCase("enable")) {
                plugin.getConfig().set("enabled", true);
                String mode = "default";

                if (args.length > 1) {
                    if (plugin.getConfig().contains("modes." + args[1].toLowerCase())) {
                        mode = args[1].toLowerCase();
                        sender.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "Maintenance mode (" + mode + ") has been enabled.");
                    } else {
                        sender.sendMessage(ChatColor.RED + "[MaintenanceMode] " + ChatColor.WHITE + "The selected mode doesn't exist, using default.");
                        sender.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "Maintenance mode has been enabled.");
                    }
                } else {
                    sender.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "Maintenance mode has been enabled.");
                }

                if (plugin.getKickEnabled(mode)) {
                    for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
                        if (!hasPermission(onlinePlayer, mode)) {
                            onlinePlayer.kickPlayer(plugin.getKickMessage(mode));
                        } else {
                            onlinePlayer.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "MaintenanceMode has been Enabled. Kicking players with no access permissions.");
                        }
                    }
                } else {
                    Bukkit.broadcastMessage(plugin.getMessage(mode));
                }

                plugin.getConfig().set("selectedmode", mode);
                plugin.conf.save();
                return true;
            } else if (args.length == 1 && args[0].equalsIgnoreCase("disable")) {
                sender.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "Maintenance mode (" + plugin.getSelectedMode() + ") has been disabled.");
                plugin.getConfig().set("enabled", false);
                plugin.getConfig().set("selectedmode", "default");
                plugin.conf.save();
                return true;
            } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                plugin.conf.reload();
                sender.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "MaintenanceMode has reloaded successfully!");
                return true;
            } else if (args.length >= 2 && args[0].equalsIgnoreCase("optimize") && args[1].equalsIgnoreCase("whitelist")) {
                final long timePeriod = 86400000; // 1 day in milliseconds
                // 2592000000  =  30 days in milliseconds
                final long curTime = System.currentTimeMillis();
                int days = 30;
                OfflinePlayer[] allPlayers = plugin.getServer().getOfflinePlayers();

                if (args.length > 2) {
                    try {
                        days = Integer.parseInt(args[2]);
                    } catch (Exception e) {
                        days = 30;
                    }
                }

                for (OfflinePlayer offp : allPlayers) {
                    if (!offp.isWhitelisted())
                        continue;
                    long lastPlayed = offp.getLastPlayed();
                    int playertime = (int) ((curTime - lastPlayed) / (1000 * 60 * 60 * 24));
                    plugin.log.infoMSG(offp.getName() + " last played " + playertime + " days ago.");

                    if ((curTime - lastPlayed) > (timePeriod * days))
                        offp.setWhitelisted(false);

                    if (!offp.isWhitelisted()) {
                        plugin.log.infoMSG(offp.getName() + " has been removed from the whitelist.");
                    }
                }

                sender.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "Whitelist has been optimized by MaintenanceMode.");
                return true;
            } else if (args.length >= 2 && args[0].equalsIgnoreCase("optimize") && args[1].equalsIgnoreCase("world")) {
                int num = 0;
                boolean isWorld = false;
                String worldNames = "";

                if (args.length > 2) {
                    for (World world : plugin.getServer().getWorlds()) {
                        if (args[2].matches(world.getName())) {
                            isWorld = true;
                        }
                    }

                    if (isWorld) {
                        isWorld = false;
                        for (Entity ent : plugin.getServer().getWorld(args[2]).getEntities()) {
                            if (ent instanceof Projectile) {
                                ent.remove();
                                ++num;
                            }

                            if (ent instanceof Boat) {
                                ent.remove();
                                ++num;
                            }

                            if (ent instanceof Item) {
                                ent.remove();
                                ++num;
                            }

                            if (ent instanceof FallingBlock) {
                                ent.remove();
                                ++num;
                            }

                            if (ent instanceof Minecart) {
                                ent.remove();
                                ++num;
                            }

                            if (ent instanceof TNTPrimed) {
                                ent.remove();
                                ++num;
                            }

                            if (ent instanceof ExperienceOrb) {
                                ent.remove();
                                ++num;
                            }
                        }
                        sender.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + num + " entities have been removed from the world '" + args[2] + "'");
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.RED + "[MaintenanceMode] " + ChatColor.WHITE + "The world you are looking for doesn't exist.");
                        return true;
                    }
                } else {
                    sender.sendMessage(ChatColor.GREEN + "[MaintenanceMode] " + ChatColor.WHITE + "please select a world:");
                    int count = 0;

                    for (World world : plugin.getServer().getWorlds()) {
                        if (count > 0) {
                            worldNames = worldNames + ", " + world.getName().toString();
                        } else {
                            worldNames = world.getName().toString();
                        }
                        count++;
                    }

                    sender.sendMessage(ChatColor.GRAY + worldNames);
                    return true;
                }
            } else if (args.length == 1 && args[0].equalsIgnoreCase("sysinfo")) {
                Runtime serverRuntime = Runtime.getRuntime();

                sender.sendMessage(ChatColor.WHITE + "System Information - " + ChatColor.GREEN + "MaintenanceMode " + pdFile.getVersion());
                sender.sendMessage(ChatColor.WHITE + "----------------------------------------------------");
                sender.sendMessage(ChatColor.WHITE + String.format("System: %s %s (%s)", System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch")));
                sender.sendMessage(ChatColor.WHITE + "Avaliable Processors: " + serverRuntime.availableProcessors());
                sender.sendMessage(ChatColor.WHITE + "Maximum Avaliable RAM: " + ChatColor.GREEN + Math.floor(serverRuntime.maxMemory() / 1024.0 / 1024.0) + "MB");
                sender.sendMessage(ChatColor.WHITE + "Total Assigned RAM: " + ChatColor.GREEN + Math.floor(serverRuntime.totalMemory() / 1024.0 / 1024.0) + "MB");
                sender.sendMessage(ChatColor.WHITE + "Used: " + ChatColor.GREEN + Math.floor((serverRuntime.totalMemory() - serverRuntime.freeMemory()) / 1024.0 / 1024.0) + "MB" + ChatColor.WHITE + " Free: " + ChatColor.GREEN + Math.floor(serverRuntime.freeMemory() / 1024.0 / 1024.0) + "MB");
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "[MaintenanceMode] " + ChatColor.WHITE + "Usage: " + ChatColor.GREEN + "/mm help" + ChatColor.WHITE + " for more information.");
            }
        }
        return true;
    }
}
