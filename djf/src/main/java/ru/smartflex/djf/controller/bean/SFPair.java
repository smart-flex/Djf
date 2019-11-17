package ru.smartflex.djf.controller.bean;

public class SFPair<N, V> {
    private N name = null;
    private V value = null;

    public SFPair() {
        super();
    }

    public SFPair(N name, V value) {
        super();
        this.name = name;
        this.value = value;
    }

    public N getName() {
        return name;
    }

    public void setName(N name) {
        this.name = name;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

}
