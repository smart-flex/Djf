package ru.smartflex.djf.widget.mask;

import ru.smartflex.djf.tool.OtherUtil;

public class ByteValidator implements IFieldValidator {

    @SuppressWarnings({"ResultOfMethodCallIgnored", "CatchMayIgnoreException"})
    @Override
    public boolean isValid(String value) {
        boolean fok = false;

        if (OtherUtil.isStringEmpty(value)) {
            return true;
        }

        try {
            Byte.parseByte(value);
            fok = true;
        } catch (Exception e) {
        }

        return fok;
    }

}
