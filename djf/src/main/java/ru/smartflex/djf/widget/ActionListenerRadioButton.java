package ru.smartflex.djf.widget;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButton;

public class ActionListenerRadioButton implements ActionListener, ISFHandler {

    private SFGroup group;

    public ActionListenerRadioButton(SFGroup group, JRadioButton button) {
        super();
        this.group = group;

        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        group.setActionCommand(cmd);
    }

    @Override
    public void closeHandler() {
        group = null;
    }
}
