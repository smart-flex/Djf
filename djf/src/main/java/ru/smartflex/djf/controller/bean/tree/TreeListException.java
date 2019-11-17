package ru.smartflex.djf.controller.bean.tree;

public class TreeListException extends RuntimeException {

    private static final long serialVersionUID = -1829002934086275169L;

    @SuppressWarnings("unused")
    public TreeListException() {
    }

    TreeListException(String msg) {
        super(msg);
    }

    TreeListException(String string, Throwable root) {
        super(string, root);
    }
}
