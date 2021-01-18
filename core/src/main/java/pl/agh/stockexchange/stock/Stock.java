package pl.agh.stockexchange.stock;

import java.math.BigDecimal;

public final class Stock {
    private final String symbol;
    private final BigDecimal price;
    private final String fullName;

    public Stock(String symbol, BigDecimal price, String fullName) {
        this.symbol = symbol;
        this.price = price;
        this.fullName = fullName;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getFullName() {
        return fullName;
    }

    public Stock withNewPrice(final BigDecimal newPrice) {
        return new Stock(symbol, newPrice, fullName);
    }

    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", price=" + price +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}