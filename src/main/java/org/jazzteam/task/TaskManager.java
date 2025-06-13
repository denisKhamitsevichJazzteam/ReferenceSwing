package org.jazzteam.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskManager {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void submit(AbstractTask abstractTask) {
        executor.submit(abstractTask);
    }

    public static void shutdown() {
        executor.shutdown();
    }
}
