package ch.dkrieger.coinsystem.tools.spigot.coinbooster;

import ch.dkrieger.coinsystem.core.CoinSystem;
import ch.dkrieger.coinsystem.tools.spigot.coinbooster.listeners.CoinPlayerCoinsChangeListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CoinBoosterExtension extends JavaPlugin{

    private static CoinBoosterExtension INSTANCE;

    public String permission;
    public String boostmessage;
    public boolean enabled_admincommand;
    public boolean enabled_vault;
    public List<String> disabled;

    @Override
    public void onEnable() {
        INSTANCE = this;
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new CoinPlayerCoinsChangeListener(),this);

        Bukkit.getScheduler().runTaskLater(this,()->{
            CoinSystem.getInstance().getConfig().add("permissions.extension.coinboost","dkcoins.booster.boost.[boost]");
            CoinSystem.getInstance().getConfig().add("message.extension.coinbooster.boost","&7You have a boost of &e[boost]%, &7you get &e[coins] &7Coins more.");
            CoinSystem.getInstance().getConfig().add("extension.coinbooster.enabled.admincommand",true);
            CoinSystem.getInstance().getConfig().add("extension.coinbooster.enabled.vaulthook",true);
            ArrayList<String> list = new ArrayList<>();
            list.add("test");
            CoinSystem.getInstance().getConfig().add("extension.coinbooster.disabled",list);
            CoinSystem.getInstance().getConfig().save();
        },(long) 1.5);
        Bukkit.getScheduler().runTaskLater(this,()->{
            permission = CoinSystem.getInstance().getConfig().getStringValue("permissions.extension.coinboost");
            boostmessage = CoinSystem.getInstance().getConfig().translate(CoinSystem.getInstance().getConfig().getString("message.extension.coinbooster.boost"));
            enabled_admincommand = CoinSystem.getInstance().getConfig().getBooleanValue("extension.coinbooster.enabled.admincommand");
            enabled_vault = CoinSystem.getInstance().getConfig().getBooleanValue("extension.coinbooster.enabled.vaulthook");
            disabled = CoinSystem.getInstance().getConfig().getStringListValue("extension.coinbooster.disabled");
        },(long) 2.5);
    }
    public static CoinBoosterExtension getInstance() {
        return INSTANCE;
    }
}
