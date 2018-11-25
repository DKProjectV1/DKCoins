package ch.dkrieger.coinsystem.core.manager;

public class PermissionManager {
	
	private static PermissionManager instance;
	public PermissionManager(){
		instance = this;
	}
	
	public String admin;
	
	public String command_coins;
	public String command_coins_others;
	public String command_coins_top;
	public String command_coins_pay;
	public String command_coins_admin;

	public static PermissionManager getInstance(){
		return instance;
	}
}
