import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockOrdersPair that = (StockOrdersPair) o;
        return Objects.equals(sellOrder, that.sellOrder) &&
                Objects.equals(buyOrder, that.buyOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sellOrder, buyOrder);
    }

    @Override
    public String toString() {
        return "StockOrdersPair{" +
                "sellOrder=" + sellOrder +
                ", buyOrder=" + buyOrder +
                '}';
    }
}
