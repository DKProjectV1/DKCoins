package ch.dkrieger.coinsystem.core.storage.storage.json;


import ch.dkrieger.coinsystem.core.config.Config;
import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import ch.dkrieger.coinsystem.core.storage.CoinStorage;
import ch.dkrieger.coinsystem.core.utils.Document;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.reflect.TypeToken;
import io.netty.util.internal.ConcurrentSet;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 24.11.18 16:20
 *
 */

public class JsonCoinStorage implements CoinStorage {

    private AtomicInteger nextID;
    private File file;
    private Document data;
    private ConcurrentSet<CoinPlayer> players;

    public JsonCoinStorage(Config config) {
        new File(config.dataFolder).mkdirs();
        this.file = new File(config.dataFolder, "players.json");
        if(file.exists() && file.isFile()) this.data = Document.loadData(file);
        else this.data = new Document();
        this.data.appendDefault("players",new ConcurrentSet<>());
        this.players = this.data.getObject("players", new TypeToken<ConcurrentSet<CoinPlayer>>(){}.getType());
        nextID = new AtomicInteger(this.players.size()+1);
    }

    @Override
    public boolean connect() {
        return true;
    }

    @Override
    public void disconnect() {}

    @Override
    public boolean isConnected() {
        return true;
    }


    @Override
    public CoinPlayer getPlayer(int id) throws Exception {
        Iterator<CoinPlayer> iterator = this.players.iterator();
        CoinPlayer player;
        while(iterator.hasNext() &&(player = iterator.next()) != null) if(player.getID() == id) return player;
        return null;
    }

    @Override
    public CoinPlayer getPlayer(UUID uuid) {
        Iterator<CoinPlayer> iterator = this.players.iterator();
        CoinPlayer player;
        while(iterator.hasNext() &&(player = iterator.next()) != null) if(player.getUUID().equals(uuid)) return player;
        return null;
    }

    @Override
    public CoinPlayer getPlayer(String name) {
        Iterator<CoinPlayer> iterator = this.players.iterator();
        CoinPlayer player;
        while(iterator.hasNext() &&(player = iterator.next()) != null) if(player.getName().equalsIgnoreCase(name)) return player;
        return null;
    }

    @Override
    public List<CoinPlayer> getPlayers() {
        return new LinkedList<>(this.players);
    }

    @Override
    public List<CoinPlayer> getTopPlayers(int maxReturnSize) {
        LinkedList<CoinPlayer> list = new LinkedList<>(this.players);
        list.sort((player1, player2) -> {
            if(player1.getCoins() > player2.getCoins()) return -1;
            else return 1;
        });
        return list;
    }

    @Override
    public CoinPlayer createPlayer(CoinPlayer player) {
        this.players.add(player);
        player.setIDSimpled(this.nextID.getAndIncrement());
        save();
        return player;
    }
    public void savePlayer(CoinPlayer coinPlayer) {
        Iterator<CoinPlayer > iterator = this.players.iterator();
        CoinPlayer player;
        while(iterator.hasNext() &&(player = iterator.next()) != null) {
            if(player.getUUID().equals(coinPlayer.getUUID())) {
                this.players.remove(player);
                this.players.add(coinPlayer);
                save();
                return;
            }
        }
    }
    @Override
    public void updateCoins(UUID uuid, long coins) {
        CoinPlayer player = getPlayer(uuid);
        player.setCoinsSimpled(coins);
        savePlayer(getPlayer(uuid));
    }
    @Override
    public void updateColor(UUID uuid, String color) {
        CoinPlayer player = getPlayer(uuid);
        player.setColorSimpled(color);
        savePlayer(getPlayer(uuid));
    }
    @Override
    public void updateInformations(UUID uuid, String name, String color, long lastLogin) {
        CoinPlayer player = getPlayer(uuid);
        player.setNameSimpled(name);
        player.setLastLoginSimpled(lastLogin);
        player.setColorSimpled(color);
        savePlayer(getPlayer(uuid));
    }
    public void save(){
        this.data.append("players",this.players);
        this.data.saveData(this.file);
    }
}