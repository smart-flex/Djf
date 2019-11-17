package ru.smartflex.djf.widget;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import ru.smartflex.djf.controller.bean.FormBag;

public class SFGroup extends JPanel {

    private static final long serialVersionUID = 8604146802084069270L;

    private GridLayout gl = new GridLayout(0, 1);
    private ButtonGroup group = new ButtonGroup();
    private String actionCommand = null;

    public SFGroup() {
        super();
        init();
    }

    private void init() {
        this.setLayout(gl);
    }

    public JRadioButton addRadio(String text, String command, boolean selected) {
        JRadioButton btn = new JRadioButton(text);
        btn.setActionCommand(command);
        btn.setSelected(selected);

        group.add(btn);

        this.add(btn);

        return btn;
    }

    public String getActionCommand() {
        return actionCommand;
    }

    public void setActionCommand(String actionCommand) {
        this.actionCommand = actionCommand;
    }

    public void closeHandler() {
        Enumeration<AbstractButton> en = group.getElements();
        while (en.hasMoreElements()) {
            AbstractButton b = en.nextElement();
            KeyListener[] kl = b.getKeyListeners();
            FormBag.closeHandler(kl);
            ActionListener[] al = b.getActionListeners();
            FormBag.closeHandler(al);
        }
    }
}
