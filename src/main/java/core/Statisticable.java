package core;

import java.util.List;

public interface Statisticable {
    List<StatisticData> update(List<StatisticData> currentStatisticData);

    List<StatisticData> init();
}
