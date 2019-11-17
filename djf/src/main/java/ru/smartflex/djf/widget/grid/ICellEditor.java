package ru.smartflex.djf.widget.grid;

import javax.swing.JComponent;

public interface ICellEditor {

    JComponent getComponent();

    void removeCellEditorListener();
}
