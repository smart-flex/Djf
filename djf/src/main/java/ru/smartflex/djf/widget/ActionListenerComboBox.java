package ru.smartflex.djf.widget;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.UIWrapper;

public class ActionListenerComboBox implements ActionListener, ISFHandler {

    private UIWrapper uiw;
    private WidgetManager wm;
    private SFComboBox field;

    ActionListenerComboBox(WidgetManager wm, UIWrapper uiw,
                           SFComboBox field) {
        this.wm = wm;
        this.uiw = uiw;
        this.field = field;

        field.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!wm.getFormBag().isFormReady()) {
            return;
        }

        if (!field.isStopAction()) {
            wm.setValueUsualWidgetComboBox(uiw);
        }
    }

    @Override
    public void closeHandler() {
        uiw = null;
        wm = null;
        field.removeActionListener(this);
        field = null;
    }

}
