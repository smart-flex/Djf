package ru.smartflex.djf.widget.grid;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

import ru.smartflex.djf.widget.ISFHandler;

public class GridCellFocusListener implements FocusListener, ISFHandler {

    private TFCellEditor cellEditor;

    public GridCellFocusListener(JTextField field, TFCellEditor cellEditor) {
        field.addFocusListener(this);
        this.cellEditor = cellEditor;
    }

    @Override
    public void focusGained(FocusEvent e) {
    }

    @Override
    public void focusLost(FocusEvent e) {
        cellEditor.cancelCellEditing();
    }

    @Override
    public void closeHandler() {
        cellEditor = null;
    }
}
