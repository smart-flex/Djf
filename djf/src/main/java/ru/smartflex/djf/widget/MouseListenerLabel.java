package ru.smartflex.djf.widget;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.tool.FormUtil;

public class MouseListenerLabel extends MouseAdapter implements ISFHandler {

    private UIWrapper uiw;
    private WidgetManager wm;

    public MouseListenerLabel(JLabel label, UIWrapper uiw, WidgetManager wm) {
        super();
        this.uiw = uiw;
        this.wm = wm;

        label.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            String method = uiw.getMouseDoubleClickAction();
            FormUtil.runActionMethod(wm, method);
        }
    }

    @Override
    public void closeHandler() {
        this.uiw = null;
        this.wm = null;
    }
}
