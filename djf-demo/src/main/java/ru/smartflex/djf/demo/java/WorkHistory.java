package ru.smartflex.djf.demo.java;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkHistory implements Serializable {

    private static final long serialVersionUID = 5950419289998823460L;

    private static AtomicInteger id = new AtomicInteger(0);

    private Integer idHistory;
    private Date dateWork = null;
    private String remark = null;

    private PojoAccEquipment equipment = null;

    @SuppressWarnings("unused")
    public WorkHistory() {
        super();
        this.idHistory = id.incrementAndGet();
    }

    @SuppressWarnings("WeakerAccess")
    public WorkHistory(String dateWork, String remark) {
        super();
        this.idHistory = id.incrementAndGet();
        this.remark = remark;

        if (dateWork != null) {
            //noinspection CatchMayIgnoreException
            try {
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                this.dateWork = df.parse(dateWork);
            } catch (Exception e) {
            }
        }
    }

    @SuppressWarnings("unused")
    public Integer getIdHistory() {
        return idHistory;
    }

    @SuppressWarnings("unused")
    public void setIdHistory(Integer idHistory) {
        this.idHistory = idHistory;
    }

    @SuppressWarnings("unused")
    public Date getDateWork() {
        return dateWork;
    }

    @SuppressWarnings("unused")
    public void setDateWork(Date dateWork) {
        this.dateWork = dateWork;
    }

    @SuppressWarnings("unused")
    public String getRemark() {
        return remark;
    }

    @SuppressWarnings("unused")
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "WorkHistory [idHistory=" + idHistory + ", dateWork=" + dateWork
                + ", rematk=" + remark + "]";
    }

    @SuppressWarnings("unused")
    public PojoAccEquipment getEquipment() {
        return equipment;
    }

    @SuppressWarnings("unused")
    public void setEquipment(PojoAccEquipment equipment) {
        this.equipment = equipment;
    }

}
