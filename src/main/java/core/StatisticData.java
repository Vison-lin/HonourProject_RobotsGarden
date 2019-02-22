package core;

public interface StatisticData<T> {
    void update(T increment);

    String display();

    StatisticData<T> deepCopy();
}
