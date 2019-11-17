package ru.smartflex.djf.demo.java;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class PojoCodeAlgParam implements Serializable {

    private static final long serialVersionUID = -3415637918488928692L;

    private static AtomicInteger id = new AtomicInteger(0);

    private Integer idAlgParam;
    private String nameParameter = null;
    private BigDecimal value = null;

    @SuppressWarnings("unused")
    public PojoCodeAlgParam() {
        super();
        this.idAlgParam = id.incrementAndGet();
    }

    @SuppressWarnings("WeakerAccess")
    public PojoCodeAlgParam(String nameParameter) {
        super();
        this.idAlgParam = id.incrementAndGet();
        this.nameParameter = nameParameter;
    }

    @SuppressWarnings("unused")
    public Integer getIdAlgParam() {
        return idAlgParam;
    }

    @SuppressWarnings("unused")
    public void setIdAlgParam(Integer idAlgParam) {
        this.idAlgParam = idAlgParam;
    }

    @SuppressWarnings("unused")
    public String getNameParameter() {
        return nameParameter;
    }

    @SuppressWarnings("unused")
    public void setNameParameter(String nameParameter) {
        this.nameParameter = nameParameter;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PojoCodeAlgParam [idAlgParam=" + idAlgParam
                + ", nameParameter=" + nameParameter + "]";
    }

}
