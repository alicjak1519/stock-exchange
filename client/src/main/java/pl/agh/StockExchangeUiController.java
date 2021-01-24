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
import javafx.scene.text.Text;
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

    @FXML
    private Text PKNBuyOrdersNumber;
    @FXML
    private Text PKOBuyOrdersNumber;
    @FXML
    private Text ALEBuyOrdersNumber;
    @FXML
    private Text JSWBuyOrdersNumber;

    @FXML
    private Text PKNSellOrdersNumber;
    @FXML
    private Text PKOSellOrdersNumber;
    @FXML
    private Text ALESellOrdersNumber;
    @FXML
    private Text JSWSellOrdersNumber;

    private final Map<String, Label> stocksLabels = new HashMap<>();
    private final Map<String, Pane> stocksPanes = new HashMap<>();
    private final Map<String, Text> stocksBuyNumbersText = new HashMap<>();
    private final Map<String, Text> stocksSellNumbersText = new HashMap<>();

    private static final int MAX_DATA_POINTS = 200;
    private int xSeriesData = 0;

    private Map<String, XYChart.Series<Number, Number>> stocksSeries = new HashMap<>();

    private Map<String, ConcurrentLinkedQueue<Number>> stocksDataQueues = new HashMap<>();

    private ExecutorService executor;

    private NumberAxis timeLine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createStocksLabels();
        createStocksPanes();
        createStocksSeries();
        createStocksDataQueues();
        createStocksBuyNumberText();
        createStocksSellNumberText();

        timeLine = new NumberAxis(0, MAX_DATA_POINTS, MAX_DATA_POINTS / 10);
        timeLine.setForceZeroInRange(false);
        timeLine.setAutoRanging(false);
        timeLine.setTickLabelsVisible(false);
        timeLine.setTickMarkVisible(false);
        timeLine.setMinorTickVisible(false);

        stocks.forEach(this::createPriceCourseChart);
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

    public void createStocksBuyNumberText() {
        stocksBuyNumbersText.put("PKO", PKOBuyOrdersNumber);
        stocksBuyNumbersText.put("ALE", ALEBuyOrdersNumber);
        stocksBuyNumbersText.put("JSW", JSWBuyOrdersNumber);
        stocksBuyNumbersText.put("PKN", PKNBuyOrdersNumber);
    }

    public void createStocksSellNumberText() {
        stocksSellNumbersText.put("PKO", PKOSellOrdersNumber);
        stocksSellNumbersText.put("ALE", ALESellOrdersNumber);
        stocksSellNumbersText.put("JSW", JSWSellOrdersNumber);
        stocksSellNumbersText.put("PKN", PKNSellOrdersNumber);
    }

    private void createStocksDataQueues() {
        stocks.forEach(stock -> stocksDataQueues.put(stock.getSymbol(), new ConcurrentLinkedQueue<>()));
    }

    private void createStocksSeries() {
        stocks.forEach(stock -> stocksSeries.put(stock.getSymbol(), new XYChart.Series<>()));
    }

    @FXML
    public void startClicked(Event e) {
        final var orders = stocks
                .stream()
                .flatMap(stock -> OrderGenerator.generateOrders(stock).stream())
                .collect(Collectors.toList());
        orders.forEach(stockExchange::addOrder);
        stockExchange.open();

        executor = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            }
        });

        UpdateStockInfo updatePriceLabels = new UpdateStockInfo();
        executor.execute(updatePriceLabels);
        prepareTimeline();
    }

    @FXML
    public void stopClicked(Event e) {
        stockExchange.close();
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

    private void createPriceCourseChart(Stock stock) {
        NumberAxis priceAxis = new NumberAxis();

        LineChart<Number, Number> priceCourseChart = new LineChart<Number, Number>(timeLine, priceAxis) {
            @Override
            protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {
            }
        };

        priceCourseChart.setMaxWidth(300);
        priceCourseChart.setMaxHeight(200);

        priceCourseChart.setAnimated(false);
        priceCourseChart.setTitle("Stock Action Price");
        priceCourseChart.setHorizontalGridLinesVisible(true);

        stocksSeries.get(stock.getSymbol()).setName("Series: " + stock.getSymbol());

        Pane priceCoursePane = stocksPanes.get(stock.getSymbol());
        priceCourseChart.getData().addAll(stocksSeries.get(stock.getSymbol()));

        priceCoursePane.getChildren().clear();
        priceCoursePane.getChildren().add(priceCourseChart);
    }

    private void prepareTimeline() {
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
        stocks.forEach(stock -> {
            ConcurrentLinkedQueue<Number> stockData = stocksDataQueues.get(stock.getSymbol());
            XYChart.Series<Number, Number> stockSeries = stocksSeries.get(stock.getSymbol());

            for (int i = 0; i < 3; i++) {
                if (stockData.isEmpty()) break;
                stockSeries.getData().add(new XYChart.Data<>(xSeriesData++, stockData.remove()));
            }

            if (stockSeries.getData().size() > MAX_DATA_POINTS) {
                stockSeries.getData().remove(0, stockSeries.getData().size() - MAX_DATA_POINTS);
            }

            timeLine.setLowerBound(xSeriesData - MAX_DATA_POINTS);
            timeLine.setUpperBound(xSeriesData - 1);
        });
    }

    private class UpdateStockInfo implements Runnable {
        public void run() {
            while (stockExchange.isOpen()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                Platform.runLater(new Runnable() {
                    public void run() {
                        stocks.forEach(stock -> {
                                    stockExchange.findBySymbol(stock.getSymbol())
                                            .map(Stock::getPrice)
                                            .ifPresent(price -> {
                                                stocksLabels.get(stock.getSymbol()).setText("Actual Stock Price: " + price + "$");
                                                stocksDataQueues.get(stock.getSymbol()).add(price);
                                            });
                                }
                        );
                        stocks.forEach(stock -> {
                                    int buyOrdersNumber = stockExchange.getBuyOrdersNumber(stock);
                                    stocksBuyNumbersText.get(stock.getSymbol()).setText("Number: " + buyOrdersNumber);
                                    int sellOrdersNumber = stockExchange.getSellOrdersNumber(stock);
                                    stocksSellNumbersText.get(stock.getSymbol()).setText("Number: " + sellOrdersNumber);
                                }
                        );
                    }
                });
            }
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
