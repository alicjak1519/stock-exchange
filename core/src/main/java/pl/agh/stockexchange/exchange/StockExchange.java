package pl.agh.stockexchange.exchange;

import pl.agh.stockexchange.order.Order;
import pl.agh.stockexchange.stock.Stock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface StockExchange {
    List<Stock> listStocks();

    Optional<Stock> findBySymbol(String name);

    StockExchange open();

    StockExchange close();

    void buy(Stock stock, int quantity, BigDecimal price);

    void sell(Stock stock, int quantity, BigDecimal price);

    void addOrder(Order order);

    boolean isOpen();

    int getBuyOrdersNumber(Stock stock);

    int getSellOrdersNumber(Stock stock);
}
