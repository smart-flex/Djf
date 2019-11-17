package ru.smartflex.djf.tool;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.smartflex.djf.SFConstants;

public class FontUtil {

    private static Set<String> monoFontMap;
    private static Set<String> fontMap;

    @SuppressWarnings("RegExpRedundantEscape")
    private static final String REG_FONT_ATTR = "(\\w*\\s*)(.*?)(\\:)(\\w*)";
    private static Pattern pattern = Pattern.compile(REG_FONT_ATTR, Pattern.DOTALL);

    static {
        monoFontMap = new HashSet<String>();
        fontMap = new HashSet<String>();

        //noinspection CStyleArrayDeclaration
        Font fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getAllFonts();
        FontRenderContext frc = new FontRenderContext(null,
                RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT,
                RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);
        for (Font font : fonts) {
            fontMap.add(font.getFamily());
            Rectangle2D iBounds = font.getStringBounds("i", frc);
            Rectangle2D mBounds = font.getStringBounds("m", frc);
            if (iBounds.getWidth() == mBounds.getWidth()) {
                monoFontMap.add(font.getFamily());
            }
        }
    }

    private FontUtil() {

    }

    public static String checkMonoFontName(String familyName) {
        if (monoFontMap.contains(familyName)) {
            return familyName;
        }
        return null;
    }

    public static String checkFontName(String familyName) {
        if (fontMap.contains(familyName)) {
            return familyName;
        }
        return null;
    }

    public static Font getFont(String attr) {
        String fontFamily = null;
        String attrSize = null; // can be P12 or 12
        if (attr != null) {
            Matcher matcher = pattern.matcher(attr);
            if (matcher.find()) {
                try {
                    String fontGroup1 = matcher.group(1);
                    if (fontGroup1 != null) {
                        fontFamily = fontGroup1;
                    }
                    String fontGroup2 = matcher.group(2);
                    if (fontFamily == null) {
                        fontFamily = fontGroup2;
                    } else {
                        fontFamily += fontGroup2;
                    }
                    attrSize = matcher.group(4);
                } catch (Exception e) {
                    // in case no group found
                    fontFamily = "No font";
                }
            }
        } else {
            fontFamily = "No font";
        }

        int style = Font.PLAIN;
        int size = SFConstants.DEFAULT_FONT_SIZE;
        // parsing attrSize
        if (attrSize != null) {
            if (attrSize.startsWith("B")) {
                style = Font.BOLD;
                attrSize = attrSize.substring(1);
            } else if (attrSize.startsWith("I")) {
                style = Font.ITALIC;
                attrSize = attrSize.substring(1);
            }
            //noinspection CatchMayIgnoreException
            try {
                size = Integer.parseInt(attrSize);
            } catch (Exception e) {
            }
        }
        return new Font(fontFamily, style, size);
    }

}
