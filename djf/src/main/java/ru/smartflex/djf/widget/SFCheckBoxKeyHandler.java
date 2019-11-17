package ru.smartflex.djf.widget;

import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;

import ru.smartflex.djf.controller.WidgetManager;

public class SFCheckBoxKeyHandler extends java.awt.event.KeyAdapter implements
        ISFHandler {

    private JCheckBox field;
    private WidgetManager wm;

    SFCheckBoxKeyHandler(JCheckBox field, WidgetManager wm) {
        this.field = field;
        this.wm = wm;

        field.addKeyListener(this);

    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                e.consume();
                wm.moveUp(field.getName());
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_TAB:
                moveDown(e);
                break;
            case KeyEvent.VK_ENTER:
                field.doClick();
                break;
        }
    }

    private void moveDown(KeyEvent e) {
        e.consume();
        wm.moveDown(field.getName());
    }

    @Override
    public void closeHandler() {
        field = null;
        wm = null;
    }

}
