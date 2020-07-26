package ch.dkrieger.coinsystem.core.storage;

import ch.dkrieger.coinsystem.core.player.CoinPlayer;

import java.util.List;
import java.util.UUID;

public interface CoinStorage {

    boolean connect();

    void disconnect();

    boolean isConnected();

    CoinPlayer getPlayer(int id) throws Exception;

    CoinPlayer getPlayer(UUID uuid) throws Exception;

    CoinPlayer getPlayer(String name) throws Exception;

    List<CoinPlayer> getPlayers();

    List<CoinPlayer> getTopPlayers(int maxReturnSize);

    CoinPlayer createPlayer(CoinPlayer player);

    void updateCoins(UUID uuid, long coins);

    void updateColor(UUID uuid, String color);

    void updateInformations(UUID uuid, String name, String color, long lastLogin);

}
