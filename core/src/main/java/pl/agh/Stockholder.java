package pl.agh;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.StrictMath.abs;


public class Stockholder {
    int budget;
    Map<Corporation, Integer> stockholderStocks;

    public Stockholder(int budget, Map<Corporation, Integer> stockholderStocks) {
        this.budget = budget;
        this.stockholderStocks = stockholderStocks;
    }

    public BuyOrder generateRandomBuyOrder() {
        Random random = new Random();

        List<Corporation> shareList = new ArrayList<>(stockholderStocks.keySet());

        Corporation corporation = shareList.get(random.nextInt(shareList.size()));
        int priceLimit = ThreadLocalRandom.current().nextInt((int) (corporation.getSharePrice() * 0.5), (int) corporation.getSharePrice() + 2);
        int sharesNumber = ThreadLocalRandom.current().nextInt(0, abs(stockholderStocks.get(corporation)));

        return generateBuyOrder(corporation, priceLimit, sharesNumber);
    }

    public SellOrder generateRandomSellOrder() {
        Random random = new Random();

        List<Corporation> shareList = new ArrayList<>(stockholderStocks.keySet());

        Corporation corporation = shareList.get(random.nextInt(shareList.size()));
        int priceLimit = ThreadLocalRandom.current().nextInt((int) (corporation.getSharePrice() * 0.5), (int) corporation.getSharePrice() + 2);
        int sharesNumber = ThreadLocalRandom.current().nextInt(0, abs(stockholderStocks.get(corporation)));

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

    public void buy(BuyOrder buyOrder) {
        budget -= buyOrder.getStockOrderValue();
        int actualShareNumber = stockholderStocks.get(buyOrder.getCorporation());
        stockholderStocks.put(buyOrder.getCorporation(), actualShareNumber + buyOrder.getSharesNumber());
    }

    public void sell(SellOrder sellOrder) {
        int actualShareNumber = stockholderStocks.get(sellOrder.getCorporation());
        stockholderStocks.put(sellOrder.getCorporation(), actualShareNumber - sellOrder.getSharesNumber());
        budget += sellOrder.getStockOrderValue();
    }

    @Override
    public String toString() {
        return "java.pl.agh.Stockholder{" +
                "budget=" + budget +
                ", stockholderStocks=" + stockholderStocks +
                '}';
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
