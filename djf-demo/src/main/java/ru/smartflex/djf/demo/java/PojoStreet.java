package ru.smartflex.djf.demo.java;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PojoStreet implements Serializable {

    private static final long serialVersionUID = 5951304178272417990L;

    private int idStreet = 0;
    private String streetName = null;

    private List<PojoBuilding> buildings = new ArrayList<PojoBuilding>();

    @SuppressWarnings("unused")
    public PojoStreet() {
        super();
    }

    @SuppressWarnings("WeakerAccess")
    public PojoStreet(int idStreet, String streetName) {
        super();
        this.idStreet = idStreet;
        this.streetName = streetName;
    }

    @SuppressWarnings("WeakerAccess")
    public int getIdStreet() {
        return idStreet;
    }

    @SuppressWarnings("WeakerAccess")
    public void setIdStreet(int idStreet) {
        this.idStreet = idStreet;
    }

    @SuppressWarnings("unused")
    public String getStreetName() {
        return streetName;
    }

    @SuppressWarnings("unused")
    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    @SuppressWarnings("WeakerAccess")
    public List<PojoBuilding> getBuildings() {
        return buildings;
    }

    @SuppressWarnings("unused")
    public void setBuildings(List<PojoBuilding> buildings) {
        this.buildings = buildings;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + idStreet;
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
        PojoStreet other = (PojoStreet) obj;
        //noinspection RedundantIfStatement
        if (idStreet != other.idStreet) {
            return false;
        }
        return true;
    }

}
