public class BuyOrder extends StockOrder {

    BuyOrder(Corporation corporation, Stockholder stockholder, int priceLimit, int sharesNumber) {
        super(corporation, stockholder, priceLimit, sharesNumber);
    }
}

