package ru.smartflex.djf.controller.helper;

public class DocumentFilterUtil {

    public static String doGlue(String newText, String prevText, int offset) {
        String raw = null;
        if (prevText != null) {
            if (offset == prevText.length()) {
                raw =  prevText + newText;
            } else {
                if (offset == 0) {
                    raw = newText + prevText;
                } else {
                    raw = prevText.substring(0, offset) + newText + prevText.substring(offset);
                }
            }
        } else {
            raw = newText;
        }
        return raw;
    }
}
