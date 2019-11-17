package ru.smartflex.djf.widget;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.UIWrapper;

public class ItemListenerCheckBox implements ItemListener, ISFHandler {

    private UIWrapper uiw;
    private WidgetManager wm;
    private JCheckBox field;
    private FieldFocusListener focusListener;

    ItemListenerCheckBox(JCheckBox field, UIWrapper uiw,
                         WidgetManager wm, FieldFocusListener focusListener) {
        super();
        this.field = field;
        this.uiw = uiw;
        this.wm = wm;
        this.focusListener = focusListener;

        field.addItemListener(this);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

        if (!wm.getFormBag().isFormReady()) {
            return;
        }
        if (!focusListener.isFocus()) {
            // workaround: when on grid is existed column in disabled state. If
            // clicks on this cell several times then on JCheckBox rises
            // ItemEvent
            SFLogger.debug(ItemListenerCheckBox.class,
                    SFConstants.MESSAGE_SWING_ERROR_WA + field.getName());
        } else {
            wm.registerSelectedWrapper(uiw);
            wm.setValueUsualWidget(uiw);
        }
    }

    @Override
    public void closeHandler() {
        field = null;
        uiw = null;
        wm = null;
        focusListener = null;
    }

}
