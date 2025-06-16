package org.jazzteam;

import org.jazzteam.core.ApplicationContext;
import org.jazzteam.model.Todo;
import org.jazzteam.task.TaskManager;
import org.jazzteam.task.Updatable;
import org.jazzteam.task.listener.CommonTaskListener;
import org.jazzteam.task.todo.*;
import org.jazzteam.ui.dialog.PriorityDialog;
import org.jazzteam.ui.dialog.TodoDialog;
import org.jazzteam.ui.panel.ControlPanel;
import org.jazzteam.ui.panel.TodoTablePanel;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class MainFrame extends JFrame {
    private TodoTablePanel todoTablePanel;
    private ControlPanel controlPanel;
    private CommonTaskListener<Void> listener;

    public MainFrame() {
        super("Reference project");
        ApplicationContext.getEventBroker().subscribe();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        initUi();
        bindListeners();
    }

    private void initUi() {
        todoTablePanel = new TodoTablePanel();
        todoTablePanel.getTableModel().update();
        listener = new CommonTaskListener<>(Collections.singletonList(todoTablePanel.getTableModel()));
        controlPanel = new ControlPanel();

        setLayout(new BorderLayout());
        add(todoTablePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void bindListeners() {
        controlPanel.setAddAction(e -> {
            TodoDialog dialog = new TodoDialog(this);
            dialog.setVisible(true);
            if (dialog.isSavePressed()) {
                SaveTodoTask task = new SaveTodoTask(dialog.getTodo(), listener);
                TaskManager.submit(task);
            }
        });

        controlPanel.setDeleteAction(e -> {
            int sel = todoTablePanel.getTable().getSelectedRow();
            if (sel >= 0) {
                DeleteTodoTask task = new DeleteTodoTask(todoTablePanel.getTableModel().getTodoAt(sel), listener);
                TaskManager.submit(task);
            }
        });

        controlPanel.setEditAction(e -> {
            int sel = todoTablePanel.getTable().getSelectedRow();
            if (sel >= 0) {
                Todo todo = todoTablePanel.getTableModel().getTodoAt(sel);
                TodoDialog dialog = new TodoDialog(this, todo);
                dialog.setVisible(true);
                if (dialog.isSavePressed()) {
                    UpdateTodoTask task = new UpdateTodoTask(dialog.getTodo(), listener);
                    TaskManager.submit(task);
                }
            }
        });

        controlPanel.setMoveUpAction(e -> {
            int sel = todoTablePanel.getTable().getSelectedRow();
            if (sel > 0) {
                MoveUpTodoTask task = new MoveUpTodoTask(todoTablePanel.getTableModel().getTodoAt(sel), listener);
                TaskManager.submit(task);
            }
        });

        controlPanel.setMoveDownAction(e -> {
            int sel = todoTablePanel.getTable().getSelectedRow();
            MoveDownTodoTask task = new MoveDownTodoTask(todoTablePanel.getTableModel().getTodoAt(sel), listener);
            TaskManager.submit(task);
        });

        controlPanel.setPrioritiesAction(e -> {
            List<Updatable> updatableElements = Collections.singletonList(todoTablePanel.getTableModel());
            PriorityDialog dialog = new PriorityDialog(this, updatableElements);
            dialog.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}

