package ru.smartflex.djf.widget.mask;

import ru.smartflex.djf.tool.OtherUtil;

public class ShortValidator implements IFieldValidator {

    @SuppressWarnings({"ResultOfMethodCallIgnored", "CatchMayIgnoreException"})
    @Override
    public boolean isValid(String value) {
        boolean fok = false;

        if (OtherUtil.isStringEmpty(value)) {
            return true;
        }

        try {
            Short.parseShort(value);
            fok = true;
        } catch (Exception e) {
        }

        return fok;
    }

}
