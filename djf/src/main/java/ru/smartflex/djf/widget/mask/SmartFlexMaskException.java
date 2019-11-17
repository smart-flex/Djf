package ru.smartflex.djf.widget.mask;

import ru.smartflex.djf.SmartFlexException;

public class SmartFlexMaskException extends SmartFlexException {

    private static final long serialVersionUID = 8713430655453467486L;

    @SuppressWarnings("unused")
    public SmartFlexMaskException() {
    }

    public SmartFlexMaskException(String msg) {
        super(msg);
    }

    @SuppressWarnings("unused")
    public SmartFlexMaskException(String string, Throwable root) {
        super(string, root);
    }
}
