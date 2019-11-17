package ru.smartflex.djf.widget;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.UIWrapper;

public class FieldFocusListener implements FocusListener, ISFHandler {

    private UIWrapper uiw;
    private WidgetManager wm;
    private boolean focus = false;

    FieldFocusListener(JCheckBox box, UIWrapper uiw, WidgetManager wm) {
        super();
        this.uiw = uiw;
        this.wm = wm;

        box.addFocusListener(this);
    }

    FieldFocusListener(JComboBox box, UIWrapper uiw, WidgetManager wm) {
        super();
        this.uiw = uiw;
        this.wm = wm;

        box.addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (!wm.getFormBag().isFormReady()) {
            return;
        }

        wm.registerSelectedWrapper(uiw);

        focus = true;
    }

    @Override
    public void focusLost(FocusEvent e) {
        focus = false;
    }

    @Override
    public void closeHandler() {
        uiw = null;
        wm = null;
    }

    public boolean isFocus() {
        return focus;
    }

}
