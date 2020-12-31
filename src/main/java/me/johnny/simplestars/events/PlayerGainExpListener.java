package me.johnny.simplestars.events;

import me.johnny.simplestars.util.SimpleStarsConstants;
import me.johnny.simplestars.util.SimpleStarsUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;

public class PlayerGainExpListener implements Listener {

    private FileConfiguration settingsConfig;
    private FileConfiguration playerConfig;
    private File playerConfigFile;

    public PlayerGainExpListener(FileConfiguration settingsConfig, FileConfiguration playerConfig, File playerConfigFile) {
        this.settingsConfig = settingsConfig;
        this.playerConfig = playerConfig;
        this.playerConfigFile = playerConfigFile;
    }

    @EventHandler
    public void onPlayerKill(PlayerGainExpByKillEvent e) {

        Player killer = e.getKiller();

        if (e.getKilled() == null) {
            String killerExpPath = String.format(SimpleStarsConstants.CONFIG_PLAYERS_FORMAT_EXP,
                    killer.getUniqueId().toString());
            String killerLevelPath = String.format(SimpleStarsConstants.CONFIG_PLAYERS_FORMAT_LEVEL,
                    killer.getUniqueId().toString());
            int killerExp = playerConfig.getInt(killerExpPath);
            int killerLevel = playerConfig.getInt(killerLevelPath);
            int expNeededForNextLevel = killerExp * (killerLevel + 1);
            Bukkit.getLogger().info("Exp needed for next level: " + expNeededForNextLevel);
            return;
        }

        Player killed = e.getKilled();

        String killerLevelPath = String.format(SimpleStarsConstants.CONFIG_PLAYERS_FORMAT_LEVEL,
                killer.getUniqueId().toString());

        String killerExpPath = String.format(SimpleStarsConstants.CONFIG_PLAYERS_FORMAT_EXP,
                killer.getUniqueId().toString());

        String killedLevelPath = String.format(SimpleStarsConstants.CONFIG_PLAYERS_FORMAT_LEVEL,
                killed.getUniqueId().toString());

        String killedExpPath = String.format(SimpleStarsConstants.CONFIG_PLAYERS_FORMAT_EXP,
                killed.getUniqueId().toString());

        int killerLevel = playerConfig.getInt(killerLevelPath);
        int killerExp = playerConfig.getInt(killerExpPath);

        Bukkit.getLogger().info("Killer level before calculation: " + killerLevel);

        int killedLevel = playerConfig.getInt(killedLevelPath);
        int killedExp = playerConfig.getInt(killedExpPath);


        /** Exp gained = 15 * level_killed/current_level * 5 **/
        int expGained = (15 * killedLevel) / (killerLevel * 5);
        /** Set the players Exp then calculate if we need to increment their level or not **/
        playerConfig.set(killerExpPath, killerExp + expGained);
        /** Exp needed for next level - this was a shit show to figure out **/
        int expNeededForNextLevel = SimpleStarsUtil.calculateExpForLevel(killerLevel + 1, settingsConfig.getInt(SimpleStarsConstants.CONFIG_LEVELS_BASEEXP));
        /** If Exp level up criteria is satisfied then increment the level **/
        if ((killerExp + expGained) > expNeededForNextLevel) {
            playerConfig.set(killerLevelPath, killerLevel + 1);
        }
        Bukkit.getLogger().info("Total Exp needed for next level: " + expNeededForNextLevel);
        Bukkit.getLogger().info("Killer level after calculation: " + killerLevel);

        try {
            playerConfig.save(playerConfigFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
