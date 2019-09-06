package ch.dkrieger.coinsystem.core.player;

import ch.dkrieger.coinsystem.core.CoinSystem;
import ch.dkrieger.coinsystem.core.config.Config;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

public class CoinPlayerManager {
	
	public CoinPlayerManager(){
	}

	public CoinPlayer getPlayer(int id){
		try{
			return CoinSystem.getInstance().getStorage().getPlayer(id);
		}catch (Exception ignored){}
		return null;
	}

	public CoinPlayer getPlayer(String name){
		try{
			return CoinSystem.getInstance().getStorage().getPlayer(name);
		}catch (Exception ignored){}
		return null;
	}

	public CoinPlayer getPlayer(UUID uuid){
		try{
			return getPlayerSave(uuid);
		}catch (Exception ignored){}
		return null;
	}

	public CoinPlayer getPlayerSave(UUID uuid) throws Exception{
		return CoinSystem.getInstance().getStorage().getPlayer(uuid);
	}

	public CoinPlayer createPlayer(String name, UUID uuid){
		CoinPlayer player = new CoinPlayer(-1,uuid,name,Config.getInstance().defaultColor,System.currentTimeMillis()
				,System.currentTimeMillis(),Config.getInstance().system_player_startcoin);
		player = CoinSystem.getInstance().getStorage().createPlayer(player);
		return player;
	}

	public List<CoinPlayer> getPlayers(){
		return CoinSystem.getInstance().getStorage().getPlayers();
	}

	public List<CoinPlayer> getTopCoins(int maxReturnSize){
		return CoinSystem.getInstance().getStorage().getTopPlayers(maxReturnSize);
	}

	public String getDate(){//dd.MM.yyyy kk:mm
		SimpleDateFormat data = new SimpleDateFormat("dd.MM.yyyy kk:mm");
		return data.format(System.currentTimeMillis());
	}
}
