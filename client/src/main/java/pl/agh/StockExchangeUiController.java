package pl.agh;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import pl.agh.stockexchange.exchange.SimpleStockExchange;
import pl.agh.stockexchange.exchange.StockExchange;
import pl.agh.stockexchange.stock.Stock;
import pl.agh.stockexchange.util.OrderGenerator;

import static pl.agh.stockexchange.stock.AvailableStocks.stocks;

import java.util.*;

public class StockExchangeUiController {
    static StockExchange stockExchange = new SimpleStockExchange(stocks);

    public Text PKNPrice = null;
    public Text PKOPrice = null;
    public Text ALEPrice = null;
    public Text JSWPrice = null;
    public Label startLabel = null;

    public Map<String, Text> stocksLabels = new HashMap<>() {{
        put("PKN", PKNPrice);
        put("PKO", PKOPrice);
        put("ALE", ALEPrice);
        put("JSW", JSWPrice);
    }};

    @FXML
    public void startClicked(Event e) {

        final var orders = OrderGenerator.generateOrders(stocks.get(0));
        orders.parallelStream()
                .forEach(stockExchange::addOrder);
        stockExchange.open();
        // runnable for that thread
        new Thread(() -> {
            while (stockExchange.isOpen()) {
                try {
                    // imitating work
                    Thread.sleep(new Random().nextInt(1000));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                // update ProgressIndicator on FX thread
                Platform.runLater(new Runnable() {
                    public void run() {
                        stocksLabels.forEach((name, textLabel) -> {
                                    stockExchange.findBySymbol(name)
                                            .map(Stock::getPrice)
                                            .ifPresent(price -> textLabel.setText(price.toString()));
                                }
                        );
                    }
                });
            }
        }).start();


//        AnimationTimer timer = new MyTimer();
//        timer.start();
    }

    private class MyTimer extends AnimationTimer {

        public MyTimer() {
        }

        @Override
        public void handle(long now) {
            doHandle();
        }

        private void doHandle() {
            stocksLabels.forEach((name, textLabel) -> {
                        System.out.println(name);
                        stockExchange.findBySymbol(name)
                                .map(Stock::getPrice)
                                .ifPresent(System.out::println);
                    }
            );

//            stockExchange.findBySymbol("PKN")
//                    .map(Stock::getPrice)
//                    .ifPresent(price -> startLabel.setText(price.toString()));
        }
    }
}
