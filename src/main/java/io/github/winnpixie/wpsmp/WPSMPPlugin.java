package io.github.winnpixie.wpsmp;

import io.github.winnpixie.annoc.Annoc;
import io.github.winnpixie.wpsmp.commands.BaseCommand;
import io.github.winnpixie.wpsmp.commands.impl.HatCommand;
import io.github.winnpixie.wpsmp.commands.impl.PingCommand;
import io.github.winnpixie.wpsmp.commands.impl.ToggleLogStrippingCommand;
import io.github.winnpixie.wpsmp.commands.impl.admin.ReloadConfigurationCommand;
import io.github.winnpixie.wpsmp.listeners.BaseListener;
import io.github.winnpixie.wpsmp.listeners.impl.ConnectionListener;
import io.github.winnpixie.wpsmp.listeners.impl.EntityActionListener;
import io.github.winnpixie.wpsmp.listeners.impl.PlayerActionListener;
import io.github.winnpixie.wpsmp.listeners.impl.WorldEventListener;
import io.github.winnpixie.wpsmp.utilities.PlayerData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WPSMPPlugin extends JavaPlugin {
    private final Map<UUID, PlayerData> playerData = new HashMap<>();

    public final Annoc annoc = new Annoc();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        annoc.setConfiguration(this.getConfig()).wrapClass(Config.class).load();

        this.registerListener(new ConnectionListener(this));
        this.registerListener(new EntityActionListener(this));
        this.registerListener(new PlayerActionListener(this));
        this.registerListener(new WorldEventListener(this));

        this.addCommand(new HatCommand(this));
        this.addCommand(new PingCommand(this));
        this.addCommand(new ReloadConfigurationCommand(this));
        // this.addCommand(new ShareExperienceCommand(this));
        // this.addCommand(new SitCommand(this));
        this.addCommand(new ToggleLogStrippingCommand(this));

        getLogger().info("Init complete");
    }

    public void registerListener(BaseListener baseListener) {
        getServer().getPluginManager().registerEvents(baseListener, this);
    }

    public boolean addCommand(BaseCommand baseCommand) {
        var pluginCommand = this.getCommand(baseCommand.getName());
        if (pluginCommand == null) return false;

        pluginCommand.setExecutor(baseCommand);
        return true;
    }

    public PlayerData getPlayerData(UUID uuid) {
        return playerData.computeIfAbsent(uuid, v -> new PlayerData());
    }

    public void removePlayerData(UUID uuid) {
        playerData.remove(uuid);
    }

    @Override
    public void onDisable() {
        this.saveConfig();

        getLogger().info("Unload complete");
    }
}
