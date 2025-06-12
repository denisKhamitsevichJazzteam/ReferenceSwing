package org.jazzteam.ui.editor;


import org.jazzteam.core.ApplicationContext;
import org.jazzteam.model.Priority;
import org.jazzteam.ui.renderer.PriorityRenderer;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.List;


public class PriorityCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JComboBox<Priority> comboBox;

    @Override
    public Object getCellEditorValue() {
        return comboBox.getSelectedItem();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        List<Priority> priorities = ApplicationContext.getPriorityService().getAllPriorities();
        comboBox = new JComboBox<>(priorities.toArray(new Priority[0]));

        comboBox.setRenderer(new PriorityRenderer());

        if (value instanceof Priority) {
            comboBox.setSelectedItem(value);
        }

        return comboBox;
    }
}

