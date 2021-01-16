import java.util.HashMap;
import java.util.Map;

public class Stockholder {
    int budget;
    Map<Share, Integer> stockholderStocks = new HashMap<Share, Integer>();

    public Stockholder(int budget) {
        this.budget = budget;
    }

    public StockOrder generateBuyOrder(Corporation corporation, int priceLimit, int sharesNumber) {
        return new BuyOrder(corporation, this, priceLimit, sharesNumber);
    }

    public StockOrder generateSellOrder(Corporation corporation, int priceLimit, int sharesNumber) {
        return new SellOrder(corporation, this, priceLimit, sharesNumber);
    }

    public void buy(SellOrder sellOrder) {
        budget = budget - sellOrder.getStockOrderValue();
        stockholderStocks.put(new Share(sellOrder.getCorporation(), this), sellOrder.getSharesNumber());
    }

    public void sell(BuyOrder buyOrder) {
        budget = budget + buyOrder.getStockOrderValue();
        stockholderStocks.put(new Share(buyOrder.getCorporation(), this), (-1) * buyOrder.getSharesNumber());
    }
}
