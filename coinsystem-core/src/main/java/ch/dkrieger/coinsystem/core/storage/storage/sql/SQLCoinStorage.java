package ch.dkrieger.coinsystem.core.storage.storage.sql;

import ch.dkrieger.coinsystem.core.config.Config;
import ch.dkrieger.coinsystem.core.manager.MessageManager;
import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import ch.dkrieger.coinsystem.core.storage.CoinStorage;
import ch.dkrieger.coinsystem.core.storage.storage.sql.query.CustomQuery;
import ch.dkrieger.coinsystem.core.storage.storage.sql.query.SelectQuery;
import ch.dkrieger.coinsystem.core.storage.storage.sql.sqlite.SQLiteCoinStorage;
import ch.dkrieger.coinsystem.core.storage.storage.sql.table.Table;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 24.11.18 15:55
 *
 */

public abstract class SQLCoinStorage implements CoinStorage {

    private Config config;
    private Connection connection;
    private Table table;

    public SQLCoinStorage(Config config) {
        this.config = config;
    }
    @Override
    public boolean isConnected() {
        try{
            return connection != null && !(connection.isClosed()) ;
        }catch (Exception exception){}
        return false;
    }
    public Connection getConnection() {
        return connection;
    }
    public Table getTable() {
        return table;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    @Override
    public boolean connect() {
        if(!isConnected()){
            loadDriver();
            System.out.println(MessageManager.getInstance().system_prefix+"connecting to SQL server at "+config.host+":"+config.port);
            try {
                connect(config);
                this.table = new Table(this, "DKCoins_players");// .create("PRIMARY KEY (`ID`)")
                createTable(table);
                System.out.println(MessageManager.getInstance().system_prefix+"successful connected to SQL server at "+config.host+":"+config.port);
            }catch (SQLException exception) {
                System.out.println(MessageManager.getInstance().system_prefix+"Could not connect to SQL server at "+config.host+":"+config.port);
                System.out.println(MessageManager.getInstance().system_prefix+"Error: "+exception.getMessage());
                System.out.println(MessageManager.getInstance().system_prefix+"Check your login data in the config.");
                connection = null;
                return false;
            }
        }
        return true;
    }
    @Override
    public void disconnect() {
        if(isConnected()){
            try {
                connection.close();
                connection = null;
                System.out.println(MessageManager.getInstance().system_name+"successful disconnected from sql server at "+this.config.host+":"+this.config.port);
            }catch (SQLException exception) {
                connection = null;
            }
        }
    }
    @Override
    public CoinPlayer getPlayer(int id) throws Exception {
        return getPlayer("id",id);
    }

    @Override
    public CoinPlayer getPlayer(UUID uuid) throws Exception{
        return getPlayer("uuid", uuid.toString());
    }
    @Override
    public CoinPlayer getPlayer(String name) throws Exception{
        return getPlayer("name", name);
    }
    private CoinPlayer getPlayer(String identifier, Object identifierObject) throws Exception{
        SelectQuery query = this.table.select().where(identifier, identifierObject);
        if(this instanceof SQLiteCoinStorage) query.noCase();
        ResultSet result = query.execute();
        if (result == null) return null;
        try {
            if(result.first()) {
                return new CoinPlayer(result.getInt("ID"),
                        UUID.fromString(result.getString("uuid")),
                        result.getString("name"),
                        result.getString("color"),
                        result.getLong("firstLogin"),
                        result.getLong("lastLogin"),
                        result.getLong("coins"));
            }
        } finally {
            query.close();
            result.close();
        }
        return null;
    }

    @Override
    public CoinPlayer createPlayer(CoinPlayer player) {
        player.setIDSimpled(this.table.insert()
                .insert("uuid")
                .insert("name")
                .insert("color")
                .insert("firstLogin")
                .insert("lastLogin")
                .insert("coins")
                .value(player.getUUID())
                .value(player.getName())
                .value(player.getColor())
                .value(player.getFirstLogin())
                .value(player.getLastLogin())
                .value(player.getCoins()).executeAndGetKeyInInt());
        return player;
    }
    @Override
    public List<CoinPlayer> getPlayers(){
        List<CoinPlayer> players = new LinkedList<>();
        try{
            SelectQuery query = this.table.select();
            ResultSet result = query.execute();
            if (result == null) return null;
            try {
                while (result.next()) {
                    players.add(new CoinPlayer(result.getInt("ID"),
                            UUID.fromString(result.getString("uuid")),
                            result.getString("name"),
                            result.getString("color"),
                            result.getLong("firstLogin"),
                            result.getLong("lastLogin"),
                            result.getLong("coins")));
                }
            } finally {
                query.close();
                result.close();
            }
        }catch (Exception exception){}
        return players;
    }

    @Override
    public List<CoinPlayer> getTopPlayers(int maxReturnSize) {
        List<CoinPlayer> players = new LinkedList<>();
        try{
            CustomQuery query = this.table.query();
            ResultSet result = query.executeAndGetResult("SELECT * FROM `DKCoins_players` ORDER BY `DKCoins_players`.`coins` DESC LIMIT "+maxReturnSize);
            if (result == null) return null;
            try {
                while (result.next()) {
                    players.add(new CoinPlayer(result.getInt("ID"),
                            UUID.fromString(result.getString("uuid")),
                            result.getString("name"),
                            result.getString("color"),
                            result.getLong("firstLogin"),
                            result.getLong("lastLogin"),
                            result.getLong("coins")));
                }
            } finally {
                query.close();
                result.close();
            }
        }catch (Exception exception){}
        return players;
    }

    @Override
    public void updateColor(UUID uuid, String color) {
        this.table.update().set("color",color).where("uuid",uuid.toString()).execute();
    }

    @Override
    public void updateCoins(UUID uuid, long coins) {
        this.table.update().set("coins",coins).where("uuid",uuid.toString()).execute();
    }
    @Override
    public void updateInformations(UUID uuid, String name, String color, long lastLogin) {
        this.table.update()
                .set("name",name)
                .set("color",color)
                .set("lastLogin",lastLogin)
                .where("uuid",uuid).execute();
    }

    public abstract void createTable(Table table);
    public abstract void connect(Config config) throws SQLException;
    public abstract void loadDriver();
}