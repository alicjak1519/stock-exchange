package pl.agh.stockexchange;

import pl.agh.stockexchange.exchange.SimpleStockExchange;
import pl.agh.stockexchange.exchange.StockExchange;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static pl.agh.stockexchange.stock.AvailableStocks.stocks;

public class Main {
    static StockExchange stockExchange = new SimpleStockExchange(stocks);

    public static void main(String[] args) throws InterruptedException {
        stockExchange.open();
        CompletableFuture.runAsync(() -> {
            stockExchange.sell(stocks.get(0), 120, new BigDecimal(54));
        });
        CompletableFuture.runAsync(() -> {
            stockExchange.buy(stocks.get(0), 100, new BigDecimal(55));
        });
        Thread.sleep(100);
        CompletableFuture.runAsync(() -> {
            stockExchange.buy(stocks.get(0), 20, new BigDecimal(55));
        });
        while (stockExchange.isOpen()) {
            System.out.println(stockExchange.listStocks());
            Thread.sleep(100);
        }
    }
}