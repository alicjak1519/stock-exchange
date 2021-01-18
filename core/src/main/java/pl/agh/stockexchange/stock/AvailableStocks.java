package pl.agh.stockexchange.stock;

import java.math.BigDecimal;
import java.util.List;

public interface AvailableStocks {
    List<Stock> stocks = List.of(
            new Stock("PKN", new BigDecimal(55), "PKN Orlen"),
            new Stock("PKO", new BigDecimal(30), "PKO BP"),
            new Stock("ALE", new BigDecimal(78), "Allegro"),
            new Stock("JSW", new BigDecimal(30), "Jastrzębska Spółka Węglowa")
    );

}
