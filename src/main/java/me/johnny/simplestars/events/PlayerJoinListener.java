package me.johnny.simplestars.events;

import me.johnny.simplestars.util.SimpleStarsConstants;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;

public class PlayerJoinListener implements Listener {

    private final FileConfiguration settingsConfig;
    private final FileConfiguration playerConfig;
    private final File playerConfigFile;

    public PlayerJoinListener(FileConfiguration settingsConfig, FileConfiguration playerConfig, File playerConfigFile) {
        this.settingsConfig = settingsConfig;
        this.playerConfig = playerConfig;
        this.playerConfigFile = playerConfigFile;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player pl = e.getPlayer();
        if (!(playerConfig.get(pl.getUniqueId().toString()) == null)) {
            return;
        }

        int defaultLevel = settingsConfig.getInt(SimpleStarsConstants.CONFIG_LEVELS_BASELEVEL);
        int defaultExp = settingsConfig.getInt(SimpleStarsConstants.CONFIG_LEVELS_BASEEXP);

        String playerLevelPath = String.format(SimpleStarsConstants.CONFIG_PLAYERS_FORMAT_LEVEL,
                pl.getUniqueId().toString());

        String playerExpPath = String.format(SimpleStarsConstants.CONFIG_PLAYERS_FORMAT_EXP,
                pl.getUniqueId().toString());

        playerConfig.set(playerLevelPath, defaultLevel);
        playerConfig.set(playerExpPath, defaultExp);

        try {
            playerConfig.save(playerConfigFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
