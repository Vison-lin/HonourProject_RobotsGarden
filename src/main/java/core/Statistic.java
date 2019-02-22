package core;

import model.Robot;

import java.util.List;
import java.util.Map;

public abstract class Statistic<T> {

    public abstract boolean showingCondition(List<Robot> robots);

    public abstract String show(List<Robot> robots, List<Map<String, StatisticData<T>>> data);

    public abstract void clean();

}
