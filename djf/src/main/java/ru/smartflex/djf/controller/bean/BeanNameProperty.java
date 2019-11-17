package ru.smartflex.djf.controller.bean;

public class BeanNameProperty {

    private String name = null;
    private String humanName = null;

    @SuppressWarnings("unused")
    public BeanNameProperty() {
        super();
    }

    BeanNameProperty(String name, String humanName) {
        super();
        this.name = name;
        this.humanName = humanName;
    }

    public String getName() {
        return name;
    }

    public String getHumanName() {
        return humanName;
    }

}
