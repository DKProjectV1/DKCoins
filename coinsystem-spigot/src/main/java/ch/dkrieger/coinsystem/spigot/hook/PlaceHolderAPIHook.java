package ch.dkrieger.coinsystem.spigot.hook;

import ch.dkrieger.coinsystem.core.CoinSystem;
import ch.dkrieger.coinsystem.spigot.SpigotCoinSystemBootstrap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import me.clip.placeholderapi.external.EZPlaceholderHook;

public class PlaceHolderAPIHook extends EZPlaceholderHook{

	public PlaceHolderAPIHook(Plugin plugin, String identifier) {
		super(plugin, identifier);
	}
	@Override
	public String onPlaceholderRequest(Player player, String identifier) {
		if(identifier.equalsIgnoreCase("coins") || identifier.equalsIgnoreCase("coin") || identifier.equalsIgnoreCase("money")){
			CoinPlayer coinplayer = CoinSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
			if(coinplayer != null) return SpigotCoinSystemBootstrap.getInstance().format(coinplayer.getCoins());
		}else if(identifier.equalsIgnoreCase("firstlogin")){
			CoinPlayer coinplayer = CoinSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
			if(coinplayer != null) return CoinSystem.getInstance().getConfig().dateFormat.format(coinplayer.getFirstLogin());
		}else if(identifier.equalsIgnoreCase("lastlogin")){
			CoinPlayer coinplayer = CoinSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
			if(coinplayer != null) return CoinSystem.getInstance().getConfig().dateFormat.format(coinplayer.getLastLogin());
		}else if(identifier.equalsIgnoreCase("id") || identifier.equalsIgnoreCase("playerid")){
			CoinPlayer coinplayer = CoinSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
			if(coinplayer != null) return ""+coinplayer.getID();
		}else if(identifier.equalsIgnoreCase("top") || identifier.equalsIgnoreCase("topcoins") || identifier.equalsIgnoreCase("coinstop")|| identifier.equalsIgnoreCase("topcoin") || identifier.equalsIgnoreCase("topmoney")){
			return ""+CoinSystem.getInstance().getPlayerManager().getTopCoins(1).get(0).getName();
		}
		return "§4Fehler";
	}
}
