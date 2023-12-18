package ru.smartflex.djf.widget;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextField;

public class MouseListenerText extends MouseAdapter implements ISFHandler {

    private JTextField field;

    public MouseListenerText(JTextField field) {
        super();
        this.field = field;

        field.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            if (!field.isEditable()) {
                field.setEditable(true);
            }
        }
    }

    @Override
    public void closeHandler() {
        field = null;
    }

}
