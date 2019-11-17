package ru.smartflex.djf.widget;

import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JRadioButton;

import ru.smartflex.djf.controller.WidgetManager;

public class ButtonKeyHandler extends java.awt.event.KeyAdapter implements
        ISFHandler {

    private Component comp;
    private WidgetManager wm;

    ButtonKeyHandler(SFFileChooser file, WidgetManager wm) {
        this.comp = file;
        this.wm = wm;

        file.getButton().addKeyListener(this);
    }

    ButtonKeyHandler(JButton button, WidgetManager wm) {
        this.comp = button;
        this.wm = wm;

        button.addKeyListener(this);
    }

    public ButtonKeyHandler(JRadioButton radio, WidgetManager wm) {
        this.comp = radio;
        this.wm = wm;

        radio.addKeyListener(this);
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                e.consume();
                wm.moveUp(comp.getName());
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_TAB:
                moveDown(e);
                break;
        }
    }

    private void moveDown(KeyEvent e) {
        e.consume();
        wm.moveDown(comp.getName());
    }

    @Override
    public void closeHandler() {
        this.comp = null;
        this.wm = null;
    }

}
