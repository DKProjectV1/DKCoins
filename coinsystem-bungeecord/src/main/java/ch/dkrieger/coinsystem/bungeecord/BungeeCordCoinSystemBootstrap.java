package ch.dkrieger.coinsystem.bungeecord;

import ch.dkrieger.coinsystem.bungeecord.event.ProxiedCoinPlayerCoinsChangeEvent;
import ch.dkrieger.coinsystem.bungeecord.event.ProxiedCoinPlayerColorSetEvent;
import ch.dkrieger.coinsystem.core.CoinSystem;
import ch.dkrieger.coinsystem.core.DKCoinsPlatform;
import ch.dkrieger.coinsystem.core.event.CoinChangeEventResult;
import ch.dkrieger.coinsystem.core.event.CoinsUpdateCause;
import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import ch.dkrieger.coinsystem.core.player.PlayerColor;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import ch.dkrieger.coinsystem.bungeecord.listeners.PlayerListener;

import java.io.File;

public class BungeeCordCoinSystemBootstrap extends Plugin implements DKCoinsPlatform {
	
	private static BungeeCordCoinSystemBootstrap instance;

	@Override
	public void onLoad() {
		instance = this;

		new CoinSystem(this);
	}

	@Override
	public void onEnable() {
		BungeeCord.getInstance().getPluginManager().registerListener(this,new PlayerListener());
	}
	@Override
	public void onDisable(){
		CoinSystem.getInstance().shutdown();
	}

	@Override
	public String getPlatformName() {
		return "BungeeCord";
	}

	@Override
	public String getServerVersion() {
		return BungeeCord.getInstance().getVersion()+" | "+BungeeCord.getInstance().getGameVersion();
	}

	@Override
	public File getFolder() {
		return new File("plugins/DKCoins/");
	}

	@Override
	public String getColor(CoinPlayer player) {
		ProxiedPlayer proxyPlayer = BungeeCord.getInstance().getPlayer(player.getUUID());
		if(proxyPlayer == null) return null;
		String color = CoinSystem.getInstance().getConfig().defaultColor;
		for(PlayerColor colors : CoinSystem.getInstance().getConfig().playerColors){
			if(proxyPlayer.hasPermission(colors.getPermission())){
				color = colors.getColor();
				break;
			}
		}
		ProxiedCoinPlayerColorSetEvent event = new ProxiedCoinPlayerColorSetEvent(color,player,proxyPlayer);
		BungeeCord.getInstance().getPluginManager().callEvent(event);
		if(event.getColor() != null) color = event.getColor();
		return color;
	}
	@Override
	public CoinChangeEventResult executeCoinChangeEvent(CoinPlayer player, Long oldCoins, Long newCoins, CoinsUpdateCause cause, String message) {
		ProxiedCoinPlayerCoinsChangeEvent event = new ProxiedCoinPlayerCoinsChangeEvent(player,oldCoins,newCoins,cause,message);
		BungeeCord.getInstance().getPluginManager().callEvent(event);
		return new CoinChangeEventResult(event.isCancelled(),event.getNewCoins());
	}
	public static BungeeCordCoinSystemBootstrap getInstance(){
		return instance;
	}
}
