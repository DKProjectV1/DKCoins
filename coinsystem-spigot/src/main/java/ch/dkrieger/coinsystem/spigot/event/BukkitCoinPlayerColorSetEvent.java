package ch.dkrieger.coinsystem.spigot.event;

import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import ch.dkrieger.coinsystem.spigot.SpigotCoinSystemBootstrap;
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
    private final CoinPlayer coinPlayer;
    private final Player player;

    public BukkitCoinPlayerColorSetEvent(String color, CoinPlayer coinPlayer, Player player) {
        super(Thread.currentThread() != SpigotCoinSystemBootstrap.MAIN_SERVER_THREAD);
        this.color = color;
        this.coinPlayer = coinPlayer;
        this.player = player;
    }
    public String getColor(){
        return this.color;
    }

    public CoinPlayer getCoinPlayer() {
        return this.coinPlayer;
    }

    public Player getPlayer() {
        return player;
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
