package ru.smartflex.djf.controller;

import java.util.Map;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.FrameHelper;
import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.helper.PrefixUtil;
import ru.smartflex.djf.tool.FormUtil;

public class ActionManager {

    public static void doAction(WidgetManager wm, UIWrapper uiw) {
        if (!uiw.isActionLong()) {
            doActionInt(wm, uiw);
        } else {
            FrameHelper.showWaitLongPanel(uiw.getActionLongMessage());

            ActionManagerThread am = new ActionManagerThread(wm, uiw);
            am.execute();
        }
    }

    static void doActionInt(WidgetManager wm, UIWrapper uiw) {
        String method = uiw.getAction();

        if (FormStack.getCurrentFormBag() != null
                && FormStack.getCurrentFormBag().isFormWasChanged()) {
            Djf.showStatusWarnMessage("${label.djf.message.warn.but_no_actn_allow_formwaschanged}");
            return;
        }

        boolean noHandler = FormUtil.runActionMethod(wm, method);

        if (noHandler) {
            if (uiw.getFormXMLName() != null) {
                noHandler = false;
                Map<String, Object> mapParameters = null;

                String formXMLName = uiw.getFormXMLName();
                if (PrefixUtil.isValueDynamic(formXMLName)) {
                    formXMLName = PrefixUtil.getFormName(formXMLName);
                }

                //noinspection ConstantConditions
                Djf.runForm(formXMLName, mapParameters);
            }
        }

        if (noHandler) {
            SFLogger.debug(ActionManager.class, PrefixUtil.getMsg(
                    "${djf.message.warn.but_no_actn_hndlr}", null));
        }

    }

}
