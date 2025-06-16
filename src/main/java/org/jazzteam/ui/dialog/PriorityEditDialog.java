package org.jazzteam.ui.dialog;

import org.jazzteam.core.ApplicationContext;
import org.jazzteam.event.EventDispatcher;
import org.jazzteam.event.listener.AppEventListener;
import org.jazzteam.event.listener.ListenerRegistration;
import org.jazzteam.event.model.EventType;
import org.jazzteam.event.model.priority.PriorityDeletedEvent;
import org.jazzteam.model.Priority;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PriorityEditDialog extends JDialog {
    private JTextField nameField;
    private JTextField weightField;
    private boolean isConfirmed = false;
    private Priority priority;
    private final List<ListenerRegistration<?>> registeredListeners = new ArrayList<>();

    public static final String NAME = "Name:";
    public static final String WEIGHT = "Weight:";
    public static final String OK = "OK";
    public static final String CANCEL = "Cancel";

    public PriorityEditDialog(Window owner, String title, Priority priority) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        this.priority = priority;
        initUI();
        pack();
        setLocationRelativeTo(owner);
        registerInEventDispatcher();
    }

    @Override
    public void dispose() {
        ApplicationContext.getEventDispatcher().unregisterAll(registeredListeners);
        super.dispose();
    }

    private void registerInEventDispatcher() {
        EventDispatcher dispatcher = ApplicationContext.getEventDispatcher();
        AppEventListener<PriorityDeletedEvent> priorityDeletedListener = (PriorityDeletedEvent e) -> {
            if (priority != null && priority.getId().equals(e.getPriorityId())) {
                JOptionPane.showMessageDialog(this,
                        "Sorry, this Priority no longer exists as it has been deleted",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                dispose();
            }
        };
        dispatcher.register(EventType.PRIORITY_DELETED, priorityDeletedListener);
        registeredListeners.add(new ListenerRegistration<>(EventType.PRIORITY_DELETED, priorityDeletedListener));
    }

    private void initUI() {
        nameField = new JTextField(20);
        weightField = new JTextField(20);

        if (priority != null) {
            nameField.setText(priority.getName());
            weightField.setText(String.valueOf(priority.getWeight()));
        }

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        addFormRow(panel, gbc, 0, NAME, nameField);
        addFormRow(panel, gbc, 1, WEIGHT, weightField);

        JPanel buttonPanel = initializeButtonsPanel();

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private JPanel initializeButtonsPanel() {
        JButton okButton = new JButton(OK);
        JButton cancelButton = new JButton(CANCEL);

        okButton.addActionListener(e -> {
            if (validateInput()) {
                isConfirmed = true;
                updatePriority();
                setVisible(false);
            }
        });

        cancelButton.addActionListener(e -> {
            isConfirmed = false;
            setVisible(false);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        return buttonPanel;
    }

    private void updatePriority() {
        if (priority == null) {
            priority = new Priority();
        }
        priority.setName(nameField.getText());
        priority.setWeight(Integer.parseInt(weightField.getText().trim()));
    }

    private boolean validateInput() {
        String name = nameField.getText().trim();
        String weightText = weightField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            Integer.parseInt(weightText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Weight must be a valid integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public Priority getPriority() {
        return priority;
    }
}
