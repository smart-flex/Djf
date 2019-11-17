package ru.smartflex.djf.widget.grid;

import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

import ru.smartflex.djf.widget.ISFHandler;

public class GridCellListener implements CellEditorListener, ISFHandler {

    private TFCellEditor cellEditor;

    public GridCellListener(TFCellEditor cellEditor) {
        this.cellEditor = cellEditor;

        cellEditor.setCellEditorListener(this);
    }

    @Override
    public void editingStopped(ChangeEvent e) {
        cellEditor.stopAndValidate(false);
    }

    @Override
    public void editingCanceled(ChangeEvent e) {
    }

    @Override
    public void closeHandler() {
        cellEditor = null;
    }

}
