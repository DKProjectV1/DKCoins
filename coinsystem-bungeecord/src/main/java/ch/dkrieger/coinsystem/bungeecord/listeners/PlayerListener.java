package ch.dkrieger.coinsystem.bungeecord.listeners;


import ch.dkrieger.coinsystem.bungeecord.BungeeCordCoinSystemBootstrap;
import ch.dkrieger.coinsystem.core.CoinSystem;
import ch.dkrieger.coinsystem.core.manager.MessageManager;
import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener{
	
	@EventHandler
	public void onLogin(final LoginEvent event){
		BungeeCord.getInstance().getScheduler().runAsync(BungeeCordCoinSystemBootstrap.getInstance(), ()->{
			CoinPlayer player = null;
			try {
				try{
					player = CoinSystem.getInstance().getPlayerManager().getPlayerSave(event.getConnection().getUniqueId());
				}catch (Exception exception2){}
				player = CoinSystem.getInstance().getPlayerManager().getPlayerSave(event.getConnection().getUniqueId());
			}catch (Exception exception){
				event.setCancelled(true);
				event.setCancelReason(new TextComponent("§cError"));
				exception.printStackTrace();
			}
			if(player == null) player = CoinSystem.getInstance().getPlayerManager().createPlayer(event.getConnection().getName()
					,event.getConnection().getUniqueId());
			else player.updateInfos(event.getConnection().getName(),CoinSystem.getInstance().getPlatform().getColor(player)
					,System.currentTimeMillis());
		});
	}
	@EventHandler
	public void onLeave(final PlayerDisconnectEvent event){
		BungeeCord.getInstance().getScheduler().runAsync(BungeeCordCoinSystemBootstrap.getInstance(),()->{
			CoinPlayer player = CoinSystem.getInstance().getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
			if(player != null) player.updateInfos(event.getPlayer().getName(),CoinSystem.getInstance().getPlatform().getColor(player)
					,System.currentTimeMillis());
		});
	}
}
