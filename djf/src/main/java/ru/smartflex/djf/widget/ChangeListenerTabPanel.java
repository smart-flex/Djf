package ru.smartflex.djf.widget;

import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.tool.FormUtil;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ChangeListenerTabPanel implements ChangeListener, ISFHandler {

    private UIWrapper uiw;
    private WidgetManager wm;

    public ChangeListenerTabPanel(JTabbedPane jtp, UIWrapper uiw, WidgetManager wm) {
        this.uiw = uiw;
        this.wm = wm;

        jtp.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof JTabbedPane) {
            JTabbedPane pane = (JTabbedPane) e.getSource();
            Object[] pars = new Integer[1];
            pars[0] = Integer.valueOf(pane.getSelectedIndex());
            String method = uiw.getSelAction();
            FormUtil.runActionMethod(wm, method, pars);
        }
    }

    @Override
    public void closeHandler() {
        this.uiw = null;
        this.wm = null;
    }

}
