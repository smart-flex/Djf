package ru.smartflex.djf.tool;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtil {

    @SuppressWarnings("RegExpRedundantEscape")
    private static final String REG_COLOR_ATTR = "(\\w*)(\\:)(\\w*)(\\:)(\\w*)";
    private static Pattern pattern = Pattern.compile(REG_COLOR_ATTR, Pattern.DOTALL);

    private ColorUtil() {
    }


    public static Color getColor(String attr) {
        if (attr == null) {
            return null;
        }
        Matcher matcher = pattern.matcher(attr);
        if (matcher.find()) {
            try {
                String r = matcher.group(1);
                String g = matcher.group(3);
                String b = matcher.group(5);
                return new Color(Integer.parseInt(r), Integer.parseInt(g), Integer.parseInt(b));
            } catch (Exception e) {
                return null;
            }
        } else {
            // hex
            if (attr.startsWith("#")) {
                return Color.decode(attr);
            }
        }
        return null;
    }

}
