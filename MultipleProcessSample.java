package com.ekocbiyik.multiprocess;


import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * enbiya on 27.02.2019
 */
public class MultipleProcessSample {

    private static final int THREAD_SIZE = 20;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_SIZE);
        List<String> stringList = generateSamples();
        // you need to add 'commons-collections4' library to your dependencies!
        List<List<String>> partitionList = ListUtils.partition(stringList, (stringList.size() / THREAD_SIZE));
        for (int i = 0; i < partitionList.size(); i++) {
            executorService.submit(new MySampleThread(String.valueOf(i), partitionList.get(i)));
        }
        waitExecutor(executorService);
    }

    private static List<String> generateSamples() {
        List<String> tempList = new ArrayList<>();
        for (int i = 0; i < 999_999; i++) tempList.add(String.valueOf(i));
        return tempList;
    }

    public static void waitExecutor(ExecutorService executorService) throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }
}

class MySampleThread implements Callable<Void> {

    private final String threadName;
    private final List<String> sampleList;

    public MySampleThread(String threadName, List<String> sampleList) {
        this.threadName = threadName;
        this.sampleList = sampleList;
    }

    @Override
    public Void call() {
        sampleList.stream().forEach(s -> System.out.println(String.format("thread: %s, value: %s", threadName, s)));
        return null;
    }
}
