package ru.smartflex.djf.widget.mask;

public class PeriodValidator implements IFieldValidator {

    private MaskInfo maskInfo;

    public PeriodValidator(MaskInfo maskInfo) {
        this.maskInfo = maskInfo;
    }

    @Override
    public boolean isValid(String value) {
        boolean fok = true;

        if (maskInfo.isFilled(value)) {
            if (maskInfo.getPeriodFromFormattedText(value) == null) {
                fok = false;
            }
        }
        return fok;
    }

}
