package ch.dkrieger.coinsystem.bungeecord.event;

/*
 * Copyright (c) 2018 Davide W. created on 06.02.18 18:52
 */

import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

public class ProxiedCoinPlayerColorSetEvent extends Event{

    private String color;
    private final CoinPlayer coinPlayer;
    private final ProxiedPlayer proxiedPlayer;

    public ProxiedCoinPlayerColorSetEvent(String color, CoinPlayer coinPlayer, ProxiedPlayer proxiedPlayer) {
        this.color = color;
        this.coinPlayer = coinPlayer;
        this.proxiedPlayer = proxiedPlayer;
    }

    public String getColor(){
        return this.color;
    }

    public CoinPlayer getCoinPlayer() {
        return coinPlayer;
    }

    public ProxiedPlayer getProxiedPlayer() {
        return proxiedPlayer;
    }

    public void setColor(String color){
        this.color = color;
    }
}
