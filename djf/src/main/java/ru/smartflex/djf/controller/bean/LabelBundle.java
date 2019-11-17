package ru.smartflex.djf.controller.bean;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.PropertyResourceBundle;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.controller.exception.MissingException;
import ru.smartflex.djf.tool.OtherUtil;

public class LabelBundle {

    private static final String CHARSET = "UTF-8";

    private PropertyResourceBundle bundle = null;

    public void load(String fileName) {
        InputStream is = null;
        InputStreamReader isr = null;
        try {
            is = OtherUtil.getBodyAsStream(Djf.getConfigurator()
                    .getPathToPanel(), fileName);
            isr = new InputStreamReader(is, CHARSET);

            bundle = new PropertyResourceBundle(isr);
        } catch (Exception e) {
            SFLogger.error("Error reading bundle: " + fileName, e);
            throw new MissingException("File not found: " + fileName);
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (Exception e) {
                    SFLogger.error("Error closing bundle: " + fileName, e);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    SFLogger.error("Error closing bundle: " + fileName, e);
                }
            }
        }
    }

    public String getMsg(String key) {
        if (key == null) {
            return null;
        }
        //noinspection CatchMayIgnoreException
        try {
            return bundle.getString(key);
        } catch (Exception e) {
        }
        return null;
    }

}
