package pl.agh.stockexchange.order;

import org.jetbrains.annotations.NotNull;
import pl.agh.stockexchange.stock.Stock;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;

public class Order implements Comparable<Order> {
    private final Stock stock;
    private final int quantity;
    private final BigDecimal price;
    private final Instant timestamp;
    private final OrderType orderType;

    public Order(Stock stock, int quantity, BigDecimal price, Instant timestamp, OrderType orderType) {
        this.stock = stock;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = timestamp;
        this.orderType = orderType;
    }

    public Stock getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public Order withNewQuantity(int quantity) {
        return new Order(stock, quantity, price, timestamp, orderType);
    }

    @Override
    public String toString() {
        return "Order{" +
                "stock=" + stock +
                ", quantity=" + quantity +
                ", price=" + price +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public int compareTo(@NotNull Order o) {
        return Comparator.comparing(Order::getTimestamp)
                .compare(this, o);
    }
}