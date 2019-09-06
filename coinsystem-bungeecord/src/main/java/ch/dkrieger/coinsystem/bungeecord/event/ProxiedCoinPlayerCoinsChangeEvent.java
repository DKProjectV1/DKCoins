package ch.dkrieger.coinsystem.bungeecord.event;

import ch.dkrieger.coinsystem.core.event.CoinsUpdateCause;
import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import net.md_5.bungee.api.plugin.Event;

public class ProxiedCoinPlayerCoinsChangeEvent extends Event{

	private final CoinPlayer coinplayer;
	private long oldCoins, newCoins;
	private boolean cancelled;
	private String message;
	private CoinsUpdateCause cause;
	
	public ProxiedCoinPlayerCoinsChangeEvent(CoinPlayer coinplayer, long oldCoins, long newCoins, CoinsUpdateCause cause, String message){
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

	public long getNewCoins(){
    	return this.newCoins;
    }

    public long getOldCoins(){
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

	public void setNewCoins(long amount){
		this.newCoins = amount;
	}
}
