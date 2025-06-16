package org.jazzteam.ui.dialog;

import org.jazzteam.core.ApplicationContext;
import org.jazzteam.event.EventDispatcher;
import org.jazzteam.event.listener.AppEventListener;
import org.jazzteam.event.listener.ListenerRegistration;
import org.jazzteam.event.model.EventType;
import org.jazzteam.event.model.priority.PriorityDeletedEvent;
import org.jazzteam.event.model.priority.PrioritySavedEvent;
import org.jazzteam.event.model.priority.PriorityUpdatedEvent;
import org.jazzteam.model.Priority;
import org.jazzteam.task.TaskManager;
import org.jazzteam.task.Updatable;
import org.jazzteam.task.listener.CommonTaskListener;
import org.jazzteam.task.priority.DeletePriorityTask;
import org.jazzteam.task.priority.GetAllPrioritiesTask;
import org.jazzteam.task.priority.SavePriorityTask;
import org.jazzteam.task.priority.UpdatePriorityTask;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class PriorityDialog extends JDialog implements Updatable {
    private final DefaultListModel<Priority> listModel = new DefaultListModel<>();
    private final JList<Priority> priorityJList = new JList<>(listModel);
    private final List<Updatable> updatableElements = new ArrayList<>();
    private CommonTaskListener<Void> listener;
    private List<Priority> currentPriorities = new ArrayList<>();
    private final List<ListenerRegistration<?>> registeredListeners = new ArrayList<>();

    public static final String PRIORITY_MANAGEMENT = "Priority Management";
    public static final String ADD = "Add";
    public static final String EDIT = "Edit";
    public static final String DELETE = "Delete";
    public static final String CLOSE = "Close";
    public static final String ADD_NEW_PRIORITY = "Add New Priority";
    public static final String EDIT_PRIORITY = "Edit Priority";


    public PriorityDialog(Frame owner, List<Updatable> updatableElements) {
        super(owner, true);
        this.updatableElements.add(this);
        this.updatableElements.addAll(updatableElements);

        setTitle(PRIORITY_MANAGEMENT);
        initUI();
        update();
        registerInEventDispatcher();

        pack();
        setLocationRelativeTo(owner);
        setMinimumSize(new Dimension(400, 500));
    }

    @Override
    public void dispose() {
        ApplicationContext.getEventDispatcher().unregisterAll(registeredListeners);
        super.dispose();
    }

    private void registerInEventDispatcher() {
        EventDispatcher dispatcher = ApplicationContext.getEventDispatcher();
        AppEventListener<PrioritySavedEvent> prioritySavedListener = (PrioritySavedEvent e) -> {
            Priority newPriority = e.getPriority();
            if (currentPriorities.stream().anyMatch(priority -> priority.getId().equals(newPriority.getId())))
                return;
            currentPriorities.add(newPriority);
            SwingUtilities.invokeLater(() -> listModel.addElement(newPriority));
        };
        dispatcher.register(EventType.PRIORITY_SAVED, prioritySavedListener);
        registeredListeners.add(new ListenerRegistration<>(EventType.PRIORITY_SAVED, prioritySavedListener));


        AppEventListener<PriorityDeletedEvent> priorityDeletedListener = (PriorityDeletedEvent e) -> {
            Long deletedId = e.getPriorityId();
            int indexToRemove = IntStream.range(0, currentPriorities.size())
                    .filter(i -> currentPriorities.get(i).getId().equals(deletedId))
                    .findFirst()
                    .orElse(-1);

            if (indexToRemove != -1) {
                currentPriorities.remove(indexToRemove);
                final int idx = indexToRemove;
                SwingUtilities.invokeLater(() -> listModel.remove(idx));
            }
        };
        dispatcher.register(EventType.PRIORITY_DELETED, priorityDeletedListener);
        registeredListeners.add(new ListenerRegistration<>(EventType.PRIORITY_DELETED, priorityDeletedListener));


        AppEventListener<PriorityUpdatedEvent> priorityUpdatedListener = (PriorityUpdatedEvent e) -> {
            Long updatedId = e.getPriorityId();
            IntStream.range(0, currentPriorities.size())
                    .filter(i -> currentPriorities.get(i).getId().equals(updatedId))
                    .findFirst()
                    .ifPresent(i -> {
                        Priority priority = currentPriorities.get(i);
                        ApplicationContext.getPriorityService().refreshPriority(priority);
                        currentPriorities.set(i, priority);
                        final int index = i;
                        SwingUtilities.invokeLater(() -> listModel.setElementAt(priority, index));
                    });
        };
        dispatcher.register(EventType.PRIORITY_UPDATED, priorityUpdatedListener);
        registeredListeners.add(new ListenerRegistration<>(EventType.PRIORITY_UPDATED, priorityUpdatedListener));

    }

    private void initUI() {
        listener = new CommonTaskListener<>(updatableElements);
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        priorityJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        priorityJList.setCellRenderer(new PriorityListRenderer());
        JScrollPane scrollPane = new JScrollPane(priorityJList);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton addBtn = createStandardButton(ADD);
        JButton editBtn = createStandardButton(EDIT);
        JButton deleteBtn = createStandardButton(DELETE);
        JButton closeBtn = createStandardButton(CLOSE);

        addBtn.addActionListener(e -> showAddPriorityDialog());
        editBtn.addActionListener(e -> showEditPriorityDialog());
        deleteBtn.addActionListener(e -> deleteSelectedPriority());
        closeBtn.addActionListener(e -> dispose());

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(closeBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private JButton createStandardButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(80, 30));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(3, 10, 3, 10)
        ));
        return button;
    }

    private void redrawPriorities() {
        listModel.clear();
        currentPriorities.forEach(listModel::addElement);
    }

    private void showAddPriorityDialog() {
        PriorityEditDialog dialog = new PriorityEditDialog(this, ADD_NEW_PRIORITY, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            SavePriorityTask task = new SavePriorityTask(dialog.getPriority(), listener);
            TaskManager.submit(task);
        }
    }

    private void showEditPriorityDialog() {
        Priority selected = priorityJList.getSelectedValue();
        if (selected == null) return;

        PriorityEditDialog dialog = new PriorityEditDialog(this, EDIT_PRIORITY, selected);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            UpdatePriorityTask task = new UpdatePriorityTask(dialog.getPriority(), listener);
            TaskManager.submit(task);
        }
    }

    private void deleteSelectedPriority() {
        Priority selected = priorityJList.getSelectedValue();
        if (selected != null) {
            DeletePriorityTask task = new DeletePriorityTask(selected, listener);
            TaskManager.submit(task);
        }
    }

    @Override
    public void update() {
        GetAllPrioritiesTask task = new GetAllPrioritiesTask(null, result -> {
            this.currentPriorities = result;
            redrawPriorities();
        });
        TaskManager.submit(task);
    }

    private static class PriorityListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Priority) {
                Priority p = (Priority) value;
                setText(p.getName() + " (Weight: " + p.getWeight() + ")");
            }
            return this;
        }
    }
}