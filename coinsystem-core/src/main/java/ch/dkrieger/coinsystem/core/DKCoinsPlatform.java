package ch.dkrieger.coinsystem.core;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 24.11.18 16:53
 *
 */

import ch.dkrieger.coinsystem.core.event.CoinChangeEventResult;
import ch.dkrieger.coinsystem.core.event.CoinsUpdateCause;
import ch.dkrieger.coinsystem.core.player.CoinPlayer;

import java.io.File;

public interface DKCoinsPlatform {

    public String getPlatformName();

    public String getServerVersion();

    public File getFolder();

    public String getColor(CoinPlayer player);

    public CoinChangeEventResult executeCoinChangeEvent(CoinPlayer player, Long oldCoins, Long newCoins, CoinsUpdateCause cause, String message);
}
