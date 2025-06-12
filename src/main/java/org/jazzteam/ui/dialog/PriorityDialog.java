package org.jazzteam.ui.dialog;

import lombok.Getter;
import org.jazzteam.core.ApplicationContext;
import org.jazzteam.model.Priority;
import org.jazzteam.service.PriorityService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PriorityDialog extends JDialog {
    private final PriorityService priorityService = ApplicationContext.getPriorityService();
    private final DefaultListModel<Priority> listModel = new DefaultListModel<>();
    private final JList<Priority> priorityJList = new JList<>(listModel);
    @Getter
    private boolean isStateChanged = false;

    public static final String PRIORITY_MANAGEMENT = "Priority Management";
    public static final String ADD = "Add";
    public static final String EDIT = "Edit";
    public static final String DELETE = "Delete";
    public static final String CLOSE = "Close";
    public static final String ADD_NEW_PRIORITY = "Add New Priority";
    public static final String EDIT_PRIORITY = "Edit Priority";


    public PriorityDialog(Frame owner) {
        super(owner, true);
        setTitle(PRIORITY_MANAGEMENT);
        initUI();
        loadPriorities();

        pack();
        setLocationRelativeTo(owner);
        setMinimumSize(new Dimension(400, 500));
    }

    private void initUI() {
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

    private void loadPriorities() {
        listModel.clear();
        List<Priority> priorities = priorityService.getAllPriorities();
        priorities.forEach(listModel::addElement);
    }

    private void showAddPriorityDialog() {
        PriorityEditDialog dialog = new PriorityEditDialog(this, ADD_NEW_PRIORITY, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            priorityService.savePriority(dialog.getPriority());
            loadPriorities();
            isStateChanged = true;
        }
    }

    private void showEditPriorityDialog() {
        Priority selected = priorityJList.getSelectedValue();
        if (selected == null) return;

        PriorityEditDialog dialog = new PriorityEditDialog(this, EDIT_PRIORITY, selected);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            priorityService.updatePriority(dialog.getPriority());
            loadPriorities();
            isStateChanged = true;
        }
    }

    private void deleteSelectedPriority() {
        Priority selected = priorityJList.getSelectedValue();
        if (selected != null) {
            priorityService.deletePriority(selected);
            listModel.removeElement(selected);
            isStateChanged = true;
        }
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