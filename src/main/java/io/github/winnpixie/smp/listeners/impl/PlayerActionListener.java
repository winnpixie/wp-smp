package io.github.winnpixie.smp.listeners.impl;

import io.github.winnpixie.smp.Config;
import io.github.winnpixie.smp.SmpCore;
import io.github.winnpixie.smp.listeners.BaseListener;
import io.github.winnpixie.smp.utilities.ChatHelper;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Set;

public class PlayerActionListener extends BaseListener {
    private final Set<Material> axes = Set.of(
            Material.WOODEN_AXE,
            Material.STONE_AXE,
            Material.IRON_AXE,
            Material.GOLDEN_AXE,
            Material.DIAMOND_AXE,
            Material.NETHERITE_AXE
    );

    public PlayerActionListener(SmpCore plugin) {
        super(plugin);
    }

    @EventHandler
    private void onChat(AsyncPlayerChatEvent event) {
        event.setFormat(ChatHelper.format(Config.CHAT_FORMAT));

        if (Config.CHAT_GREEN_TEXT && event.getMessage().startsWith(">")) {
            event.setMessage("\u00A7a" + event.getMessage());
        } else if (Config.FORMAT_CHAT_COLORS) {
            event.setMessage(ChatHelper.format(event.getMessage()));
        }
    }

    @EventHandler
    private void onRightClick(PlayerInteractEvent event) {
        if (doBoneMeal(event)) return;

        if (doLogStrip(event)) return;

        if (showHeadInfo(event)) return;

        if (doSignEdit(event)) return;
    }

    private boolean doBoneMeal(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return false;
        if (event.getHand() == null) return false;

        var clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return false;
        if (!isGrowable(clickedBlock)) return false;

        ItemStack item = event.getPlayer().getInventory().getItem(event.getHand());
        if (item == null) return false;
        if (item.getType() != Material.BONE_MEAL) return false;

        var nextBlock = clickedBlock.getRelative(BlockFace.UP, 1);
        while (nextBlock.getType() == clickedBlock.getType()) {
            nextBlock = nextBlock.getRelative(BlockFace.UP, 1);
        }

        int height = 0;
        while (nextBlock.getRelative(BlockFace.DOWN, height + 1).getType() == clickedBlock.getType()) {
            height++;
        }

        if (height > 2) return false;
        if (nextBlock.getY() >= clickedBlock.getWorld().getMaxHeight()) return false;
        if (!nextBlock.getType().isAir()) return false;

        nextBlock.setType(clickedBlock.getType());
        event.getPlayer().spawnParticle(Particle.VILLAGER_HAPPY, nextBlock.getLocation().add(0.5, 0.5, 0.5),
                20, 0.5, 0.5, 0.5);

        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return true;
        item.setAmount(item.getAmount() - 1);
        event.getPlayer().getInventory().setItem(event.getHand(), item);

        return true;
    }

    private boolean isGrowable(Block block) {
        return block.getType() == Material.CACTUS || block.getType() == Material.SUGAR_CANE;
    }

    private boolean doLogStrip(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return false;
        if (event.getHand() == null) return false;

        var clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return false;
        if (!Tag.LOGS.isTagged(clickedBlock.getType())) return false;
        if (clickedBlock.getType().name().startsWith("STRIPPED_")) return false;

        ItemStack item = event.getPlayer().getInventory().getItem(event.getHand());
        if (item == null) return false;
        if (!axes.contains(item.getType())) return false;

        if (getPlugin().getPlayerData(event.getPlayer().getUniqueId()).canStripLogs) return false;
        event.getPlayer().sendMessage(ChatHelper.format("&cREMINDER: &eYou have log stripping disabled."));
        event.setCancelled(true);

        return true;
    }

    private boolean showHeadInfo(PlayerInteractEvent event) {
        if (event.getPlayer().isSneaking()) return false;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return false;
        if (event.getHand() == null || !event.getHand().equals(EquipmentSlot.HAND)) return false;

        var clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return false;
        if (!(clickedBlock.getState() instanceof Skull skullState)) return false;
        if (skullState.getOwningPlayer() == null) return false;

        var ownerName = skullState.getOwningPlayer().getName();
        if (ownerName == null) return false;

        event.getPlayer().sendMessage(String.format(ChatHelper.format("&eThis head belongs to &c%s"), ownerName));
        event.setCancelled(true);

        return true;
    }

    private boolean doSignEdit(PlayerInteractEvent event) {
        if (event.getPlayer().isSneaking()) return false;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return false;
        if (event.getHand() == null || !event.getHand().equals(EquipmentSlot.HAND)) return false;

        var clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return false;
        if (!(clickedBlock.getState() instanceof Sign signState)) return false;

        event.getPlayer().openSign(signState);
        event.setCancelled(true);

        return true;
    }

    @EventHandler
    private void onEntityAttackEntity(EntityDamageByEntityEvent event) {
        if (Config.HEAD_PATS) {
            if (doHeadPat(event)) return; // Don't execute anything else, this was a friendly gesture.
        }

        if (Config.HOT_HANDS) {
            if (doHotHandsAttack(event)) return;
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

    private boolean doHotHandsAttack(EntityDamageByEntityEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return false;
        if (!(event.getDamager() instanceof Player player)) return false;
        if (player.getFireTicks() < 1) return false;

        var victim = event.getEntity();
        if (victim.getFireTicks() < 100) { // 5 seconds
            victim.setFireTicks(victim.getFireTicks() + 200); // Add 10 seconds
        } else {
            victim.setFireTicks(victim.getFireTicks() + 100); // Add 5 seconds
        }

        return true;
    }

    @EventHandler
    private void onDeath(PlayerDeathEvent event) {
        if (!(event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent damageEvent)) return;

        var killer = damageEvent.getDamager();
        if (killer instanceof Projectile projectile) {
            if (!(projectile.getShooter() instanceof LivingEntity)) return;

            killer = (LivingEntity) projectile.getShooter();
        }

        if (killer.equals(event.getEntity())) return;

        if (killer instanceof Zombie zombie && !zombie.isAdult() && Config.PHILZA_DEATHS) {
            // RIP Philza hardcore world
            event.setDeathMessage(String.format("%s went out like Philza!", event.getEntity().getDisplayName()));
        }

        // PvP player heads
        if (killer instanceof Player && Config.DROP_HEAD_ON_DEATH) {
            var headStack = new ItemStack(Material.PLAYER_HEAD, 1);
            if (headStack.getItemMeta() == null) return;

            var skullMeta = (SkullMeta) headStack.getItemMeta();
            skullMeta.setOwningPlayer(event.getEntity());
            headStack.setItemMeta(skullMeta);
            event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), headStack);
        }
    }
}