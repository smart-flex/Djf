package ru.smartflex.djf.builder;

import java.awt.LayoutManager;
import java.util.List;

import ru.smartflex.djf.FormAssistant;
import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.BeanFormDef;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.exception.MissingException;
import ru.smartflex.djf.controller.helper.ObjectCreator;
import ru.smartflex.djf.model.gen.AssistType;
import ru.smartflex.djf.model.gen.FormType;
import ru.smartflex.djf.model.gen.ModelType;
import ru.smartflex.djf.model.gen.PanelType;

public class FormBuilder {

    private FormBuilder() {
    }

    public static UIWrapper build(FormType form, WidgetManager wm,
                                  BeanFormDef beanDef) {

        validateForm(form);

        UIWrapper formWrapper = new UIWrapper();

        Object uiForm = ObjectCreator.createForm(form.isModal());
        formWrapper.setObjectUI(uiForm);

        UIWrapper layout = LayoutBuilder.build(form.getLayout(), wm);
        if (layout.getObjectUI() != null) {
            ((java.awt.Container) uiForm).setLayout((LayoutManager) layout
                    .getObjectUI());
        }

        List<PanelType> panelList = form.getPanel();
        if (panelList.size() == 0) {
            SFLogger.warn(FormBuilder.class, "There is no panel for form");
        }

        PanelBuilder.buildPanels(uiForm, panelList, wm, beanDef, null);

        ((java.awt.Container) uiForm).setFocusTraversalPolicy(wm
                .getFocusPolicy());

        return formWrapper;
    }

    public static FormAssistant createAssistant(FormType form) {
        FormAssistant obj = null;
        AssistType assist = form.getAssist();
        if (assist != null) {
            String className = assist.getClazz();
            obj = (FormAssistant) ObjectCreator
                    .createObjectWithoutParameter(className);
        }
        return obj;
    }

    private static void validateForm(FormType form) {
        if (form.getModels() == null) {
            return;
        }

        List<ModelType> modelList = form.getModels().getModel();
        for (ModelType mt : modelList) {
            if (mt.getId() == null) {
                throw new MissingException("Model must has own Id");
            }
            if (mt.getBean() == null) {
                throw new MissingException("Model must has bean definition");
            }
            boolean selfCreated = false;
            if (mt.getBean().isSelfCreated() != null) {
                selfCreated = mt.getBean().isSelfCreated();
            }
            if (!selfCreated) {
                if (mt.getLoad() == null) {
                    throw new MissingException("Model must has load section (for bean class: \"" + mt.getBean().getClazz() + "\"");
                }
                if (mt.getLoad().getMethod() == null) {
                    throw new MissingException(
                            "Model must has load method attribute (for bean class: \"" + mt.getBean().getClazz() + "\"");
                }
            }
        }
    }
}
