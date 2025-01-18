package ru.smartflex.djf.demo.java;

import ru.smartflex.djf.Djf;

public class SFWidgetsAssistant extends GridFieldsAssistant {

    public void enableSumPayment() {
        boolean fok = Djf.getCheckBoxStatus("itemCheckForSumPayment");
        if (fok) {
            Djf.enableWidget("itemSumPayment");
            Djf.requestFocusOnWidget("itemSumPayment");
        } else {
            Djf.disableWidget("itemSumPayment");
        }
    }

    public void fillDate() {
        Djf.runForm("ru/smartflex/djf/demo/xml/SFWidgetsDate.frm.xml");
    }

}
