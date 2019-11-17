package ru.smartflex.djf.controller.exception;

import ru.smartflex.djf.SmartFlexException;

public class PropertyNotFilledException extends SmartFlexException {

    private static final long serialVersionUID = -5514595163090209370L;

    private String notNullProps = null;

    @SuppressWarnings("unused")
    public PropertyNotFilledException() {
        super();
    }

    @SuppressWarnings("unused")
    public PropertyNotFilledException(String string, Throwable root) {
        super(string, root);
    }

    public PropertyNotFilledException(String msg) {
        super(msg);
    }

    public String getNotNullProps() {
        return notNullProps;
    }

    public void setNotNullProps(String notNullProps) {
        this.notNullProps = notNullProps;
    }

}
