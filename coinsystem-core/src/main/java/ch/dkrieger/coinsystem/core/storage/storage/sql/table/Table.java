package ch.dkrieger.coinsystem.core.storage.storage.sql.table;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 16.11.18 20:57
 *
 */

import ch.dkrieger.coinsystem.core.storage.storage.sql.SQLCoinStorage;
import ch.dkrieger.coinsystem.core.storage.storage.sql.query.*;

public class Table {

    private String name;
    private SQLCoinStorage sql;

    public Table(SQLCoinStorage sql, String name){
        this.sql = sql;
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public CreateQuery create(){
        return new CreateQuery(sql.getConnection(),"CREATE TABLE IF NOT EXISTS `"+this.name+"` (");
    }
    public InsertQuery insert(){
        return new InsertQuery(sql.getConnection(),"INSERT INTO `"+this.name+"` (");
    }
    public UpdateQuery update(){
        return new UpdateQuery(sql.getConnection(),"UPDATE `"+this.name+"` SET");
    }
    public SelectQuery select(){
        return select("*");
    }
    public SelectQuery select(String selection){
        return new SelectQuery(sql.getConnection(), "SELECT "+selection+" FROM `"+this.name+"`");
    }
    public DeleteQuery delete(){
        return new DeleteQuery(sql.getConnection(), "DELETE FROM `"+this.name+"`");
    }
    public CustomQuery query(){
        return new CustomQuery(sql.getConnection());
    }
}
