package ru.smartflex.djf.widget.grid;

import java.awt.Component;

import javax.swing.JTable;

import ru.smartflex.djf.controller.WidgetManager;

public class ComboBoxCellRenderer extends GridCellRenderer {

    private static final long serialVersionUID = 796810776443102915L;

    private WidgetManager wm;

    ComboBoxCellRenderer(WidgetManager wm) {
        super();
        this.wm = wm;
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int col) {

        if (!wm.getFormBag().isFormReady()) {
            return this;
        }

        super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, col);

        return this;
    }

}
