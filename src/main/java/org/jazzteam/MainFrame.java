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
import java.util.List;

public class MainFrame extends JFrame {
    private final TodoService todoService = ApplicationContext.getTodoService();
    private final TodoTablePanel todoTablePanel;
    private final ControlPanel controlPanel;

    public MainFrame() {
        super("Reference project");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        List<Todo> todos = todoService.getAllTodos();

        todoTablePanel = new TodoTablePanel(todos);
        controlPanel = new ControlPanel();

        setLayout(new BorderLayout());
        add(todoTablePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        bindListeners(todos);
    }

    private void bindListeners(List<Todo> todos) {
        controlPanel.setAddAction(e -> {
            TodoDialog dialog = new TodoDialog(this);
            dialog.setVisible(true);
            if (dialog.isSavePressed()) {
                todoService.addTodo(dialog.getTodo());
                todoTablePanel.getTableModel().fireTableDataChanged();
            }
        });

        controlPanel.setDeleteAction(e -> {
            int sel = todoTablePanel.getTable().getSelectedRow();
            if (sel >= 0) {
                todoService.deleteTodo(sel);
                todoTablePanel.getTableModel().fireTableDataChanged();
            }
        });

        controlPanel.setEditAction(e -> {
            int sel = todoTablePanel.getTable().getSelectedRow();
            if (sel >= 0) {
                Todo todo = todos.get(sel);
                TodoDialog dialog = new TodoDialog(this, todo);
                dialog.setVisible(true);
                if (dialog.isSavePressed()) {
                    todoService.updateTodo(sel,dialog.getTodo());
                    todoTablePanel.getTableModel().fireTableDataChanged();
                }
            }
        });

        controlPanel.setMoveUpAction(e -> {
            int sel = todoTablePanel.getTable().getSelectedRow();
            if (sel > 0) {
                todoService.moveUp(sel);
                todoTablePanel.getTableModel().fireTableDataChanged();
            }
        });

        controlPanel.setMoveDownAction(e -> {
            int sel = todoTablePanel.getTable().getSelectedRow();
            if (sel >= 0 && sel < todos.size() - 1) {
                todoService.moveDown(sel);
                todoTablePanel.getTableModel().fireTableDataChanged();
            }
        });

        controlPanel.setPrioritiesAction(e -> {
            PriorityDialog dialog = new PriorityDialog(this);
            dialog.setVisible(true);
            if (dialog.isStateChanged()) {
                todoTablePanel.getTableModel().fireTableDataChanged();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}

