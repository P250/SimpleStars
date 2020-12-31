package me.johnny.simplestars.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerGainExpByKillEvent extends Event {

    private static HandlerList handlers = new HandlerList();
    private Player killer;
    private Player killed;

    public PlayerGainExpByKillEvent(Player killer, Player killed) {
        this.killer = killer;
        this.killed = killed;
    }

    public Player getKiller() {
        return killer;
    }

    public void setKiller(Player killer) {
        this.killer = killer;
    }

    public Player getKilled() {
        return killed;
    }

    public void setKilled(Player killed) {
        this.killed = killed;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
