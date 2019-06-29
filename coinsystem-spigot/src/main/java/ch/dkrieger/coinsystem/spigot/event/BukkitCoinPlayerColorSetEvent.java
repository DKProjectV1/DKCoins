package ch.dkrieger.coinsystem.spigot.event;

import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 24.11.18 18:34
 *
 */

public class BukkitCoinPlayerColorSetEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private String color;
    private CoinPlayer player;
    private Player bukkitPlayer;

    public BukkitCoinPlayerColorSetEvent(String color, CoinPlayer player, Player bukkitPlayer) {
        super(Thread.currentThread().getId() != 1);
        this.color = color;
        this.player = player;
        this.bukkitPlayer = bukkitPlayer;
    }
    public String getColor(){
        return this.color;
    }
    public CoinPlayer getPlayer() {
        return this.player;
    }
    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }
    public void setColor(String color){
        this.color = color;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
