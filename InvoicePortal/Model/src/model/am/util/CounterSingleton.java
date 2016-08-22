package model.am.util;

import java.util.concurrent.atomic.AtomicInteger;

public class CounterSingleton {

    private AtomicInteger atomicInteger;
    private static CounterSingleton obj = null;

    private CounterSingleton(int initialValue) {
        this.atomicInteger = new AtomicInteger(initialValue);
    }

    public static CounterSingleton getInstance() {
        if (obj == null) {
            obj = new CounterSingleton(100);
        }
        return obj;
    }

    public int getCounter() {
        return atomicInteger.getAndIncrement();
    }
}
