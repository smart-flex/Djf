package ru.smartflex.djf.controller;

import ru.smartflex.djf.FrameHelper;
import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.controller.bean.FormBag;
import ru.smartflex.djf.controller.helper.PrefixUtil;
import ru.smartflex.djf.model.gen.FormType;
import ru.smartflex.djf.model.gen.ModelType;
import ru.smartflex.djf.widget.TaskStatusLevelEnum;

public class FormManager {

    private FormBag formBag;

    public FormManager(FormBag formBag) {
        this.formBag = formBag;

        formBag.setFormManager(this);

        handleForm();
    }

    public FormType getForm() {
        return formBag.getFormType();
    }

    private void handleForm() {

        formBag.initFrameButtons();
        formBag.disableFrameButtons();

        boolean wasError = false;
        boolean emptyModel = false;
        try {
            if (formBag.getFormType().getModels() == null) {
                emptyModel = true;
            } else {
                for (ModelType mt : formBag.getFormType().getModels().getModel()) {
                    formBag.getWidgetManager().makeHierarchyUIItems(mt.getId());
                }
                formBag.getWidgetManager().checkForModel();
            }
        } catch (Exception e) {
            wasError = true;
            SFLogger.error("Error by form creating", e);
        }
        // remove it because of winking
        //formBag.getWidgetManager().disableItems();

        if (emptyModel) {
            FrameHelper.showStatusMessage(TaskStatusLevelEnum.OK, formBag.getWelcomeMessage());
            FrameHelper.hideWaitLongPanel();
            formBag.enableFrameButtons();

            formBag.getWidgetManager().setAllowFocusMovement(true);
            WidgetManagerHelper.requestFocustOnFirstComponent(this);
            formBag.setFormReady(true);

        } else {
            if (wasError) {
                FrameHelper.showStatusMessage(TaskStatusLevelEnum.ERROR, PrefixUtil.getMsg("${djf.message.error.form.creation}", null));
                formBag.registerErrorOnForm();

                FrameHelper.hideWaitLongPanel();
            } else {

                FormManagerThread fm = new FormManagerThread(this);
                fm.execute();
            }
        }
    }

    public WidgetManager getWidgetManager() {
        return formBag.getWidgetManager();
    }

    public FormBag getFormBag() {
        return formBag;
    }

}
