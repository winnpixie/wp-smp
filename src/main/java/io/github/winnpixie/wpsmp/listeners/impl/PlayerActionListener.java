package io.github.winnpixie.wpsmp.listeners.impl;

import io.github.winnpixie.wpsmp.Config;
import io.github.winnpixie.wpsmp.WPSMPPlugin;
import io.github.winnpixie.wpsmp.listeners.BaseListener;
import io.github.winnpixie.wpsmp.utilities.TextHelper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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

    public PlayerActionListener(WPSMPPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    private void onChat(AsyncPlayerChatEvent event) {
        event.setFormat(TextHelper.formatColors(Config.CHAT_FORMAT));

        if (Config.CHAT_GREEN_TEXT && event.getMessage().startsWith(">")) {
            event.setMessage(TextHelper.formatColors("&a" + event.getMessage()));
        } else if (Config.FORMAT_CHAT_COLORS) {
            event.setMessage(TextHelper.formatColors(event.getMessage()));
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

        switch (event.getHand()) {
            case HAND -> event.getPlayer().swingMainHand();
            case OFF_HAND -> event.getPlayer().swingOffHand();
        }

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

        event.getPlayer().spigot().sendMessage(new ComponentBuilder("REMINDER: ").color(ChatColor.RED)
                .append("You have log stripping disabled.").color(ChatColor.YELLOW).create());
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

        event.getPlayer().spigot().sendMessage(new ComponentBuilder("This head belongs to ").color(ChatColor.YELLOW)
                .append(ownerName).color(ChatColor.RED).create());
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
