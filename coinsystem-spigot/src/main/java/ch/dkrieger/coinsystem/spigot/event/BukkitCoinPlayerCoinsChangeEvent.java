package ch.dkrieger.coinsystem.spigot.event;

import ch.dkrieger.coinsystem.core.event.CoinsUpdateCause;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ch.dkrieger.coinsystem.core.player.CoinPlayer;

public class BukkitCoinPlayerCoinsChangeEvent extends Event{
	
	private static final HandlerList handlers = new HandlerList();
	private CoinPlayer coinplayer;
	private String message;
	private Long oldcoins, newcoins;
	private CoinsUpdateCause cause;
	private boolean cancelled;
	
	public BukkitCoinPlayerCoinsChangeEvent(CoinPlayer coinplayer, Long oldcoins, Long newcoins, CoinsUpdateCause cause, String message){
		this.coinplayer = coinplayer;
		this.oldcoins = oldcoins;
		this.newcoins = newcoins;
		this.cause = cause;
		this.message = message;
        cancelled = false;
	}
	@Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
    public CoinPlayer getCoinPlayer(){
    	return this.coinplayer;
    }
    public String getMessage() {
        return message;
    }
    public Long getOldCoins(){
    	return this.oldcoins;
    }
    public Long getNewCoins(){
        return this.newcoins;
    }
    public CoinsUpdateCause getCause() {
        return cause;
    }
    public boolean isCancelled(){
	    return cancelled;
    }
    public void setCancelled(Boolean value){
	    this.cancelled = value;
    }
    public void setNewCoins(Long coins){
	    this.newcoins = coins;
    }
}
