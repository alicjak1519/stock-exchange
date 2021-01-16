package pl.agh;

import java.util.Objects;

public class Corporation {
    int sharePrice;
    String name;

    public Corporation(int sharePrice, String name) {
        this.sharePrice = sharePrice;
        this.name = name;
    }

    public int getSharePrice() {
        return sharePrice;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Corporation that = (Corporation) o;
        return sharePrice == that.sharePrice &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sharePrice, name);
    }
}
