package ch.dkrieger.coinsystem.tools.spigot.coinbooster;

import ch.dkrieger.coinsystem.core.config.Config;
import ch.dkrieger.coinsystem.tools.spigot.coinbooster.listeners.CoinPlayerCoinsChangeListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CoinBoosterExtension extends JavaPlugin{

    private static CoinBoosterExtension INSTANCE;

    public String permission;
    public String boostmessage;;
    public boolean enabled_admincommand, enabled_vault;
    public List<String> disabled;

    @Override
    public void onEnable() {
        INSTANCE = this;
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new CoinPlayerCoinsChangeListener(),this);

        Bukkit.getScheduler().runTaskLater(this,()->{
            Config.getInstance().add("permissions.extension.coinboost","dkcoins.booster.boost.[boost]");
            Config.getInstance().add("message.extension.coinbooster.boost","&7You have a boost of &e[boost]%, &7you get &e[coins] &7Coins more.");
            Config.getInstance().add("extension.coinbooster.enabled.admincommand",true);
            Config.getInstance().add("extension.coinbooster.enabled.vaulthook",true);
            ArrayList<String> list = new ArrayList<>();
            list.add("test");
            Config.getInstance().add("extension.coinbooster.disabled",list);
            Config.getInstance().save();
        },(long) 1.5);
        Bukkit.getScheduler().runTaskLater(this,()->{
            permission = Config.getInstance().getStringValue("permissions.extension.coinboost");
            boostmessage = Config.getInstance().translate(Config.getInstance().getString("message.extension.coinbooster.boost"));
            enabled_admincommand = Config.getInstance().getBooleanValue("extension.coinbooster.enabled.admincommand");
            enabled_vault = Config.getInstance().getBooleanValue("extension.coinbooster.enabled.vaulthook");
            disabled = Config.getInstance().getStringListValue("extension.coinbooster.disabled");
        },(long) 2.5);
    }
    public static CoinBoosterExtension getInstance() {
        return INSTANCE;
    }
}
