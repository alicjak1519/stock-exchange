package pl.agh;

import java.util.List;
import java.util.stream.Collectors;

public class StockExchange {
    private int time = 100;

    private StockExchange() {
    }

    public static void startSession(OrderSheet orderSheet, List<Stockholder> stockholders) {
        new StockExchange().doStartSession(orderSheet, stockholders);
    }

    private void doStartSession(OrderSheet orderSheet, List<Stockholder> stockholders) {
        List<Thread> buyOrdersGeneration = stockholders
                .stream()
                .map(stockholder -> createBuyingThread(orderSheet, stockholder))
                .collect(Collectors.toList());

        List<Thread> sellOrdersGeneration = stockholders
                .stream()
                .map(stockholder -> createSellingThread(orderSheet, stockholder))
                .collect(Collectors.toList());

        Thread printMeOrders = new Thread(() -> {
            while (true) {
                try {
                    System.out.println(orderSheet.getCurrentState());
                    Thread.sleep(time * 3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread realizeTransaction = createRealizeTransactionThread(orderSheet);

        buyOrdersGeneration.forEach(Thread::start);
        sellOrdersGeneration.forEach(Thread::start);
        printMeOrders.start();
        realizeTransaction.start();
    }

    private Thread createRealizeTransactionThread(OrderSheet orderSheet) {
        return new Thread(() -> {
            while (true) {
                try {
                    List<StockOrdersPair> stockOrdersPairs = orderSheet.buildStockOrdersPairs();
                    stockOrdersPairs.forEach(orderSheet::realizeTransaction);
                    Thread.sleep(time * 3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Thread createSellingThread(OrderSheet orderSheet, Stockholder stockholder) {
        return new Thread(() -> {
            while (true) {
                try {
                    SellOrder sellOrder = stockholder.generateRandomSellOrder();
                    orderSheet.addSellOrder(sellOrder);
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Thread createBuyingThread(OrderSheet orderSheet, Stockholder stockholder) {
        return new Thread(() -> {
            while (true) {
                try {
                    BuyOrder buyOrder = stockholder.generateRandomBuyOrder();
                    orderSheet.addBuyOrder(buyOrder);
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
