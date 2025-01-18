package ru.smartflex.djf.demo.java;

import java.util.Date;

public class DateSimple {
    private String text;
    private Date dateFilled = new Date();

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
}
