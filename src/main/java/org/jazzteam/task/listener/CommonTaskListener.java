package org.jazzteam.task.listener;

import org.jazzteam.exception.EntityException;
import org.jazzteam.task.Updatable;

import javax.swing.*;
import java.util.List;

public class CommonTaskListener<T> implements TaskListener<T> {
    private final List<Updatable> elements;

    public CommonTaskListener(List<Updatable> elements) {
        this.elements = elements;
    }

    @Override
    public void onFinished(T result, Exception exception) {
        if (exception != null) {
            String message;

            if (exception instanceof EntityException) {
                message = exception.getMessage();
            } else {
                message = "An unexpected error occurred: " + exception.getMessage();
            }
            JOptionPane.showMessageDialog(null,
                    message,
                    "Data Conflict",
                    JOptionPane.ERROR_MESSAGE);
        }

        SwingUtilities.invokeLater(() -> {
            for (Updatable updatable : elements) {
                updatable.update();
            }
        });
    }
}
