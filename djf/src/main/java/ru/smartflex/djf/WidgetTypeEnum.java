package ru.smartflex.djf;

public enum WidgetTypeEnum {

    DATE, BUTTON, COMBOBOX, LABEL, TEXT, CHECKBOX, PANEL, TABPANEL, GRID, PASSWORD, RUN, TGRID, PERIOD, TGRID_TREE_FIELD, INT,
    NUMERIC, OPERATOR, SEPARATOR, TEXTAREA, BYTE, SHORT, LONG, FILE, GROUP, RADIO, PHONE;

    public static boolean isFocusAble(WidgetTypeEnum type) {
        boolean fok = true;

        if (type == LABEL || type == PANEL || type == TABPANEL || type == GROUP) {
            fok = false;
        }

        return fok;
    }

    public static boolean isOrderAble(WidgetTypeEnum type) {
        boolean fok = false;

        if (type == DATE || type == BUTTON || type == TEXT || type == CHECKBOX
                || type == GRID || type == PASSWORD || type == RUN
                || type == TGRID || type == PERIOD || type == INT
                || type == BYTE || type == SHORT || type == LONG
                || type == NUMERIC || type == OPERATOR || type == TEXTAREA || type == FILE || type == RADIO
                || type == PHONE) {
            fok = true;
        }

        return fok;
    }

    public static boolean isScrollAbleInMDStyle(WidgetTypeEnum type) {
        boolean fok = false;

        if (type == GRID || type == TGRID) {
            fok = true;
        }

        return fok;
    }

    @SuppressWarnings("unused")
    public static boolean isCanBeUseFilledModel(WidgetTypeEnum type) {
        boolean fok = false;

        if (type == GRID || type == TGRID || type == COMBOBOX) {
            fok = true;
        }

        return fok;
    }

}
