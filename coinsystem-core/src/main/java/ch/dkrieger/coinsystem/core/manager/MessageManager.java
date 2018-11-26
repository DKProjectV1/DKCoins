package ch.dkrieger.coinsystem.core.manager;

public class MessageManager {
	
	private static MessageManager instance;

	public String system_prefix;
	public String system_name;
	public String prefix;
	public String noperms;
	public String mysql_noconnection;
	public String playernotfound;
	
	public String coins_plural;
	public String coins_singular;
	public String coins_symbol;
	
	public String command_coins_showownmoney;
	public String command_coins_showothermoney;
	
	public String command_coins_top_header;
	public String command_coins_top_list;
	
	public String command_coins_pay_notenough;
	public String command_coins_pay_sender;
	public String command_coins_pay_receiver;
	
	public String command_coins_set_sender;
	public String command_coins_set_receiver;
	
	public String command_coins_add_sender;
	public String command_coins_add_receiver;
	
	public String command_coins_remove_sender;
	public String command_coins_remove_receiver;
	
	public String command_coins_help_header;
	public String command_coins_help_coins;
	public String command_coins_help_top;
	public String command_coins_help_pay;
	public String command_coins_help_set;
	public String command_coins_help_add;
	public String command_coins_help_remove;
	public String command_coins_help_reset;

	public MessageManager(String system_name){
		this.system_name = system_name;
		this.system_prefix = "["+system_name+"] ";
		instance = this;
	}

	public static MessageManager getInstance(){
		return instance;
	}
}
