package ru.smartflex.djf.controller.bean.tree;

public class TreeListPair<T, E> {
    private T key = null;
    private E value = null;

    TreeListPair() {
        super();
    }

    TreeListPair(T key, E value) {
        super();
        this.key = key;
        this.value = value;
    }

    T getKey() {
        return key;
    }

    void setKey(T key) {
        this.key = key;
    }

    public E getValue() {
        return value;
    }

    public void setValue(E value) {
        this.value = value;
    }

    public boolean isFilled() {
        return (key != null) && (value != null);
    }
}
