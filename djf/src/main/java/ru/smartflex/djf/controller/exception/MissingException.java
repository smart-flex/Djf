package ru.smartflex.djf.controller.exception;

import ru.smartflex.djf.SmartFlexException;

public class MissingException extends SmartFlexException {

    private static final long serialVersionUID = 6074111834643492571L;

    public MissingException() {
    }

    public MissingException(String msg) {
        super(msg);
    }

    public MissingException(String string, Throwable root) {
        super(string, root);
    }

}
