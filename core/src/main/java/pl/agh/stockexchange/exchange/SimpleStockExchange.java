package pl.agh.stockexchange.exchange;

import pl.agh.stockexchange.stock.Stock;

import java.util.Optional;
import java.util.Set;

public class SimpleStockExchange implements StockExchange {
    private final Set<Stock> stocks;

    public SimpleStockExchange(Set<Stock> stocks) {
        this.stocks = stocks;
    }

    @Override
    public Set<Stock> listStocks() {
        return stocks;
    }

    @Override
    public Optional<Stock> findBySymbol(String symbol) {
        return stocks.stream().filter(stock -> stock.symbol().equals(symbol)).findFirst();
    }

    @Override
    public StockExchange open() {
        return null;
    }

    @Override
    public StockExchange close() {
        return null;
    }

    @Override
    public boolean isOpen() {
        return false;
    }
}
