package dev.projectg.geyserhub.module.world;

import dev.projectg.geyserhub.GeyserHubMain;
import dev.projectg.geyserhub.config.ConfigId;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class WorldSettings implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        FileConfiguration config = GeyserHubMain.getInstance().getConfigManager().getFileConfiguration(ConfigId.MAIN);
        if (!(event.getEntity() instanceof Player)) return;

        if (config.getBoolean("World-settings.disable-fall-damage")
                && event.getCause() == EntityDamageEvent.DamageCause.FALL)
            event.setCancelled(true);

        else if (config.getBoolean("World-settings.disable-drowning")
                && event.getCause() == EntityDamageEvent.DamageCause.DROWNING)
            event.setCancelled(true);

        else if (config.getBoolean("World-settings.disable-fire-damage")
                && (event.getCause() == EntityDamageEvent.DamageCause.FIRE
                || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause()
                == EntityDamageEvent.DamageCause.LAVA))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onFoodChange(FoodLevelChangeEvent event) {
        FileConfiguration config = GeyserHubMain.getInstance().getConfigManager().getFileConfiguration(ConfigId.MAIN);
        if (!config.getBoolean("World-settings.disable-hunger-loss"))
            return;
        if (!(event.getEntity() instanceof Player))
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onFireSpread(BlockIgniteEvent event) {
        FileConfiguration config = GeyserHubMain.getInstance().getConfigManager().getFileConfiguration(ConfigId.MAIN);
        if (!config.getBoolean("World-settings.disable-block-fire-spread"))
            return;
        if (event.getCause() == BlockIgniteEvent.IgniteCause.SPREAD)
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        FileConfiguration config = GeyserHubMain.getInstance().getConfigManager().getFileConfiguration(ConfigId.MAIN);
        if (!config.getBoolean("World-settings.disable-disable-block-burn"))
            return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLeafDecay(LeavesDecayEvent event) {
        FileConfiguration config = GeyserHubMain.getInstance().getConfigManager().getFileConfiguration(ConfigId.MAIN);
        if (!config.getBoolean("World-settings.disable_block-leaf-decay"))
            return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        FileConfiguration config = GeyserHubMain.getInstance().getConfigManager().getFileConfiguration(ConfigId.MAIN);
        if (!config.getBoolean("World-settings.disable-mob-spawning"))
            return;
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onWeatherChange(WeatherChangeEvent event) {
        FileConfiguration config = GeyserHubMain.getInstance().getConfigManager().getFileConfiguration(ConfigId.MAIN);
        if (!config.getBoolean("World-settings.disable-weather-change"))
            return;

        event.setCancelled(event.toWeatherState());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        FileConfiguration config = GeyserHubMain.getInstance().getConfigManager().getFileConfiguration(ConfigId.MAIN);
        if (!config.getBoolean("World-settings.disable-player-pvp"))
            return;
        if (!(event.getEntity() instanceof Player)) return;
        event.setCancelled(true);

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        FileConfiguration config = GeyserHubMain.getInstance().getConfigManager().getFileConfiguration(ConfigId.MAIN);
        if (!config.getBoolean("World-settings.disable-block-break")
                || event.isCancelled())
            return;
        Player player = event.getPlayer();
        if (player.hasPermission("geyserhub.blockbreak")) {
            return;
        }
        player.sendMessage("You can't break blocks here!");
        event.setCancelled(true);

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        FileConfiguration config = GeyserHubMain.getInstance().getConfigManager().getFileConfiguration(ConfigId.MAIN);
        if (!config.getBoolean("World-settings.disable-block-place")
                || event.isCancelled())
            return;
        ItemStack item = event.getItemInHand();
        if (item.getType() == Material.AIR)
            return;
        Player player = event.getPlayer();
        if (player.hasPermission("geyserhub.blockplace")) {
            return;
        }
        player.sendMessage("You can't place blocks here!");
        event.setCancelled(true);
    }

    @EventHandler
    public void onCommandUse(PlayerCommandPreprocessEvent event) {
        FileConfiguration config = GeyserHubMain.getInstance().getConfigManager().getFileConfiguration(ConfigId.MAIN);
        if (!config.getBoolean("Disable-Commands.Enable", false)) {
            return;
        }
        Player player = event.getPlayer();
        List<String> commands = config.getStringList("Disable-Commands.Commands");
        commands.forEach(all -> {
            if (event.getMessage().toLowerCase().equalsIgnoreCase("/" + all.toLowerCase())) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.DARK_RED + "That command has been disabled!");

            }
        });
    }
}
