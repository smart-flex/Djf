package ru.smartflex.djf.widget;

import java.awt.event.KeyEvent;

import javax.swing.JComboBox;

import ru.smartflex.djf.controller.WidgetManager;

public class SFComboBoxKeyHandler extends java.awt.event.KeyAdapter implements
        ISFHandler {

    private JComboBox field;
    private WidgetManager wm;

    SFComboBoxKeyHandler(JComboBox field, WidgetManager wm) {
        this.field = field;
        this.wm = wm;

        field.addKeyListener(this);
    }

    public void keyPressed(KeyEvent e) {
        boolean isPopupVisible = field.isPopupVisible();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                e.consume();
                if (isPopupVisible) {
                    field.hidePopup();
                } else {
                    field.showPopup();
                }
                break;
            case KeyEvent.VK_UP:
                if (!isPopupVisible) {
                    e.consume();
                    wm.moveUp(field.getName());
                }
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_TAB:
                moveDown(e);
                break;
        }
    }

    private void moveDown(KeyEvent e) {
        if (!field.isPopupVisible()) {
            e.consume();
            wm.moveDown(field.getName());
        }
    }

    @Override
    public void closeHandler() {
        field.removeKeyListener(this);

        field = null;
        wm = null;
    }

}
