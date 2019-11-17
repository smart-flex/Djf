package ru.smartflex.djf.widget.grid;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.widget.ISFHandler;

public class GridFocusListener implements FocusListener, ISFHandler {

    private UIWrapper uiw;
    private WidgetManager wm;

    GridFocusListener(WidgetManager wm, UIWrapper uiw) {
        this.wm = wm;
        this.uiw = uiw;
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (!SFGridMouseMotionFlag.isMotion()) {
            SFLogger.debug(GridFocusListener.class, "Grid focus gained: ", uiw.getUiName());
            wm.registerSelectedWrapper(uiw);
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
    }

    @Override
    public void closeHandler() {
        uiw = null;
        wm = null;
    }

}
