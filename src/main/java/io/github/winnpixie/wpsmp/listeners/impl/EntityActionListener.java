package io.github.winnpixie.wpsmp.listeners.impl;

import io.github.winnpixie.wpsmp.Config;
import io.github.winnpixie.wpsmp.WPSMPPlugin;
import io.github.winnpixie.wpsmp.listeners.BaseListener;
import io.github.winnpixie.wpsmp.utilities.BloodHelper;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class EntityActionListener extends BaseListener {
    public EntityActionListener(WPSMPPlugin plugin) {
        super(plugin);

        BloodHelper.init(plugin);
    }

    @EventHandler
    private void onEntityDamaged(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent eveEvent) {
            if (Config.HEAD_PATS) {
                if (doHeadPat(eveEvent)) return; // Don't execute anything else, this was a friendly gesture.
            }

            if (Config.HOT_HANDS) {
                doHotHandsAttack(eveEvent);
            }
        }

        if (Config.BLOOD_SPLATTER) {
            doBloodSplatter(event);
        }
    }

    private boolean doHeadPat(EntityDamageByEntityEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return false;
        if (!(event.getDamager() instanceof Player player)) return false;
        if (!player.isSneaking()) return false;
        if (!(event.getEntity() instanceof Tameable receiver)) return false;
        if (!receiver.isTamed()) return false;

        receiver.getWorld().spawnParticle(Particle.HEART, receiver.getEyeLocation(), 5,
                0.5, 0.5, 0.5);
        event.setCancelled(true);

        return true;
    }

    private void doBloodSplatter(EntityDamageEvent event) {
        if (event.getFinalDamage() <= 0) return;
        if (!(event.getEntity() instanceof LivingEntity victim)) return;

        var location = event.getCause() == EntityDamageEvent.DamageCause.FALL ? victim.getLocation() : victim.getEyeLocation();
        var blockData = BloodHelper.getBlockForEntity(victim.getType());
        victim.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 50, 0.5, 0.3, 0.5, blockData);
    }

    private void doHotHandsAttack(EntityDamageByEntityEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        if (!(event.getDamager() instanceof Player player)) return;
        if (player.getFireTicks() < 1) return;

        var victim = event.getEntity();
        if (victim.getFireTicks() < 100) { // 5 seconds
            victim.setFireTicks(victim.getFireTicks() + 200); // Add 10 seconds
        } else {
            victim.setFireTicks(victim.getFireTicks() + 100); // Add 5 seconds
        }
    }

    @EventHandler
    private void onProjectileCollide(ProjectileHitEvent event) {
        if (!isThrowable(event.getEntity())) return;
        if (!(event.getHitEntity() instanceof Player player)) return;

        player.damage(0.0, event.getEntity());
    }

    private boolean isThrowable(Entity entity) {
        return entity instanceof Egg || entity instanceof Snowball;
    }
}
