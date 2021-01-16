import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        Corporation testCorporation = new Corporation(10, "TestCorpo");

        Map<Corporation, Integer> stockholderAStocks = new HashMap<>();
        stockholderAStocks.put(testCorporation, 20);

        Map<Corporation, Integer> stockholderBStocks = new HashMap<>();
        stockholderBStocks.put(testCorporation, 50);

        Stockholder testStockholderA = new Stockholder(100, stockholderAStocks);
        Stockholder testStockholderB = new Stockholder(200, stockholderBStocks);

        OrderSheet testOrderSheet = new OrderSheet(testCorporation);

        List<Stockholder> stockholders = new ArrayList<>();
        stockholders.add(testStockholderA);
        stockholders.add(testStockholderB);

        List<Thread> buyOrdersGeneration = stockholders
                .stream()
                .map(stockholder -> new Thread(() -> {
                    while (true) {
                        try {
                            BuyOrder buyOrder = stockholder.generateRandomBuyOrder();
                            testOrderSheet.addBuyOrder(buyOrder);
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }))
                .collect(Collectors.toList());

        List<Thread> sellOrdersGeneration = stockholders
                .stream()
                .map(stockholder -> new Thread(() -> {
                    while (true) {
                        try {
                            SellOrder sellOrder = stockholder.generateRandomSellOrder();
                            testOrderSheet.addSellOrder(sellOrder);
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }))
                .collect(Collectors.toList());

        Thread printMeOrders = new Thread(() -> {
            while (true) {
                try {
                    System.out.println(testOrderSheet.getCurrentState());
                    Thread.sleep(1000 * 3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread realizeTransaction = new Thread(() -> {
            while (true) {
                try {
                    List<StockOrdersPair> stockOrdersPairs = testOrderSheet.buildStockOrdersPairs();
                    stockOrdersPairs.forEach(stockOrdersPair -> {
                        testOrderSheet.realizeTransaction(stockOrdersPair);
                    });
                    Thread.sleep(1000 * 3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        buyOrdersGeneration.forEach(thread -> thread.start());
        sellOrdersGeneration.forEach(thread -> thread.start());
        printMeOrders.start();
        realizeTransaction.start();
    }
}