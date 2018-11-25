package ch.dkrieger.coinsystem.spigot.commands;

import ch.dkrieger.coinsystem.core.CoinSystem;
import ch.dkrieger.coinsystem.core.config.Config;
import ch.dkrieger.coinsystem.core.event.CoinsUpdateCause;
import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import ch.dkrieger.coinsystem.spigot.SpigotCoinSystemBootstrap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ch.dkrieger.coinsystem.core.manager.MessageManager;
import ch.dkrieger.coinsystem.core.manager.PermissionManager;

public class CoinsCommand extends Command{

	public CoinsCommand() {
		super(Config.getInstance().command_name,"Coins command","/"+Config.getInstance().command_name+" <player>",Config.getInstance().command_aliases);
	}
	@Override
	public boolean execute(CommandSender sender, String label,String[] args) {
		if(!sender.hasPermission(PermissionManager.getInstance().command_coins)){
			sender.sendMessage(MessageManager.getInstance().noperms);
			return true;
		}
		if(!CoinSystem.getInstance().getStorage().isConnected()){
			sender.sendMessage(MessageManager.getInstance().mysql_noconnection);
			return true;
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
					return true;
				}
				sender.sendMessage(MessageManager.getInstance().command_coins_top_header);
				int rang = 0;
				for(CoinPlayer coinplayers : CoinSystem.getInstance().getPlayerManager().getTopCoins(10)){
					rang++;
					sender.sendMessage(MessageManager.getInstance().command_coins_top_list.replace("[rang]"
							, String.valueOf(rang)).replace("[player]",coinplayers.getColor()+coinplayers.getName()).replace("[amount]"
							, SpigotCoinSystemBootstrap.getInstance().format(coinplayers.getCoins())));
				}
				return true;
			}else if(args[0].equalsIgnoreCase("help")){
				sendHelp(sender);
				return true;
			}else if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")){
				if(sender.hasPermission(PermissionManager.getInstance().command_coins_admin)) CoinSystem.getInstance().reload();
			}
			if(!sender.hasPermission(PermissionManager.getInstance().command_coins_others)){
				Player p = (Player) sender;
				p.performCommand("coins");
				return true;
			}
			CoinPlayer coinplayer = CoinSystem.getInstance().getPlayerManager().getPlayer(args[0]);
			if(coinplayer == null){
				sender.sendMessage(MessageManager.getInstance().playernotfound.replace("[player]",Config.getInstance().defaultColor+args[0]));
				return true;
			}
			sender.sendMessage(MessageManager.getInstance().command_coins_showothermoney.replace("[player]",coinplayer.getColor()+coinplayer.getName()).replace("[amount]", SpigotCoinSystemBootstrap.getInstance().format(coinplayer.getCoins())));
		}else if(args.length == 2){
			if(args[0].equalsIgnoreCase("reset")){
				CoinPlayer coinplayer = CoinSystem.getInstance().getPlayerManager().getPlayer(args[1]);
				if(coinplayer == null){
					sender.sendMessage(MessageManager.getInstance().playernotfound.replace("[player]",Config.getInstance().defaultColor+args[0]));
					return true;
				}
				coinplayer.setCoins(Config.getInstance().system_player_startcoin,CoinsUpdateCause.ADMIN);
				sender.sendMessage(MessageManager.getInstance().command_coins_set_sender.replace("[player]",coinplayer.getColor()+coinplayer.getName()).replace("[amount]", SpigotCoinSystemBootstrap.getInstance().format(Config.getInstance().system_player_startcoin)));
				Player player = Bukkit.getPlayer(args[1]);
				if(player != null) player.sendMessage(MessageManager.getInstance().command_coins_set_receiver.replace("[amount]",""+Config.getInstance().system_player_startcoin));
			}else sendHelp(sender);
		}else if(args.length == 3){
			if(args[0].equalsIgnoreCase("pay")){
				if(sender.getName().equalsIgnoreCase(args[1])){
					sendHelp(sender);
					return true;
				}
				if(!sender.hasPermission(PermissionManager.getInstance().command_coins_pay)){
					sender.sendMessage(MessageManager.getInstance().noperms);
					return true;
				}
				CoinPlayer coinplayer = CoinSystem.getInstance().getPlayerManager().getPlayer(args[1]);
				if(coinplayer == null){
					sender.sendMessage(MessageManager.getInstance().playernotfound.replace("[player]",Config.getInstance().defaultColor+args[0]));
					return true;
				}
				if(!isNumber(args[2])){
					sendHelp(sender);
					return true;
				}
				Long amount = Long.valueOf(args[2]);
				if(amount < 1){
					sendHelp(sender);
					return true;
				}
				CoinPlayer coinsender = CoinSystem.getInstance().getPlayerManager().getPlayer(sender.getName());

				if(!coinsender.hasCoins(amount)){
					sender.sendMessage(MessageManager.getInstance().command_coins_pay_notenough);
					return true;
				}
				coinsender.removeCoins(amount,CoinsUpdateCause.PAY);
				coinplayer.addCoins(amount,CoinsUpdateCause.PAY);
				sender.sendMessage(MessageManager.getInstance().command_coins_pay_sender.replace("[player]",coinplayer.getColor()+coinplayer.getName()).replace("[amount]", SpigotCoinSystemBootstrap.getInstance().format(amount)));
				Player player = Bukkit.getPlayer(args[1]);
				if(player != null) player.sendMessage(MessageManager.getInstance().command_coins_pay_receiver.replace("[amount]", SpigotCoinSystemBootstrap.getInstance().format(amount)).replace("[player]",coinplayer.getColor()+coinsender.getName()));
			}else if(!sender.hasPermission(PermissionManager.getInstance().command_coins_admin)){
				sendHelp(sender);
				return true;
			}else if(args[0].equalsIgnoreCase("set")){
				CoinPlayer coinplayer = CoinSystem.getInstance().getPlayerManager().getPlayer(args[1]);
				if(coinplayer == null){
					sender.sendMessage(MessageManager.getInstance().playernotfound.replace("[player]",Config.getInstance().defaultColor+args[0]));
					return true;
				}
				if(!isNumber(args[2])){
					sendHelp(sender);
					return true;
				}
				Long amount = Long.valueOf(args[2]);
				if(amount < 0) amount = 0L;
				coinplayer.setCoins(amount,CoinsUpdateCause.ADMIN);
				sender.sendMessage(MessageManager.getInstance().command_coins_set_sender.replace("[player]",coinplayer.getColor()+coinplayer.getName()).replace("[amount]", SpigotCoinSystemBootstrap.getInstance().format(amount)));
				Player player = Bukkit.getPlayer(args[1]);
				if(player != null) player.sendMessage(MessageManager.getInstance().command_coins_set_receiver.replace("[amount]", SpigotCoinSystemBootstrap.getInstance().format(amount)));
			}else if(args[0].equalsIgnoreCase("add")){
				CoinPlayer coinplayer = CoinSystem.getInstance().getPlayerManager().getPlayer(args[1]);
				if(coinplayer == null){
					sender.sendMessage(MessageManager.getInstance().playernotfound.replace("[player]",Config.getInstance().defaultColor+args[0]));
					return true;
				}
				if(!isNumber(args[2])){
					sendHelp(sender);
					return true;
				}
				Long amount = Long.valueOf(args[2]);
				if(amount < 1) amount = 1L;
				coinplayer.addCoins(amount,CoinsUpdateCause.ADMIN);
				sender.sendMessage(MessageManager.getInstance().command_coins_add_sender.replace("[player]",coinplayer.getColor()+coinplayer.getName()).replace("[amount]", SpigotCoinSystemBootstrap.getInstance().format(amount)));
				Player player = Bukkit.getPlayer(args[1]);
				if(player != null) player.sendMessage(MessageManager.getInstance().command_coins_add_receiver.replace("[amount]", SpigotCoinSystemBootstrap.getInstance().format(amount)));
			}else if(args[0].equalsIgnoreCase("remove")){
				CoinPlayer coinplayer = CoinSystem.getInstance().getPlayerManager().getPlayer(args[1]);
				if(coinplayer == null){
					sender.sendMessage(MessageManager.getInstance().playernotfound.replace("[player]", Config.getInstance().defaultColor+args[0]));
					return true;
				}
				if(!isNumber(args[2])){
					sendHelp(sender);
					return true;
				}
				Long amount = Long.valueOf(args[2]);
				if(amount < 1) amount = 1L;
				if(coinplayer.hasCoins(amount)){
					coinplayer.removeCoins(amount,CoinsUpdateCause.ADMIN);
				}else coinplayer.setCoins((long) 0,CoinsUpdateCause.ADMIN);
				sender.sendMessage(MessageManager.getInstance().command_coins_remove_sender.replace("[player]",coinplayer.getColor()+coinplayer.getName()).replace("[amount]", SpigotCoinSystemBootstrap.getInstance().format(amount)));
				Player player = Bukkit.getPlayer(args[1]);
				if(player != null) player.sendMessage(MessageManager.getInstance().command_coins_remove_receiver.replace("[amount]", SpigotCoinSystemBootstrap.getInstance().format(amount)));
			}else sendHelp(sender);
		}else sendHelp(sender);
		return false;
	}
	private void
	sendHelp(CommandSender sender){
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
	private Boolean isNumber(String value){
		try{
			int number = Integer.parseInt(value);
			return true; 
		}catch(NumberFormatException e) {
		    return false;
		}
	}
}
