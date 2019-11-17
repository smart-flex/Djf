package ru.smartflex.djf.controller.exception;

import ru.smartflex.djf.SmartFlexException;

public class FormParseXMLException extends SmartFlexException {

    private static final long serialVersionUID = 7688611754696651291L;

    @SuppressWarnings("unused")
    public FormParseXMLException() {
    }

    public FormParseXMLException(String msg) {
        super(msg);
    }

    public FormParseXMLException(String string, Throwable root) {
        super(string, root);
    }

}
