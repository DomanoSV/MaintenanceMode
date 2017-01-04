package net.dkebnh.bukkit.MaintenanceMode.Utilities;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.dkebnh.bukkit.MaintenanceMode.MaintenanceMode;

public class UpdateChecker {

    private MaintenanceMode plugin;
    private URL filesFeed;
    private String version;
    private String link;

    public UpdateChecker(MaintenanceMode plugin, String url) {
        this.plugin = plugin;

        try {
            this.filesFeed = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateNeeded() {
        if (plugin.getConfig().getBoolean("update-checker.enabled")) {
            try {
                InputStream bukkitFeed = this.filesFeed.openConnection().getInputStream();
                Document xmlPage = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(bukkitFeed);

                Node latestFile = xmlPage.getElementsByTagName("item").item(0);
                NodeList fileList = latestFile.getChildNodes();

                this.version = fileList.item(1).getTextContent().replaceAll("[a-zA-Z ]", "");
                this.link = fileList.item(3).getTextContent();

                if (!plugin.getDescription().getVersion().equals(this.version)) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public String getVersion() {
        return this.version;
    }

    public String getLink() {
        return this.link;
    }
}
