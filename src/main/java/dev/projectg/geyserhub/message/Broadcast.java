package dev.projectg.geyserhub.message;

import dev.projectg.geyserhub.GeyserHub;
import dev.projectg.geyserhub.Logger;
import dev.projectg.geyserhub.config.ConfigId;
import dev.projectg.geyserhub.utils.PlaceholderUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public class Broadcast {
    public static void startBroadcastTimer(BukkitScheduler scheduler) {
        FileConfiguration config = GeyserHub.getInstance().getConfigManager().getFileConfiguration(ConfigId.MAIN);
        scheduler.scheduleSyncDelayedTask(GeyserHub.getInstance(), () -> {

            if (config.getBoolean("Broadcasts.Enable", false)) {
                ConfigurationSection parentSection = config.getConfigurationSection("Broadcasts.Messages");
                if (parentSection == null) {
                    Logger.getLogger().severe("Broadcasts.Messages configuration section is malformed, unable to send.");
                    return;
                }

                String broadcastId = getRandomElement(new ArrayList<>(parentSection.getKeys(false)));

                if (parentSection.contains(broadcastId, true) && parentSection.isList(broadcastId)) {
                    for (String message : parentSection.getStringList(broadcastId)) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.sendMessage(PlaceholderUtils.setPlaceholders(player, message));
                        }
                    }
                } else {
                    Logger.getLogger().severe("Broadcast with ID " + broadcastId + " has a malformed message list, unable to send.");
                }
            }
            startBroadcastTimer(scheduler);
        }, config.getLong("Broadcasts-Interval", 3600));
    }

    private static String getRandomElement(List<String> list) {
        return list.get(new Random().nextInt(list.size()));
    }
}
