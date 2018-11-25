package ch.dkrieger.coinsystem.core.event;

public class CoinChangeEventResult {

    private boolean cancelled;
    private Long coins;

    public CoinChangeEventResult(boolean cancelled, Long coins){
        this.cancelled = cancelled;
        this.coins = coins;
    }
    public Boolean isCancelled(){
        return this.cancelled;
    }
    public Long getCoins(){
        return this.coins;
    }
}
