package pl.agh;

import java.util.Objects;

public class StockOrder {
    Corporation corporation;
    Stockholder stockholder;
    int priceLimit;
    int sharesNumber;

    StockOrder(Corporation corporation, Stockholder stockholder, int priceLimit, int sharesNumber) {
        this.corporation = corporation;
        this.stockholder = stockholder;
        this.priceLimit = priceLimit;
        this.sharesNumber = sharesNumber;
    }

    public int getStockOrderValue() {
        return priceLimit * sharesNumber;
    }

    public Stockholder getStockholder() {
        return stockholder;
    }

    public int getPriceLimit() {
        return priceLimit;
    }

    public int getSharesNumber() {
        return sharesNumber;
    }

    public Corporation getCorporation() {
        return corporation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockOrder that = (StockOrder) o;
        return priceLimit == that.priceLimit &&
                sharesNumber == that.sharesNumber &&
                corporation.equals(that.corporation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(corporation, priceLimit, sharesNumber);
    }

}
