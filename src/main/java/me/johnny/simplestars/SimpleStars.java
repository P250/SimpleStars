package me.johnny.simplestars;

import me.johnny.simplestars.commands.CommandViewLevel;
import me.johnny.simplestars.commands.CommandViewTopLevels;
import me.johnny.simplestars.events.PlayerGainExpListener;
import me.johnny.simplestars.events.PlayerJoinListener;
import me.johnny.simplestars.events.PlayerKilledListener;
import me.johnny.simplestars.util.PlayerTopLevelsCacheManager;
import me.johnny.simplestars.util.SimpleStarsConstants;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class SimpleStars extends JavaPlugin {

    private PlayerTopLevelsCacheManager playerTopLevelsCacheManager;
    private BukkitTask playerTopLevelsCacheRunnable;
    private FileConfiguration settingsConfig;
    private File settingsConfigFile;
    private FileConfiguration playerConfig;
    private File playerConfigFile;

    private void setupSettingsConfig() {
        settingsConfigFile = new File(getDataFolder(), "simplestars_config.yml");
        settingsConfig = YamlConfiguration.loadConfiguration(settingsConfigFile);

        if (!(settingsConfigFile.exists())) {
            HashMap<String, Object> defaults = new HashMap<String, Object>();
            // Defaults relating to Level
            defaults.put(SimpleStarsConstants.CONFIG_LEVELS_BASELEVEL, 1);
            defaults.put(SimpleStarsConstants.CONFIG_LEVELS_BASEEXP, 1000);

            // Defaults relating to Commands
            defaults.put(SimpleStarsConstants.CONFIG_COMMANDS_LEVEL_VIEWSTATS, "&7&l[SimpleStars] &r&bLevel of &l%player% &r&bis &6&l%level% &r&bwith &6&l%exp% &bxp total");
            defaults.put(SimpleStarsConstants.CONFIG_COMMANDS_LEVEL_INVALIDPLAYER, "&7&l[SimpleStars] &r&cError, player name is invalid.");
            defaults.put(SimpleStarsConstants.CONFIG_COMMANDS_LEVELTOP_LEADERBOARDFORMAT, "&7&l%ranking%. &r&6&l%player% &r&6with &6&l%exp% &r&bxp total");

            // Defaults relating to permissions level.lvltop
            defaults.put(SimpleStarsConstants.CONFIG_PERMISSIONS_COMMANDS_LEVEL, "level.curlvl");
            defaults.put(SimpleStarsConstants.CONFIG_PERMISSIONS_COMMANDS_LEVEL_NO_PERMISSION, "&4You don't have permission for this command.");
            defaults.put(SimpleStarsConstants.CONFIG_PERMISSIONS_COMMANDS_LEVELTOP, "level.lvltop");
            defaults.put(SimpleStarsConstants.CONFIG_PERMISSIONS_COMMANDS_LEVELTOP_NO_PERMISSION, "&4You don't have permission for this command.");

            settingsConfig.addDefaults(defaults);
            settingsConfig.options().copyDefaults(true);
        }
        try {
            settingsConfig.save(settingsConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupPlayerConfig() {
        playerConfigFile = new File(getDataFolder(), "simplestars_player-levels.yml");
        playerConfig = YamlConfiguration.loadConfiguration(playerConfigFile);

        try {
            playerConfig.save(playerConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        setupSettingsConfig();
        setupPlayerConfig();
        playerTopLevelsCacheManager = new PlayerTopLevelsCacheManager(playerConfig);
        /** Run the cache service every 1 minute **/
        playerTopLevelsCacheRunnable = playerTopLevelsCacheManager.runTaskTimerAsynchronously(this, 0L, 20L * 60L);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(settingsConfig, playerConfig, playerConfigFile), this);
        Bukkit.getPluginManager().registerEvents(new PlayerKilledListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerGainExpListener(settingsConfig, playerConfig, playerConfigFile), this);
        getCommand("curlevel").setExecutor(new CommandViewLevel(settingsConfig, playerConfig));
        getCommand("leveltop").setExecutor(new CommandViewTopLevels(playerTopLevelsCacheManager, playerConfig, settingsConfig));
    }

    @Override
    public void onDisable() {
        playerTopLevelsCacheRunnable.cancel();
    }

}
