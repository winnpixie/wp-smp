package io.github.winnpixie.smp.listeners.impl;

import io.github.winnpixie.smp.SmpCore;
import io.github.winnpixie.smp.listeners.BaseListener;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;

public class EntityActionListener extends BaseListener {
    public EntityActionListener(SmpCore plugin) {
        super(plugin);
    }

    @EventHandler
    private void onProjectileCollide(ProjectileHitEvent event) {
        if (!isThrowable(event.getEntity())) return;
        if (!(event.getHitEntity() instanceof Player player)) return;

        player.damage(0.0);
        // TODO: Velocity?
    }

    private boolean isThrowable(Entity entity) {
        return entity instanceof Egg || entity instanceof Snowball;
    }
}
