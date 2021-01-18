package pl.agh.stockexchange.order;

import pl.agh.stockexchange.stock.Stock;

import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class OrderSheet {
    private final Stock stock;
    private final Queue<SellOrder> sellOrders = new PriorityBlockingQueue<>();
    private final Queue<BuyOrder> buyOrders = new PriorityBlockingQueue<>();

    public OrderSheet(Stock stock) {
        this.stock = stock;
    }
}
