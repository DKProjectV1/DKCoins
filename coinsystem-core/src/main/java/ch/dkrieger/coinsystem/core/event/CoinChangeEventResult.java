package ch.dkrieger.coinsystem.core.event;

public class CoinChangeEventResult {

    private boolean cancelled;
    private long coins;

    public CoinChangeEventResult(boolean cancelled, long coins){
        this.cancelled = cancelled;
        this.coins = coins;
    }

    public boolean isCancelled(){
        return this.cancelled;
    }

    public long getCoins(){
        return this.coins;
    }
}
