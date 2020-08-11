package ru.smartflex.djf.widget.mask;

import ru.smartflex.djf.controller.helper.PhoneZoneUtil;

public class PhoneValidator implements IFieldValidator {

    public boolean isValid(String value) {
        return PhoneZoneUtil.isPhoneFormattedValid(value);
    }

}
