package algorithms.src.gatheringalgorithm;

import core.StatisticData;

public class NumOfRun implements StatisticData<Integer> {

    public static final String TAG = "NumOfRun";
    int ctr = 0;

    @Override
    public void update(Integer increment) {
        ctr = ctr + increment;
    }

    @Override
    public String display() {
        return ctr + "";
    }

    @Override
    public StatisticData<Integer> deepCopy() {
        NumOfRun numOfRun = new NumOfRun();
        numOfRun.setCtr(this.ctr);
        return numOfRun;
    }

    @Override
    public Integer getValue() {
        return ctr;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    public void setCtr(int ctr) {
        this.ctr = ctr;
    }
}
