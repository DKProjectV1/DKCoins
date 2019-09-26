package ch.dkrieger.coinsystem.spigot.hook;

import ch.dkrieger.coinsystem.core.CoinSystem;
import ch.dkrieger.coinsystem.core.manager.MessageManager;
import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class VaultHook extends AbstractEconomy{

	public EconomyResponse bankBalance(String arg0) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, MessageManager.getInstance().system_name+" has no bank support");
	}

	public EconomyResponse bankDeposit(String arg0, double arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, MessageManager.getInstance().system_name+" has no bank support");
	}

	public EconomyResponse bankHas(String arg0, double arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, MessageManager.getInstance().system_name+" has no bank support");
	}

	public EconomyResponse bankWithdraw(String arg0, double arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, MessageManager.getInstance().system_name+" has no bank support");
	}

	public EconomyResponse createBank(String arg0, String arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, MessageManager.getInstance().system_name+" has no bank support");
	}

	public EconomyResponse createBank(String arg0, OfflinePlayer arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, MessageManager.getInstance().system_name+" has no bank support");
	}

	public boolean createPlayerAccount(String arg0) {
		return false;
	}

	public boolean createPlayerAccount(OfflinePlayer arg0) {
		return false;
	}

	public boolean createPlayerAccount(String arg0, String arg1) {
		return false;
	}

	public boolean createPlayerAccount(OfflinePlayer arg0, String arg1) {
		return false;
	}

	public String currencyNamePlural() {
		return MessageManager.getInstance().coins_plural;
	}

	public String currencyNameSingular() {
		return MessageManager.getInstance().coins_singular;
	}

	public EconomyResponse deleteBank(String arg0) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, MessageManager.getInstance().system_name+" has no bank support");
	}

	public EconomyResponse depositPlayer(String player, double coins) {
		CoinPlayer coinplayer = CoinSystem.getInstance().getPlayerManager().getPlayer(player);
		if(coinplayer == null) return new EconomyResponse(0, 0, ResponseType.FAILURE, "Player notfound");
		coinplayer.addCoins((long) coins,"vaulthook");
		return new EconomyResponse(0, coinplayer.getCoins(), ResponseType.SUCCESS,"");
	}

	public EconomyResponse depositPlayer(OfflinePlayer player, double coins) {
		return depositPlayer(player.getName(), coins);
	}

	public EconomyResponse depositPlayer(String player, String arg1, double coins) {
		return depositPlayer(player, coins);
	}

	public EconomyResponse depositPlayer(OfflinePlayer player, String arg1,double coins) {
		return depositPlayer(player.getName(), coins);
	}

	public String format(double coins) {
		return coins+MessageManager.getInstance().coins_symbol;
	}

	public int fractionalDigits() {
		return 0;
	}

	public double getBalance(String player) {
		CoinPlayer coinplayer = CoinSystem.getInstance().getPlayerManager().getPlayer(player);
		if(coinplayer != null) return coinplayer.getCoins();
		return 0;
	}

	public double getBalance(OfflinePlayer player) {
		return getBalance(player.getName());
	}

	public double getBalance(String name, String arg1) {
		return getBalance(name);
	}

	public double getBalance(OfflinePlayer player, String arg1) {
		return getBalance(player.getName());
	}
	public List<String> getBanks() {
		return null;
	}

	public String getName() {
		return MessageManager.getInstance().system_name;
	}

	public boolean has(String player, double coins) {
		CoinPlayer coinplayer = CoinSystem.getInstance().getPlayerManager().getPlayer(player);
		return coinplayer != null && coinplayer.getCoins() >= coins;
	}
	public boolean has(OfflinePlayer player, double coins) {
		return has(player.getName(), coins);
	}

	public boolean has(String player, String arg1, double coins) {
		return has(player, coins);
	}

	public boolean has(OfflinePlayer player, String arg1, double coins) {
		return has(player.getName(), coins);
	}

	public boolean hasAccount(String player) {
		CoinPlayer coinplayer = CoinSystem.getInstance().getPlayerManager().getPlayer(player);
		return coinplayer != null;
	}
	public boolean hasAccount(OfflinePlayer player) {
		return hasAccount(player.getName());
	}

	public boolean hasAccount(String player, String arg1) {
		return hasAccount(player);
	}

	public boolean hasAccount(OfflinePlayer player, String arg1) {
		return hasAccount(player.getName());
	}

	public boolean hasBankSupport() {
		return false;
	}

	public EconomyResponse isBankMember(String arg0, String arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, MessageManager.getInstance().system_name+" has no bank support");
	}

	public EconomyResponse isBankMember(String arg0, OfflinePlayer arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, MessageManager.getInstance().system_name+" has no bank support");
	}

	public EconomyResponse isBankOwner(String arg0, String arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, MessageManager.getInstance().system_name+" has no bank support");
	}

	public EconomyResponse isBankOwner(String arg0, OfflinePlayer arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, MessageManager.getInstance().system_name+" has no bank support");
	}

	public boolean isEnabled() {
		return true;
	}

	public EconomyResponse withdrawPlayer(String player, double coins) {
		CoinPlayer coinplayer = CoinSystem.getInstance().getPlayerManager().getPlayer(player);
		if(coinplayer == null) return new EconomyResponse(0, 0, ResponseType.FAILURE, "Player notfound");
		if(coins <= 0) return new EconomyResponse(0, coinplayer.getCoins(), ResponseType.FAILURE, "Cannot withdraw negative funds");
		if(!coinplayer.hasCoins((long) coins)){
			return new EconomyResponse(0, coinplayer.getCoins(), ResponseType.FAILURE, "Player has not enough coins");
		}
		coinplayer.removeCoins((long) coins,"vaulthook");
		return new EconomyResponse(coins, coinplayer.getCoins(), ResponseType.SUCCESS, "");
	}

	public EconomyResponse withdrawPlayer(OfflinePlayer player, double coins) {
		return withdrawPlayer(player.getName(), coins);
	}

	public EconomyResponse withdrawPlayer(String player, String arg1, double coins) {
		return withdrawPlayer(player, coins);
	}

	public EconomyResponse withdrawPlayer(OfflinePlayer player, String arg1,double coins) {
		return withdrawPlayer(player.getName(), coins);
	}
}
