package com.paulturner.nanorest.modules.sum;

import java.util.concurrent.atomic.AtomicLong;

public class SumState {

    private final AtomicLong sum;

    public SumState() {
        this.sum = new AtomicLong(0L);
    }

    public long getSum() {
        return sum.get();
    }

    public long add(long value) {
        return this.sum.addAndGet(value);
    }

}
