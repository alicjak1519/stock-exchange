package pl.agh.stockexchange.order;

public class MatchMaker implements Runnable {
    private final OrderSheet orderSheet;

    public MatchMaker(OrderSheet orderSheet) {
        this.orderSheet = orderSheet;
    }

    @Override
    public void run() {
        try {
            final var bestBuy = orderSheet.getBestBuy();
            final var bestSell = orderSheet.getBestSell();

            if (bestBuy == null || bestSell == null) return;

            if (bestBuy.getPrice().compareTo(bestSell.getPrice()) >= 0) {
                final var quantityDiff = bestBuy.getQuantity() - bestSell.getQuantity();
                if (quantityDiff < 0) {
                    orderSheet.addOrder(bestSell.withNewQuantity(-quantityDiff));
                }
                if (quantityDiff > 0) {
                    orderSheet.addOrder(bestBuy.withNewQuantity(quantityDiff));
                }
            }

            orderSheet.updateStockPrice(bestSell.getPrice());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
