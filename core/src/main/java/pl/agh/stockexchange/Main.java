package pl.agh.stockexchange;

import pl.agh.stockexchange.exchange.SimpleStockExchange;
import pl.agh.stockexchange.exchange.StockExchange;
import pl.agh.stockexchange.stock.Stock;
import pl.agh.stockexchange.util.OrderGenerator;

import static pl.agh.stockexchange.stock.AvailableStocks.stocks;

public class Main {
    static StockExchange stockExchange = new SimpleStockExchange(stocks);

    public static void main(String[] args) throws InterruptedException {
        stockExchange.open();
        final var orders = OrderGenerator.generateOrders(stocks.get(0));
        orders.parallelStream()
                .forEach(stockExchange::addOrder);
        while (stockExchange.isOpen()) {
            stockExchange.findBySymbol("PKN")
                    .map(Stock::getPrice)
                    .ifPresent(System.out::println);
            Thread.sleep(100);
        }
    }
}