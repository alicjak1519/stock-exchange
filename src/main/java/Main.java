import java.util.LinkedList;

public class Main {
    public static void main(String[] args)
            throws InterruptedException {

        LinkedList<Corporation> corporations = new LinkedList<Corporation>();
        LinkedList<OrderSheet> orderSheets = new LinkedList<OrderSheet>();
        LinkedList<Stockholder> stockholders = new LinkedList<Stockholder>();

        Corporation testCorporation = new Corporation(10);

        Stockholder testStockholderA = new Stockholder(100);
        Stockholder testStockholderB = new Stockholder(200);

        BuyOrder testBuyOrder = (BuyOrder) testStockholderA.generateBuyOrder(testCorporation, 2, 2);
        SellOrder testSellOrder = (SellOrder) testStockholderB.generateSellOrder(testCorporation, 2, 2);

        OrderSheet testOrderSheet = new OrderSheet(testCorporation);
        orderSheets.add(testOrderSheet);
        testOrderSheet.addBuyOrder(testBuyOrder);
        testOrderSheet.addSellOrder(testSellOrder);

        for (OrderSheet orderSheet: orderSheets) {

            final StockExchange stockExchange = new StockExchange(orderSheet);

            Thread selling = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        stockExchange.sellShares();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            Thread buying = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        stockExchange.buyShares();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            selling.start();
            buying.start();

            selling.join();
            buying.join();
        }
    }
}