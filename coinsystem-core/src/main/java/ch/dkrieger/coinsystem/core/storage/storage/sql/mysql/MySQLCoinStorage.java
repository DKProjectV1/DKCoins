package ch.dkrieger.coinsystem.core.storage.storage.sql.mysql;

import ch.dkrieger.coinsystem.core.config.Config;
import ch.dkrieger.coinsystem.core.manager.MessageManager;
import ch.dkrieger.coinsystem.core.storage.storage.sql.SQLCoinStorage;
import ch.dkrieger.coinsystem.core.storage.storage.sql.table.Table;

import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 17.11.18 20:23
 *
 */

public class MySQLCoinStorage extends SQLCoinStorage implements Runnable {

	public MySQLCoinStorage(Config config) {
        super(config);
	}
    @Override
    public void loadDriver() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch(ClassNotFoundException exception) {
			System.out.println(MessageManager.getInstance().system_name+"Could not load MySQLCoinStorage driver.");
		}
	}
    @Override
    public void connect(Config config) throws SQLException {
	    setConnection(DriverManager.getConnection("jdbc:mysql://"+config.host+":"+config.port+"/"+config.database+
                "?autoReconnect=true&allowMultiQueries=true&reconnectAtTxEnd=true",config.user, config.password));
    }

	public void reconnect() {
		disconnect();
		connect();
	}
	@Override
	public void run() {
		reconnect();
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
		try{
			DatabaseMetaData metadata = getConnection().getMetaData();
			ResultSet resultSet = metadata.getColumns(null, null, table.getName(), "ip");
			if(resultSet.next()){
				System.out.println(MessageManager.getInstance().system_prefix+" translating mysql table from v2.x.x to v3.0.4");
				table.query().execute("ALTER TABLE "+table.getName()+" DROP ip;");
				table.update().set("firstlogin",System.currentTimeMillis()).execute();
			}
		}catch (Exception exception){}
	}
}