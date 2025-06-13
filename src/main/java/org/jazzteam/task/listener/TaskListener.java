package org.jazzteam.task.listener;

public interface TaskListener<T> {
    void onFinished(T result);
}
