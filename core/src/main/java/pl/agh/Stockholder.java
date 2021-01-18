package pl.agh;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.StrictMath.abs;


public class Stockholder {
    private int budget;
    private Map<Corporation, Integer> stockholderStocks;
    private String name = "Stockholder";

    public Stockholder(int budget, Map<Corporation, Integer> stockholderStocks, String name) {
        this.name = name;
        this.budget = budget;
        this.stockholderStocks = stockholderStocks;
    }

    public Stockholder(int budget, Map<Corporation, Integer> stockholderStocks) {
        this.budget = budget;
        this.stockholderStocks = stockholderStocks;
    }

    public BuyOrder generateRandomBuyOrder() {
        Random random = new Random();

        List<Corporation> shareList = new ArrayList<>(stockholderStocks.keySet());

        Corporation corporation = shareList.get(random.nextInt(shareList.size()));
        int priceLimit = ThreadLocalRandom.current().nextInt((int) (corporation.getSharePrice() * 0.5), (int) corporation.getSharePrice() + 2);
        int sharesNumber = ThreadLocalRandom.current().nextInt(0, abs(stockholderStocks.get(corporation)) + 1);

        return generateBuyOrder(corporation, priceLimit, sharesNumber);
    }

    public SellOrder generateRandomSellOrder() {
        Random random = new Random();

        List<Corporation> shareList = new ArrayList<>(stockholderStocks.keySet());

        Corporation corporation = shareList.get(random.nextInt(shareList.size()));
        int priceLimit = ThreadLocalRandom.current().nextInt((int) (corporation.getSharePrice() * 0.5), (int) corporation.getSharePrice() + 2);
        int sharesNumber = ThreadLocalRandom.current().nextInt(0, abs(stockholderStocks.get(corporation)) + 1);

        return generateSellOrder(corporation, priceLimit, sharesNumber);
    }

    public BuyOrder generateBuyOrder(Corporation corporation, int priceLimit, int sharesNumber) {
        return new BuyOrder(corporation, this, priceLimit, sharesNumber);
    }

    public SellOrder generateSellOrder(Corporation corporation, int priceLimit, int sharesNumber) {
        return new SellOrder(corporation, this, priceLimit, sharesNumber);
    }

    public boolean canBuy(BuyOrder buyOrder) {
        return budget >= buyOrder.getStockOrderValue();
    }

    public boolean canSell(SellOrder sellOrder) {
        int actualShareNumber = stockholderStocks.get(sellOrder.getCorporation());
        return actualShareNumber >= sellOrder.getSharesNumber();
    }

    public void buy(StockOrdersPair stockOrdersPair) {
        BuyOrder buyOrder = stockOrdersPair.getBuyOrder();
        budget -= buyOrder.getStockOrderValue();
        int actualShareNumber = stockholderStocks.get(buyOrder.getCorporation());
        stockholderStocks.put(buyOrder.getCorporation(), actualShareNumber + buyOrder.getSharesNumber());
    }

    public void sell(StockOrdersPair stockOrdersPair) {
        SellOrder sellOrder = stockOrdersPair.getSellOrder();
        int actualShareNumber = stockholderStocks.get(sellOrder.getCorporation());
        stockholderStocks.put(sellOrder.getCorporation(), actualShareNumber - sellOrder.getSharesNumber());
        budget += stockOrdersPair.getBuyOrder().getStockOrderValue();
    }

    @Override
    public String toString() {
        return name + ": " +
                "budget=" + budget +
                ", stockholderStocks=" + stockholderStocks.values().stream().reduce(0, Integer::sum);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stockholder that = (Stockholder) o;
        return budget == that.budget &&
                Objects.equals(stockholderStocks, that.stockholderStocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(budget, stockholderStocks);
    }
}
