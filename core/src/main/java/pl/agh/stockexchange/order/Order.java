package pl.agh.stockexchange.order;

import pl.agh.stockexchange.stock.Stock;

import java.math.BigDecimal;
import java.time.Instant;

public abstract class Order {
    protected Stock stock;
    protected int quantity;
    protected BigDecimal price;
    protected Instant timestamp;
}