package ru.smartflex.djf.widget;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;

import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.controller.ActionManager;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.tool.FormUtil;

public class ActionListenerWidget implements ActionListener, ISFHandler {

    private UIWrapper uiw;
    private WidgetManager wm;

    ActionListenerWidget(JButton button, UIWrapper uiw, WidgetManager wm) {
        this.uiw = uiw;
        this.wm = wm;

        button.addActionListener(this);
    }

    ActionListenerWidget(JCheckBox check, UIWrapper uiw, WidgetManager wm) {
        this.uiw = uiw;
        this.wm = wm;

        check.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (uiw.getWidgetType()) {
            case BUTTON:
                actionPerformedButton();
                break;
            case CHECKBOX:
                actionPerformed();
                break;
        }
    }

    private void actionPerformed() {
        String method = uiw.getAction();
        FormUtil.runActionMethod(wm, method);
    }

    private void actionPerformedButton() {
        String formXMLName = wm.getFormBag().getFormXml();
        SFLogger.activityInfo("*** Activity *** form: ", formXMLName, "; Click on: ", uiw.getUiName(), "; action: ", uiw.getAction());

        ActionManager.doAction(wm, uiw);

    }

    @Override
    public void closeHandler() {
        this.uiw = null;
        this.wm = null;
    }

}
