package ru.smartflex.djf.widget.mask;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MaskFieldKeyRegister {
    // если будет серия DELETE Или BACKSPACE то нужен будет стэк?
    private AtomicInteger keyCode = new AtomicInteger(0);
    private AtomicBoolean ctrlVPressed = new AtomicBoolean(false);

    public void setDeleteKeyCode(int code) {
        keyCode.set(code);
    }

    public int getDeleteKeyCode() {
        int key = keyCode.get();
        keyCode.set(0);
        return key;
    }

    public void registerCtrlVPressed() {
        ctrlVPressed.set(true);
    }

    public boolean isCtrlVWasPressed() {
        boolean pressed = ctrlVPressed.get();
        ctrlVPressed.set(false);
        return pressed;
    }
}
