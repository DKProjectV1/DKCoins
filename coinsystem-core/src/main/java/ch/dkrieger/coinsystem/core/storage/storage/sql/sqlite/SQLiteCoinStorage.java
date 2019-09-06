package ch.dkrieger.coinsystem.core.storage.storage.sql.sqlite;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 17.11.18 20:38
 *
 */

import ch.dkrieger.coinsystem.core.config.Config;
import ch.dkrieger.coinsystem.core.manager.MessageManager;
import ch.dkrieger.coinsystem.core.storage.storage.sql.SQLCoinStorage;
import ch.dkrieger.coinsystem.core.storage.storage.sql.table.Table;
import com.zaxxer.hikari.HikariConfig;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteCoinStorage extends SQLCoinStorage {

    public SQLiteCoinStorage(Config config) {
        super(config);
    }
    @Override
    public void connect(Config config) throws SQLException {
        new File(config.dataFolder).mkdirs();
        try {
            new File(config.dataFolder,"players.db").createNewFile();
        } catch (IOException exception) {
            System.err.println("Could not create SQLite database file ("+exception.getMessage()+").");
        }
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:sqlite:"+config.dataFolder+"players.db");
        setDataSource(hikariConfig);
    }
    @Override
    public void loadDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println(MessageManager.getInstance().system_name+"Could not load SQLiteCoinStorage driver.");
        }
    }
    @Override
    public void createTable(Table table) {
        table.create()
                .create("`ID` INTEGER PRIMARY KEY AUTOINCREMENT")
                .create("`uuid` varchar(120) NOT NULL")
                .create("`name` varchar(32) NOT NULL")
                .create("`color` varchar(32) NOT NULL")
                .create("`firstLogin` varchar(60) NOT NULL")
                .create("`lastLogin` varchar(60) NOT NULL")
                .create("`coins` int(200) NOT NULL")
                .execute();
    }
}