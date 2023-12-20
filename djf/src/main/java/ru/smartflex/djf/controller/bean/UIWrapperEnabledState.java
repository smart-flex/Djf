package ru.smartflex.djf.controller.bean;

import ru.smartflex.djf.FormAssistant;
import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.controller.FormStack;
import ru.smartflex.djf.controller.exception.ObjectCreationException;

class UIWrapperEnabledState {

    private Boolean staticEnableFlag = null;
    private boolean invokeEnabledMethodFromAssistant = false;

    private UIWrapperEnabledState() {
    }

    private UIWrapperEnabledState(Boolean staticEnableFlag, boolean invokeEnabledMethodFromAssistant) {
        this.staticEnableFlag = staticEnableFlag;
        this.invokeEnabledMethodFromAssistant = invokeEnabledMethodFromAssistant;
    }

    /**
     *
     * @param info
     * @param enabledByMouseClick - true только для input item (не в составе грида)
     * @return
     */
    static UIWrapperEnabledState defineInitialStatus(String info, boolean enabledByMouseClick) {
        Boolean staticEnableFlag = null;
        boolean invokeEnabledMethodFromAssistant = false;

        if (info != null) {
            staticEnableFlag = UIWrapper.translateStringToBoolean(info, enabledByMouseClick);

            if (staticEnableFlag == null) {
                // checks dynamic
                if (info.equals(SFConstants.INVOKE_ENABLED_METHOD_ON_ASSISTANT)) {
                    invokeEnabledMethodFromAssistant = true;
                } else {
                    throw new ObjectCreationException(
                            "There is not recognized enabled/disabled rules: "
                                    + info);
                }
            }
        }
        return new UIWrapperEnabledState(staticEnableFlag, invokeEnabledMethodFromAssistant);
    }

    static UIWrapperEnabledState getEditableStaticOffBehavior() {
        return new UIWrapperEnabledState(Boolean.FALSE, false);
    }

    boolean getCurrentState(UIWrapper uiw) {
        boolean state = true;

        if (staticEnableFlag != null) {
            state = staticEnableFlag.booleanValue();
        } else {
            if (invokeEnabledMethodFromAssistant) {
                FormAssistant fa = FormStack.getCurrentFormBag().getAssistant();
                state = fa.enabled(uiw.getBind(), uiw.getUiName());
            }
        }

        return state;
    }

}
