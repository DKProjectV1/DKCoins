package ch.dkrieger.coinsystem.core;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 24.11.18 16:49
 *
 */

import ch.dkrieger.coinsystem.core.config.Config;
import ch.dkrieger.coinsystem.core.manager.MessageManager;
import ch.dkrieger.coinsystem.core.player.CoinPlayerManager;
import ch.dkrieger.coinsystem.core.storage.CoinStorage;
import ch.dkrieger.coinsystem.core.storage.storage.StorageType;
import ch.dkrieger.coinsystem.core.storage.storage.json.JsonCoinStorage;
import ch.dkrieger.coinsystem.core.storage.storage.mongodb.MongoDBCoinStorage;
import ch.dkrieger.coinsystem.core.storage.storage.sql.mysql.MySQLCoinStorage;
import ch.dkrieger.coinsystem.core.storage.storage.sql.sqlite.SQLiteCoinStorage;
import ch.dkrieger.coinsystem.core.utils.UpdateChecker;

import java.net.MalformedURLException;

public class CoinSystem {

    private static CoinSystem INSTANCE;
    private final String version;
    private final DKCoinsPlatform platform;
    private CoinPlayerManager playerManager;
    private CoinStorage storage;
    private Config config;
    private UpdateChecker updateChecker;

    public CoinSystem(DKCoinsPlatform platform) {
        INSTANCE = this;
        this.version = "3.1.9";
        this.platform = platform;

        new MessageManager("DKCoins");
        System.out.println("["+MessageManager.getInstance().system_name+"] plugin is starting");
        System.out.println("["+MessageManager.getInstance().system_name+"] CoinSystem v"+this.version+" by Davide Wietlisbach");

        try {
            this.updateChecker = new UpdateChecker(39159);
            if(this.updateChecker.hasNewVersion()) {
                System.out.println(MessageManager.getInstance().system_prefix + "New version available: " + this.updateChecker.getLatestVersionString());
            }
        } catch (MalformedURLException ignored) {
            System.out.println(MessageManager.getInstance().system_prefix + "Can't check newest version.");
        }

        systemBootstrap();

        System.out.println("["+MessageManager.getInstance().system_name+"] plugin successfully started");
    }

    private void systemBootstrap(){
        this.platform.getFolder().mkdirs();

        this.config = new Config(this.platform);
        this.config.loadConfig();

        this.playerManager = new CoinPlayerManager();

        setupStorage();
    }

    private void setupStorage() {
        if(this.config.storageType == StorageType.MYSQL) this.storage = new MySQLCoinStorage(this.config);
        else if(this.config.storageType == StorageType.SQLITE) this.storage = new SQLiteCoinStorage(this.config);
        else if(this.config.storageType == StorageType.MONGODB) this.storage = new MongoDBCoinStorage(this.config);
        else if(this.config.storageType == StorageType.JSON) this.storage = new JsonCoinStorage(this.config);

        if(this.storage != null && this.storage.connect()) {
            System.out.println(MessageManager.getInstance().system_name+"Used Storage: "+this.config.storageType.toString());
            return;
        }
        System.out.println(MessageManager.getInstance().system_name+"Used Backup Storage: "+StorageType.SQLITE.toString());
        this.storage = new SQLiteCoinStorage(this.config);
    }

    public void reload(){
        System.out.println("["+MessageManager.getInstance().system_name+"] plugin is reloading");
        System.out.println("["+MessageManager.getInstance().system_name+"] CoinSystem v"+this.version+" by Davide Wietlisbach");
        if(this.storage != null) this.storage.disconnect();
        systemBootstrap();
        System.out.println("["+MessageManager.getInstance().system_name+"] plugin successfully reloaded");
    }

    public void shutdown(){
        System.out.println("["+MessageManager.getInstance().system_name+"] plugin is stopping");
        System.out.println("["+MessageManager.getInstance().system_name+"] CoinSystem v"+this.version+" by Davide Wietlisbach");
        if(this.storage != null) this.storage.disconnect();
        System.out.println("["+MessageManager.getInstance().system_name+"] plugin successfully stopped");
    }

    public String getVersion() {
        return version;
    }

    public DKCoinsPlatform getPlatform() {
        return platform;
    }

    public CoinPlayerManager getPlayerManager() {
        return playerManager;
    }

    public CoinStorage getStorage() {
        return storage;
    }

    public Config getConfig() {
        return config;
    }

    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    public static CoinSystem getInstance() {
        return INSTANCE;
    }
}
