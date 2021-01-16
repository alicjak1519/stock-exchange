import java.util.LinkedList;

public class StockExchange {

    OrderSheet orderSheet;
    LinkedList<Transaction> transactionsToDo = new LinkedList<Transaction>();
    int capacity = 5;

    public StockExchange(OrderSheet orderSheet) {
        this.orderSheet = orderSheet;
    }

    public void sellShares() throws InterruptedException {
        while (true) {
            synchronized (this) {
                while (transactionsToDo.size() == capacity)
                    wait();

                StockOrdersPair stockOrdersPair = orderSheet.getStockOrdersPairToTransaction();
                transactionsToDo.add(new Transaction(stockOrdersPair));

                notify();

                Thread.sleep(1000);
            }
        }
    }

    public void buyShares() throws InterruptedException {
        while (true) {
            synchronized (this) {

                while (transactionsToDo.size() == 0)
                    wait();

                Transaction transactionToDo = transactionsToDo.removeFirst();
                transactionToDo.realize();

                notify();

                Thread.sleep(1000);
            }
        }
    }
}