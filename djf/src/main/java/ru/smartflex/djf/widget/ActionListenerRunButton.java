package ru.smartflex.djf.widget;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.FormAssistant;
import ru.smartflex.djf.IParameterMaker;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.helper.PrefixUtil;

public class ActionListenerRunButton implements ActionListener, ISFHandler {

    private UIWrapper uiw;
    private WidgetManager wm;

    ActionListenerRunButton(JButton button, UIWrapper uiw,
                            WidgetManager wm) {
        this.uiw = uiw;
        this.wm = wm;

        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Map<String, Object> mapParameters = null;

        FormAssistant assist = wm.getFormBag().getAssistant();
        if (assist != null) {
            if (assist instanceof IParameterMaker) {
                IParameterMaker pm = (IParameterMaker) assist;
                mapParameters = pm.makeParametersMap();
            }
        }

        String formXMLName = uiw.getFormXMLName();
        if (PrefixUtil.isValueDynamic(formXMLName)) {
            formXMLName = PrefixUtil.getFormName(formXMLName);
        }
        Djf.runForm(formXMLName, mapParameters);
    }

    @Override
    public void closeHandler() {
        this.uiw = null;
        this.wm = null;
    }
}
