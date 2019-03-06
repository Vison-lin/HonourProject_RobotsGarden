package algorithms.src.fakedalgorithmhelper;

import model.StatisticData;

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

    @Override
    public Integer getValue() {
        return ctr;
    }

    @Override
    public String getTag() {
        return "NumOfRun";
    }

    public void setCtr(int ctr) {
        this.ctr = ctr;
    }
}
