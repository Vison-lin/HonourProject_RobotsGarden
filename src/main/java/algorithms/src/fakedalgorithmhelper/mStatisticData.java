package algorithms.src.fakedalgorithmhelper;

import core.StatisticData;

public class mStatisticData implements StatisticData<Integer> {

    int ctr;

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
        mStatisticData data = new mStatisticData();
        data.setCtr(this.ctr);
        return data;
    }

    public void setCtr(int ctr) {
        this.ctr = ctr;
    }
}
