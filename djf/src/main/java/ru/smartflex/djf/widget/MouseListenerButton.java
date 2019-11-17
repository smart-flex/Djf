package ru.smartflex.djf.widget;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class MouseListenerButton extends MouseAdapter implements ISFHandler {

    private JButton button;

    public MouseListenerButton(JButton button) {
        this.button = button;

        button.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            if (!button.isEnabled()) {
                button.setEnabled(true);
            }
        }
    }

    @Override
    public void closeHandler() {
        button = null;
    }

}
