package ch.dkrieger.coinsystem.core.player;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 24.11.18 21:49
 *
 */

public class PlayerColor {

    private String permission, color;

    public PlayerColor(String permission, String color){
        this.permission = permission;
        this.color = color;
    }
    public String getPermission(){
        return this.permission;
    }
    public String getColor(){
        return this.color;
    }
}
