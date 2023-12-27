package ru.smartflex.djf.demo.java;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PojoAccEquipment implements Serializable {

    private static final long serialVersionUID = 4961951738762253880L;
    private static AtomicInteger id = new AtomicInteger(0);

    private int idEquipment;

    private BigDecimal amount = null;
    private String serialNumber = null;
    private String remark = null;
    private PojoAccount account = null;
    private Boolean disabled = null;

    private List<WorkHistory> workHistory = new ArrayList<WorkHistory>();

    @SuppressWarnings("unused")
    public PojoAccEquipment() {
        super();
        this.idEquipment = id.incrementAndGet();
    }

    @SuppressWarnings("WeakerAccess")
    public PojoAccEquipment(BigDecimal amount) {
        super();
        this.idEquipment = id.incrementAndGet();
        this.amount = amount;
    }

    @SuppressWarnings("WeakerAccess")
    public PojoAccEquipment(BigDecimal amount, String remark) {
        super();
        this.idEquipment = id.incrementAndGet();
        this.amount = amount;
        this.remark = remark;
    }

    public PojoAccEquipment(BigDecimal amount, String serialNumber, String remark) {
        super();
        this.idEquipment = id.incrementAndGet();
        this.amount = amount;
        this.serialNumber = serialNumber;
        this.remark = remark;
    }

    @SuppressWarnings("unused")
    public int getIdEquipment() {
        return idEquipment;
    }

    @SuppressWarnings("unused")
    public void setIdEquipment(int idEquipment) {
        this.idEquipment = idEquipment;
    }

    @SuppressWarnings("unused")
    public BigDecimal getAmount() {
        return amount;
    }

    @SuppressWarnings("unused")
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + idEquipment;
        return result;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PojoAccEquipment other = (PojoAccEquipment) obj;
        if (idEquipment != other.idEquipment)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PojoAccEquipment [idEquipment=" + idEquipment + "]";
    }

    @SuppressWarnings("unused")
    public String getRemark() {
        return remark;
    }

    @SuppressWarnings("unused")
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @SuppressWarnings("WeakerAccess")
    public List<WorkHistory> getWorkHistory() {
        return workHistory;
    }

    @SuppressWarnings("unused")
    public void setWorkHistory(List<WorkHistory> workHistory) {
        this.workHistory = workHistory;
    }

    @SuppressWarnings("unused")
    public String getSerialNumber() {
        return serialNumber;
    }

    @SuppressWarnings("unused")
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @SuppressWarnings("unused")
    public PojoAccount getAccount() {
        return account;
    }

    @SuppressWarnings("unused")
    public void setAccount(PojoAccount account) {
        this.account = account;
    }

    @SuppressWarnings("unused")
    public Boolean getDisabled() {
        return disabled;
    }

    @SuppressWarnings("WeakerAccess")
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

}
