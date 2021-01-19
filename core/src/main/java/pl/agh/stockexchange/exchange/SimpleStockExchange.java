package pl.agh.stockexchange.exchange;

import org.jetbrains.annotations.NotNull;
import pl.agh.stockexchange.order.MatchMaker;
import pl.agh.stockexchange.order.Order;
import pl.agh.stockexchange.order.OrderSheet;
import pl.agh.stockexchange.order.OrderType;
import pl.agh.stockexchange.stock.Stock;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SimpleStockExchange implements StockExchange {
    private final static Logger LOGGER = Logger.getLogger(SimpleStockExchange.class.getName());

    private final Clock clock;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(100);
    private final ConcurrentMap<String, OrderSheet> orderSheets;
    private volatile boolean open = false;


    public SimpleStockExchange(List<Stock> stocks) {
        this(stocks, Clock.systemUTC());
    }

    public SimpleStockExchange(List<Stock> stocks, Clock clock) {
        this(clock, stocks.stream()
                .map(stock -> new AbstractMap.SimpleEntry<>(stock.getSymbol(), new OrderSheet(new AtomicReference<>(stock))))
                .collect(Collectors.toConcurrentMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)));
    }

    public SimpleStockExchange(Clock clock, ConcurrentMap<String, OrderSheet> orderSheets) {
        this.clock = clock;
        this.orderSheets = orderSheets;
    }

    @Override
    public List<Stock> listStocks() {
        return orderSheets.values().stream().map(OrderSheet::getStock).map(AtomicReference::get).collect(Collectors.toList());
    }

    @Override
    public Optional<Stock> findBySymbol(@NotNull String symbol) {
        return Optional.ofNullable(orderSheets.get(symbol))
                .map(OrderSheet::getStock)
                .map(AtomicReference::get);
    }

    @Override
    public StockExchange open() {
        LOGGER.info("Stock exchange opened at: " + Instant.now(clock));
        orderSheets.forEach((s, orderSheet) -> executorService.scheduleAtFixedRate(new MatchMaker(orderSheet), 0, Constants.TICKING_INTERVAL, Constants.DEFAULT_TIME_UNIT));
        this.open = true;
        return this;
    }

    @Override
    public StockExchange close() {
        try {
            executorService.awaitTermination(100, TimeUnit.MICROSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        this.open = false;
        return this;
    }

    @Override
    public void buy(Stock stock, int quantity, BigDecimal price) {
        var order = new Order(stock, quantity, price, Instant.now(clock), OrderType.BUY);

        orderSheets.get(stock.getSymbol()).addOrder(order);
    }

    @Override
    public void sell(Stock stock, int quantity, BigDecimal price) {
        var order = new Order(stock, quantity, price, Instant.now(clock), OrderType.SELL);

        orderSheets.get(stock.getSymbol()).addOrder(order);
    }

    @Override
    public void addOrder(Order order) {
        orderSheets.get(order.getStock().getSymbol()).addOrder(order);
    }

    @Override
    public boolean isOpen() {
        return open;
    }
}
