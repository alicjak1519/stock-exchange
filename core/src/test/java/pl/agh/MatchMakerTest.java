package pl.agh;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatchMakerTest {

    @Test
    public void matchMake() {
        //given
        var corporation = new Corporation(10, "Allegro");
        var stockHolder = new Stockholder(20, Map.of(corporation, 20));
        var buyOrders = List.of(new BuyOrder(corporation, stockHolder, 20, 1));
        var sellOrders = List.of(new SellOrder(corporation, stockHolder, 20, 1));

        //when
        var stockOrdersPairs = new MatchMaker().matchMake(buyOrders, sellOrders);

        //then
        assertEquals(1, stockOrdersPairs.size());
        assertEquals(stockOrdersPairs.get(0), new StockOrdersPair(sellOrders.get(0), buyOrders.get(0)));
    }
}