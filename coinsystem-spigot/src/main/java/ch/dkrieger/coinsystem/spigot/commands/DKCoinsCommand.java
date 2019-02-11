package ch.dkrieger.coinsystem.spigot.commands;

import ch.dkrieger.coinsystem.core.manager.MessageManager;
import ch.dkrieger.coinsystem.spigot.SpigotCoinSystemBootstrap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DKCoinsCommand implements CommandExecutor{

	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] arg3) {
		sender.sendMessage(MessageManager.getInstance().prefix+"§7CoinSystem §cv"+ SpigotCoinSystemBootstrap.getInstance().getDescription().getVersion()+" §7by §cDavide Wietlisbach");
		return false;
	}
}
