package pl.agh;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.StrictMath.abs;


public class Stockholder {
    int budget;
    Map<Corporation, Integer> stockholderStocks;
    int counterB = 0;
    int counterS = 0;

    public Stockholder(int budget, Map<Corporation, Integer> stockholderStocks) {
        this.budget = budget;
        this.stockholderStocks = stockholderStocks;
    }

    public BuyOrder generateRandomBuyOrder() {
        Random random = new Random();

        List<Corporation> shareList = new ArrayList<>(stockholderStocks.keySet());

        Corporation corporation = shareList.get(random.nextInt(shareList.size()));
        int priceLimit = ThreadLocalRandom.current().nextInt((int) (corporation.getSharePrice() * 0.5), corporation.getSharePrice() + 2);
        int sharesNumber = ThreadLocalRandom.current().nextInt(0, abs(stockholderStocks.get(corporation)));
//        int priceLimit = counterB;
//        int sharesNumber = counterB;
//        counterB++;

        return generateBuyOrder(corporation, priceLimit, sharesNumber);
    }

    public SellOrder generateRandomSellOrder() {
        Random random = new Random();

        List<Corporation> shareList = new ArrayList<>(stockholderStocks.keySet());

        Corporation corporation = shareList.get(random.nextInt(shareList.size()));
        int priceLimit = ThreadLocalRandom.current().nextInt((int) (corporation.getSharePrice() * 0.5), corporation.getSharePrice() + 2);
        int sharesNumber = ThreadLocalRandom.current().nextInt(0, abs(stockholderStocks.get(corporation)));
//        int priceLimit = counterS;
//        int sharesNumber = counterS;
//        counterS++;

        return generateSellOrder(corporation, priceLimit, sharesNumber);
    }

    public BuyOrder generateBuyOrder(Corporation corporation, int priceLimit, int sharesNumber) {
        return new BuyOrder(corporation, this, priceLimit, sharesNumber);
    }

    public SellOrder generateSellOrder(Corporation corporation, int priceLimit, int sharesNumber) {
        return new SellOrder(corporation, this, priceLimit, sharesNumber);
    }

    public void buy(SellOrder sellOrder) {
        budget = budget - sellOrder.getStockOrderValue();
        int actualShareNumber = stockholderStocks.get(sellOrder.getCorporation());
        stockholderStocks.put(sellOrder.getCorporation(), actualShareNumber + sellOrder.getSharesNumber());
    }

    public void sell(BuyOrder buyOrder) {
        budget = budget + buyOrder.getStockOrderValue();
        int actualShareNumber = stockholderStocks.get(buyOrder.getCorporation());
        stockholderStocks.put(buyOrder.getCorporation(), actualShareNumber - buyOrder.getSharesNumber());
    }

    @Override
    public String toString() {
        return "java.pl.agh.Stockholder{" +
                "budget=" + budget +
                ", stockholderStocks=" + stockholderStocks +
                '}';
    }
}
