package org.jazzteam.task;

import lombok.extern.slf4j.Slf4j;
import org.jazzteam.task.listener.TaskListener;

import javax.swing.*;

@Slf4j
public abstract class AbstractTask<T> implements Runnable {
    private final TaskListener<T> listener;

    protected AbstractTask(TaskListener<T> listener) {
        this.listener = listener;
    }

    protected abstract T execute() throws Exception;

    @Override
    public void run() {
        T result = null;
        try {
            result = execute();
        } catch (Exception e) {
            log.error("Task execution failed with exception: {}", e.getMessage());
        }

        final T finalResult = result;
        SwingUtilities.invokeLater(() -> listener.onFinished(finalResult));
    }
}

