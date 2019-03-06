package model;

import java.util.HashMap;

public interface Statisticable {
    HashMap<String, StatisticData> update(HashMap<String, StatisticData> currentStatisticData);

    HashMap<String, StatisticData> init();
}
