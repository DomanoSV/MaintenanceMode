package net.dkebnh.bukkit.MaintenanceMode.QueryServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.dkebnh.bukkit.MaintenanceMode.MaintenanceMode;

public final class RequestHandler extends Thread {
    private final MaintenanceMode plugin;
    private final Socket socket;

    public RequestHandler(MaintenanceMode plugin, Socket socket) {
        this.plugin = plugin;
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            handleRequest(socket, reader.readLine());
            socket.close();
        } catch (IOException ex) {
            plugin.log.severeMSG("MaintenanceMode's Query Server thread shutting down");
        }
    }

    private void handleRequest(Socket socket, String request) throws IOException {
        if (request == null) {
            return;
        }

        if (request.equalsIgnoreCase("MM_QUERY")) {
            MaintenanceMode m = getQuery();

            StringBuilder resp = new StringBuilder();
            resp.append("{");
            resp.append("\"server\":").append("\"" + Bukkit.getServerName() + "\"").append(",");
            resp.append("\"serverPort\":").append(m.getServer().getPort()).append(",");
            resp.append("\"motd\":").append("\"" + m.getMOTD() + "\"").append(",");
            resp.append("\"mmenabled\":").append("\"" + m.getEnabled() + "\"").append(",");
            resp.append("\"kickenabled\":").append("\"" + m.getKickEnabled(plugin.getSelectedMode()) + "\"").append(",");
            resp.append("\"msg\":").append("\"" + m.getMessage(plugin.getSelectedMode()) + "\"").append(",");
            resp.append("\"playerCount\":").append(m.getServer().getOnlinePlayers().toArray().length).append(",");
            resp.append("\"maxPlayers\":").append(m.getServer().getMaxPlayers()).append(",");
            resp.append("\"playerList\":");
            resp.append("[");

            // Iterate through the players.
            int count = 0;
            for (Player player : m.getServer().getOnlinePlayers()) {
                resp.append("\"" + player.getName() + "\"");
                if (++count < m.getServer().getOnlinePlayers().toArray().length) {
                    resp.append(",");
                }
            }

            resp.append("]");
            resp.append("}\n");

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeBytes(resp.toString());
        }
    }

    public MaintenanceMode getQuery() {
        return plugin;
    }
}
