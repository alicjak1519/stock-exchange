import java.util.LinkedList;

public class OrderSheet {
    Corporation corporation;
    LinkedList<BuyOrder> buyOrders = new LinkedList<>();
    LinkedList<SellOrder> sellOrders = new LinkedList<>();
    LinkedList<StockOrdersPair> stockOrdersPairs = new LinkedList<>();

    public OrderSheet(Corporation corporation) {
        this.corporation = corporation;
    }

    public void addSellOrder(SellOrder sellOrder) {
        sellOrders.add(sellOrder);
    }

    public void addBuyOrder(BuyOrder buyOrder) {
        buyOrders.add(buyOrder);
    }

    public LinkedList<SellOrder> getSellOrders() {
        return sellOrders;
    }

    public LinkedList<BuyOrder> getBuyOrders() {
        return buyOrders;
    }

    private void matchMakeOrders() {
        for (SellOrder sellOrder : sellOrders) {
            for (BuyOrder buyOrder : buyOrders) {
                if (sellOrder.corporation == buyOrder.corporation
                        && sellOrder.priceLimit == buyOrder.priceLimit
                        && sellOrder.sharesNumber == buyOrder.sharesNumber) {
                    StockOrdersPair stockOrdersPair = new StockOrdersPair(sellOrder, buyOrder);
                    if (!stockOrdersPairs.contains(stockOrdersPair)) {
                        stockOrdersPairs.add(stockOrdersPair);
                    }
                }
            }
        }
    }

    public LinkedList<StockOrdersPair> getStockOrdersPairs() {
        matchMakeOrders();
        return stockOrdersPairs;
    }

    public StockOrdersPair getStockOrdersPairToTransaction() {
        matchMakeOrders();
        StockOrdersPair stockOrdersPairToRealizeTransaction = stockOrdersPairs.removeFirst();
        buyOrders.removeFirstOccurrence(stockOrdersPairToRealizeTransaction.getBuyOrder());
        sellOrders.removeFirstOccurrence(stockOrdersPairToRealizeTransaction.getSellOrder());
        return stockOrdersPairToRealizeTransaction;
    }
}
