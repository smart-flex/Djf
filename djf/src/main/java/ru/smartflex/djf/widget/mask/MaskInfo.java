package ru.smartflex.djf.widget.mask;

import java.text.DecimalFormat;

import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.WidgetTypeEnum;

public class MaskInfo {

    private static final String ERROR_PERIOD = "####-##";

    private int lastCaretPosition = -1;
    private String mask;
    private String maskDelimiter;
    private WidgetTypeEnum type;
    private String maskPeriodDelim = null;
    private DecimalFormat formatPeriodYear = null;
    private DecimalFormat formatPeriodMonth = null;
    private boolean concatDirectionFirstYear = true;
    private int indexPeriodDelimiter = -1;

    public MaskInfo(String mask, String maskDelimiter, WidgetTypeEnum type) {
        super();
        this.mask = mask;
        this.maskDelimiter = maskDelimiter;
        this.type = type;
        handle();
    }

    @SuppressWarnings("unused")
    public WidgetTypeEnum getWidgetTypeEnum() {
        return type;
    }

    private void handle() {
        if (type == WidgetTypeEnum.PERIOD) {
            maskPeriodDelim = maskDelimiter.trim();
            indexPeriodDelimiter = mask.indexOf(maskPeriodDelim);
            String part1 = mask.substring(0, indexPeriodDelimiter);
            String part2 = mask.substring(indexPeriodDelimiter + 1);
            String maskPeriodYear;
            String maskPeriodMonth;
            if (part1.length() > part2.length()) {
                maskPeriodYear = part1;
                maskPeriodMonth = part2;
            } else {
                maskPeriodYear = part2;
                maskPeriodMonth = part1;
                concatDirectionFirstYear = false;
            }
            formatPeriodYear = new DecimalFormat(maskPeriodYear);
            formatPeriodMonth = new DecimalFormat(maskPeriodMonth);
        }
    }

    public String getPeriodAsString(int year, int month) {
        String per = ERROR_PERIOD;

        if (type == WidgetTypeEnum.PERIOD) {
            if (isYearAndMonthIsValid(year, month)) {

                if (concatDirectionFirstYear) {
                    per = formatPeriodYear.format(year) + maskPeriodDelim
                            + formatPeriodMonth.format(month);
                } else {
                    per = formatPeriodMonth.format(month) + maskPeriodDelim
                            + formatPeriodYear.format(year);
                }
            }
        }
        return per;
    }

    private boolean isYearAndMonthIsValid(int year, int month) {
        boolean fok = false;

        if (year <= SFConstants.MAX_PERIOD && year >= SFConstants.MIN_PERIOD
                && month >= SFConstants.MONTH_NUM_JAN
                && month <= SFConstants.MONTH_NUM_DEC) {
            fok = true;
        }

        return fok;
    }

    @SuppressWarnings("CatchMayIgnoreException")
    public Integer getPeriodFromFormattedText(String text) {
        Integer period = null;

        if (type == WidgetTypeEnum.PERIOD) {

            try {
                String part1 = text.substring(0, indexPeriodDelimiter);
                String part2 = text.substring(indexPeriodDelimiter + 1);
                int year;
                int mm;
                if (part1.length() > part2.length()) {
                    year = Integer.parseInt(part1);
                    mm = Integer.parseInt(part2);
                } else {
                    year = Integer.parseInt(part2);
                    mm = Integer.parseInt(part1);
                }
                if (isYearAndMonthIsValid(year, mm)) {
                    period = year * 100 + mm;
                }
            } catch (Exception e) {
            }
        }

        return period;
    }

    boolean isFilled(String value) {
        boolean fok = false;

        if (type == WidgetTypeEnum.DATE || type == WidgetTypeEnum.PERIOD) {
            for (int i = 0; i < maskDelimiter.length(); i++) {
                char symbDelim = maskDelimiter.charAt(i);
                if (symbDelim == ISFMaskConstants.CHAR_SPACE) {
                    char symbVal = value.charAt(i);
                    if (symbVal != ISFMaskConstants.CHAR_SPACE) {
                        fok = true;
                        break;
                    }
                }
            }
        }

        return fok;
    }

    int getLastCaretPosition() {
        return lastCaretPosition;
    }

    public void setLastCaretPosition(int lastCaretPosition) {
        this.lastCaretPosition = lastCaretPosition;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getMaskDelimiter() {
        return maskDelimiter;
    }

    @SuppressWarnings("unused")
    public void setMaskDelimiter(String maskDelimiter) {
        this.maskDelimiter = maskDelimiter;
    }

    @Override
    public String toString() {
        return "MaskInfo [mask=" + mask + ", maskDelimiter=" + maskDelimiter
                + "]";
    }

}
