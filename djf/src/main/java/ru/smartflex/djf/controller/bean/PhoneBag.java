package ru.smartflex.djf.controller.bean;

public class PhoneBag {
    private String phoneFormatted = null;
    private PhoneZone phoneZone = null;
    private boolean fullNumber = false;

    public PhoneBag(String phoneFormatted, PhoneZone phoneZone, boolean fullNumber) {
        this.phoneFormatted = phoneFormatted;
        this.phoneZone = phoneZone;
        this.fullNumber = fullNumber;
    }

    public PhoneBag(String phoneFormatted) {
        this.phoneFormatted = phoneFormatted;
    }

    public String getPhoneFormatted() {
        return phoneFormatted;
    }

    public PhoneZone getPhoneZone() {
        return phoneZone;
    }

    public boolean isFullNumber() {
        return fullNumber;
    }

    @Override
    public String toString() {
        return "PhoneBag{" +
                "phoneFormatted='" + phoneFormatted + '\'' +
                ", phoneZone=" + phoneZone +
                '}';
    }
}
