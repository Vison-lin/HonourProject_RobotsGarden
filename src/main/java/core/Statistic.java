package core;

import model.Robot;

import java.util.List;

public abstract class Statistic {

    public abstract boolean showingCondition(List<Robot> robots);

    public abstract String show();

}
