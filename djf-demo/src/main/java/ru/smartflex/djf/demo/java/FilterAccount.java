package ru.smartflex.djf.demo.java;

public class FilterAccount {

    private String accountNumber = null;
    private PojoAccPropertyType typeAccount = null;

    @SuppressWarnings("WeakerAccess")
    public String getAccountNumber() {
        return accountNumber;
    }

    @SuppressWarnings("unused")
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @SuppressWarnings("WeakerAccess")
    public PojoAccPropertyType getTypeAccount() {
        return typeAccount;
    }

    @SuppressWarnings("unused")
    public void setTypeAccount(PojoAccPropertyType typeAccount) {
        this.typeAccount = typeAccount;
    }

}
