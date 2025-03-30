package ru.smartflex.djf.controller.helper;

import org.apache.commons.lang3.StringUtils;
import ru.smartflex.djf.controller.bean.SnilsBag;

public class SnilsUtil {

    public static boolean isSnilsFormattedValid(String snils) {
        boolean fok = true;
        if (!StringUtils.isEmpty(snils)) {
            String rawClear = snils.replace(" ", "").replace("-", "");
            int l = rawClear.length();
            if (l != 11) {
                fok = false;
            } else {
                char[] snilsArray = rawClear.toCharArray();
                for (int i=0; i<11; i++) {
                    if (!Character.isDigit(snilsArray[i])) {
                        fok = false;
                    }
                }
            }
        }
        return fok;
    }

    public static SnilsBag formatSnils(String finishedText) {
        return formatSnilsInt(finishedText, null, -1);
    }

    public static SnilsBag formatSnils(String newText, String prevText, int offset) {
        return formatSnilsInt(newText, prevText, offset);
    }

    private static SnilsBag formatSnilsInt(String newText, String prevText, int offset) {
        SnilsBag snilsBag = null;
        String raw = DocumentFilterUtil.doGlue(newText, prevText, offset);
        if (!StringUtils.isEmpty(raw)) {
            String rawClear = raw.replace(" ", "").replace("-", "");
            char[] charsClear = rawClear.toCharArray();
            StringBuilder sb = new StringBuilder(14);
            for (int i=0; i<charsClear.length; i++) {
                sb.append(charsClear[i]);
                switch (i) {
                    case 2:
                    case 5:
                        sb.append("-");
                        break;
                    case 8:
                        sb.append(" ");
                        break;
                }
            }
            boolean fullNumber = false;
            if (rawClear.length() == 11) {
                fullNumber = true;
            }
            snilsBag = new SnilsBag(sb.toString(), fullNumber);
        }
        return snilsBag;
    }
}
