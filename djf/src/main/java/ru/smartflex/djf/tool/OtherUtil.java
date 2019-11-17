package ru.smartflex.djf.tool;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.ImageIcon;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.controller.exception.MissingException;

public class OtherUtil {

    private static final String CHARSET = "UTF-8";

    private OtherUtil() {
    }

    public static ImageIcon loadSFImages(String img) {
        if (img == null) {
            return null;
        }

        ImageIcon image;
        String imgUrl;
        URL url = null;
        if (Djf.getConfigurator().getPathToIcon() != null) {
            imgUrl = Djf.getConfigurator().getPathToIcon() + img;
            url = getURL(imgUrl);
        }

        if (url == null) {
            imgUrl = SFConstants.IMAGE_PACKAGE + img;
            url = getURL(imgUrl);
        }

        image = new ImageIcon(url);
        return image;
    }

    private static URL getURL(String imgUrl) {
        URL url;

        url = ClassLoader.getSystemResource(imgUrl);
        if (url == null) {
            url = OtherUtil.class.getClassLoader().getResource(imgUrl);
        }

        return url;
    }

    public static InputStream getBodyAsStream(String path, String src) {
        String body;
        if (path == null) {
            body = src;
        } else {
            body = path + src;
        }
        InputStream is;
        is = ClassLoader.getSystemResourceAsStream(body);
        if (is == null) {
            is = OtherUtil.class.getClassLoader().getResourceAsStream(body);
        }
        if (is == null) {
            throw new MissingException("No definition is found for: " + body);
        }
        return is;
    }

    public static String getBodyAsText(String src) {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            is = OtherUtil.getBodyAsStream(null, src);
            isr = new InputStreamReader(is, CHARSET);
            br = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String str;
            do {
                str = br.readLine();
                if (str != null) {
                    sb.append(str);
                    sb.append(SFConstants.NEW_LINE_CR);
                }
            } while (str != null);

            if (sb.length() > 0) {
                return sb.toString();
            }

        } catch (Exception e) {
            SFLogger.error("Error reading stream: " + src, e);
            throw new MissingException("File not found: " + src);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    SFLogger.error("Error closing stream: " + src, e);
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (Exception e) {
                    SFLogger.error("Error closing stream: " + src, e);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    SFLogger.error("Error closing stream: " + src, e);
                }
            }
        }

        return null;
    }

    public static boolean isStringEmpty(String str) {
        //noinspection UnclearExpression,RedundantIfStatement
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return false;
    }
}
