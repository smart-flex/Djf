package ru.smartflex.djf.demo.java;

public class GridFieldsTabAssistant extends GridFieldsAssistant {

    private static final String MILITARY_UNIT = "pa.militaryUnit";

    /**
     * In this method you can redefine enabled-disabled behavior dynamically.
     * As a result for one panel on different forms you will get various behavior.
     */
    @Override
    public boolean enabled(String bind, String idWidget) {
        boolean fok = true;
        if (bind != null && bind.equals(MILITARY_UNIT)) {
            fok = false;
        }
        return fok;
    }

}
