package pl.agh;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import pl.agh.stockexchange.exchange.SimpleStockExchange;
import pl.agh.stockexchange.exchange.StockExchange;
import pl.agh.stockexchange.order.Order;
import pl.agh.stockexchange.order.OrderType;
import pl.agh.stockexchange.stock.Stock;
import pl.agh.stockexchange.util.OrderGenerator;

import static pl.agh.stockexchange.stock.AvailableStocks.stocks;

import java.math.BigDecimal;
import java.net.URL;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

public class StockExchangeUiController implements Initializable {
    static StockExchange stockExchange = new SimpleStockExchange(stocks);

    @FXML
    private TextField corporationNameTextField;
    @FXML
    private TextField actionsNumberTextField;
    @FXML
    private TextField unitPriceTextField;


    @FXML
    private Label PKNPriceLabel;
    @FXML
    private Label PKOPriceLabel;
    @FXML
    private Label ALEPriceLabel;
    @FXML
    private Label JSWPriceLabel;

    @FXML
    private Pane PKNPriceCoursePane;
    @FXML
    private Pane PKOPriceCoursePane;
    @FXML
    private Pane ALEPriceCoursePane;
    @FXML
    private Pane JSWPriceCoursePane;

    private final Map<String, Label> stocksLabels = new HashMap<>();
    private final Map<String, Pane> stocksPanes = new HashMap<>();

    private static final int MAX_DATA_POINTS = 50;
    private int xSeriesData = 0;

    private XYChart.Series<Number, Number> PKNSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> PKOSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> ALESeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> JSWSeries = new XYChart.Series<>();

    private Map<String, XYChart.Series<Number, Number>> socksSeries = new HashMap<>();

    private ConcurrentLinkedQueue<Number> PKNData = new ConcurrentLinkedQueue<>();


    private ExecutorService executor;

    private NumberAxis timeLine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timeLine = new NumberAxis(0, MAX_DATA_POINTS, MAX_DATA_POINTS / 10);
        timeLine.setForceZeroInRange(false);
        timeLine.setAutoRanging(false);
        timeLine.setTickLabelsVisible(false);
        timeLine.setTickMarkVisible(false);
        timeLine.setMinorTickVisible(false);

        NumberAxis yAxis = new NumberAxis();

        LineChart<Number, Number> PKNPriceCourseChart = new LineChart<Number, Number>(timeLine, yAxis) {
            @Override
            protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {
            }
        };

        PKNPriceCourseChart.setMaxWidth(300);
        PKNPriceCourseChart.setMaxHeight(200);

        PKNPriceCourseChart.setAnimated(false);
        PKNPriceCourseChart.setTitle("Stock Action Price");
        PKNPriceCourseChart.setHorizontalGridLinesVisible(true);

        // Set Name for Series
        PKNSeries.setName("Series 1");

        // Add Chart Series
        PKNPriceCourseChart.getData().addAll(PKNSeries);
        PKNPriceCoursePane.getChildren().clear();
        PKNPriceCoursePane.getChildren().add(PKNPriceCourseChart);
    }

//    private void createPriceCourseChart(Stock stock){
//        NumberAxis priceAxis = new NumberAxis();
//
//        LineChart<Number, Number> PriceCourseChart = new LineChart<Number, Number>(timeLine, priceAxis) {
//            @Override
//            protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {
//            }
//        };
//
//        PriceCourseChart.setMaxWidth(300);
//        PriceCourseChart.setMaxHeight(200);
//
//        PriceCourseChart.setAnimated(false);
//        PriceCourseChart.setTitle("Stock Action Price");
//        PriceCourseChart.setHorizontalGridLinesVisible(true);
//
//        // Set Name for Series
//        PKNSeries.setName("Series 1");
//
//        // Add Chart Series
//        Pane PriceCoursePane =
//        PriceCourseChart.getData().addAll(PKNSeries);
//        PriceCourseChart.getChildren().clear();
//        PriceCourseChart.getChildren().add(PriceCourseChart);
//    }

    private void prepareTimeline() {
        // Every frame to take any data from queue and add to chart
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                addDataToSeries();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void addDataToSeries() {
        for (int i = 0; i < 20; i++) { //-- add 20 numbers to the plot+
            if (PKNData.isEmpty()) break;
            PKNSeries.getData().add(new XYChart.Data<>(xSeriesData++, PKNData.remove()));
        }
        // remove points to keep us at no more than MAX_DATA_POINTS
        if (PKNSeries.getData().size() > MAX_DATA_POINTS) {
            PKNSeries.getData().remove(0, PKNSeries.getData().size() - MAX_DATA_POINTS);
        }
        // update
        timeLine.setLowerBound(xSeriesData - MAX_DATA_POINTS);
        timeLine.setUpperBound(xSeriesData - 1);
    }

    private class AddToQueue implements Runnable {
        public void run() {
            try {
                // add a item of random data to queue
                PKNData.add(Math.random());

                Thread.sleep(500);
                executor.execute(this);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private class UpdatePriceLabels implements Runnable {
        public void run() {
            while (stockExchange.isOpen()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                Platform.runLater(new Runnable() {
                    public void run() {
                        stocksLabels.forEach((name, stockLabel) -> {
                                    stockExchange.findBySymbol(name)
                                            .map(Stock::getPrice)
                                            .ifPresent(price -> {
                                                stockLabel.setText("Actual Stock Price: " + price + "$");
                                                PKNData.add(price);
                                            });


                                }
                        );
                    }
                });
            }
        }
    }

    @FXML
    public void startClicked(Event e) {
        final var orders = stocks
                .stream()
                .flatMap(stock -> OrderGenerator.generateOrders(stock).stream())
                .collect(Collectors.toList());
        orders.forEach(stockExchange::addOrder);
        stockExchange.open();

        createStocksLabels();

        executor = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            }
        });

        AddToQueue addToQueue = new AddToQueue();
        UpdatePriceLabels updatePriceLabels = new UpdatePriceLabels();
        executor.execute(addToQueue);
        executor.execute(updatePriceLabels);

        //-- Prepare Timeline
        prepareTimeline();
    }

    public void createStocksLabels() {
        stocksLabels.put("PKO", PKOPriceLabel);
        stocksLabels.put("ALE", ALEPriceLabel);
        stocksLabels.put("JSW", JSWPriceLabel);
        stocksLabels.put("PKN", PKNPriceLabel);
    }

    public void createStocksPanes() {
        stocksPanes.put("PKO", PKOPriceCoursePane);
        stocksPanes.put("ALE", ALEPriceCoursePane);
        stocksPanes.put("JSW", JSWPriceCoursePane);
        stocksPanes.put("PKN", PKNPriceCoursePane);
    }


    @FXML
    public void generateSellClicked(Event e) {
        if (isCorporationNameValid() & isActionsNumberValid() & isUnitPriceValid()) {
            Stock stock = getStock(corporationNameTextField.getText());
            int actionsNumber = getActionsNumber();
            BigDecimal price = BigDecimal.valueOf((long) getUnitPrice() * actionsNumber);
            Order sellOrder = new Order(stock, actionsNumber, price, Instant.now(), OrderType.SELL);

            stockExchange.addOrder(sellOrder);
        }
    }

    @FXML
    public void generateBuyClicked(Event e) {
        if (isCorporationNameValid() & isActionsNumberValid() & isUnitPriceValid()) {
            Stock stock = getStock(corporationNameTextField.getText());
            int actionsNumber = getActionsNumber();
            BigDecimal price = BigDecimal.valueOf((long) getUnitPrice() * actionsNumber);
            Order buyOrder = new Order(stock, actionsNumber, price, Instant.now(), OrderType.BUY);

            stockExchange.addOrder(buyOrder);
        }
    }

    private Stock getStock(String corporationName) {
        int stockId = stocks.indexOf(stocks.stream()
                .filter(stock -> stock.getSymbol().equals(corporationName))
                .collect(Collectors.toList())
                .get(0));
        return stocks.get(stockId);
    }

    private boolean isCorporationNameValid() {
        String corporationName = corporationNameTextField.getText();
        if (corporationName != null) {
            return stocks.stream().map(Stock::getSymbol)
                    .collect(Collectors.toList())
                    .contains(corporationName);
        }
        return false;
    }

    private Integer getActionsNumber() {
        try {
            return Integer.parseInt(actionsNumberTextField.getText());
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private boolean isActionsNumberValid() {
        return getActionsNumber() > 0;
    }

    private int getUnitPrice() {
        try {
            return Integer.parseInt(unitPriceTextField.getText());
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private boolean isUnitPriceValid() {
        return getUnitPrice() > 0;
    }
}
