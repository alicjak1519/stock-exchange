package pl.agh.stockexchange.order;

public class SellOrder extends Order implements Comparable<SellOrder> {

    @Override
    public int compareTo(SellOrder o) {
        return 0;
    }
}
