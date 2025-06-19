package org.jazzteam.ui.dialog;

import lombok.Getter;
import org.jazzteam.model.Priority;
import org.jazzteam.model.Status;
import org.jazzteam.model.Todo;
import org.jazzteam.task.TaskManager;
import org.jazzteam.task.priority.GetAllPrioritiesTask;
import org.jazzteam.ui.renderer.PriorityRenderer;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Getter
public class TodoDialog extends JDialog {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<Priority> priorityComboBox;
    private JTextField creationDateField;
    private JTextField dueDateField;
    private JComboBox<Status> statusComboBox;
    private boolean isSavePressed = false;
    private Todo todo;

    public static final String ADD_NEW_TASK_TITLE = "Add New Task";
    public static final String EDIT_TASK_TITLE = "Edit Task";
    public static final String TITLE = "Title:";
    public static final String DESCRIPTION = "Description:";
    public static final String PRIORITY = "Priority:";
    public static final String CREATED = "Created:";
    public static final String DUE_DATE = "Due Date:";
    public static final String DATE_TIP_TEXT = "YYYY-MM-DD";
    public static final String STATUS = "Status:";
    public static final String SAVE = "Save";
    public static final String CANCEL = "Cancel";

    public TodoDialog(Frame owner) {
        this(owner, null);
    }

    public TodoDialog(Frame owner, Todo todo) {
        super(owner, true);
        setTitle(todo == null ? ADD_NEW_TASK_TITLE : EDIT_TASK_TITLE);
        this.todo = todo;

        initUI();
        fillFields();

        pack();
        setLocationRelativeTo(owner);
        setMinimumSize(new Dimension(450, 500));
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;

        // Title
        titleField = new JTextField(25);
        addFormRow(formPanel, gbc, 0, TITLE, createInputField(titleField));

        // Description
        descriptionArea = new JTextArea(5, 25);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        addFormRow(formPanel, gbc, 1, DESCRIPTION, new JScrollPane(descriptionArea));

        // Priority
        priorityComboBox = new JComboBox<>();
        priorityComboBox.setEnabled(false);
        priorityComboBox.setRenderer(new PriorityRenderer());
        GetAllPrioritiesTask task = new GetAllPrioritiesTask(null, (result, ex) -> SwingUtilities.invokeLater(() -> {
            priorityComboBox.removeAllItems();
            for (Priority priority : result) {
                priorityComboBox.addItem(priority);
            }
            priorityComboBox.setEnabled(true);
            priorityComboBox.setSelectedItem(todo != null && todo.getPriority() != null ? todo.getPriority() : "");
        }));
        TaskManager.submit(task);

        addFormRow(formPanel, gbc, 2, PRIORITY, priorityComboBox);

        // Creation Date (readonly)
        creationDateField = new JTextField(25);
        creationDateField.setEditable(false);
        addFormRow(formPanel, gbc, 3, CREATED, creationDateField);

        // Due Date
        dueDateField = new JTextField(25);
        dueDateField.setToolTipText(DATE_TIP_TEXT);
        addFormRow(formPanel, gbc, 4, DUE_DATE, dueDateField);

        // Status
        statusComboBox = new JComboBox<>(Status.values());
        addFormRow(formPanel, gbc, 5, STATUS, statusComboBox);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton okBtn = new JButton(SAVE);
        JButton cancelBtn = new JButton(CANCEL);

        okBtn.setPreferredSize(new Dimension(100, 30));
        cancelBtn.setPreferredSize(new Dimension(100, 30));

        buttonPanel.add(okBtn);
        buttonPanel.add(cancelBtn);

        okBtn.addActionListener(e -> onOk());
        cancelBtn.addActionListener(e -> onCancel());

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private JComponent createInputField(JTextField field) {
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }

    private void fillFields() {
        if (todo != null) {
            titleField.setText(todo.getTitle());
            descriptionArea.setText(todo.getDescription());
            creationDateField.setText(todo.getCreationDate().toString());
            dueDateField.setText(todo.getDueDate() != null ? todo.getDueDate().toString() : "");
            statusComboBox.setSelectedItem(todo.getStatus());
        } else {
            creationDateField.setText(LocalDate.now().toString());
        }
    }

    private void onOk() {
        if (titleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Title is required",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDate dueDate = dueDateField.getText().isEmpty() ?
                    null : LocalDate.parse(dueDateField.getText());

            todo = new Todo(todo != null ? todo.getId() : null,
                    titleField.getText().trim(),
                    descriptionArea.getText().trim(),
                    (Priority) priorityComboBox.getSelectedItem(),
                    LocalDate.parse(creationDateField.getText()),
                    dueDate,
                    (Status) statusComboBox.getSelectedItem(),
                    todo != null ? todo.getSortOrder() : null
            );

            isSavePressed = true;
            dispose();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid date format. Please use YYYY-MM-DD",
                    "Date Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        isSavePressed = false;
        dispose();
    }
}