package org.jazzteam.ui.table;


import lombok.extern.slf4j.Slf4j;
import org.jazzteam.core.ApplicationContext;
import org.jazzteam.event.EventDispatcher;
import org.jazzteam.event.model.EventType;
import org.jazzteam.event.model.priority.PriorityDeletedEvent;
import org.jazzteam.event.model.priority.PriorityUpdatedEvent;
import org.jazzteam.event.model.todo.*;
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
import java.util.Optional;

@Slf4j
public class TodoTableModel extends AbstractTableModel implements Updatable {
    private List<Todo> todos = new ArrayList<>();
    private final String[] columns = {"Title", "Description", "Priority", "Creation Date", "Due Date", "Status"};
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Todo getTodoAt(int rowIndex) {
        return todos.get(rowIndex);
    }

    public TodoTableModel() {
        registerInEventDispatcher();
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


    private void registerInEventDispatcher() {
        EventDispatcher dispatcher = ApplicationContext.getEventDispatcher();

        dispatcher.register(EventType.TODO_UPDATED, (TodoUpdatedEvent e) ->
                findTodoById(e.getTodoId()).ifPresent(this::refreshAndRepaintRow)
        );

        dispatcher.register(EventType.TODO_SAVED, (TodoSavedEvent e) -> {
            Todo newTodo = e.getTodo();
            if (todos.stream().anyMatch(todo -> todo.getId().equals(newTodo.getId()))) return;
            todos.add(newTodo);
            int index = todos.size() - 1;
            fireTableRowsInserted(index, index);
        });

        dispatcher.register(EventType.TODO_MOVED_UP, (TodoMovedUpEvent e) -> moveUp(e.getTodoId()));
        dispatcher.register(EventType.TODO_MOVED_DOWN, (TodoMovedDownEvent e) -> moveDown(e.getTodoId()));

        dispatcher.register(EventType.TODO_DELETED, (TodoDeletedEvent e) -> removeTodoById(e.getTodoId()));

        dispatcher.register(EventType.PRIORITY_DELETED, (PriorityDeletedEvent e) -> {
            long priorityId = e.getPriorityId();
            List<Integer> updatedIndexes = new ArrayList<>();

            for (int i = 0; i < todos.size(); i++) {
                Todo todo = todos.get(i);
                Priority priority = todo.getPriority();
                if (priority != null && priority.getId().equals(priorityId)) {
                    todo.setPriority(null);
                    ApplicationContext.getTodoDAO().update(todo);
                    updatedIndexes.add(i);
                }
            }

            repaintRows(updatedIndexes);
        });

        dispatcher.register(EventType.PRIORITY_UPDATED, (PriorityUpdatedEvent e) -> {
            long priorityId = e.getPriorityId();
            List<Integer> affectedIndexes = new ArrayList<>();
            Priority toRefresh = null;

            for (int i = 0; i < todos.size(); i++) {
                Todo todo = todos.get(i);
                Priority p = todo.getPriority();
                if (p != null && p.getId().equals(priorityId)) {
                    toRefresh = p;
                    affectedIndexes.add(i);
                }
            }

            if (toRefresh != null) {
                ApplicationContext.getPriorityService().refreshPriority(toRefresh);
            }

            repaintRows(affectedIndexes);
        });
    }


    private void moveUp(Long todoId) {
        if (todos.size() < 2) return;

        int index = findTodoIndexById(todoId);
        if (index <= 0) return;

        Todo todo = todos.get(index);
        Todo above = todos.get(index - 1);

        switchTodos(todo, above);

        todos.set(index - 1, todo);
        todos.set(index, above);

        fireTableRowsUpdated(index - 1, index);
    }

    private void moveDown(Long todoId) {
        if (todos.size() < 2) return;

        int index = findTodoIndexById(todoId);
        if (index == -1 || index >= todos.size() - 1) return;

        Todo todo = todos.get(index);
        Todo below = todos.get(index + 1);

        switchTodos(todo, below);

        todos.set(index, below);
        todos.set(index + 1, todo);

        fireTableRowsUpdated(index, index + 1);
    }

    private Optional<Todo> findTodoById(Long id) {
        return todos.stream().filter(todo -> todo.getId().equals(id)).findFirst();
    }

    private void refreshAndRepaintRow(Todo todo) {
        ApplicationContext.getTodoService().refreshTodo(todo);
        int rowIndex = todos.indexOf(todo);
        if (rowIndex != -1) {
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }

    private void removeTodoById(Long id) {
        int index = findTodoIndexById(id);
        if (index != -1) {
            todos.remove(index);
            fireTableRowsDeleted(index, index);
        }
    }

    private void repaintRows(List<Integer> indexes) {
        if (!indexes.isEmpty()) {
            for (int i : indexes) {
                fireTableRowsUpdated(i, i);
            }

        }
    }

    private void switchTodos(Todo todo, Todo above) {
        int tempOrder = todo.getSortOrder();
        todo.setSortOrder(above.getSortOrder());
        above.setSortOrder(tempOrder);
    }

    private int findTodoIndexById(Long id) {
        return java.util.stream.IntStream.range(0, todos.size())
                .filter(i -> todos.get(i).getId().equals(id))
                .findFirst()
                .orElse(-1);
    }

}
