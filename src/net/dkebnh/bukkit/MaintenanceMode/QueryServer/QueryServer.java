package net.dkebnh.bukkit.MaintenanceMode.QueryServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import net.dkebnh.bukkit.MaintenanceMode.MaintenanceMode;

public final class QueryServer extends Thread {

    private MaintenanceMode plugin;
    private final ServerSocket listener;

    public QueryServer(MaintenanceMode plugin, String host, int port) throws IOException {
        this.plugin = plugin;

        InetSocketAddress address;
        if (host.equalsIgnoreCase("ANY")) {
            plugin.log.infoMSG("Starting MaintenanceMode's Query Server on *:" + Integer.toString(port));
            address = new InetSocketAddress(port);
        } else {
            plugin.log.infoMSG("Starting MaintenanceMode's Query Server on " + host + ":" + Integer.toString(port));
            address = new InetSocketAddress(host, port);
        }

        listener = new ServerSocket();
        listener.bind(address);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = getListener().accept();
                (new Thread(new RequestHandler(getQuery(), socket))).start();
            }
        } catch (IOException ex) {
            plugin.log.infoMSG("Stopping MaintenanceMode's Query Server");
        }
    }

    public MaintenanceMode getQuery() {
        return plugin;
    }

    public ServerSocket getListener() {
        return listener;
    }

}

