package ch.dkrieger.coinsystem.spigot.hook;

import ch.dkrieger.coinsystem.core.CoinSystem;
import ch.dkrieger.coinsystem.core.manager.MessageManager;
import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import ch.dkrieger.coinsystem.spigot.SpigotCoinSystemBootstrap;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceHolderAPIHook extends PlaceholderExpansion {

	@Override
	public String getIdentifier() {
		return MessageManager.getInstance().system_name.toLowerCase();
	}

	@Override
	public String getPlugin() {
		return MessageManager.getInstance().system_name;
	}

	@Override
	public String getAuthor() {
		return "Dkrieger";
	}

	@Override
	public String getVersion() {
		return CoinSystem.getInstance().getVersion();
	}

	@Override
	public String onPlaceholderRequest(Player player, String identifier) {
		if(player == null|| identifier == null) return "PlayerIsNull";
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
