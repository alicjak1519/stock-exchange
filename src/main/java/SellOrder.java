public class SellOrder extends StockOrder {

    SellOrder(Corporation corporation, Stockholder stockholder, int priceLimit, int sharesNumber) {
        super(corporation, stockholder, priceLimit, sharesNumber);
    }
}
