package pl.agh.stockexchange.util;

import pl.agh.stockexchange.order.Order;
import pl.agh.stockexchange.order.OrderType;
import pl.agh.stockexchange.stock.Stock;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OrderGenerator {

    public static List<Order> generateOrders(Stock stock) {
        return IntStream.range(1, 10000)
                .mapToObj(i -> new Order(stock, (i * 100) % 1000, stock.getPrice().multiply(BigDecimal.valueOf(i)), Instant.now(), ThreadLocalRandom.current().nextBoolean() ? OrderType.BUY : OrderType.SELL))
                .collect(Collectors.toList());
    }
}
