package me.johnny.simplestars.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerTopLevelsCacheManager extends BukkitRunnable {

    private final FileConfiguration playerConfig;
    private final HashMap<UUID, Integer> playerLevelsCache = new HashMap<>();
    private final LinkedHashMap<UUID, Integer> playerLevelsCacheSorted = new LinkedHashMap<>();

    public PlayerTopLevelsCacheManager(FileConfiguration playerConfig) {
        this.playerConfig = playerConfig;
    }

    public void run() {
        /** Reset the caches **/
        playerLevelsCache.clear();
        playerLevelsCacheSorted.clear();

        /** Grab every players level and cache the value **/
        for (String uuid : playerConfig.getKeys(false)) {
            int playerLevel = playerConfig.getInt(String.format(SimpleStarsConstants.CONFIG_PLAYERS_FORMAT_LEVEL, uuid));
            playerLevelsCache.put(UUID.fromString(uuid), playerLevel);
        }

        /** Sort the levels in ascending order, using comparingByValue() (the "natural order") **/
        playerLevelsCache.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(sortedValue -> playerLevelsCacheSorted.put(sortedValue.getKey(), sortedValue.getValue()));
    }

    /** The class that displays leaderboard can use this for values **/
    public LinkedHashMap<UUID, Integer> getPlayerLevelsCacheSorted() {
        return playerLevelsCacheSorted;
    }
}
