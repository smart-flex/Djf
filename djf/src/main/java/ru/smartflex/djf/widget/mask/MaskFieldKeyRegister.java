package ru.smartflex.djf.widget.mask;

import java.util.concurrent.atomic.AtomicInteger;

public class MaskFieldKeyRegister {
    // если будет серия DELETE Или BACKSPACE то нужен будет стэк?
    private AtomicInteger keyCode = new AtomicInteger(0);

    public void setKeyCode(int code) {
        keyCode.set(code);
    }

    public int getKeyCode() {
        int key = keyCode.get();
        keyCode.set(0);
        return key;
    }
}
