package pl.agh;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        Corporation testCorporation = new Corporation(10, "TestCorpo");

        Map<Corporation, Integer> stockholderAStocks = new HashMap<>();
        stockholderAStocks.put(testCorporation, 20);

        Map<Corporation, Integer> stockholderBStocks = new HashMap<>();
        stockholderBStocks.put(testCorporation, 50);

        Stockholder testStockholderA = new Stockholder(100, stockholderAStocks);
        Stockholder testStockholderB = new Stockholder(200, stockholderBStocks);

        OrderSheet testOrderSheet = new OrderSheet(testCorporation);

        List<Stockholder> stockholders = new ArrayList<>();
        stockholders.add(testStockholderA);
        stockholders.add(testStockholderB);

        StockExchange.startSession(testOrderSheet, stockholders);
    }
}