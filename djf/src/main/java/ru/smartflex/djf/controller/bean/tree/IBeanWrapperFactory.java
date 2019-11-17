package ru.smartflex.djf.controller.bean.tree;

public interface IBeanWrapperFactory {

    IBeanWrapper createBeanWrappper(Object data, BeanStatusEnum status);

}
