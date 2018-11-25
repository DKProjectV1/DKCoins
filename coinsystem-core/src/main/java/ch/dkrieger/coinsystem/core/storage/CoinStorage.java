package ch.dkrieger.coinsystem.core.storage;

import ch.dkrieger.coinsystem.core.player.CoinPlayer;

import java.util.List;
import java.util.UUID;

public interface CoinStorage {

    public boolean connect();

    public void disconnect();

    public boolean isConnected();

    public CoinPlayer getPlayer(int id) throws Exception;

    public CoinPlayer getPlayer(UUID uuid) throws Exception;

    public CoinPlayer getPlayer(String name) throws Exception;

    public List<CoinPlayer> getPlayers();

    public List<CoinPlayer> getTopPlayers(int maxReturnSize);

    public CoinPlayer createPlayer(CoinPlayer player);

    public void updateCoins(UUID uuid, long coins);

    public void updateColor(UUID uuid, String color);

    public void updateInformations(UUID uuid, String name, String color, long lastLogin);

}
