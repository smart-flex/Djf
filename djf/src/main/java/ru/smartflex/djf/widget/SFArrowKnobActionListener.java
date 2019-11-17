package ru.smartflex.djf.widget;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class SFArrowKnobActionListener implements ActionListener, ISFHandler {

    private SFLazyClicker clicker;
    private JButton button;
    private boolean arrowUp;

    SFArrowKnobActionListener(SFLazyClicker clicker, JButton button,
                              boolean arrowDirection) {
        super();
        this.clicker = clicker;
        this.button = button;
        arrowUp = arrowDirection;

        button.addActionListener(this);
    }

    @Override
    public void closeHandler() {
        button.removeActionListener(this);
        button = null;
        clicker = null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        clicker.doClickInThread(this.arrowUp);
    }

}
