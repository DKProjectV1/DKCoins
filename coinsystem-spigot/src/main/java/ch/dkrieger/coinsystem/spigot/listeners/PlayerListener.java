package ch.dkrieger.coinsystem.spigot.listeners;

import ch.dkrieger.coinsystem.core.CoinSystem;
import ch.dkrieger.coinsystem.core.manager.MessageManager;
import ch.dkrieger.coinsystem.core.manager.PermissionManager;
import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import ch.dkrieger.coinsystem.spigot.SpigotCoinSystemBootstrap;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener{
	
	@EventHandler
	public void onLogin(final PlayerLoginEvent event){
		Bukkit.getScheduler().runTaskAsynchronously(SpigotCoinSystemBootstrap.getInstance(), ()->{
			if(!CoinSystem.getInstance().getStorage().isConnected()){
				if(event.getPlayer().hasPermission(PermissionManager.getInstance().admin)){
					event.getPlayer().sendMessage(MessageManager.getInstance().mysql_noconnection);
				}
				return;
			}
			if(CoinSystem.getInstance().getConfig().system_player_onlyproxy_check) return;
			CoinPlayer player = null;
			try {
				try{
					player = CoinSystem.getInstance().getPlayerManager().getPlayerSave(event.getPlayer().getUniqueId());
				}catch (Exception ignored){}
				player = CoinSystem.getInstance().getPlayerManager().getPlayerSave(event.getPlayer().getUniqueId());
			}catch (Exception exception){
				event.disallow(PlayerLoginEvent.Result.KICK_BANNED,"§cError");
				exception.printStackTrace();
			}
			if(player == null) CoinSystem.getInstance().getPlayerManager().createPlayer(event.getPlayer().getName(),event.getPlayer().getUniqueId());
			else player.updateInfos(event.getPlayer().getName(),CoinSystem.getInstance().getPlatform().getColor(player)
					,System.currentTimeMillis());
		});
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		if(CoinSystem.getInstance().getConfig().system_player_onlyproxy_check) return;
		Bukkit.getScheduler().runTaskAsynchronously(SpigotCoinSystemBootstrap.getInstance(),()->{
			CoinPlayer player = CoinSystem.getInstance().getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
			if(player != null) player.updateInfos(event.getPlayer().getName(),CoinSystem.getInstance().getPlatform().getColor(player)
					,System.currentTimeMillis());
		});
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		Bukkit.getScheduler().runTaskAsynchronously(SpigotCoinSystemBootstrap.getInstance(),()->{
			if(CoinSystem.getInstance().getConfig().system_player_addcoinsonkill && e.getEntity().getKiller() != null){
				CoinPlayer coinplayer = CoinSystem.getInstance().getPlayerManager().getPlayer(e.getEntity().getKiller().getUniqueId());
				if(coinplayer != null) coinplayer.addCoins(CoinSystem.getInstance().getConfig().system_player_addcoinsonkill_amount,"kill");
			}
			if(CoinSystem.getInstance().getConfig().system_player_removecoinsondeath){
				CoinPlayer coinplayer = CoinSystem.getInstance().getPlayerManager().getPlayer(e.getEntity().getUniqueId());
				if(coinplayer != null){
					if(coinplayer.getCoins() == 0) return;
					coinplayer.removeCoins(Math.min(coinplayer.getCoins(), CoinSystem.getInstance().getConfig().system_player_removecoinsondeath_amount), "death");
				}
			}
		});
	}
}
