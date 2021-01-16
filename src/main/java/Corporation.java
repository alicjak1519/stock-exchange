public class Corporation {
    int sharePrice;

    public Corporation(int sharePrice) {
        this.sharePrice = sharePrice;
    }

    public int getSharePrice() {
        return sharePrice;
    }

    public void changePrice(int newSharePrice) {
        this.sharePrice = newSharePrice;
    }

}
