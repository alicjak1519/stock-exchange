package pl.agh;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        List<Corporation> corporations = CorporationsGenerator.generateCorporations(5);
        OrderSheet orderSheet = OrderSheetGenerator.generateOrderSheet(corporations);
        List<Stockholder> stockholders = StockHoldersGenerator.generateStockholders(4, corporations);

        StockExchange.startSession(orderSheet, stockholders);
    }
}