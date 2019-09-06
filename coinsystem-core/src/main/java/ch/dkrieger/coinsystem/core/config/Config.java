/*
 * created by Dkrieger on 24.02.18 11:44
 */

package ch.dkrieger.coinsystem.core.config;

import ch.dkrieger.coinsystem.core.CoinSystem;
import ch.dkrieger.coinsystem.core.DKCoinsPlatform;
import ch.dkrieger.coinsystem.core.manager.MessageManager;
import ch.dkrieger.coinsystem.core.manager.PermissionManager;
import ch.dkrieger.coinsystem.core.player.PlayerColor;
import ch.dkrieger.coinsystem.core.storage.storage.StorageType;
import net.md_5.bungee.api.ChatColor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Config extends SimpleConfig{

    private final DKCoinsPlatform platform;

    public StorageType storageType;
    public String host, port, user, password, database, mongoDbAuthenticationDatabase, dataFolder, defaultColor;
    public int maxConnections;
    public boolean ssl, mongoDbSrv, mongoDbAuthentication, liveColorUpdate;
    public SimpleDateFormat dateFormat;
    public List<PlayerColor> playerColors;

    public long system_player_startcoin;
    public boolean system_player_onlyproxy_check;

    public boolean system_player_addcoinsonkill;
    public long system_player_addcoinsonkill_amount;

    public boolean system_player_removecoinsondeath;
    public long system_player_removecoinsondeath_amount;

    public String command_name;
    public List<String> command_aliases;

    public String command_pay_name;
    public List<String> command_pay_aliases;
    public boolean command_pay_enabled;

    public boolean number_formatting_enabled;
    public String number_formatting_symbol;

    public boolean hook_vault_enabled;
    public String hook_vault_priority;

    public Config(DKCoinsPlatform platform) {
        super(new File(platform.getFolder(),"config.yml"));
        this.platform = platform;
        loadConfig();
    }

    @Override
    public void onLoad() {
        this.dataFolder = getStringValue("storage.folder");
        this.storageType = StorageType.parse(getStringValue("storage.type"));
        this.host = getStringValue("storage.host");
        this.port = getStringValue("storage.port");
        this.user = getStringValue("storage.user");
        this.password = getStringValue("storage.password");
        this.database = getStringValue("storage.database");
        this.ssl = getBooleanValue("storage.ssl");
        this.maxConnections = getIntValue("storage.sql.maxConnections");
        this.mongoDbAuthentication = getBooleanValue("storage.mongodb.mongodbauthentication");
        this.mongoDbAuthenticationDatabase = getStringValue("storage.mongodb.authenticationDatabase");
        this.mongoDbSrv = getBooleanValue("storage.mongodb.srv");

        this.dateFormat = new SimpleDateFormat(getStringValue("date.format"));

        this.system_player_startcoin = getLongValue("system.player.startcoins");
        this.system_player_onlyproxy_check = getBooleanValue("system.player.checkonlyonproxy");

        this.system_player_addcoinsonkill = getBooleanValue("system.player.addcoinsonkill.enabled");
        this.system_player_addcoinsonkill_amount = getLongValue("system.player.addcoinsonkill.amount");
        this.system_player_removecoinsondeath = getBooleanValue("system.player.removecoinsondeath.enabled");
        this.system_player_removecoinsondeath_amount = getLongValue("system.player.removecoinsondeath.amount");

        this.command_name = getString("system.command.default");
        this.command_aliases = getStringListValue("system.command.aliases");

        this.command_pay_enabled = getBooleanValue("system.commandpay.enabled");
        this.command_pay_name = getString("system.commandpay.default");
        this.command_pay_aliases = getStringListValue("system.commandpay.aliases");

        this.number_formatting_enabled = getBooleanValue("number.formatting.enabled");
        this.number_formatting_symbol = getStringValue("number.formatting.symbol");

        this.liveColorUpdate = getBooleanValue("color.liveupdate");
        this.defaultColor = getMessageValue("color.default");
        this.playerColors = new LinkedList<>();
        for(String colors : getStringListValue("color.colors")){
            String[] split = colors.split(":");
            if(colors.length() >= 2)
                this.playerColors.add(new PlayerColor(split[0],split[1]));
        }

        PermissionManager pm = new PermissionManager();
        pm.admin = getString("permissions.admin");
        pm.command_coins_admin = getString("permissions.admin");
        pm.command_coins = getString("permissions.coins.see");
        pm.command_coins_others = getString("permissions.coins.seeothers");
        pm.command_coins_top = getString("permissions.coins.top");
        pm.command_coins_pay = getString("permissions.coins.pay");

        MessageManager mm = MessageManager.getInstance();
        mm.prefix = translate(getString("messages.prefix"));

        mm.coins_plural = translate(getString("messages.coins.plural"));
        mm.coins_singular = translate(getString("messages.coins.singular"));
        mm.coins_symbol = translate(getString("messages.coins.symbol"));

        mm.noperms = mm.prefix+translate(getString("messages.nopermissions"));
        mm.mysql_noconnection = mm.prefix+translate(getString("messages.noconnection"));
        mm.playernotfound = mm.prefix+translate(getString("messages.player.notfound"));

        this.hook_vault_enabled = getBooleanValue("hook.vault.enabled");
        this.hook_vault_priority = getStringValue("hook.vault.priority");

        mm.command_coins_showownmoney = mm.prefix+translate(getString("messages.command.coins"));
        mm.command_coins_showothermoney= mm.prefix+translate(getString("messages.command.coinsothers"));
        mm.command_coins_top_header = mm.prefix+translate(getString("messages.command.top.header"));
        mm.command_coins_top_list = translate(getString("messages.command.top.list"));
        mm.command_coins_pay_notenough = mm.prefix+translate(getString("messages.command.pay.notenough"));
        mm.command_coins_pay_sender = mm.prefix+translate(getString("messages.command.pay.sender"));
        mm.command_coins_pay_receiver = mm.prefix+translate(getString("messages.command.pay.receiver"));
        mm.command_coins_set_sender = mm.prefix+translate(getString("messages.command.set.sender"));
        mm.command_coins_set_receiver = mm.prefix+translate(getString("messages.command.set.receiver"));
        mm.command_coins_add_sender = mm.prefix+translate(getString("messages.command.add.sender"));
        mm.command_coins_add_receiver = mm.prefix+translate(getString("messages.command.add.receiver"));
        mm.command_coins_remove_sender = mm.prefix+translate(getString("messages.command.remove.sender"));
        mm.command_coins_remove_receiver = mm.prefix+translate(getString("messages.command.remove.receiver"));

        mm.command_coins_help_header = translate(getString("messages.command.help.header"));
        mm.command_coins_help_coins = translate(getString("messages.command.help.coins.see"));
        mm.command_coins_help_top = translate(getString("messages.command.help.coins.top"));
        mm.command_coins_help_pay = translate(getString("messages.command.help.coins.pay"));
        mm.command_coins_help_set = translate(getString("messages.command.help.coins.set"));
        mm.command_coins_help_add = translate(getString("messages.command.help.coins.add"));
        mm.command_coins_help_remove = translate(getString("messages.command.help.coins.remove"));
        mm.command_coins_help_reset = translate(getString("messages.command.help.coins.reset"));
    }
    @Override
    public void registerDefaults() {
        addValue("storage.folder",this.platform.getFolder()+"/data/");
        addValue("storage.type", StorageType.SQLITE.toString().toUpperCase());//change to json
        addValue("storage.host", "127.0.0.1");
        addValue("storage.port", "3306"); //mongo 27018
        addValue("storage.user", "root");
        addValue("storage.password", "password");
        addValue("storage.database", "DKCoins");
        addValue("storage.ssl", true);
        addValue("storage.sql.maxConnections", 10);
        addValue("storage.mongodb.mongodbauthentication",true);
        addValue("storage.mongodb.authenticationDatabase", "admin");
        addValue("storage.mongodb.srv", false);

        addValue("date.format","yyyy.MM.dd HH:mm");

        addValue("color.liveupdate",false);
        addValue("color.default","&8");
        List<String> colors = new LinkedList<>();
        colors.add("dkcoins.color.admin:&4");
        colors.add("dkcoins.color.developer:&b");
        colors.add("dkcoins.color.mod:&c");
        colors.add("dkcoins.color.supporter:&9");
        colors.add("dkcoins.color.builder:&3");
        colors.add("dkcoins.color.youtuber:&5");
        colors.add("dkcoins.color.premium:&6");
        addValue("color.colors",colors);

        add("system.player.startcoins", 0);
        add("system.player.checkonlyonproxy",false);
        add("system.player.addcoinsonkill.enabled", false);
        add("system.player.addcoinsonkill.amount", 10);
        add("system.player.removecoinsondeath.enabled", false);
        add("system.player.removecoinsondeath.amount", 5);

        add("system.command.default", "coins");
        List<String> list = new ArrayList<>();
        list.add("money");
        list.add("coin");
        add("system.command.aliases", list);

        add("system.commandpay.enabled",true);
        add("system.commandpay.default","pay");
        List<String> list2 = new ArrayList<>();
        list.add("bezahlen");
        add("system.commandpay.aliases", list2);

        add("number.formatting.enabled",true);
        add("number.formatting.symbol","'");

        add("permissions.admin", "dkcoins.admin");
        add("permissions.coins.see", "dkcoins.coins.see");
        add("permissions.coins.seeothers", "dkcoins.coins.see.others");
        add("permissions.coins.top", "dkcoins.coins.top");
        add("permissions.coins.pay", "dkcoins.coins.pay");

        add("hook.vault.enabled", true);
        add("hook.vault.priority", "Highest");

        add("messages.prefix", "&8» &6DKCoins &8| &f");
        add("messages.nopermissions", "&cYou don't have permissions for this command!");
        add("messages.noconnection", "&cNo connection to database.");
        add("messages.coins.singular", "coin");
        add("messages.coins.plural", "coins");
        add("messages.coins.symbol", "$");
        add("messages.player.notfound", "&cThe player &e[player] &cwas not found.");
        add("messages.command.coins", "&7Balance&8: &e[amount] &7coins");
        add("messages.command.coinsothers", "&7Balance from &e[player]&8: &e[amount] &7coins.");
        add("messages.command.top.header", "&6Top 10 Players");
        add("messages.command.top.list", " &8[&7[rang]&8] &e[player]&8: &e[amount] &7coins");
        add("messages.command.pay.notenough","&cYou don't have enough coins.");
        add("messages.command.pay.sender","&7You have sent &e[player] &e[amount] &7coins.");
        add("messages.command.pay.receiver","&7You received &e[amount] &7coins &7from &e[player]&7.");
        add("messages.command.set.sender", "&7Set &e[player]'s &7coins to &e[amount]");
        add("messages.command.set.receiver", "&7Set your &7coins &7to &e[amount]");
        add("messages.command.add.sender", "&7added &e[player] &e[amount] &7coins.");
        add("messages.command.add.receiver", "&7added &e[amount] &7coins.");
        add("messages.command.remove.sender", "&7removed &e[player] &e[amount] &7coins.");
        add("messages.command.remove.receiver", "&7removed &e[amount] &7coins.");
        add("messages.command.help.header", "&8--------[&6DKCoins&8]--------");
        add("messages.command.help.coins.see", "&e/coins <player>");
        add("messages.command.help.coins.top", "&e/coins top");
        add("messages.command.help.coins.pay", "&e/coins pay <player> <amount>");
        add("messages.command.help.coins.set", "&e/coins set <player> <amount>");
        add("messages.command.help.coins.add", "&e/coins add <player> <amount>");
        add("messages.command.help.coins.remove", "&e/coins remove <player> <amount>");
        add("messages.command.help.coins.reset", "&e/coins reset <player>");
    }
    public String getString(String path){
        return getStringValue(path);
    }

    public String translate(String value){
        return ChatColor.translateAlternateColorCodes('&',value);
    }

    public void add(String path, Object value){
        addValue(path,value);
    }

    @Deprecated
    public static Config getInstance() {
        return CoinSystem.getInstance().getConfig();
    }
}
