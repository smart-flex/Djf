package ru.smartflex.djf.demo.java;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PojoCode implements Serializable {

    private static final long serialVersionUID = 4603019967924690163L;

    private static AtomicInteger id = new AtomicInteger(0);

    private Integer idCode = null;
    private String nameCode = null;
    private String unit = null;
    private Boolean calcPrivatization = null;

    private List<PojoCode> subCode = new ArrayList<PojoCode>(0);
    private List<PojoCodeAlg> histAlg = new ArrayList<PojoCodeAlg>(0);

    public PojoCode() {
        super();
    }

    public PojoCode(String nameCode, String unit) {
        super();
        this.idCode = id.incrementAndGet();
        this.nameCode = nameCode;
        this.unit = unit;
    }

    public PojoCode(String nameCode, String unit, Boolean calcPrivatization) {
        super();
        this.idCode = id.incrementAndGet();
        this.nameCode = nameCode;
        this.unit = unit;
        this.calcPrivatization = calcPrivatization;
    }

    @SuppressWarnings("unused")
    public Integer getIdCode() {
        return idCode;
    }

    @SuppressWarnings("unused")
    public void setIdCode(Integer idCode) {
        this.idCode = idCode;
    }

    @SuppressWarnings("unused")
    public String getNameCode() {
        return nameCode;
    }

    @SuppressWarnings("unused")
    public void setNameCode(String nameCode) {
        this.nameCode = nameCode;
    }

    @SuppressWarnings("WeakerAccess")
    public List<PojoCode> getSubCode() {
        return subCode;
    }

    @SuppressWarnings("unused")
    public void setSubCode(List<PojoCode> subCode) {
        this.subCode = subCode;
    }

    @SuppressWarnings("unused")
    public String getUnit() {
        return unit;
    }

    @SuppressWarnings("unused")
    public void setUnit(String unit) {
        this.unit = unit;
    }

    @SuppressWarnings("WeakerAccess")
    public List<PojoCodeAlg> getHistAlg() {
        return histAlg;
    }

    @SuppressWarnings("unused")
    public void setHistAlg(List<PojoCodeAlg> histAlg) {
        this.histAlg = histAlg;
    }

    @Override
    public String toString() {
        return "PojoCode [idCode=" + idCode + ", nameCode=" + nameCode
                + ", unit=" + unit + "]";
    }

    @SuppressWarnings("unused")
    public Boolean getCalcPrivatization() {
        return calcPrivatization;
    }

    @SuppressWarnings("unused")
    public void setCalcPrivatization(Boolean calcPrivatization) {
        this.calcPrivatization = calcPrivatization;
    }

}
