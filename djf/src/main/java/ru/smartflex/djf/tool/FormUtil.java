package ru.smartflex.djf.tool;

import org.apache.commons.beanutils.MethodUtils;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.FormAssistant;
import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.controller.FormStack;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.ButtonStatus;
import ru.smartflex.djf.controller.bean.FormBag;
import ru.smartflex.djf.widget.SFDialogForm;

public class FormUtil {

    private FormUtil() {
    }

    public static void exitForm() {
        FormBag fb = FormStack.getCurrentFormBag();
        SFLogger.activityInfo("exit form: ", fb.getFormXml());
        removeForm(fb);
    }

    public static void exitFormViaButonStatus() {
        ButtonStatus bs = null;
        try {
            bs = Djf.getConfigurator().getButtonCache()
                    .getButtonStatus(SFConstants.BUTTON_NAME_EXIT);
        } catch (NullPointerException e) {
            // the case when first form is not created (for example: server's
            // trouble
            Djf.closeFrame();
        }
        if (bs != null && bs.isVisible() && bs.isEnable()) {
            FormUtil.exitForm();
        }
    }

    public static void closeForm(String welcomeForParent, boolean forceRefresh) {
        FormBag fb = FormStack.getCurrentFormBag();
        SFLogger.activityInfo("close form: ", fb.getFormXml());
        fb.setFormWasSaved(true);
        fb.setWelcomeMessageForParentForm(welcomeForParent);
        fb.setForceRefreshForParentForm(forceRefresh);
        removeForm(fb);
    }

    private static void removeForm(FormBag fb) {
        if (!fb.isFormModal()) {
            FormStack.removeCurrentForm();
        } else {
            SFDialogForm dial = (SFDialogForm) FormStack.getCurrentFormBag()
                    .getFormWrapper().getObjectUI();
            dial.closeDialog();
        }
    }

    public static boolean runActionMethod(WidgetManager wm, String method) {
        return runActionMethod(wm, method, null,  false);
    }

    public static boolean runActionMethod(WidgetManager wm, String method, Object[] params) {
        return runActionMethod(wm, method, params, true);
    }

    private static boolean runActionMethod(WidgetManager wm, String method, Object[] params, boolean withPars) {
        if (method == null || wm.getFormBag() == null) {
            return true;
        }

        boolean noHandler = true;

        FormAssistant assist = wm.getFormBag().getAssistant();
        if (assist != null) {
            Object[] pars = null;
            noHandler = false;
            try {
                //noinspection ConstantConditions
                if (withPars) {
                    MethodUtils.invokeMethod(assist, method, params);
                } else {
                    MethodUtils.invokeExactMethod(assist, method, pars);
                }
            } catch (Exception ee) {
                SFLogger.error("Error by invoke assist method", ee);
                Djf.showStatusErrorMessage("${djf.message.error.form.action}");
            }
        } else {
            Djf.showStatusWarnMessage("${djf.message.warn.no_assistant_class}");
        }

        return noHandler;
    }

    public static void saveForm() {
        ButtonStatus bs = Djf.getConfigurator().getButtonCache()
                .getButtonStatus(SFConstants.BUTTON_NAME_SAVE);
        if (bs.isVisible() && bs.isEnable()) {
            FormBag fb = FormStack.getCurrentFormBag();
            fb.saveForm();

            if (fb.isCloseOnSave()) {
                if (fb.isFormWasSaved()) {
                    closeForm(null, false);
                }
            }
        }
    }
}
