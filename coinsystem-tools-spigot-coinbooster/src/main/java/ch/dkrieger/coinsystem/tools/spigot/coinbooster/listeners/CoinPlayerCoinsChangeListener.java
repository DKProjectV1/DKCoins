package ch.dkrieger.coinsystem.tools.spigot.coinbooster.listeners;

import ch.dkrieger.coinsystem.core.event.CoinsUpdateCause;
import ch.dkrieger.coinsystem.core.manager.MessageManager;
import ch.dkrieger.coinsystem.spigot.SpigotCoinSystemBootstrap;
import ch.dkrieger.coinsystem.spigot.event.BukkitCoinPlayerCoinsChangeEvent;
import ch.dkrieger.coinsystem.tools.spigot.coinbooster.CoinBoosterExtension;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class CoinPlayerCoinsChangeListener implements Listener {

    @EventHandler
    public void onCoinChange(BukkitCoinPlayerCoinsChangeEvent e){
        if(e.getCause() != null){
            if(e.getCause() == CoinsUpdateCause.PAY) return;
            if(e.getCause() == CoinsUpdateCause.ADMIN && !(CoinBoosterExtension.getInstance().enabled_admincommand)) return;
            if(e.getMessage() != null){
                if(e.getMessage().equalsIgnoreCase("vaulthook") && !(CoinBoosterExtension.getInstance().enabled_vault)) return;
                if(CoinBoosterExtension.getInstance().disabled.contains(e.getMessage())) return;
            }
            if(e.getNewCoins() < 1) return;
            if(e.getOldCoins() > e.getNewCoins()) return;
            long different = e.getNewCoins()-e.getOldCoins();
            if(different < 1) return;
            int boost = booster(e.getCoinPlayer().getUUID());
            if(boost < 1) return;
            Player player = Bukkit.getPlayer(e.getCoinPlayer().getUUID());
            if(player == null) return;

            double coinboost = different/100.0;
            coinboost = coinboost*boost;
            long amount = (long) coinboost;
            long newCoins = e.getNewCoins()+amount;
            e.setNewCoins(newCoins);
            player.sendMessage(MessageManager.getInstance().prefix +CoinBoosterExtension.getInstance().boostmessage
                    .replace("[boost]",String.valueOf(boost)).replace("[coins]", SpigotCoinSystemBootstrap.getInstance().format(amount)));
        }
    }

    private int booster(UUID uuid){
        Player player = Bukkit.getPlayer(uuid);
        if(player == null) return 0;
        for(int i = 100;i > 0;i--)
            if(player.hasPermission(CoinBoosterExtension.getInstance().permission.replace("[boost]",String.valueOf(i)))) return i;
        return 0;
    }
}
