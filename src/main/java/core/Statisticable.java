package core;

import java.util.List;

public interface Statisticable<T> {
    List<StatisticData<T>> update(List<StatisticData<T>> currentStatisticData);

    List<StatisticData<T>> init();
}
