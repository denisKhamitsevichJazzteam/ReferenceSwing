package org.jazzteam.ui.panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ControlPanel extends JPanel {
    private final JButton addBtn = new JButton("Add");
    private final JButton editBtn = new JButton("Edit");
    private final JButton deleteBtn = new JButton("Delete");
    private final JButton moveUpBtn = new JButton("Move Up");
    private final JButton moveDownBtn = new JButton("Move Down");
    private final JButton prioritiesBtn = new JButton("Priorities");

    public ControlPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        add(addBtn);
        add(editBtn);
        add(deleteBtn);
        add(moveUpBtn);
        add(moveDownBtn);
        add(prioritiesBtn);
    }

    public void setAddAction(ActionListener listener) {
        addBtn.addActionListener(listener);
    }

    public void setEditAction(ActionListener listener) {
        editBtn.addActionListener(listener);
    }

    public void setDeleteAction(ActionListener listener) {
        deleteBtn.addActionListener(listener);
    }

    public void setMoveUpAction(ActionListener listener) {
        moveUpBtn.addActionListener(listener);
    }

    public void setMoveDownAction(ActionListener listener) {
        moveDownBtn.addActionListener(listener);
    }

    public void setPrioritiesAction(ActionListener listener) {
        prioritiesBtn.addActionListener(listener);
    }
}

