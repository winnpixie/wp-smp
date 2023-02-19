package io.github.winnpixie.wpsmp.listeners.impl;

import io.github.winnpixie.wpsmp.Config;
import io.github.winnpixie.wpsmp.WPSMPPlugin;
import io.github.winnpixie.wpsmp.listeners.BaseListener;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.weather.LightningStrikeEvent;

import java.util.Set;

public class WorldEventListener extends BaseListener {
    private final Set<Material> metallicSwords = Set.of(
            Material.GOLDEN_SWORD,
            Material.IRON_SWORD,
            Material.NETHERITE_SWORD // Netherite contains gold.
    );

    public WorldEventListener(WPSMPPlugin plugin) {
        super(plugin);

        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            if (!Config.ALLOW_NETHER_ROOF) return;

            for (var player : plugin.getServer().getOnlinePlayers()) {
                if (player.isDead()) continue;
                if (player.getWorld().getEnvironment() != World.Environment.NETHER) continue;
                if (player.getLocation().getY() < 128) continue;

                player.damage(4.0);
            }
        }, 0L, 10L);
    }

    @EventHandler
    private void onLightningStrike(LightningStrikeEvent event) {
        // TODO: Add back Sea of Thieves' cutlass attraction during storm mechanic?
    }
}