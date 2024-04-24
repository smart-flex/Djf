package ru.smartflex.djf.widget;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.controller.bean.UIWrapper;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextField;

public class MouseListenerText extends MouseAdapter implements ISFHandler {

    private UIWrapper wrapper = null;
    private JTextField field;

    public MouseListenerText(UIWrapper wrapper) {
        super();
        this.wrapper = wrapper;
        this.field = (JTextField) wrapper.getObjectUI();

        field.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Djf.enableWidget(wrapper.getUiName());
        }
    }

    @Override
    public void closeHandler() {
        field = null;
    }

}
