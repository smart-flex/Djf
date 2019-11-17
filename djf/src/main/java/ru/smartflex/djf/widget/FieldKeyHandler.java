package ru.smartflex.djf.widget;

import java.awt.event.KeyEvent;

import javax.swing.JTextField;

import ru.smartflex.djf.controller.WidgetManager;

/**
 * Key handler for text field.
 */
public class FieldKeyHandler extends java.awt.event.KeyAdapter implements
        ISFHandler {

    private JTextField field;
    private WidgetManager wm;

    FieldKeyHandler(JTextField field, WidgetManager wm) {
        this.field = field;
        this.wm = wm;

        field.addKeyListener(this);
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                wm.moveDown(field.getName());
                break;
            case KeyEvent.VK_UP:
                e.consume();
                wm.moveUp(field.getName());
                break;
            case KeyEvent.VK_TAB:
            case KeyEvent.VK_DOWN:
                e.consume();
                wm.moveDown(field.getName());
                break;
        }
    }

    @Override
    public void closeHandler() {
        field = null;
        wm = null;
    }

}
