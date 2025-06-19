package org.jazzteam.ui.editor;


import org.jazzteam.model.Priority;
import org.jazzteam.task.TaskManager;
import org.jazzteam.task.priority.GetAllPrioritiesTask;
import org.jazzteam.ui.renderer.PriorityRenderer;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;


public class PriorityCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JComboBox<Priority> comboBox;

    @Override
    public Object getCellEditorValue() {
        return comboBox.getSelectedItem();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        comboBox = new JComboBox<>();
        comboBox.setEnabled(false);
        comboBox.setRenderer(new PriorityRenderer());

        GetAllPrioritiesTask task = new GetAllPrioritiesTask(null, (result, ex) -> SwingUtilities.invokeLater(() -> {
            comboBox.removeAllItems();
            for (Priority priority : result) {
                comboBox.addItem(priority);
            }
            comboBox.setEnabled(true);
            if (value instanceof Priority) {
                comboBox.setSelectedItem(value);
            }
        }));
        TaskManager.submit(task);

        return comboBox;
    }
}

