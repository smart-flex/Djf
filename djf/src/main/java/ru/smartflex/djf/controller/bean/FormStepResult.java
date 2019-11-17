package ru.smartflex.djf.controller.bean;

import ru.smartflex.djf.controller.helper.PrefixUtil;

public class FormStepResult {

    private boolean stepOk = false;
    private String stepErrorMessage = null;
    private Exception exception = null;

    public boolean isStepOk() {
        return stepOk;
    }

    @SuppressWarnings("unused")
    public void setStepOk(boolean stepOk) {
        this.stepOk = stepOk;
    }

    public String getStepErrorMessage() {
        return stepErrorMessage;
    }

    @SuppressWarnings("unused")
    public void setStepErrorMessage(String stepErrorMessage) {
        this.stepErrorMessage = stepErrorMessage;
    }

    @SuppressWarnings("unused")
    public void setStepErrorMessageByLink(String link) {
        this.stepErrorMessage = PrefixUtil.getMsg("${" + link + "}", null);
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

}
