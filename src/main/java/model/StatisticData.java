package model;

public interface StatisticData<T> {

    void update(T increment);

    String display();

    StatisticData<T> deepCopy();

    T getValue();

    String getTag();
}
