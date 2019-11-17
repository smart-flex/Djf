package ru.smartflex.djf.controller.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JButton;

public class ButtonOperCache {

    private static final String DIAL_SUFFIX = "_dial";

    private Map<String, JButton> map = new HashMap<String, JButton>();
    private Map<String, ButtonStatus> mapStatus = new HashMap<String, ButtonStatus>();

    private Lock lockButton = new ReentrantLock(false);

    private boolean dialog = false;

    public ButtonOperCache() {
    }

    public ButtonOperCache(boolean dialog) {
        this.dialog = dialog;
    }

    public void setButton(String name, JButton but) {
        if (dialog) {
            name += DIAL_SUFFIX;
        }
        map.put(name, but);
        mapStatus.put(name, new ButtonStatus());
    }

    void setButtonEnabled(String name) {
        if (dialog) {
            name += DIAL_SUFFIX;
        }
        setButtonEnabled(name, true);
    }

    void setButtonDisabled(String name) {
        if (dialog) {
            name += DIAL_SUFFIX;
        }
        setButtonEnabled(name, false);
    }

    private void setButtonEnabled(String name, boolean enbl) {
        lockButton.lock();
        try {
            JButton but = map.get(name);
            if (but != null) {
                // can be null in case if in modal form does not use standard
                // operational buttons
                but.setEnabled(enbl);

                mapStatus.get(name).setEnable(enbl);
            }
        } finally {
            lockButton.unlock();
        }
    }

    void setButtonShow(String name) {
        if (dialog) {
            name += DIAL_SUFFIX;
        }
        setButtonVisible(name, true);
    }

    void setButtonHide(String name) {
        if (dialog) {
            name += DIAL_SUFFIX;
        }
        setButtonVisible(name, false);
    }

    private void setButtonVisible(String name, boolean vsbl) {
        lockButton.lock();
        try {
            JButton but = map.get(name);
            if (but != null) {
                but.setVisible(vsbl);

                mapStatus.get(name).setVisible(vsbl);
            }
        } finally {
            lockButton.unlock();
        }
    }

    public ButtonStatus getButtonStatus(String name) {
        if (dialog) {
            name += DIAL_SUFFIX;
        }
        ButtonStatus bs;
        lockButton.lock();
        try {
            bs = mapStatus.get(name);
        } finally {
            lockButton.unlock();
        }
        return bs;
    }

}
