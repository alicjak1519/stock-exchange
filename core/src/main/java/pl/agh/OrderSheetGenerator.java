package pl.agh;

import java.util.List;

public class OrderSheetGenerator {
    public static OrderSheet generateOrderSheet(List<Corporation> corporations){
        return new OrderSheet(corporations);
    }
}
