package com.linxh.paas.demo;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadTest {

    @Test
    public void createThread() {
        final AtomicInteger counter = new AtomicInteger(0);
        try {
            while (true) {
                new ThreadCounter(counter).start();
            }

        } catch (Throwable e) {
            System.out.println("failed At=>" + counter.get());
        }
    }

}

class ThreadCounter extends Thread {
    private AtomicInteger counter;

    public ThreadCounter(AtomicInteger counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        try {
            System.out.println("The " + counter.getAndIncrement() + " thread be created.");
            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}