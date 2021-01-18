package pl.agh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderSheet {
    private List<Corporation> corporations;
    private List<BuyOrder> buyOrders = new ArrayList<>();
    private List<SellOrder> sellOrders = new ArrayList<>();
    private MatchMaker matchMaker = new MatchMaker();
    private float actualGlobalSharePrice = 0;
    Map<Corporation, Float> currentState = new HashMap<>();

    public OrderSheet(List<Corporation> corporations) {
        this.corporations = corporations;
        this.corporations.forEach(corporation -> {
            this.currentState.put(corporation, (float) 0);
        });
    }

    public void addSellOrder(SellOrder sellOrder) {
        synchronized (this) {
            sellOrders.add(sellOrder);
        }
    }

    public void addBuyOrder(BuyOrder buyOrder) {
        synchronized (this) {
            buyOrders.add(buyOrder);
        }
    }

    public Map<Corporation, Float> getCurrentState(){
        return currentState;
    }

    public void updateCurrentState(Corporation corporation, Float sharePrice){
        currentState.put(corporation, sharePrice);
    }

    public List<StockOrdersPair> buildStockOrdersPairs() {
        List<StockOrdersPair> stockOrdersPairs;
        synchronized (this) {
            stockOrdersPairs = matchMaker.matchMake(buyOrders, sellOrders);
        }
        return stockOrdersPairs;
    }

    public void realizeTransaction(StockOrdersPair stockOrdersPair) {
        Stockholder seller = stockOrdersPair.getSellOrder().getStockholder();
        Stockholder buyer = stockOrdersPair.getBuyOrder().getStockholder();

        if (seller.canSell(stockOrdersPair.getSellOrder()) && buyer.canBuy(stockOrdersPair.getBuyOrder())) {
            seller.sell(stockOrdersPair);
            buyer.buy(stockOrdersPair);

            synchronized (this) {
                sellOrders.remove(stockOrdersPair.getSellOrder());
                buyOrders.remove(stockOrdersPair.getBuyOrder());
            }
            actualGlobalSharePrice = stockOrdersPair.getBuyOrder().priceLimit;
            updateCurrentState(stockOrdersPair.getBuyOrder().getCorporation(), actualGlobalSharePrice);
        }
    }

    @Override
    public String toString() {
        return "java.pl.agh.OrderSheet{" +
                ", buyOrders=" + buyOrders +
                ", sellOrders=" + sellOrders +
                '}';
    }
}
