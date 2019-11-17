package ru.smartflex.djf.widget.grid;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.BeanFormDefProperty;

public class CheckBoxCellRenderer extends JCheckBox implements TableCellRenderer {

    private static final long serialVersionUID = -936233345909791673L;

    private WidgetManager wm;

    CheckBoxCellRenderer(WidgetManager wm) {
        super();
        this.wm = wm;

        setBorder(null);
        setBorderPainted(true);
        setHorizontalAlignment(SwingConstants.CENTER);

    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int col) {

        if (!wm.getFormBag().isFormReady()) {
            return this;
        }

        if (value instanceof Boolean) {
            Boolean b = (Boolean) value;
            setSelected(b);
        } else {
            setSelected(false);
        }

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }

        setFont(table.getFont());

        if (hasFocus) {
            setBorder(UIManager
                    .getBorder("Table.focusCellHighlightBorder"));
        } else {
            setBorder(null);
        }

        // SFLogger.debug(CheckBoxCellRenderer.class,"Checkbox cell value return: "+value);

        return this;
    }

    void setUpBackGroundAsNotNull(BeanFormDefProperty prop) {

        if (prop != null && prop.getNotNull() != null
                && prop.getNotNull()) {
            super.setBackground(SFConstants.FIELD_REQUIRED_BACKGROUND_COLOR);
        }
    }

}
