package ru.smartflex.djf.widget.mask;

import ru.smartflex.djf.tool.OtherUtil;

public class IntValidator implements IFieldValidator {

    @SuppressWarnings("CatchMayIgnoreException")
    @Override
    public boolean isValid(String value) {
        boolean fok = false;

        if (OtherUtil.isStringEmpty(value)) {
            return true;
        }

        try {
            Integer.parseInt(value);
            fok = true;
        } catch (Exception e) {
        }

        return fok;
    }
}
