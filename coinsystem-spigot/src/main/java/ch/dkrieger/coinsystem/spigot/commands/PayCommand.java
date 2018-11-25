package ch.dkrieger.coinsystem.spigot.commands;

import ch.dkrieger.coinsystem.core.config.Config;
import ch.dkrieger.coinsystem.core.manager.MessageManager;
import ch.dkrieger.coinsystem.core.manager.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand extends Command{

	public PayCommand() {
		super(Config.getInstance().command_pay_name,"pay","/"+Config.getInstance().command_pay_name+" <player> <amount>",Config.getInstance().command_pay_aliases);
	}
	@Override
	public boolean execute(CommandSender sender, String label,String[] args) {
		if(sender.hasPermission(PermissionManager.getInstance().command_coins_pay)){
			if(args.length >= 2){
				if(sender instanceof Player) ((Player)sender).performCommand(Config.getInstance().command_name+" pay "+args[0]+" "+args[1]);
			}else sender.sendMessage(MessageManager.getInstance().prefix+MessageManager.getInstance().command_coins_help_pay);
		}else sender.sendMessage(MessageManager.getInstance().noperms);
		return false;
	}
}
