package ru.smartflex.djf.demo.java;

import java.io.Serializable;

public class PojoAccPropertyType implements Serializable {

    private static final long serialVersionUID = -2608392989226866874L;

    private int idType = 0;
    private String nameType = null;

    @SuppressWarnings("unused")
    public PojoAccPropertyType() {
        super();
    }

    @SuppressWarnings("WeakerAccess")
    public PojoAccPropertyType(int idType, String nameType) {
        super();
        this.idType = idType;
        this.nameType = nameType;
    }

    @SuppressWarnings("WeakerAccess")
    public int getIdType() {
        return idType;
    }

    @SuppressWarnings("unused")
    public void setIdType(int idType) {
        this.idType = idType;
    }

    @SuppressWarnings("unused")
    public String getNameType() {
        return nameType;
    }

    @SuppressWarnings("unused")
    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + idType;
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
        PojoAccPropertyType other = (PojoAccPropertyType) obj;
        //noinspection ControlFlowStatementWithoutBraces
        if (idType != other.idType)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PojoAccPropertyType [idType=" + idType + ", nameType="
                + nameType + "]";
    }

}
