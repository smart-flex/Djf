package ru.smartflex.djf.widget.mask;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextField;

import ru.smartflex.djf.widget.ISFHandler;
import ru.smartflex.djf.widget.ItemHandler;

public class MaskFieldMouseHandler extends MouseAdapter implements ISFHandler {

    private JTextField field;
    private String mask;

    public MaskFieldMouseHandler(JTextField field, String mask) {
        this.field = field;
        this.mask = mask;

        field.addMouseListener(this);
    }

    public void mouseClicked(MouseEvent e) {
        if (mask != null) {
            ItemHandler.slideCaretFromStartToRight(field, mask);
        }
    }

    @Override
    public void closeHandler() {
        field = null;
        mask = null;
    }

}
