package ru.smartflex.djf.widget;

import java.awt.event.KeyEvent;

import javax.swing.text.JTextComponent;

import ru.smartflex.djf.controller.WidgetManager;

public class TextAreaKeyHandler extends java.awt.event.KeyAdapter implements
        ISFHandler {

    private WidgetManager wm;
    private String uiName;

    public TextAreaKeyHandler(JTextComponent text, WidgetManager wm, String uiName) {
        super();
        this.wm = wm;
        this.uiName = uiName;

        text.addKeyListener(this);
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (e.isControlDown()) {
                    e.consume();
                    wm.moveUp(uiName);
                }
                break;
            case KeyEvent.VK_DOWN:
                if (e.isControlDown()) {
                    e.consume();
                    wm.moveDown(uiName);
                }
                break;
        }
    }

    @Override
    public void closeHandler() {
        wm = null;
    }

}
