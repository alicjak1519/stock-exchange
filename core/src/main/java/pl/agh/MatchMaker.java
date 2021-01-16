package pl.agh;

import java.util.ArrayList;
import java.util.List;

public class MatchMaker {
    public List<StockOrdersPair> matchMake(List<BuyOrder> buyOrders, List<SellOrder> sellOrders) {
        List<StockOrdersPair> stockOrdersPairs = new ArrayList<>();
        synchronized (this) {
            List<BuyOrder> tmpBuyOrders = new ArrayList<>(List.copyOf(buyOrders));
            sellOrders.forEach(sellOrder -> tmpBuyOrders
                    .stream()
                    .filter(buyOrder ->
                            sellOrder.corporation == buyOrder.corporation
                                    && sellOrder.priceLimit == buyOrder.priceLimit
                                    && sellOrder.sharesNumber == buyOrder.sharesNumber)
                    .findFirst()
                    .ifPresent(
                            buyOrder -> {
                                stockOrdersPairs.add(new StockOrdersPair(sellOrder, buyOrder));
                                tmpBuyOrders.remove(buyOrder);
                            }));
        }
        return stockOrdersPairs;
    }
}
