package ru.smartflex.djf.widget.mask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateValidator implements IFieldValidator {

    private DateFormat df;
    private MaskInfo maskInfo;

    public DateValidator(MaskInfo maskInfo) {
        this.maskInfo = maskInfo;
        df = new SimpleDateFormat(maskInfo.getMask());
    }

    @Override
    public boolean isValid(String value) {
        boolean fok = true;

        if (maskInfo.isFilled(value)) {
            try {
                df.parse(value);
                fok = true;
            } catch (Exception e) {
                fok = false;
            }
        }
        return fok;
    }

}
