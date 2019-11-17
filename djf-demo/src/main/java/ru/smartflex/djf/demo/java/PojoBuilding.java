package ru.smartflex.djf.demo.java;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class PojoBuilding implements Serializable {

    private static final long serialVersionUID = -3576067427004696255L;
    private static AtomicInteger id = new AtomicInteger(0);

    private int idBuilding;
    private String buildingNumber = null;

    @SuppressWarnings("unused")
    public PojoBuilding() {
        super();
        this.idBuilding = id.incrementAndGet();
    }

    @SuppressWarnings("WeakerAccess")
    public PojoBuilding(String buildingNumber) {
        super();
        this.idBuilding = id.incrementAndGet();
        this.buildingNumber = buildingNumber;
    }

    @SuppressWarnings("unused")
    public int getIdBuilding() {
        return idBuilding;
    }

    @SuppressWarnings("unused")
    public void setIdBuilding(int idBuilding) {
        this.idBuilding = idBuilding;
    }

    @SuppressWarnings("unused")
    public String getBuildingNumber() {
        return buildingNumber;
    }

    @SuppressWarnings("unused")
    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + idBuilding;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PojoBuilding other = (PojoBuilding) obj;
        //noinspection RedundantIfStatement
        if (idBuilding != other.idBuilding)
            return false;
        return true;
    }

}
