package ch.dkrieger.coinsystem.spigot.commands;

import ch.dkrieger.coinsystem.core.CoinSystem;
import ch.dkrieger.coinsystem.core.manager.MessageManager;
import ch.dkrieger.coinsystem.core.manager.PermissionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand extends Command{

	public PayCommand() {
		super(CoinSystem.getInstance().getConfig().command_pay_name,"pay","/"+CoinSystem.getInstance().getConfig().command_pay_name+" <player> <amount>",CoinSystem.getInstance().getConfig().command_pay_aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String label,String[] args) {
		if(sender.hasPermission(PermissionManager.getInstance().command_coins_pay)){
			if(args.length >= 2){
				if(sender instanceof Player) ((Player)sender).performCommand(CoinSystem.getInstance().getConfig().command_name+" pay "+args[0]+" "+args[1]);
			}else sender.sendMessage(MessageManager.getInstance().prefix+MessageManager.getInstance().command_coins_help_pay);
		}else sender.sendMessage(MessageManager.getInstance().noperms);
		return false;
	}
}
