package ru.smartflex.djf.controller.bean;

public class SnilsBag {
    private String snilsFormatted = null;
    private boolean fullNumber = false;

    public SnilsBag(String snilsFormatted, boolean fullNumber) {
        this.snilsFormatted = snilsFormatted;
        this.fullNumber = fullNumber;
    }

    public String getSnilsFormatted() {
        return snilsFormatted;
    }

    public boolean isFullNumber() {
        return fullNumber;
    }

}
