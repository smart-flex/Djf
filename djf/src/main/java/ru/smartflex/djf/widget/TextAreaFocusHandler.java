package ru.smartflex.djf.widget;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.text.JTextComponent;

import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.UIWrapper;

public class TextAreaFocusHandler implements FocusListener, ISFHandler {

    private UIWrapper uiw;
    private WidgetManager wm;


    public TextAreaFocusHandler(JTextComponent text, UIWrapper uiw, WidgetManager wm) {
        super();
        this.uiw = uiw;
        this.wm = wm;

        text.addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (!wm.getFormBag().isFormReady()) {
            return;
        }

        wm.registerSelectedWrapper(uiw);
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (!wm.getFormBag().isFormReady()) {
            return;
        }

        wm.setValueUsualWidget(uiw);
    }

    @Override
    public void closeHandler() {
        uiw = null;
        wm = null;
    }

}
