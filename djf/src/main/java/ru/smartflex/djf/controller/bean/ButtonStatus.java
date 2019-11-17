package ru.smartflex.djf.controller.bean;

public class ButtonStatus {
    private boolean visible = true;
    private boolean enable = true;

    ButtonStatus() {
        super();
    }

    @SuppressWarnings("unused")
    ButtonStatus(boolean visible, boolean enable) {
        super();
        this.visible = visible;
        this.enable = enable;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isEnable() {
        return enable;
    }

    void setEnable(boolean enable) {
        this.enable = enable;
    }

}
