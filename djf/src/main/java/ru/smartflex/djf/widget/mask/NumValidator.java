package ru.smartflex.djf.widget.mask;

import java.math.BigDecimal;

import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.helper.ConverterUtil;
import ru.smartflex.djf.tool.OtherUtil;

public class NumValidator implements IFieldValidator {

    private UIWrapper uiw;
    private int precision = 0;
    private int delta = 0;

    public NumValidator(UIWrapper uiw) {
        super();
        this.uiw = uiw;
        handleProp();
    }

    private void handleProp() {
        if (uiw.getBeanFormDefPropertyFromBind() != null) {
            precision = uiw.getBeanFormDefPropertyFromBind().getPrecision();
            int scale = uiw.getBeanFormDefPropertyFromBind().getScale();
            delta = precision - scale;
        }
    }

    @SuppressWarnings("CatchMayIgnoreException")
    @Override
    public boolean isValid(String value) {
        boolean fok = false;

        if (OtherUtil.isStringEmpty(value)) {
            return true;
        }

        try {
            BigDecimal bg = ConverterUtil.convertToBigDecimal(value, uiw);
            if (precision == 0) {
                // no requirements for length of numeric
                fok = true;
            } else {
                if (bg.precision() <= precision) {
                    if ((bg.precision() - bg.scale()) <= delta) {
                        fok = true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return fok;
    }

}
