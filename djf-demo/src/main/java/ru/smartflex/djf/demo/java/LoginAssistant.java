package ru.smartflex.djf.demo.java;

import java.util.HashMap;
import java.util.Map;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.FormAssistant;
import ru.smartflex.djf.FormStepEnum;
import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.controller.bean.FormStepResult;
import ru.smartflex.djf.controller.bean.IFormSession;

public class LoginAssistant extends FormAssistant {

    @Override
    public FormStepResult step(FormStepEnum step, IFormSession formSess) {

        if (step == FormStepEnum.BEFORE_CREATE) {
            //noinspection CatchMayIgnoreException
            try {
                // simulate checking connection or any long process
                Thread.sleep(10);
            } catch (Exception e) {
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    public void login() {

        // read current bean
        CredentialInfo ci = (CredentialInfo) Djf.getCurrentObject();
        SFLogger.debug(LoginAssistant.class, ci);

        if (ci.isFilled()) {
            // because credentials are filled we suppose that all ok and then allow to run next form

            // without this map in GridFields.frm we will get restriction in view of fields
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("itemsAccess", "sfSpecialService");

            // run next form
            Djf.runForm("ru/smartflex/djf/demo/xml/GridFields.frm.xml", map);
        } else {
            Djf.showStatusErrorMessage("${demo.login.msg.nologin}");
        }
    }

}
