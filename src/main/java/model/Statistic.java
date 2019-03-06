package model;

import java.util.List;
import java.util.Map;

public abstract class Statistic<T> {

    public abstract boolean showingCondition(List<Robot> robots);

    public abstract double result(List<Robot> robots, List<Map<String, StatisticData<T>>> data);

    public abstract String tag();

    public abstract void clean();

}
