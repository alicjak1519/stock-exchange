package pl.agh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockHoldersGenerator {
    public static List<Stockholder> generateStockholders(int stockholdersNumber, List<Corporation> corporations) {
        List<Stockholder> stockholders = new ArrayList<>();

        for (int i = 0; i < stockholdersNumber; i++) {
            Map<Corporation, Integer> stockholderStocks = new HashMap<>();
            corporations.forEach(corporation -> {
                stockholderStocks.put(corporation, 50);
            });
            stockholders.add(new Stockholder(100, stockholderStocks, "Stockholder_" + i));
        }
        return stockholders;
    }
}
