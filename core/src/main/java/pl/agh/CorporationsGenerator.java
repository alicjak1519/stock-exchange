package pl.agh;

import java.util.ArrayList;
import java.util.List;

public class CorporationsGenerator {
    public static List<Corporation> generateCorporations(int corporationsNumber) {
        List<Corporation> corporationList = new ArrayList<>();
        for (int i = 0; i < corporationsNumber; i++) {
            corporationList.add(new Corporation(10, "TestCorpo_" + i));
        }
        return corporationList;
    }
}
