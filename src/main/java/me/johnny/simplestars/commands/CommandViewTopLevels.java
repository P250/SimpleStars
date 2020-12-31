package me.johnny.simplestars.commands;

import me.johnny.simplestars.SimpleStars;
import me.johnny.simplestars.util.PlayerTopLevelsCacheManager;
import me.johnny.simplestars.util.SimpleStarsConstants;
import me.johnny.simplestars.util.SimpleStarsUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandViewTopLevels implements CommandExecutor {

    private final PlayerTopLevelsCacheManager playerTopLevelsCacheManager;
    private final HashMap<UUID, Integer> playerTopLevelsCacheSorted;
    private final FileConfiguration settingsConfig;
    private final FileConfiguration playerConfig;

    public CommandViewTopLevels(PlayerTopLevelsCacheManager playerTopLevelsCacheManager, FileConfiguration playerConfig, FileConfiguration settingsConfig) {
        this.playerTopLevelsCacheManager = playerTopLevelsCacheManager;
        this.playerTopLevelsCacheSorted = playerTopLevelsCacheManager.getPlayerLevelsCacheSorted();
        this.playerConfig = playerConfig;
        this.settingsConfig = settingsConfig;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player pl = (Player) sender;
            String permission = settingsConfig.getString(SimpleStarsConstants.CONFIG_PERMISSIONS_COMMANDS_LEVELTOP);
            if (!(pl.hasPermission(permission))) {
                String errorMessage = settingsConfig.getString(SimpleStarsConstants.CONFIG_PERMISSIONS_COMMANDS_LEVELTOP_NO_PERMISSION);
                pl.sendMessage(SimpleStarsUtil.cc((errorMessage)));
                return true;
            }
        }

        if (playerTopLevelsCacheSorted.isEmpty()) {
            sender.sendMessage(SimpleStarsUtil.cc("&4Leaderboard cache is empty.. try again in a couple of minutes (if persistent errors occur contact an admin)."));
            return true;
        }

        List<String> leaderboardMessage = new ArrayList<>();
        List<UUID> uuidArrayList = new ArrayList<>(playerTopLevelsCacheSorted.keySet());

        String leaderboardFormat = settingsConfig.getString(SimpleStarsConstants.CONFIG_COMMANDS_LEVELTOP_LEADERBOARDFORMAT);

        for (int i = 0; i < playerTopLevelsCacheSorted.size(); i++) {
            int ranking = i + 1;
            String leaderboardFormatWithValues = leaderboardFormat
                    .replace("%ranking%", Integer.toString(ranking))
                    .replace("%player%", Bukkit.getPlayer(uuidArrayList.get(i)).getName())
                    .replace("%exp%", Integer.toString(playerConfig.getInt(String.format(SimpleStarsConstants.CONFIG_PLAYERS_FORMAT_EXP, uuidArrayList.get(i)))));
            leaderboardMessage.add(SimpleStarsUtil.cc(leaderboardFormatWithValues));
            if (ranking == 10) {
                break;
            }
        }
        String[] leaderboardMessageArray = leaderboardMessage.toArray(new String[0]);
        sender.sendMessage(leaderboardMessageArray);
        return true;
    }
}
