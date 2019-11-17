package ru.smartflex.djf.controller.bean.tree;

import java.io.Serializable;

public class BeanWrapper implements IBeanWrapper, Serializable {

    private static final long serialVersionUID = 8016709901762590202L;

    private BeanStatusEnum statusNewOrPersistent;
    private Object data;

    private BeanStatusEnum statusSession = null;

    public BeanWrapper(Object data, BeanStatusEnum status) {
        super();
        this.data = data;
        this.statusNewOrPersistent = status;
        if (!(status == BeanStatusEnum.NEW || status == BeanStatusEnum.PERSISTENT)) {
            throw new TreeListException(
                    "For object creaing there are only two values: NEW or PERSISTENT");
        }
    }

    public Object getData() {
        return data;
    }

    public boolean isBeanWrapperLocked() {
        boolean fok = false;
        if (statusSession == BeanStatusEnum.LOCKED) {
            fok = true;
        }
        return fok;
    }

    @Override
    public void setupBeanLocked() {
        if (statusNewOrPersistent == BeanStatusEnum.NEW) {
            throw new TreeListException(
                    "For NEW status object can not be locked");
        }
        if (statusSession == BeanStatusEnum.CHANGED) {
            throw new TreeListException(
                    "For CHANGED status object can not be locked");
        }
        if (statusSession == BeanStatusEnum.DELETED) {
            throw new TreeListException(
                    "For DELETED status object can not be locked");
        }
        statusSession = BeanStatusEnum.LOCKED;
    }

    @Override
    public BeanStatusEnum getCreatedStatus() {
        return statusNewOrPersistent;
    }

    @Override
    public BeanStatusEnum getObtainedStatus() {
        return statusSession;
    }

    @Override
    public boolean isBeanWrapperDeleted() {
        boolean fok = false;
        if (statusSession == BeanStatusEnum.DELETED) {
            fok = true;
        }
        return fok;
    }

    @Override
    public boolean changeSelectStatus() {
        boolean fok = false;
        if (statusNewOrPersistent == BeanStatusEnum.PERSISTENT) {
            if (statusSession == null
                    || statusSession == BeanStatusEnum.SELECTED) {
                if (statusSession == null) {
                    statusSession = BeanStatusEnum.SELECTED;
                } else {
                    statusSession = null;
                }
                fok = true;
            }
        }
        return fok;
    }

    @Override
    public void setupBeanChanged() {
        if (statusSession == null) {
            statusSession = BeanStatusEnum.CHANGED;
        }
    }

    @Override
    public boolean changeDeleteStatus() {
        boolean fok = false;
        if (statusNewOrPersistent == BeanStatusEnum.PERSISTENT) {
            if (statusSession == null
                    || statusSession == BeanStatusEnum.DELETED) {
                if (statusSession == null) {
                    statusSession = BeanStatusEnum.DELETED;
                } else {
                    statusSession = null;
                }
                fok = true;
            }
        }
        return fok;
    }

    @Override
    public boolean isBeanWrapperChanged() {
        boolean fok = false;
        if (statusSession != null && statusSession == BeanStatusEnum.CHANGED) {
            fok = true;
        }
        return fok;
    }

    @Override
    public boolean isPossibleMarkAsDelete() {
        boolean fok = false;
        if (statusNewOrPersistent == BeanStatusEnum.PERSISTENT) {
            if (statusSession == null
                    || statusSession == BeanStatusEnum.DELETED) {
                fok = true;
            }
        }
        return fok;
    }

    @Override
    public boolean isBeanWrapperSelected() {
        boolean fok = false;
        if (statusSession == BeanStatusEnum.SELECTED) {
            fok = true;
        }
        return fok;
    }
/*
    @Override
    public void acceptChanges() {
        if (isBeanWrapperChanged()) {
            statusSession = null;
        }
    }
*/

    @Override
    public String getClazzName() {
        return data.getClass().getName();
    }

    @Override
    public boolean isPossibleMarkAsSelected() {
        boolean fok = false;
        if (statusNewOrPersistent == BeanStatusEnum.PERSISTENT) {
            if (statusSession == null
                    || statusSession == BeanStatusEnum.SELECTED) {
                fok = true;
            }
        }
        return fok;
    }

}
