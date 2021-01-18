package pl.agh.stockexchange.exchange;

import pl.agh.stockexchange.stock.Stock;

import java.util.Optional;
import java.util.Set;

public interface StockExchange {
    Set<Stock> listStocks();
    Optional<Stock> findBySymbol(String name);
    StockExchange open();
    StockExchange close();
    boolean isOpen();
}
