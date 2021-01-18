package pl.agh.stockexchange.order;

import pl.agh.stockexchange.exchange.Constants;
import pl.agh.stockexchange.stock.Stock;

import java.math.BigDecimal;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class OrderSheet {
    private final BlockingQueue<Order> sellOrders = new PriorityBlockingQueue<>();
    private final BlockingQueue<Order> buyOrders = new PriorityBlockingQueue<>();

    private final AtomicReference<Stock> stock;

    public OrderSheet(AtomicReference<Stock> stock) {
        this.stock = stock;
    }

    public BlockingQueue<Order> getSellOrders() {
        return sellOrders;
    }

    public Order getBestSell() throws InterruptedException {
        return sellOrders.poll(Constants.TICKING_INTERVAL, Constants.DEFAULT_TIME_UNIT);
    }

    public BlockingQueue<Order> getBuyOrders() {
        return buyOrders;
    }

    public Order getBestBuy() throws InterruptedException {
        return buyOrders.poll(100, TimeUnit.MILLISECONDS);
    }

    public void updateStockPrice(final BigDecimal newStockPrice) {
        stock.getAndUpdate(s -> s.withNewPrice(newStockPrice));
    }

    public void addOrder(final Order order) {
        try {
            if (order.getOrderType().equals(OrderType.SELL)) sellOrders.put(order);
            else buyOrders.put(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AtomicReference<Stock> getStock() {
        return stock;
    }

    @Override
    public String toString() {
        return "OrderSheet{" +
                "sellOrders=" + sellOrders +
                ", buyOrders=" + buyOrders +
                ", stock=" + stock +
                '}';
    }
}
