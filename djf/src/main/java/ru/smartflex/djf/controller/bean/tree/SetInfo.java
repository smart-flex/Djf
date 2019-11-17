package ru.smartflex.djf.controller.bean.tree;

public class SetInfo {

    private String setPropertyName;
    private String setPropertyPathName;
    @SuppressWarnings("rawtypes")
    private Class beanClassInSet;

    SetInfo(String setPropertyName, String setPropertyPathName,
            @SuppressWarnings("rawtypes") Class beanClassInSet) {
        super();
        this.setPropertyName = setPropertyName;
        this.setPropertyPathName = setPropertyPathName;
        this.beanClassInSet = beanClassInSet;
    }

    String getSetPropertyName() {
        return setPropertyName;
    }

    String getSetPropertyPathName() {
        return setPropertyPathName;
    }

    @SuppressWarnings("rawtypes")
    Class getBeanClassInSet() {
        return beanClassInSet;
    }

    @Override
    public String toString() {
        return "SetInfo [setPropertyName=" + setPropertyName
                + ", setPropertyPathName=" + setPropertyPathName
                + ", beanClassInSet=" + beanClassInSet + "]";
    }
}
