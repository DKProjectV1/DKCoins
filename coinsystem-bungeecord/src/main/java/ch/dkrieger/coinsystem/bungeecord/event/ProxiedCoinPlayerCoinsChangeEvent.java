package ch.dkrieger.coinsystem.bungeecord.event;

import ch.dkrieger.coinsystem.core.event.CoinsUpdateCause;
import net.md_5.bungee.api.plugin.Event;
import ch.dkrieger.coinsystem.core.player.CoinPlayer;

public class ProxiedCoinPlayerCoinsChangeEvent extends Event{


	private CoinPlayer coinplayer;
	private Long oldCoins, newCoins;
	private boolean cancelled;
	private String message;
	private CoinsUpdateCause cause;
	
	public ProxiedCoinPlayerCoinsChangeEvent(CoinPlayer coinplayer, Long oldCoins, Long newCoins, CoinsUpdateCause cause, String message){
		this.newCoins = newCoins;
		this.oldCoins = oldCoins;
		this.coinplayer = coinplayer;
		this.cause = cause;
		this.message = message;
		this.cancelled = false;
	}
    public CoinPlayer getCoinPlayer(){
    	return this.coinplayer;
    }
	public String getMessage() {
		return message;
	}
	public Long getNewCoins(){
    	return this.newCoins;
    }
    public Long getOldCoins(){
		return this.oldCoins;
	}
	public CoinsUpdateCause getCause() {
		return cause;
	}
	public boolean isCancelled(){
		return this.cancelled;
	}
	public void setCancelled(Boolean value){
		this.cancelled = value;
	}
	public void setNewCoins(Long amount){
		this.newCoins = amount;
	}
}
