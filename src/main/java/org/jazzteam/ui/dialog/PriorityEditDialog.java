package org.jazzteam.ui.dialog;

import org.jazzteam.model.Priority;

import javax.swing.*;
import java.awt.*;

public class PriorityEditDialog extends JDialog {
    private JTextField nameField;
    private JTextField weightField;
    private boolean isConfirmed = false;

    public static final String NAME = "Name:";
    public static final String WEIGHT = "Weight:";
    public static final String OK = "OK";
    public static final String CANCEL = "Cancel";

    public PriorityEditDialog(Window owner, String title, Priority priority) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        initUI(priority);
        pack();
        setLocationRelativeTo(owner);
    }

    private void initUI(Priority priority) {
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

    public String getPriorityName() {
        return nameField.getText().trim();
    }

    public int getPriorityWeight() {
        return Integer.parseInt(weightField.getText().trim());
    }
}
