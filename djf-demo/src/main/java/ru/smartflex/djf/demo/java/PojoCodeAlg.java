package ru.smartflex.djf.demo.java;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PojoCodeAlg implements Serializable {

    private static final long serialVersionUID = -4941789692303535026L;

    private static AtomicInteger id = new AtomicInteger(0);

    private Integer idAlg;
    private String nameAlg = null;

    private List<PojoCodeAlgParam> parameterList = new ArrayList<PojoCodeAlgParam>(
            0);

    @SuppressWarnings("unused")
    public PojoCodeAlg() {
        this.idAlg = id.incrementAndGet();
    }

    @SuppressWarnings("WeakerAccess")
    public PojoCodeAlg(String nameAlg) {
        super();
        this.idAlg = id.incrementAndGet();
        this.nameAlg = nameAlg;
    }

    @SuppressWarnings("unused")
    public Integer getIdAlg() {
        return idAlg;
    }

    @SuppressWarnings("unused")
    public void setIdAlg(Integer idAlg) {
        this.idAlg = idAlg;
    }

    @SuppressWarnings("unused")
    public String getNameAlg() {
        return nameAlg;
    }

    @SuppressWarnings("unused")
    public void setNameAlg(String nameAlg) {
        this.nameAlg = nameAlg;
    }

    @Override
    public String toString() {
        return "PojoCodeAlg [idAlg=" + idAlg + ", nameAlg=" + nameAlg + "]";
    }

    @SuppressWarnings("WeakerAccess")
    public List<PojoCodeAlgParam> getParameterList() {
        return parameterList;
    }

    @SuppressWarnings("unused")
    public void setParameterList(List<PojoCodeAlgParam> parameterList) {
        this.parameterList = parameterList;
    }

}
