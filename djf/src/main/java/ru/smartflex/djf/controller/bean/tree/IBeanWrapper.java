package ru.smartflex.djf.controller.bean.tree;

public interface IBeanWrapper {

    Object getData();

    boolean isBeanWrapperLocked();

    boolean isBeanWrapperDeleted();

    boolean isBeanWrapperChanged();

    void setupBeanLocked();

    void setupBeanChanged();

    BeanStatusEnum getCreatedStatus();

    BeanStatusEnum getObtainedStatus();

    boolean changeSelectStatus();

    boolean changeDeleteStatus();

    boolean isPossibleMarkAsDelete();

    boolean isBeanWrapperSelected();

    // never used void acceptChanges();

    String getClazzName();

    boolean isPossibleMarkAsSelected();

}
