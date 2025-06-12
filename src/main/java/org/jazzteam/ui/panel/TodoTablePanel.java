package org.jazzteam.ui.panel;

import lombok.Getter;
import org.jazzteam.core.ApplicationContext;
import org.jazzteam.model.Status;
import org.jazzteam.ui.editor.PriorityCellEditor;
import org.jazzteam.ui.table.TodoTableModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;


@Getter
public class TodoTablePanel extends JPanel {
    private JTable table;
    private TodoTableModel tableModel;

    public static final int PRIORITY_COLUMN_INDEX = 2;
    public static final int DEFAULT_ROW_HEIGHT = 30;


    public TodoTablePanel() {
        initUi();

        TableColumn priorityColumn = table.getColumnModel().getColumn(PRIORITY_COLUMN_INDEX);
        priorityColumn.setCellEditor(new PriorityCellEditor());

        table.setDefaultEditor(Status.class, new DefaultCellEditor(new JComboBox<>(Status.values())));
    }

    public void updateData() {
        tableModel.updateData();
    }

    private void initUi() {
        setLayout(new BorderLayout());

        tableModel = new TodoTableModel(ApplicationContext.getTodoService());
        table = new JTable(tableModel);

        table.setRowHeight(DEFAULT_ROW_HEIGHT);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

    }
}
