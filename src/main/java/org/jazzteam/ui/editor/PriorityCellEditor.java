package org.jazzteam.ui.editor;


import org.jazzteam.core.ApplicationContext;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.List;


public class PriorityCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JComboBox<String> comboBox;

    @Override
    public Object getCellEditorValue() {
        return comboBox.getSelectedItem();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        List<String> priorities = ApplicationContext.getPriorityService().getPriorityNames();

        comboBox = new JComboBox<>(priorities.toArray(new String[0]));
        comboBox.setSelectedItem(value);
        return comboBox;
    }
}
