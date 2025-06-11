package org.jazzteam.ui.table;

import org.jazzteam.core.ApplicationContext;
import org.jazzteam.model.Priority;
import org.jazzteam.model.Status;
import org.jazzteam.model.Todo;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TodoTableModel extends AbstractTableModel {
    private final List<Todo> todos;
    private final String[] columns = {"Title", "Description", "Priority", "Creation Date", "Due Date", "Status"};

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TodoTableModel(List<Todo> todos) {
        this.todos = todos;
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
            case 3:
            case 4:
                return String.class;
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
                return todo.getPriorityName();
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
                todo.setPriorityName((String) value);
                break;
            case 4:
                try {
                    todo.setDueDate(java.time.LocalDate.parse((String) value, formatter));
                } catch (Exception e) {
                    todo.setDueDate(null);
                }
                break;
            case 5:
                todo.setStatus((Status) value);
                break;
            default:
                return;
        }
        ApplicationContext.getTodoService().updateTodo(row, todo);
        fireTableCellUpdated(row, col);
    }
}
