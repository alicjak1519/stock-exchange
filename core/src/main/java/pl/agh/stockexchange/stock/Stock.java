package pl.agh.stockexchange.stock;

import java.math.BigDecimal;

public record Stock(String symbol, BigDecimal price, String fullName){

}