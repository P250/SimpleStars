package me.johnny.simplestars.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerKilledListener implements Listener {

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent e) {
        Player damaged = (Player) e.getEntity();
        Player damager = damaged.getKiller();

        if (damager == null) {
            return;
        }

        Bukkit.getPluginManager().callEvent(new PlayerGainExpByKillEvent(damager, damaged));

    }

}
