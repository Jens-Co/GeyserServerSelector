package dev.projectg.geyserhub.module.tablist;

import dev.projectg.geyserhub.GeyserHubMain;
import dev.projectg.geyserhub.config.ConfigId;
import dev.projectg.geyserhub.utils.PlaceholderUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

public class TabListSetup {

    public void enableTablist(Player getPlayer){
        FileConfiguration config = GeyserHubMain.getInstance().getConfigManager().getFileConfiguration(ConfigId.MAIN);
        List<String> header = config.getStringList("TabList.Header");
        List<String> Footer = config.getStringList("TabList.Footer");
        StringBuilder headerBuild = new StringBuilder();
        StringBuilder footerBuild = new StringBuilder();
        Iterator<String> var1 = header.iterator();
        Iterator<String> var2 = Footer.iterator();

        while(var1.hasNext()) {
            String head = var1.next();
            if (headerBuild.length() == 0) {
                headerBuild.append(ChatColor.translateAlternateColorCodes('&', head));
            } else {
                headerBuild.append("\n").append(ChatColor.translateAlternateColorCodes('&', head));
            }
            while(var2.hasNext()) {
                String foot = var2.next();
                if (footerBuild.length() == 0) {
                    footerBuild.append(ChatColor.translateAlternateColorCodes('&', foot));
                } else {
                    footerBuild.append("\n").append(ChatColor.translateAlternateColorCodes('&', foot));
                }
            }
        }

        for (Player target : Bukkit.getOnlinePlayers()) {
            target.setPlayerListHeaderFooter(PlaceholderUtils.setPlaceholders(getPlayer,headerBuild.toString()),PlaceholderUtils.setPlaceholders(getPlayer, footerBuild.toString()));
        }
    }
}
