package ch.dkrieger.coinsystem.core.storage.storage.sql.mysql;

import ch.dkrieger.coinsystem.core.config.Config;
import ch.dkrieger.coinsystem.core.manager.MessageManager;
import ch.dkrieger.coinsystem.core.storage.storage.sql.SQLCoinStorage;
import ch.dkrieger.coinsystem.core.storage.storage.sql.table.Table;
import com.zaxxer.hikari.HikariConfig;

import java.sql.SQLException;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 17.11.18 20:23
 *
 */

public class MySQLCoinStorage extends SQLCoinStorage {

	public MySQLCoinStorage(Config config) {
        super(config);
	}

    @Override
    public void loadDriver() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch(ClassNotFoundException exception) {
			System.out.println(MessageManager.getInstance().system_name+"Could not load MySQL driver.");
		}
	}
    @Override
    public void connect(Config pluginConfig) throws SQLException {
		HikariConfig config = new HikariConfig();
		config.setUsername(pluginConfig.user);
		config.setPassword(pluginConfig.password);
		config.setJdbcUrl("jdbc:mysql://"+pluginConfig.host+":"+pluginConfig.port+"/"+pluginConfig.database);
		setDataSource(config);
    }

	@Override
	public void createTable(Table table) {
		table.create()
				.create("`ID` int NOT NULL AUTO_INCREMENT")
				.create("`uuid` varchar(120) NOT NULL")
				.create("`name` varchar(32) NOT NULL")
				.create("`color` varchar(32) NOT NULL")
				.create("`firstLogin` varchar(60) NOT NULL")
				.create("`lastLogin` varchar(60) NOT NULL")
				.create("`coins` int(200) NOT NULL")
				.create("PRIMARY KEY (`ID`)")
				.execute();
	}
}