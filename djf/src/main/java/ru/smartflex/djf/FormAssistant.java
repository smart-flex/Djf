package ru.smartflex.djf;

import ru.smartflex.djf.controller.bean.FormStepResult;
import ru.smartflex.djf.controller.bean.IFormSession;
import ru.smartflex.djf.controller.bean.tree.BeanStatusEnum;
import ru.smartflex.djf.controller.bean.tree.BeanWrapper;
import ru.smartflex.djf.controller.bean.tree.IBeanWrapper;
import ru.smartflex.djf.controller.bean.tree.IBeanWrapperFactory;

public abstract class FormAssistant implements IBeanWrapperFactory {

    private String additionalFormMessage = null;
    private String customDescription = null;

    /**
     * These steps should be short because invoking is not wrapped by blinking windows
     */
    public abstract FormStepResult step(FormStepEnum step, IFormSession formSess);

    public IBeanWrapper createBeanWrappper(Object data, BeanStatusEnum status) {
        return new BeanWrapper(data, status);
    }

    /**
     * Dynamically defined enable widget or no
     */
    public boolean enabled(String bind, String idWidget) {
        return true;
    }

    public String getAdditionalFormMessage() {
        return additionalFormMessage;
    }

    @SuppressWarnings("unused")
    public void setAdditionalFormMessage(String additionalFormMessage) {
        this.additionalFormMessage = additionalFormMessage;
    }

    public String getCustomDescription() {
        return customDescription;
    }

    @SuppressWarnings("unused")
    public void setCustomDescription(String customDescription) {
        this.customDescription = customDescription;
    }

    public void beanWasChanged(@SuppressWarnings("unused") IBeanWrapper bw) {

    }

}
