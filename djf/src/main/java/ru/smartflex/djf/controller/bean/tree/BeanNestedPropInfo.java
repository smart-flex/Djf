package ru.smartflex.djf.controller.bean.tree;

import ru.smartflex.djf.controller.bean.BeanFormDef;
import ru.smartflex.djf.controller.bean.BeanNameProperty;

public class BeanNestedPropInfo {

    private BeanNameProperty[] notNullProperties = null;
    private String parentPropertyName = null;
    private BeanFormDef beanDef = null;

    String getParentPropertyName() {
        return parentPropertyName;
    }

    void setParentPropertyName(String parentPropertyName) {
        this.parentPropertyName = parentPropertyName;
    }

    public BeanFormDef getBeanDef() {
        return beanDef;
    }

    public void setBeanDef(BeanFormDef beanDef) {
        this.beanDef = beanDef;
    }

    BeanNameProperty[] getNotNullProperties() {
        return notNullProperties;
    }

    void setNotNullProperties(BeanNameProperty[] notNullProperties) {
        this.notNullProperties = notNullProperties;
    }

}
