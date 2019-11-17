package ru.smartflex.djf;

public class SmartFlexException extends RuntimeException {

    private static final long serialVersionUID = 3392433390699730016L;

    public SmartFlexException() {
    }

    public SmartFlexException(String msg) {
        super(msg);
    }

    public SmartFlexException(String string, Throwable root) {
        super(string, root);
    }

}
