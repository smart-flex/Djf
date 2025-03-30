package ru.smartflex.djf.widget.mask;

import ru.smartflex.djf.controller.helper.SnilsUtil;

public class SnilsValidator implements IFieldValidator {

    public boolean isValid(String value) {
        return SnilsUtil.isSnilsFormattedValid(value);
    }

}
