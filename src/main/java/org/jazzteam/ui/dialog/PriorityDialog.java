package org.jazzteam.ui.dialog;

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

public class PriorityDialog extends JDialog implements Updatable {
    private final DefaultListModel<Priority> listModel = new DefaultListModel<>();
    private final JList<Priority> priorityJList = new JList<>(listModel);
    private final List<Updatable> updatableElements = new ArrayList<>();
    private CommonTaskListener<Void> listener;
    private List<Priority> currentPriorities = new ArrayList<>();

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

        pack();
        setLocationRelativeTo(owner);
        setMinimumSize(new Dimension(400, 500));
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