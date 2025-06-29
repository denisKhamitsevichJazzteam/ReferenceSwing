package org.jazzteam.ui.renderer;

import org.jazzteam.model.Priority;

import javax.swing.*;
import java.awt.*;

public class PriorityRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Priority) {
            setText(((Priority) value).getName());
        }
        return this;
    }
}
