public class Transaction {
    Stockholder seller;
    Stockholder buyer;
    SellOrder sellOrder;
    BuyOrder buyOrder;

    public Transaction(StockOrdersPair stockOrdersPair) {
        this.sellOrder = stockOrdersPair.getSellOrder();
        this.buyOrder = stockOrdersPair.getBuyOrder();
        this.seller = sellOrder.getStockholder();
        this.buyer = buyOrder.getStockholder();
    }

    public void realize() {
        seller.buy(sellOrder);
        buyer.sell(buyOrder);
        System.out.println("Transaction done!!!");
    }

}
