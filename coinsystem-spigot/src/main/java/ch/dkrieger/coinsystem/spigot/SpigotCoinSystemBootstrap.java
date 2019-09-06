package ch.dkrieger.coinsystem.spigot;

import ch.dkrieger.coinsystem.core.CoinSystem;
import ch.dkrieger.coinsystem.core.DKCoinsPlatform;
import ch.dkrieger.coinsystem.core.config.Config;
import ch.dkrieger.coinsystem.core.event.CoinChangeEventResult;
import ch.dkrieger.coinsystem.core.event.CoinsUpdateCause;
import ch.dkrieger.coinsystem.core.manager.MessageManager;
import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import ch.dkrieger.coinsystem.core.player.PlayerColor;
import ch.dkrieger.coinsystem.spigot.commands.CoinsCommand;
import ch.dkrieger.coinsystem.spigot.commands.DKCoinsCommand;
import ch.dkrieger.coinsystem.spigot.commands.PayCommand;
import ch.dkrieger.coinsystem.spigot.event.BukkitCoinPlayerCoinsChangeEvent;
import ch.dkrieger.coinsystem.spigot.event.BukkitCoinPlayerColorSetEvent;
import ch.dkrieger.coinsystem.spigot.hook.PlaceHolderAPIHook;
import ch.dkrieger.coinsystem.spigot.hook.VaultHook;
import ch.dkrieger.coinsystem.spigot.listeners.PlayerListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;

public class SpigotCoinSystemBootstrap extends JavaPlugin implements DKCoinsPlatform {

	public static Thread MAIN_SERVER_THREAD;
	private static SpigotCoinSystemBootstrap INSTANCE;

	@Override
	public void onLoad() {
		MAIN_SERVER_THREAD = Thread.currentThread();
		INSTANCE = this;

		new CoinSystem(this);
	}

	@Override
	public void onEnable() {
		INSTANCE = this;

		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

		getCommand("dkcoins").setExecutor(new DKCoinsCommand());
		registerCommand(new CoinsCommand());
		if(Config.getInstance().command_pay_enabled) registerCommand(new PayCommand());
		hook();
	}

	@Override
	public void onDisable() {
		CoinSystem.getInstance().shutdown();
	}

	@Override
	public String getPlatformName() {
		return "Spigot";
	}

	@Override
	public String getServerVersion() {
		return Bukkit.getVersion();
	}

	@Override
	public File getFolder() {
		return new File("plugins/DKCoins/");
	}

	@Override
	public String getColor(CoinPlayer player) {
		Player bukkitPlayer =  Bukkit.getPlayer(player.getUUID());
		if(bukkitPlayer == null) return null;
		String color = CoinSystem.getInstance().getConfig().defaultColor;
		for(PlayerColor colors : CoinSystem.getInstance().getConfig().playerColors){
			if(bukkitPlayer.hasPermission(colors.getPermission())){
				color = colors.getColor();
				break;
			}
		}
		BukkitCoinPlayerColorSetEvent event = new BukkitCoinPlayerColorSetEvent(color,player,bukkitPlayer);
		Bukkit.getPluginManager().callEvent(event);
		if(event.getColor() != null) color = event.getColor();
		return color;
	}

	@Override
	public CoinChangeEventResult executeCoinChangeEvent(CoinPlayer player, long oldCoins, long newCoins, CoinsUpdateCause cause, String message) {
		BukkitCoinPlayerCoinsChangeEvent event = new BukkitCoinPlayerCoinsChangeEvent(player,oldCoins,newCoins,cause,message);
		Bukkit.getPluginManager().callEvent(event);
		return new CoinChangeEventResult(event.isCancelled(),event.getNewCoins());
	}

	public void registerCommand(Command command){
		CommandMap cmap = null;
		try{
			final Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			field.setAccessible(true);
			cmap = (CommandMap)field.get(Bukkit.getServer());
			cmap.register("", command);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	private void hook(){
		if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
			System.out.println("["+MessageManager.getInstance().system_name+"] PlaceHolderAPI found");
			new PlaceHolderAPIHook().register();
		}
		if(Config.getInstance().hook_vault_enabled){
			if(Bukkit.getPluginManager().isPluginEnabled("Vault")){
				ServicePriority priority = ServicePriority.Highest;
				try{
					priority = ServicePriority.valueOf(Config.getInstance().hook_vault_priority);
				}catch (Exception ignored){}
				System.out.println("["+MessageManager.getInstance().system_name+"] Vault found (priority="+priority.toString()+")");
				Bukkit.getServer().getServicesManager().register(Economy.class, new VaultHook(), this,priority);
			}
		}
	}

	public String format(long coins){
		if(!Config.getInstance().number_formatting_enabled) return ""+coins;
		String number = String.valueOf(coins);
		if(number.length() > 3){
			int charposition = 0;
			char[] chars = number.toCharArray();
			String formatted = "";
			for(int i = chars.length-1;i > -1;i--){
				formatted+=chars[i];
				if(charposition == 2){
					charposition = 0;
					formatted +=Config.getInstance().number_formatting_symbol;
				}else charposition++;
			}
			String finish = "";
			char[] finished = formatted.toCharArray();
			for(int i = finished.length-1;i > -1;i--) finish += finished[i];
			if(finish.startsWith(Config.getInstance().number_formatting_symbol)) finish = finish.substring(1);
			return finish;
		}
		return ""+coins;
	}

	public static SpigotCoinSystemBootstrap getInstance(){
		return INSTANCE;
	}
}

