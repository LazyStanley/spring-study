package com.stanley.study.concurrent;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

/**
 * @author Stanley Shen
 * @version 2019/10/30
 */
public class TestCountDownLatch {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH-mm-ss");
    private static final ExecutorService executorService = new ThreadPoolExecutor(2,
            2, 2L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(20));

    public static void main(String[] args) throws InterruptedException {
        final int totalThread = 10;
        CountDownLatch countDownLatch = new CountDownLatch(totalThread);

        for (int i = 0; i < totalThread; i++) {
            executorService.execute(() -> {
                try {
                    Thread.sleep(2_000);
                } catch (InterruptedException e) {
                    System.out.println("e = " + e);
                }
                System.out.println(LocalTime.now().format(DATE_TIME_FORMATTER));
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        System.out.println("end");
        executorService.shutdown();
    }

}
