package org.jazzteam.ui.table;


import lombok.extern.slf4j.Slf4j;
import org.jazzteam.model.Priority;
import org.jazzteam.model.Status;
import org.jazzteam.model.Todo;
import org.jazzteam.task.TaskManager;
import org.jazzteam.task.Updatable;
import org.jazzteam.task.listener.CommonTaskListener;
import org.jazzteam.task.todo.GetAllTodosTask;
import org.jazzteam.task.todo.UpdateTodoTask;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class TodoTableModel extends AbstractTableModel implements Updatable {
    private List<Todo> todos = new ArrayList<>();
    private final String[] columns = {"Title", "Description", "Priority", "Creation Date", "Due Date", "Status"};
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Todo getTodoAt(int rowIndex) {
        return todos.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return todos.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    @Override
    public Class<?> getColumnClass(int col) {
        switch (col) {
            case 2:
                return Priority.class;
            case 5:
                return Status.class;
            default:
                return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col != 3;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Todo todo = todos.get(row);
        switch (col) {
            case 0:
                return todo.getTitle();
            case 1:
                return todo.getDescription();
            case 2:
                return todo.getPriority() != null ? todo.getPriority().getName() : "";
            case 3:
                return todo.getCreationDate() != null ? todo.getCreationDate().format(formatter) : "";
            case 4:
                return todo.getDueDate() != null ? todo.getDueDate().format(formatter) : "";
            case 5:
                return todo.getStatus();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        Todo todo = todos.get(row);
        switch (col) {
            case 0:
                todo.setTitle((String) value);
                break;
            case 1:
                todo.setDescription((String) value);
                break;
            case 2:
                todo.setPriority((Priority) value);
                break;
            case 4:
                try {
                    todo.setDueDate(java.time.LocalDate.parse((String) value, formatter));
                } catch (Exception e) {
                    todo.setDueDate(null);
                    log.warn("Could not parse due date", e);
                }
                break;
            case 5:
                todo.setStatus((Status) value);
                break;
            default:
                return;
        }

        CommonTaskListener<Void> listener = new CommonTaskListener<>(Collections.singletonList(this));
        UpdateTodoTask task = new UpdateTodoTask(todo, listener);
        TaskManager.submit(task);
    }

    @Override
    public void update() {
        GetAllTodosTask task = new GetAllTodosTask(null, result -> {
            this.todos = result;
            fireTableDataChanged();
        });
        TaskManager.submit(task);
    }

}
