package io.github.winnpixie.smp.listeners.impl.utilities;

import io.github.winnpixie.smp.SmpCore;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;

public class BloodHelper {
    private static BlockData redstoneBlockData;
    private static BlockData coalBlockData;
    private static BlockData boneBlockData;
    private static BlockData ironBlockData;
    private static BlockData snowBlockData;
    private static BlockData tntBlockData;
    private static BlockData lavaBlockData;
    private static BlockData endPortalBlockData;

    public static void init(SmpCore plugin) {
        redstoneBlockData = plugin.getServer().createBlockData(Material.REDSTONE_BLOCK);
        coalBlockData = plugin.getServer().createBlockData(Material.COAL_BLOCK);
        boneBlockData = plugin.getServer().createBlockData(Material.BONE_BLOCK);
        ironBlockData = plugin.getServer().createBlockData(Material.IRON_BLOCK);
        snowBlockData = plugin.getServer().createBlockData(Material.SNOW_BLOCK);
        tntBlockData = plugin.getServer().createBlockData(Material.TNT);
        lavaBlockData = plugin.getServer().createBlockData(Material.LAVA);
        endPortalBlockData = plugin.getServer().createBlockData(Material.END_PORTAL);
    }

    public static BlockData getBlockForEntity(EntityType type) {
        return switch (type) {
            case SKELETON, SKELETON_HORSE -> boneBlockData;
            case WITHER, WITHER_SKELETON -> coalBlockData;
            case IRON_GOLEM -> ironBlockData;
            case SNOWMAN -> snowBlockData;
            case CREEPER -> tntBlockData;
            case BLAZE, MAGMA_CUBE, STRIDER -> lavaBlockData;
            case ENDER_DRAGON, ENDERMAN, ENDERMITE -> endPortalBlockData;
            default -> redstoneBlockData;
        };
    }
}
