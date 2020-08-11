package ru.smartflex.djf.demo.java;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class PojoAccount implements Serializable {

    private static final long serialVersionUID = 5483863606574653452L;
    private static AtomicInteger id = new AtomicInteger(0);

    private int idAccount;

    private String accountNumber = null;
    private Date dateCreate = null;
    private String homePhone = null;
    private String ceilPhone = null;
    private String postalIndex = null;
    private boolean employed;
    private String militaryRank = null;
    private String militaryUnit = null;
    private Boolean hasCredit = null;
    private Boolean hasChildren = null;
    private Integer calcPeriod = null;
    private long innCode = 0;
    private BigDecimal salary = null;
    private Boolean fired = null;
    private String remark = null;

    private PojoAccPropertyType typeAccount = null;
    private PojoAccount mother = null;
    private PojoCarType carType = null;
    private PojoCarModel carModel = null;
    @SuppressWarnings("rawtypes")
    private Set equipment = new HashSet(0);
    private PojoStreet street = null;
    private PojoBuilding building = null;

    public PojoAccount() {
        super();
        employed = true; // else new record is auto-locked
        this.idAccount = id.incrementAndGet();
    }

    public PojoAccount(String accountNumber, Date dateCreate,
                       String homePhone, String ceilPhone, String postalIndex,
                       boolean employed, PojoAccPropertyType typeAccount,
                       Integer calcPeriod, BigDecimal salary) {
        super();
        this.idAccount = id.incrementAndGet();
        this.accountNumber = accountNumber;
        this.dateCreate = dateCreate;
        this.homePhone = homePhone;
        this.ceilPhone = ceilPhone;
        this.postalIndex = postalIndex;
        this.employed = employed;
        this.typeAccount = typeAccount;
        this.calcPeriod = calcPeriod;
        this.salary = salary;
    }

    public PojoAccount(String accountNumber, Date dateCreate,
                       String homePhone, String ceilPhone, String postalIndex,
                       boolean employed, PojoAccPropertyType typeAccount,
                       PojoAccount mother) {
        super();
        this.idAccount = id.incrementAndGet();
        this.accountNumber = accountNumber;
        this.dateCreate = dateCreate;
        this.homePhone = homePhone;
        this.ceilPhone = ceilPhone;
        this.postalIndex = postalIndex;
        this.employed = employed;
        this.typeAccount = typeAccount;
        this.mother = mother;
    }

    @SuppressWarnings("unused")
    public int getIdAccount() {
        return idAccount;
    }

    @SuppressWarnings("unused")
    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    @SuppressWarnings("WeakerAccess")
    public String getAccountNumber() {
        return accountNumber;
    }

    @SuppressWarnings("unused")
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @SuppressWarnings("unused")
    public Date getDateCreate() {
        return dateCreate;
    }

    @SuppressWarnings("unused")
    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    @SuppressWarnings("unused")
    public String getHomePhone() {
        return homePhone;
    }

    @SuppressWarnings("unused")
    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    @SuppressWarnings("unused")
    public String getCeilPhone() {
        return ceilPhone;
    }

    @SuppressWarnings("unused")
    public void setCeilPhone(String ceilPhone) {
        this.ceilPhone = ceilPhone;
    }

    @SuppressWarnings("unused")
    public String getPostalIndex() {
        return postalIndex;
    }

    @SuppressWarnings("unused")
    public void setPostalIndex(String postalIndex) {
        this.postalIndex = postalIndex;
    }

    @SuppressWarnings("unused, WeakerAccess")
    public boolean isEmployed() {
        return employed;
    }

    @SuppressWarnings("unused")
    public void setEmployed(boolean employed) {
        this.employed = employed;
    }

    @SuppressWarnings("unused, WeakerAccess")
    public PojoAccPropertyType getTypeAccount() {
        return typeAccount;
    }

    @SuppressWarnings("unused")
    public void setTypeAccount(PojoAccPropertyType typeAccount) {
        this.typeAccount = typeAccount;
    }

    @SuppressWarnings("unused")
    public PojoAccount getMother() {
        return mother;
    }

    @SuppressWarnings("unused")
    public void setMother(PojoAccount mother) {
        this.mother = mother;
    }

    @SuppressWarnings({"rawtypes", "WeakerAccess"})
    public Set getEquipment() {
        return equipment;
    }

    @SuppressWarnings({"rawtypes", "unused"})
    public void setEquipment(Set equipment) {
        this.equipment = equipment;
    }

    @Override
    public String toString() {
        return "PojoAccount [idAccount=" + idAccount + ", accountNumber="
                + accountNumber + ", street=" + street + ", building="
                + building + "]";
    }

    @SuppressWarnings("unused")
    public String getMilitaryRank() {
        return militaryRank;
    }

    @SuppressWarnings("WeakerAccess")
    public void setMilitaryRank(String militaryRank) {
        this.militaryRank = militaryRank;
    }

    @SuppressWarnings("unused")
    public String getMilitaryUnit() {
        return militaryUnit;
    }

    @SuppressWarnings("WeakerAccess")
    public void setMilitaryUnit(String militaryUnit) {
        this.militaryUnit = militaryUnit;
    }

    @SuppressWarnings("unused")
    public Boolean getHasCredit() {
        return hasCredit;
    }

    @SuppressWarnings("WeakerAccess")
    public void setHasCredit(Boolean hasCredit) {
        this.hasCredit = hasCredit;
    }

    @SuppressWarnings("unused")
    public Boolean getHasChildren() {
        return hasChildren;
    }

    @SuppressWarnings("WeakerAccess")
    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    @SuppressWarnings("unused")
    public PojoCarType getCarType() {
        return carType;
    }

    @SuppressWarnings("WeakerAccess")
    public void setCarType(PojoCarType carType) {
        this.carType = carType;
    }

    @SuppressWarnings("unused")
    public Integer getCalcPeriod() {
        return calcPeriod;
    }

    @SuppressWarnings("unused")
    public void setCalcPeriod(Integer calcPeriod) {
        this.calcPeriod = calcPeriod;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((accountNumber == null) ? 0 : accountNumber.hashCode());
        result = prime * result + idAccount;
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
        PojoAccount other = (PojoAccount) obj;
        if (accountNumber == null) {
            if (other.accountNumber != null)
                return false;
        } else if (!accountNumber.equals(other.accountNumber))
            return false;
        //noinspection RedundantIfStatement
        if (idAccount != other.idAccount)
            return false;
        return true;
    }

    @SuppressWarnings("unused")
    public long getInnCode() {
        return innCode;
    }

    @SuppressWarnings("unused")
    public void setInnCode(long innCode) {
        this.innCode = innCode;
    }

    @SuppressWarnings("unused")
    public BigDecimal getSalary() {
        return salary;
    }

    @SuppressWarnings("unused")
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    @SuppressWarnings("unused")
    public PojoCarModel getCarModel() {
        return carModel;
    }

    @SuppressWarnings("WeakerAccess")
    public void setCarModel(PojoCarModel carModel) {
        this.carModel = carModel;
    }

    @SuppressWarnings("unused")
    public PojoStreet getStreet() {
        return street;
    }

    @SuppressWarnings("WeakerAccess")
    public void setStreet(PojoStreet street) {
        this.street = street;
    }

    @SuppressWarnings("unused")
    public PojoBuilding getBuilding() {
        return building;
    }

    @SuppressWarnings("WeakerAccess")
    public void setBuilding(PojoBuilding building) {
        this.building = building;
    }

    @SuppressWarnings("unused")
    public Boolean getFired() {
        return fired;
    }

    @SuppressWarnings("WeakerAccess")
    public void setFired(Boolean fired) {
        this.fired = fired;
    }

}
