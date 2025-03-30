package ru.smartflex.djf.demo.java;

import java.math.BigDecimal;

public class StepperPercentInfo {

    private int percent = 0;
    private String text15;
    private PojoStreet emptyStreet;
    private PojoStreet emptyStreet15;
    private PojoStreet street;
    private BigDecimal sumPayment;
    private String textOrder;
    private String phoneFilledMistake = "89093372772";
    private String phoneFilledOk = "+7 919 825 76 58";
    private String phoneEmpty;
    private Integer calcPeriodError = 202333;
    private String snilsEmpty;
    private String snilsFilledMistake = "073---427-39734";
    private String snilsFilledOk = "148-025-789 78";

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getText15() {
        return text15;
    }

    public void setText15(String text15) {
        this.text15 = text15;
    }

    public PojoStreet getEmptyStreet() {
        return emptyStreet;
    }

    public void setEmptyStreet(PojoStreet emptyStreet) {
        this.emptyStreet = emptyStreet;
    }

    public PojoStreet getEmptyStreet15() {
        return emptyStreet15;
    }

    public void setEmptyStreet15(PojoStreet emptyStreet15) {
        this.emptyStreet15 = emptyStreet15;
    }

    public PojoStreet getStreet() {
        return street;
    }

    public void setStreet(PojoStreet street) {
        this.street = street;
    }

    public BigDecimal getSumPayment() {
        return sumPayment;
    }

    public void setSumPayment(BigDecimal sumPayment) {
        this.sumPayment = sumPayment;
    }

    public String getTextOrder() {
        return textOrder;
    }

    public void setTextOrder(String textOrder) {
        this.textOrder = textOrder;
    }

    public String getPhoneFilledMistake() {
        return phoneFilledMistake;
    }

    public void setPhoneFilledMistake(String phoneFilledMistake) {
        this.phoneFilledMistake = phoneFilledMistake;
    }

    public String getPhoneFilledOk() {
        return phoneFilledOk;
    }

    public void setPhoneFilledOk(String phoneFilledOk) {
        this.phoneFilledOk = phoneFilledOk;
    }

    public String getPhoneEmpty() {
        return phoneEmpty;
    }

    public void setPhoneEmpty(String phoneEmpty) {
        this.phoneEmpty = phoneEmpty;
    }

    public Integer getCalcPeriodError() {
        return calcPeriodError;
    }

    public void setCalcPeriodError(Integer calcPeriodError) {
        this.calcPeriodError = calcPeriodError;
    }

    public String getSnilsEmpty() {
        return snilsEmpty;
    }

    public void setSnilsEmpty(String snilsEmpty) {
        this.snilsEmpty = snilsEmpty;
    }

    public String getSnilsFilledMistake() {
        return snilsFilledMistake;
    }

    public void setSnilsFilledMistake(String snilsFilledMistake) {
        this.snilsFilledMistake = snilsFilledMistake;
    }

    public String getSnilsFilledOk() {
        return snilsFilledOk;
    }

    public void setSnilsFilledOk(String snilsFilledOk) {
        this.snilsFilledOk = snilsFilledOk;
    }
}
