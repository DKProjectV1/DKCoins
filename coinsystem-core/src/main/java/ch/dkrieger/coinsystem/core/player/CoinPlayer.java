package ch.dkrieger.coinsystem.core.player;

import ch.dkrieger.coinsystem.core.CoinSystem;
import ch.dkrieger.coinsystem.core.config.Config;
import ch.dkrieger.coinsystem.core.event.CoinChangeEventResult;
import ch.dkrieger.coinsystem.core.event.CoinsUpdateCause;
import net.md_5.bungee.api.ChatColor;

import java.util.UUID;

public class CoinPlayer {
	
	private int id;
	private String name, color;
	private UUID uuid;
	private long coins, firstLogin,lastLogin;
	
	public CoinPlayer(int id, UUID uuid, String name,String color, long firstLogin, long lastLogin, long coins){
		this.id = id;
		this.uuid = uuid;
		this.name = name;
		this.color = color;
		this.firstLogin = firstLogin;
		this.lastLogin = lastLogin;
		this.coins = coins;
	}
	public int getID(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public UUID getUUID(){
		return this.uuid;
	}

	/*

	Removed IP address saving, because in the new DSGVO regulation (European Union)
	 ist is not allowed to save ip address for a long time (When not necessary).

	 */

	@SuppressWarnings("Removed IP address saving, because in the new DSGVO regulation (European Union)\n" +
			"\t ist is not allowed to save ip address for a long time (When not necessary)")
	@Deprecated
	public String getIP(){
		return "Removed IP address saving, because in the new DSGVO regulation (European Union) ist is not allowed to save ip address for a long time (When not necessary)";
	}
	public long getFirstLogin(){
		return this.firstLogin;
	}
	public long getLastLogin(){
		return this.lastLogin;
	}
	public String getColor(){
		if(Config.getInstance().liveColorUpdate){
			String color = CoinSystem.getInstance().getPlatform().getColor(this);
			if(color != null) this.color = color;
		}
		return ChatColor.translateAlternateColorCodes('&',this.color);
	}
	public long getCoins(){
		return this.coins;
	}
	public Boolean hasCoins(long coins){
		if(this.coins >= coins) return true;
		return false;
	}
	public void addCoins(long coins){
		addCoins(coins,null,null);
	}
	public void addCoins(long coins, CoinsUpdateCause cause){
		addCoins(coins,cause,null);
	}
	public void addCoins(long coins, String message){
		addCoins(coins,null,message);
	}
	public void addCoins(long coins, CoinsUpdateCause cause, String message){
		setCoins(this.coins+coins,cause,message);
	}
	public void removeCoins(long coins){
		removeCoins(coins,null,null);
	}
	public void removeCoins(long coins, CoinsUpdateCause cause){
		removeCoins(coins,cause,null);
	}
	public void removeCoins(long coins, String message){
		removeCoins(coins,null,message);
	}
	public void removeCoins(long coins, CoinsUpdateCause cause, String message){
		setCoins(this.coins-coins,cause,message);
	}
	public void setCoins(long coins){
		setCoins(coins,null,null);
	}
	public void setCoins(long coins, CoinsUpdateCause cause){
		setCoins(coins,cause,null);
	}
	public void setCoins(long coins, String message){
		setCoins(coins,null,message);
	}
	public void setCoins(long coins, CoinsUpdateCause cause, String message){
		final long oldCoins = this.coins;
		saveCoins(oldCoins,coins,cause,message);
	}
	@SuppressWarnings("This is only for storages")
	public void setIDSimpled(int id){
		this.id = id;
	}
	@SuppressWarnings("This is only for storages")
	public void setCoinsSimpled(long coins){
		this.coins = coins;
	}
	@SuppressWarnings("This is only for storages")
	public void setNameSimpled(String name){
		this.name = name;
	}
	@SuppressWarnings("This is only for storages")
	public void setLastLoginSimpled(long lastLogin){
		this.lastLogin = lastLogin;
	}
	public void setColorSimpled(String color){
		this.color = color;
	}
	public void setColor(String color){
		if(color == null || this.color.equals(color)) return;
		this.color = color;
		CoinSystem.getInstance().getStorage().updateColor(this.uuid,color);
	}
	public void updateInfos(String name, String color, long lastLogin){
		this.name = name;
		this.lastLogin = lastLogin;
		if(color != null) this.color = color;
		if(this.color == null) this.color = Config.getInstance().defaultColor;
		CoinSystem.getInstance().getStorage().updateInformations(this.uuid,name,this.color,lastLogin);
	}
	private void saveCoins(long oldCoins,long newCoins, CoinsUpdateCause cause, String message){
		if(cause == null) cause = CoinsUpdateCause.API;
		if(message == null) message = cause.toString();
		CoinChangeEventResult result = CoinSystem.getInstance().getPlatform().executeCoinChangeEvent(this,oldCoins,newCoins,cause,message);
		if(result == null) return;
		if(result.isCancelled()) return;
		this.coins = result.getCoins();
		CoinSystem.getInstance().getStorage().updateCoins(this.uuid,coins);
	}
}
