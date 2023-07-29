package ru.smartflex.djf.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.FormAssistant;
import ru.smartflex.djf.FormStepEnum;
import ru.smartflex.djf.FrameHelper;
import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.builder.BeanBuilder;
import ru.smartflex.djf.builder.FormBuilder;
import ru.smartflex.djf.controller.bean.BeanFormDef;
import ru.smartflex.djf.controller.bean.FormBag;
import ru.smartflex.djf.controller.bean.FormStepResult;
import ru.smartflex.djf.controller.bean.IFormSession;
import ru.smartflex.djf.controller.bean.SimpleFormSession;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.helper.PrefixUtil;
import ru.smartflex.djf.controller.helper.UnMarshalHelper;
import ru.smartflex.djf.model.gen.FormType;
import ru.smartflex.djf.tool.OtherUtil;
import ru.smartflex.djf.widget.ActionListenerWidget;
import ru.smartflex.djf.widget.IForm;
import ru.smartflex.djf.widget.SFDialogForm;
import ru.smartflex.djf.widget.TaskStatusLevelEnum;

public class FormStack {

    private static List<FormBag> formStack = new ArrayList<FormBag>();
    private static Lock lockFormStack = new ReentrantLock(false);

    private FormStack() {
    }

    public static void createForm(String formXmlBody,
                                  Map<String, Object> formParameters) {

        if (formXmlBody == null) {
            Djf.showStatusWarnMessage("${label.djf.message.warn.no_form_is_there}");
            return;
        }

        if (getCurrentFormBag() != null
                && getCurrentFormBag().isFormWasChanged()) {
            Djf.showStatusWarnMessage("${label.djf.message.warn.but_no_actn_allow_formwaschanged}");
            SFLogger.debug(ActionListenerWidget.class, PrefixUtil.getMsg(
                    "${djf.message.warn.but_no_actn_allow_formwaschanged}",
                    null));
            return;
        }
        // FrameHelper.showWaitLongPanel();

        if (formParameters == null) {
            // for case when map is empty, but you have need to initialize own
            // parameters in BEFORE_CREATE step section
            formParameters = new HashMap<String, Object>();
        }

        InputStream isForm;

        boolean showFormAllowed = true;
        FormType form = null;
        WidgetManager wm;
        UIWrapper formWrapper = null;
        FormBag formBag = null;
        try {
            isForm = OtherUtil.getBodyAsStream(Djf.getConfigurator()
                    .getPathToForm(), formXmlBody);

            // For each form. There can be exception. Therefore engine must show
            // error and go to rollback.
            form = UnMarshalHelper.unmarshalForm(isForm);
            wm = new WidgetManager(formParameters);

            FormStepResult stepResult = null;
            FormAssistant assist = FormBuilder.createAssistant(form);
            IFormSession formSession = new SimpleFormSession(form,
                    formParameters);
            if (assist != null) {
                stepResult = assist.step(FormStepEnum.BEFORE_CREATE,
                        formSession);
            }

            if (stepResult == null || stepResult.isStepOk()) {

                BeanFormDef beanDef = BeanBuilder.build(form);

                formWrapper = FormBuilder.build(form, wm, beanDef);
                formBag = new FormBag(form, wm, formWrapper, assist,
                        formSession, beanDef, formXmlBody);
            } else {
                showFormAllowed = false;
                if (stepResult.getStepErrorMessage() != null) {
                    FrameHelper.showStatusMessage(TaskStatusLevelEnum.ERROR,
                            stepResult.getStepErrorMessage());
                } else {
                    FrameHelper
                            .showStatusMessage(
                                    TaskStatusLevelEnum.ERROR,
                                    PrefixUtil
                                            .getMsg("${djf.message.error.form.creation}",
                                                    null));
                }
            }
        } catch (Exception e) {
            showFormAllowed = false;
            SFLogger.error("Error by form building", e);
            FrameHelper.showStatusMessage(TaskStatusLevelEnum.ERROR, PrefixUtil
                    .getMsg("${djf.message.error.form.creation}", null));

            // Rollback must be here. Previous form has to be ready to work.
        }

        if (showFormAllowed) {

            // parameters passing
            formBag.setMapParameters(formParameters);

            if (form.isModal() == null
                    || !form.isModal()) {

                FrameHelper.showWaitLongPanel();

                Djf.getConfigurator().getFrame()
                        .addForm((IForm) formWrapper.getObjectUI());

                // stepsAfterFormShow(formBag);

                registerForm(formBag);

                showDescription();

                new FormManager(formBag);

            } else {
                FormBag fbCurr = getCurrentFormBag();
                if (fbCurr != null) {
                    if (fbCurr.isFormModal()) {
                        FrameHelper
                                .showStatusMessage(
                                        TaskStatusLevelEnum.ERROR,
                                        PrefixUtil
                                                .getMsg("${djf.message.error.form.modal_already}",
                                                        null));
                        return;
                    }
                }

                SFDialogForm dial = (SFDialogForm) formWrapper.getObjectUI();
                dial.setFormBag(formBag);

                formBag.showDialog();

                // ((ISFDialog) formWrapper.getObjectUI()).show(null);
            }

            /*
             * } else { FrameHelper.hideWaitLongPanel();
             */
        }

    }

    private static void showDescription() {
        String msgMain = getCurrentFormBag().getDescription();
        String msgAdd = null;
        if (getCurrentFormBag().getAssistant() != null) {
            getCurrentFormBag().getAssistant().step(
                    FormStepEnum.BEFORE_READ_DATA,
                    getCurrentFormBag().getFormSession());
            msgAdd = getCurrentFormBag().getAssistant()
                    .getAdditionalFormMessage();
            if (getCurrentFormBag().getAssistant().getCustomDescription() != null) {
                msgMain = getCurrentFormBag().getAssistant()
                        .getCustomDescription();
            }
        }
        Djf.getConfigurator().getFrame().showDescription(msgMain, msgAdd);

    }

    public static void registerForm(FormBag formBag) {
        lockFormStack.lock();
        try {
            formStack.add(formBag);
        } finally {
            lockFormStack.unlock();
        }
    }

    /*
     * @Deprecated public static void stepsAfterFormShow(FormBag formBag) {
     * formStack.add(formBag);
     *
     * new FormManager(formBag);
     *
     * // formStack.add(formBag); }
     */
    public static FormBag getCurrentFormBag() {
        FormBag fb = null;

        lockFormStack.lock();
        try {
            int indexCurrent = formStack.size() - 1;
            if (indexCurrent >= 0) {
                fb = formStack.get(indexCurrent);
            }
        } finally {
            lockFormStack.unlock();
        }
        return fb;
    }

    public static void removeCurrentForm() {
        lockFormStack.lock();
        try {
            int indexCurrent = formStack.size() - 1;
            FormBag currentForm = formStack.get(indexCurrent);
            String welcomeForParent = currentForm
                    .getWelcomeMessageForParentForm();
            TaskStatusLevelEnum taskStatusLevelEnumForParentForm = currentForm.getTaskStatusLevelEnumForParentForm();
            boolean closeFrame = false;
            boolean formWasRefreshed = false;
            //noinspection ConstantConditions
            if (indexCurrent >= 0) {
                FormBag previousForm = getPreviousFormRecursive(indexCurrent);
                if (previousForm == null) {
                    closeFrame = true;
                } else {
                    ((JFrame) Djf.getConfigurator().getFrame()).repaint();
                    previousForm.initFrameButtons();
                    if (currentForm.isParentHasToBeRefreshed()) {
                        if (currentForm.isFormWasSaved()) {
                            if (previousForm.isParentHasToBeRefreshed()) {
                                // pass saved flag for next cascade refreshing
                                previousForm.setFormWasSaved(true);
                            }
                            // refresh parent form
                            previousForm.refeshForm(welcomeForParent, taskStatusLevelEnumForParentForm);
                            formWasRefreshed = true;
                        }
                    }

                    showDescription();

                    if (!currentForm.isParentHasToBeRefreshed()) {
                        if (welcomeForParent != null) {
                            FrameHelper.showStatusMessage(
                                    taskStatusLevelEnumForParentForm, welcomeForParent);
                        } else {
                            FrameHelper.showStatusMessage(
                                    TaskStatusLevelEnum.OK,
                                    previousForm.getWelcomeMessage());
                        }
                    }
                    if (currentForm.isForceRefreshForParentForm()) {
                        if (!formWasRefreshed) {
                            // pass flag to parent form
                            previousForm.setForceRefreshForParentForm(currentForm.isForceRefreshForParentForm());
                            previousForm.refeshForm(welcomeForParent);
                        }
                    }
                }

            } else {
                // first form was crashed
                closeFrame = true;
            }
            if (closeFrame) {
                Djf.closeFrame();
            }
        } finally {
            lockFormStack.unlock();
        }

    }

    private static FormBag getPreviousFormRecursive(int index) {
        FormBag fb = null;

        if (index < formStack.size() && index >= 0) {
            FormBag currentForm = formStack.get(index);
            currentForm.invokeBeforeRemoveStep();

            formStack.remove(index);

            Djf.getConfigurator()
                    .getFrame()
                    .removeForm(
                            (IForm) currentForm.getFormWrapper().getObjectUI());

            currentForm.closeForm();

            index--;

            if (index < formStack.size() && index >= 0) {
                FormBag prevForm = formStack.get(index);
                if (!prevForm.isOnlyOnceTime()) {
                    fb = prevForm;
                } else {
                    fb = getPreviousFormRecursive(index);
                }
            }

        }
        return fb;
    }

}
