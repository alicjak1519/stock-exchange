package pl.agh;

import java.util.ArrayList;
import java.util.List;

public class OrderSheet {
    Corporation corporation;
    List<BuyOrder> buyOrders = new ArrayList<>();
    List<SellOrder> sellOrders = new ArrayList<>();
    MatchMaker matchMaker = new MatchMaker();

    public OrderSheet(Corporation corporation) {
        this.corporation = corporation;
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

    public String getCurrentState() {
        String currentState;
        synchronized (this) {
            currentState = buyOrders.size() + ", " + sellOrders.size();
        }
        return currentState;
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

        seller.buy(stockOrdersPair.getSellOrder());
        buyer.sell(stockOrdersPair.getBuyOrder());

        synchronized (this) {
            sellOrders.remove(stockOrdersPair.getSellOrder());
            buyOrders.remove(stockOrdersPair.getBuyOrder());
        }
        System.out.println("Transaction done!!! " + stockOrdersPair.getSellOrder());
    }

    @Override
    public String toString() {
        return "java.pl.agh.OrderSheet{" +
                "corporation=" + corporation +
                ", buyOrders=" + buyOrders +
                ", sellOrders=" + sellOrders +
                '}';
    }
}
