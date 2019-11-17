package ru.smartflex.djf.widget.template;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;

import ru.smartflex.djf.widget.IForm;

@SuppressWarnings("unused")
public class FormUI extends JInternalFrame implements IForm {

    private static final long serialVersionUID = -7566567283656078011L;

    public FormUI() {
        super("", true, true, true, true);
        this.setLayout(new BorderLayout());
        this.setVisible(true);
    }
}
