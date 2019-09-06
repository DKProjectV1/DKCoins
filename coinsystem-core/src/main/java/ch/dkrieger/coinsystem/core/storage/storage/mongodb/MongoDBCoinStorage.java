package ch.dkrieger.coinsystem.core.storage.storage.mongodb;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 16.11.18 19:49
 *
 */

import ch.dkrieger.coinsystem.core.config.Config;
import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import ch.dkrieger.coinsystem.core.storage.CoinStorage;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import java.util.List;
import java.util.UUID;

public class MongoDBCoinStorage implements CoinStorage {

    private Config config;
    private MongoDatabase database;
    private MongoCollection collection;

    public MongoDBCoinStorage(Config config) {
        this.config = config;
    }

    @Override
    public boolean connect() {
        String uri = "mongodb"+(config.mongoDbSrv?"+srv":"")+"://";
        if(config.mongoDbAuthentication) uri += config.user+":"+config.password+"@";
        uri += config.host+"/";
        if(config.mongoDbAuthentication) uri += config.mongoDbAuthenticationDatabase;
        uri += "?retryWrites=true&connectTimeoutMS=500&socketTimeoutMS=500";
        //(login.hasSSL()) uri+= "&ssl=true";

        MongoClient mongoClient = new MongoClient(new MongoClientURI(uri));
        this.database = mongoClient.getDatabase(config.database);
        this.collection = database.getCollection("DKCoins_players");
        return true;
    }
    @Override
    public CoinPlayer getPlayer(int id) {
        return MongoDBUtil.findFirst(collection, Filters.eq("id",id),CoinPlayer.class);
    }
    @Override
    public CoinPlayer getPlayer(UUID uuid) {
        return MongoDBUtil.findFirst(collection, Filters.eq("uuid",uuid.toString()),CoinPlayer.class);
    }
    @Override
    public CoinPlayer getPlayer(String name) {
        return MongoDBUtil.findFirst(collection,
                new org.bson.Document().append("name", new org.bson.Document("$regex", name)
                        .append("$options","i")),CoinPlayer.class);
    }
    @Override
    public List<CoinPlayer> getPlayers() {
        return MongoDBUtil.findALL(collection,CoinPlayer.class);
    }
    @Override
    public List<CoinPlayer> getTopPlayers(int maxReturnSize) {
        return MongoDBUtil.findAndSort(collection,CoinPlayer.class,"coins",maxReturnSize);
    }

    @Override
    public CoinPlayer createPlayer(CoinPlayer player) {
        player.setIDSimpled(Integer.parseInt(String.valueOf((this.collection.countDocuments()+1))));
        MongoDBUtil.insertOne(collection,player);
        return player;
    }
    @Override
    public void updateCoins(UUID uuid, long coins) {
        MongoDBUtil.updateOne(collection,"uuid", uuid.toString(), "coins",coins);
    }

    @Override
    public void updateColor(UUID uuid, String color) {
        MongoDBUtil.updateOne(collection,"uuid", uuid.toString(), "color",color);
    }

    @Override
    public void updateInformations(UUID uuid, String name, String color, long lastLogin) {
        MongoDBUtil.updateOne(collection,"uuid", uuid.toString(),"name",name);
        MongoDBUtil.updateOne(collection,"uuid", uuid.toString(),"lastLogin",lastLogin);
        MongoDBUtil.updateOne(collection,"uuid", uuid.toString(),"color",color);
    }

    @Override
    public void disconnect() {
        //No disconnect needed
    }
    @Override
    public boolean isConnected() {
        //Implement
        return true;
    }
}