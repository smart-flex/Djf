package ru.smartflex.djf.controller.exception;

import ru.smartflex.djf.SmartFlexException;

public class ObjectCreationException extends SmartFlexException {

    private static final long serialVersionUID = 1786804760247211672L;

    @SuppressWarnings("unused")
    public ObjectCreationException() {
    }

    public ObjectCreationException(String msg) {
        super(msg);
    }

    public ObjectCreationException(String string, Throwable root) {
        super(string, root);
    }

}
