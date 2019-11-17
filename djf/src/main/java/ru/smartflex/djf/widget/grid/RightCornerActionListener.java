package ru.smartflex.djf.widget.grid;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTable;

import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.widget.ISFHandler;

public class RightCornerActionListener implements ActionListener, ISFHandler {

    private boolean arrowUp;
    private WidgetManager widgetManager;
    private JTable table;
    private JButton button;

    public RightCornerActionListener(JTable table, JButton button,
                                     boolean arrowDirection, WidgetManager widgetManager) {
        super();
        this.table = table;
        arrowUp = arrowDirection;
        this.button = button;
        button.addActionListener(this);
        this.widgetManager = widgetManager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (widgetManager == null) {
            return;
        }
        if (!widgetManager.getFormBag().isFormReady()) {
            return;
        }

        int currentColumn = table.getSelectedColumn();
        if (arrowUp) {
            table.changeSelection(0, currentColumn, false, false);
        } else {
            int size = table.getModel().getRowCount();
            table.changeSelection(size - 1, currentColumn, false, false);
        }
        // because mouse is flying through table then Djf set off refistering last requested grid
        // and when we clicks on right corner Djf does not setup last requested grid
        // Therefore we have to off motion flag there
        SFGridMouseMotionFlag.setMotionFlag(false);
        table.requestFocus();
    }

    @Override
    public void closeHandler() {
        button.removeActionListener(this);
        widgetManager = null;
        table = null;
        button = null;
    }
}
