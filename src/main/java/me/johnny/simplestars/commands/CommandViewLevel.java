package me.johnny.simplestars.commands;

import me.johnny.simplestars.events.PlayerGainExpByKillEvent;
import me.johnny.simplestars.util.SimpleStarsConstants;
import me.johnny.simplestars.util.SimpleStarsUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


public class CommandViewLevel implements CommandExecutor {

    private final FileConfiguration settingsConfig;
    private final FileConfiguration playerConfig;

    public CommandViewLevel(FileConfiguration settingsConfig, FileConfiguration playerConfig) {
        this.settingsConfig = settingsConfig;
        this.playerConfig = playerConfig;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0 && sender instanceof Player) {
            Player pl = (Player) sender;
            String permission = settingsConfig.getString(SimpleStarsConstants.CONFIG_PERMISSIONS_COMMANDS_LEVEL);
            if (!(pl.hasPermission(permission))) {
                String errorMessage = settingsConfig.getString(SimpleStarsConstants.CONFIG_PERMISSIONS_COMMANDS_LEVEL_NO_PERMISSION);
                pl.sendMessage(SimpleStarsUtil.cc(errorMessage));
                return true;
            }

            String viewStatsString = settingsConfig.getString(SimpleStarsConstants.CONFIG_COMMANDS_LEVEL_VIEWSTATS);

            String playerLevelPath = String.format(SimpleStarsConstants.CONFIG_PLAYERS_FORMAT_LEVEL,
                    pl.getUniqueId().toString());

            String playerExpPath = String.format(SimpleStarsConstants.CONFIG_PLAYERS_FORMAT_EXP,
                    pl.getUniqueId().toString());

            int playerLevel = playerConfig.getInt(playerLevelPath);
            int playerExp = playerConfig.getInt(playerExpPath);

            String viewStatsStringWithValues = viewStatsString
                    .replace("%player%", pl.getName())
                    .replace("%level%", Integer.toString(playerLevel))
                    .replace("%exp%", Integer.toString(playerExp));

            pl.sendMessage(SimpleStarsUtil.cc(viewStatsStringWithValues));
            Bukkit.getPluginManager().callEvent(new PlayerGainExpByKillEvent(pl, null));
            return true;

        } else if (args.length == 0) {
            sender.sendMessage(ChatColor.DARK_RED + "You can't view your own level if you aren't a player can you ..?");
            return true;
        } else {
            Player pl = (Player) sender;
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

            if (!(target.hasPlayedBefore())) {
                String errorMessage = settingsConfig.getString(SimpleStarsConstants.CONFIG_COMMANDS_LEVEL_INVALIDPLAYER);
                pl.sendMessage(SimpleStarsUtil.cc(errorMessage));
                return true;
            }

            String viewStatsString = settingsConfig.getString(SimpleStarsConstants.CONFIG_COMMANDS_LEVEL_VIEWSTATS);

            String playerLevelPath = String.format(SimpleStarsConstants.CONFIG_PLAYERS_FORMAT_LEVEL,
                    target.getUniqueId().toString());

            String playerExpPath = String.format(SimpleStarsConstants.CONFIG_PLAYERS_FORMAT_EXP,
                    target.getUniqueId().toString());

            int playerLevel = playerConfig.getInt(playerLevelPath);
            int playerExp = playerConfig.getInt(playerExpPath);

            String viewStatsStringWithValues = viewStatsString
                    .replace("%player%", target.getName())
                    .replace("%level%", Integer.toString(playerLevel))
                    .replace("%exp%", Integer.toString(playerExp));

            pl.sendMessage(SimpleStarsUtil.cc(viewStatsStringWithValues));
            return true;

        }
    }
}
