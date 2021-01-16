public class StockOrdersPair {
    SellOrder sellOrder;
    BuyOrder buyOrder;

    public StockOrdersPair(SellOrder sellOrder, BuyOrder buyOrder) {
        this.sellOrder = sellOrder;
        this.buyOrder = buyOrder;
    }

    public SellOrder getSellOrder() {
        return sellOrder;
    }

    public BuyOrder getBuyOrder() {
        return buyOrder;
    }
}
