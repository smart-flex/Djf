package ru.smartflex.djf.widget.grid;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;

import ru.smartflex.djf.FrameHelper;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.GridColumnInfo;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.helper.ConverterUtil;
import ru.smartflex.djf.controller.helper.PrefixUtil;
import ru.smartflex.djf.widget.TaskStatusLevelEnum;
import ru.smartflex.djf.widget.mask.IFieldValidator;

public class TFCellEditor extends DefaultCellEditor implements ICellEditor {

    private static final long serialVersionUID = 7981986423278157750L;

    private JComponent field;
    private IFieldValidator validator = null;
    private UIWrapper uiw;
    private WidgetManager wm;
    private GridColumnInfo colInfo;
    private CellEditorListener ceListener = null;
    private JTable table = null;

    public TFCellEditor(JTextField textField, UIWrapper uiw, WidgetManager wm,
                        GridColumnInfo colInfo) {
        super(textField);
        field = textField;
        this.uiw = uiw;
        this.wm = wm;
        this.colInfo = colInfo;
    }

    public TFCellEditor(JCheckBox checkBox, UIWrapper uiw, WidgetManager wm,
                        GridColumnInfo colInfo, JTable table) {
        super(checkBox);
        this.field = checkBox;
        this.uiw = uiw;
        this.wm = wm;
        this.colInfo = colInfo;
        this.table = table;
    }

    public TFCellEditor(JComboBox comboBox, UIWrapper uiw, WidgetManager wm,
                        GridColumnInfo colInfo, JTable table) {
        super(comboBox);
        this.field = comboBox;
        this.uiw = uiw;
        this.wm = wm;
        this.colInfo = colInfo;
        this.table = table;
    }

    public JComponent getComponent() {
        return field;
    }

    void setCellEditorListener(CellEditorListener ceListener) {
        this.ceListener = ceListener;
        addCellEditorListener(ceListener);
    }

    public void removeCellEditorListener() {
        if (ceListener != null) {
            super.removeCellEditorListener(ceListener);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void stopAndValidate(boolean invokeStop) {

        if (!wm.getFormBag().isFormReady()) {
            return;
        }

        if (field instanceof JCheckBox) {
            stopAndValidateCheckBox(invokeStop);
        } else if (field instanceof JComboBox) {
            // nothing to do because works will be done at table.setValueAt
        } else {
            boolean valid = true;
            if (validator != null) {
                valid = validator.isValid(((JTextField) field).getText());
                if (!valid) {
                    FrameHelper.showStatusMessage(TaskStatusLevelEnum.WARNING,
                            PrefixUtil.getMsg("${djf.message.warn.validate}", null));
                }
            }

            if (valid) {
                setUpNewValue(invokeStop);
            }
        }

    }

    private void setUpNewValue(boolean invokeStop) {
        Object val = ConverterUtil.getValue(colInfo.getWidgetType(),
                colInfo.getDateFormat(), field, colInfo);
        setUpNewValue(invokeStop, val);
    }

    private void setUpNewValue(boolean invokeStop, Object val) {
        wm.setValueScrollWidget(uiw, colInfo.getModelBase().getProperty(), val);

        if (invokeStop) {
            super.stopCellEditing();
        }

    }

    private void stopAndValidateCheckBox(boolean invokeStop) {
        // grid listener invokes too late, after cell editing stopped event
        // (GridCellListener)
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < table.getRowCount()) {
            // oops: listener will also invokes the next 2 lines
            wm.moveToRow(uiw, selectedRow);
            wm.registerSelectedWrapper(uiw);

            setUpNewValue(invokeStop);
        }

    }

    public void setValidator(IFieldValidator validator) {
        this.validator = validator;
    }

}
