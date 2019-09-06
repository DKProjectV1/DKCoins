package ch.dkrieger.coinsystem.spigot.commands;

import ch.dkrieger.coinsystem.core.CoinSystem;
import ch.dkrieger.coinsystem.core.event.CoinsUpdateCause;
import ch.dkrieger.coinsystem.core.manager.MessageManager;
import ch.dkrieger.coinsystem.core.manager.PermissionManager;
import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import ch.dkrieger.coinsystem.spigot.SpigotCoinSystemBootstrap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CoinsCommand extends Command{

	public static DummyAllCoinPlayer DUMMY_ALL_PLAYER = new DummyAllCoinPlayer();

	public CoinsCommand() {
		super(CoinSystem.getInstance().getConfig().command_name,"Coins command","/"+CoinSystem.getInstance().getConfig().command_name+" <player>",CoinSystem.getInstance().getConfig().command_aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String label,String[] args) {
		Bukkit.getScheduler().runTaskAsynchronously(SpigotCoinSystemBootstrap.getInstance(),()->{
			if(!sender.hasPermission(PermissionManager.getInstance().command_coins)){
				sender.sendMessage(MessageManager.getInstance().noperms);
				return;
			}
			if(!CoinSystem.getInstance().getStorage().isConnected()){
				sender.sendMessage(MessageManager.getInstance().mysql_noconnection);
				return;
			}
			if(args.length == 0){
				CoinPlayer coinplayer = CoinSystem.getInstance().getPlayerManager().getPlayer(sender.getName());
				if(coinplayer != null){
					sender.sendMessage(MessageManager.getInstance().command_coins_showownmoney.replace("[amount]"
							, SpigotCoinSystemBootstrap.getInstance().format(coinplayer.getCoins())));
				}
			}else if(args.length == 1){
				if(args[0].equalsIgnoreCase("top")){
					if(!sender.hasPermission(PermissionManager.getInstance().command_coins_top)){
						sender.sendMessage(MessageManager.getInstance().noperms);
						return;
					}
					sender.sendMessage(MessageManager.getInstance().command_coins_top_header);
					int rang = 0;
					for(CoinPlayer coinplayers : CoinSystem.getInstance().getPlayerManager().getTopCoins(10)){
						rang++;
						sender.sendMessage(MessageManager.getInstance().command_coins_top_list.replace("[rang]"
								, String.valueOf(rang)).replace("[player]",coinplayers.getColor()+coinplayers.getName()).replace("[amount]"
								, SpigotCoinSystemBootstrap.getInstance().format(coinplayers.getCoins())));
					}
					return;
				}else if(args[0].equalsIgnoreCase("help")){
					sendHelp(sender);
					return;
				}else if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")
						&& sender.hasPermission(PermissionManager.getInstance().command_coins_admin)){
					CoinSystem.getInstance().reload();
				}
				if(!sender.hasPermission(PermissionManager.getInstance().command_coins_others)){
					Player p = (Player) sender;
					p.performCommand("coins");
					return;
				}
				CoinPlayer coinplayer = CoinSystem.getInstance().getPlayerManager().getPlayer(args[0]);
				if(coinplayer == null){
					sender.sendMessage(MessageManager.getInstance().playernotfound.replace("[player]",CoinSystem.getInstance().getConfig().defaultColor+args[0]));
					return;
				}
				sender.sendMessage(MessageManager.getInstance().command_coins_showothermoney.replace("[player]",coinplayer.getColor()+coinplayer.getName()).replace("[amount]", SpigotCoinSystemBootstrap.getInstance().format(coinplayer.getCoins())));
			}else if(args.length == 2){
				if(args[0].equalsIgnoreCase("reset")){
					CoinPlayer coinplayer = getCoinPlayer(sender,args[1]);
					if(coinplayer == null){
						sender.sendMessage(MessageManager.getInstance().playernotfound.replace("[player]",CoinSystem.getInstance().getConfig().defaultColor+args[0]));
						return;
					}
					coinplayer.setCoins(CoinSystem.getInstance().getConfig().system_player_startcoin,CoinsUpdateCause.ADMIN);
					sender.sendMessage(MessageManager.getInstance().command_coins_set_sender.replace("[player]",coinplayer.getColor()+coinplayer.getName()).replace("[amount]", SpigotCoinSystemBootstrap.getInstance().format(CoinSystem.getInstance().getConfig().system_player_startcoin)));
					Player player = Bukkit.getPlayer(args[1]);
					if(player != null) player.sendMessage(MessageManager.getInstance().command_coins_set_receiver.replace("[amount]",""+CoinSystem.getInstance().getConfig().system_player_startcoin));
				}else sendHelp(sender);
			}else if(args.length == 3){
				if(args[0].equalsIgnoreCase("pay")){
					if(sender.getName().equalsIgnoreCase(args[1])){
						sendHelp(sender);
						return;
					}
					if(!sender.hasPermission(PermissionManager.getInstance().command_coins_pay)){
						sender.sendMessage(MessageManager.getInstance().noperms);
						return;
					}
					CoinPlayer coinplayer = CoinSystem.getInstance().getPlayerManager().getPlayer(args[1]);
					if(coinplayer == null){
						sender.sendMessage(MessageManager.getInstance().playernotfound.replace("[player]",CoinSystem.getInstance().getConfig().defaultColor+args[0]));
						return;
					}
					if(!isNumber(args[2])){
						sendHelp(sender);
						return;
					}
					long amount = Long.parseLong(args[2]);
					if(amount < 1){
						sendHelp(sender);
						return;
					}
					CoinPlayer coinsender = CoinSystem.getInstance().getPlayerManager().getPlayer(sender.getName());

					if(!coinsender.hasCoins(amount)){
						sender.sendMessage(MessageManager.getInstance().command_coins_pay_notenough);
						return;
					}
					coinsender.removeCoins(amount,CoinsUpdateCause.PAY);
					coinplayer.addCoins(amount,CoinsUpdateCause.PAY);
					sender.sendMessage(MessageManager.getInstance().command_coins_pay_sender.replace("[player]",coinplayer.getColor()+coinplayer.getName()).replace("[amount]", SpigotCoinSystemBootstrap.getInstance().format(amount)));
					Player player = Bukkit.getPlayer(args[1]);
					if(player != null) player.sendMessage(MessageManager.getInstance().command_coins_pay_receiver.replace("[amount]", SpigotCoinSystemBootstrap.getInstance().format(amount)).replace("[player]",coinplayer.getColor()+coinsender.getName()));
				}else if(!sender.hasPermission(PermissionManager.getInstance().command_coins_admin)){
					sendHelp(sender);
				}else if(args[0].equalsIgnoreCase("set")){
					CoinPlayer coinplayer = getCoinPlayer(sender,args[1]);
					if(coinplayer == null){
						sender.sendMessage(MessageManager.getInstance().playernotfound.replace("[player]",CoinSystem.getInstance().getConfig().defaultColor+args[0]));
						return;
					}
					if(!isNumber(args[2])){
						sendHelp(sender);
						return;
					}
					long amount = Long.parseLong(args[2]);
					if(amount < 0) amount = 0L;
					coinplayer.setCoins(amount,CoinsUpdateCause.ADMIN);
					sender.sendMessage(MessageManager.getInstance().command_coins_set_sender.replace("[player]",coinplayer.getColor()+coinplayer.getName()).replace("[amount]", SpigotCoinSystemBootstrap.getInstance().format(amount)));
					Player player = Bukkit.getPlayer(args[1]);
					if(player != null) player.sendMessage(MessageManager.getInstance().command_coins_set_receiver.replace("[amount]", SpigotCoinSystemBootstrap.getInstance().format(amount)));
				}else if(args[0].equalsIgnoreCase("add")){
					CoinPlayer coinplayer = getCoinPlayer(sender,args[1]);
					if(coinplayer == null){
						sender.sendMessage(MessageManager.getInstance().playernotfound.replace("[player]",CoinSystem.getInstance().getConfig().defaultColor+args[0]));
						return;
					}
					if(!isNumber(args[2])){
						sendHelp(sender);
						return;
					}
					long amount = Long.parseLong(args[2]);
					if(amount < 1) amount = 1L;
					coinplayer.addCoins(amount,CoinsUpdateCause.ADMIN);
					sender.sendMessage(MessageManager.getInstance().command_coins_add_sender.replace("[player]",coinplayer.getColor()+coinplayer.getName()).replace("[amount]", SpigotCoinSystemBootstrap.getInstance().format(amount)));
					Player player = Bukkit.getPlayer(args[1]);
					if(player != null) player.sendMessage(MessageManager.getInstance().command_coins_add_receiver.replace("[amount]", SpigotCoinSystemBootstrap.getInstance().format(amount)));
				}else if(args[0].equalsIgnoreCase("remove")){
					CoinPlayer coinplayer = getCoinPlayer(sender,args[1]);
					if(coinplayer == null){
						sender.sendMessage(MessageManager.getInstance().playernotfound.replace("[player]", CoinSystem.getInstance().getConfig().defaultColor+args[0]));
						return;
					}
					if(!isNumber(args[2])){
						sendHelp(sender);
						return;
					}
					long amount = Long.parseLong(args[2]);
					if(amount < 1) amount = 1L;
					if(coinplayer.hasCoins(amount)){
						coinplayer.removeCoins(amount,CoinsUpdateCause.ADMIN);
					}else coinplayer.setCoins((long) 0,CoinsUpdateCause.ADMIN);
					sender.sendMessage(MessageManager.getInstance().command_coins_remove_sender.replace("[player]",coinplayer.getColor()+coinplayer.getName()).replace("[amount]", SpigotCoinSystemBootstrap.getInstance().format(amount)));
					Player player = Bukkit.getPlayer(args[1]);
					if(player != null) player.sendMessage(MessageManager.getInstance().command_coins_remove_receiver.replace("[amount]", SpigotCoinSystemBootstrap.getInstance().format(amount)));
				}else sendHelp(sender);
			}else sendHelp(sender);
		});
		return false;
	}

	private CoinPlayer getCoinPlayer(CommandSender sender,String name){
		if(name.equalsIgnoreCase("@p")){
			Location location;
			if(sender instanceof BlockCommandSender) location = ((BlockCommandSender) sender).getBlock().getLocation();
			else if(sender instanceof Player) location = ((Player) sender).getLocation();
			else return null;
			Player nearest = null;
			double nearestDistance = 0L;
			for(Player player : Bukkit.getOnlinePlayers()){
				if(player != sender && player.getLocation().getWorld() == location.getWorld()){
					double distance = player.getLocation().distance(location);
					if(distance < nearestDistance){
						nearestDistance = distance;
						nearest = player;
					}
				}
			}
			return nearest!=null?CoinSystem.getInstance().getPlayerManager().getPlayer(nearest.getUniqueId()):null;
		}else if(name.equalsIgnoreCase("@a") || name.equalsIgnoreCase("@all")){
			return DUMMY_ALL_PLAYER;
		}else return CoinSystem.getInstance().getPlayerManager().getPlayer(name);
	}

	private void sendHelp(CommandSender sender){
		sender.sendMessage(MessageManager.getInstance().command_coins_help_header);
		sender.sendMessage(MessageManager.getInstance().command_coins_help_coins);
		if(sender.hasPermission(PermissionManager.getInstance().command_coins_top)){
			sender.sendMessage(MessageManager.getInstance().command_coins_help_top);
		}
		if(sender.hasPermission(PermissionManager.getInstance().command_coins_pay)){
			sender.sendMessage(MessageManager.getInstance().command_coins_help_pay);
		}
		if(sender.hasPermission(PermissionManager.getInstance().command_coins_admin)){
			sender.sendMessage(MessageManager.getInstance().command_coins_help_set);
			sender.sendMessage(MessageManager.getInstance().command_coins_help_add);
			sender.sendMessage(MessageManager.getInstance().command_coins_help_remove);
			sender.sendMessage(MessageManager.getInstance().command_coins_help_reset);
		}
	}

	private boolean isNumber(String value){
		try{
			Integer.parseInt(value);
			return true; 
		}catch(NumberFormatException e) {
		    return false;
		}
	}

	public static class DummyAllCoinPlayer extends CoinPlayer{

		private DummyAllCoinPlayer() {
			super(-30,UUID.randomUUID(),"All","§4",0L,0L, 0L);
		}

		@Override
		public boolean hasCoins(long coins) {
			return true;
		}

		@Override
		public void addCoins(long coins, CoinsUpdateCause cause, String message) {
			for(Player player : Bukkit.getOnlinePlayers()){
				CoinPlayer coinPlayer = CoinSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
				if(coinPlayer != null){
					player.sendMessage(MessageManager.getInstance().command_coins_add_receiver.replace("[amount]", SpigotCoinSystemBootstrap.getInstance().format(coins)));
					coinPlayer.addCoins(coins, cause, message);
				}
			}
		}

		@Override
		public void removeCoins(long coins, CoinsUpdateCause cause, String message) {
			for(Player player : Bukkit.getOnlinePlayers()){
				CoinPlayer coinPlayer = CoinSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
				if(coinPlayer != null){
					player.sendMessage(MessageManager.getInstance().command_coins_remove_receiver.replace("[amount]", SpigotCoinSystemBootstrap.getInstance().format(coins)));
					if(coinPlayer.hasCoins(coins))coinPlayer.removeCoins(coins, cause, message);
				}
			}
		}

		@Override
		public void setCoins(long coins, CoinsUpdateCause cause, String message) {
			for(Player player : Bukkit.getOnlinePlayers()){
				CoinPlayer coinPlayer = CoinSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
				if(coinPlayer != null){
					player.sendMessage(MessageManager.getInstance().command_coins_set_receiver.replace("[amount]", SpigotCoinSystemBootstrap.getInstance().format(coins)));
					coinPlayer.setCoins(coins, cause, message);
				}
			}
		}

		@Override
		public void setColor(String color) {
			setColorSimpled(color);
		}

		@Override
		public void updateInfos(String name, String color, long lastLogin) {
			throw new UnsupportedOperationException("@All dummy player is only update able");
		}

	}
}
