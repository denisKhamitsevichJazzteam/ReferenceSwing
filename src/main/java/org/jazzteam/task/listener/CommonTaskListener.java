package org.jazzteam.task.listener;

import org.jazzteam.task.Updatable;

import javax.swing.*;
import java.util.List;

public class CommonTaskListener<T> implements TaskListener<T> {
    private final List<Updatable> elements;

    public CommonTaskListener(List<Updatable> elements) {
        this.elements = elements;
    }

    @Override
    public void onFinished(T result) {
        SwingUtilities.invokeLater(() -> {
            for (Updatable updatable : elements) {
                updatable.update();
            }
        });
    }
}
