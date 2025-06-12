package org.jazzteam;

import org.jazzteam.core.ApplicationContext;
import org.jazzteam.model.Todo;
import org.jazzteam.service.TodoService;
import org.jazzteam.ui.dialog.PriorityDialog;
import org.jazzteam.ui.dialog.TodoDialog;
import org.jazzteam.ui.panel.ControlPanel;
import org.jazzteam.ui.panel.TodoTablePanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final TodoService todoService = ApplicationContext.getTodoService();
    private TodoTablePanel todoTablePanel;
    private ControlPanel controlPanel;

    public MainFrame() {
        super("Reference project");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        initUi();
        bindListeners();
    }

    private void initUi() {
        todoTablePanel = new TodoTablePanel();
        todoTablePanel.updateData();
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
                todoService.addTodo(dialog.getTodo());
                todoTablePanel.updateData();
            }
        });

        controlPanel.setDeleteAction(e -> {
            int sel = todoTablePanel.getTable().getSelectedRow();
            if (sel >= 0) {
                todoService.deleteTodo(todoTablePanel.getTableModel().getTodoAt(sel));
                todoTablePanel.updateData();
            }
        });

        controlPanel.setEditAction(e -> {
            int sel = todoTablePanel.getTable().getSelectedRow();
            if (sel >= 0) {
                Todo todo = todoTablePanel.getTableModel().getTodoAt(sel);
                TodoDialog dialog = new TodoDialog(this, todo);
                dialog.setVisible(true);
                if (dialog.isSavePressed()) {
                    todoService.updateTodo(dialog.getTodo());
                    todoTablePanel.updateData();
                }
            }
        });

        controlPanel.setMoveUpAction(e -> {
            int sel = todoTablePanel.getTable().getSelectedRow();
            if (sel > 0) {
                todoService.moveUp(todoTablePanel.getTableModel().getTodoAt(sel).getId());
                todoTablePanel.updateData();
            }
        });

        controlPanel.setMoveDownAction(e -> {
            int sel = todoTablePanel.getTable().getSelectedRow();
            todoService.moveDown(todoTablePanel.getTableModel().getTodoAt(sel).getId());
            todoTablePanel.updateData();
        });

        controlPanel.setPrioritiesAction(e -> {
            PriorityDialog dialog = new PriorityDialog(this);
            dialog.setVisible(true);
            if (dialog.isStateChanged()) {
                todoTablePanel.updateData();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}

