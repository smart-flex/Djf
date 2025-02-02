package ru.smartflex.djf.demo.java;

import ru.smartflex.djf.demo.MainDjfDemo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateSimple {
    private String text;
    private Date dateFilled = new Date();
    private Date dateEmpty = null;
    private Integer calcPeriodCorrect = 202501;
    private Integer calcPeriodError = 202333;
    private Integer calcPeriodEmpty = null;

    public DateSimple() {
        SimpleDateFormat sdf = new SimpleDateFormat(MainDjfDemo.getDateMask());
        text = sdf.format(dateFilled);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDateFilled() {
        return dateFilled;
    }

    public void setDateFilled(Date dateFilled) {
        this.dateFilled = dateFilled;
    }

    public Date getDateEmpty() {
        return dateEmpty;
    }

    public void setDateEmpty(Date dateEmpty) {
        this.dateEmpty = dateEmpty;
    }

    public Integer getCalcPeriodCorrect() {
        return calcPeriodCorrect;
    }

    public void setCalcPeriodCorrect(Integer calcPeriodCorrect) {
        this.calcPeriodCorrect = calcPeriodCorrect;
    }

    public Integer getCalcPeriodError() {
        return calcPeriodError;
    }

    public void setCalcPeriodError(Integer calcPeriodError) {
        this.calcPeriodError = calcPeriodError;
    }

    public Integer getCalcPeriodEmpty() {
        return calcPeriodEmpty;
    }

    public void setCalcPeriodEmpty(Integer calcPeriodEmpty) {
        this.calcPeriodEmpty = calcPeriodEmpty;
    }
}
