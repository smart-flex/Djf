package ru.smartflex.djf.tool;

import ru.smartflex.djf.SFLogger;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LocalStorage {
    private static final String FILE_SETTINGS_NAME = "sf.settings.txt";
    private static final String PROPERTIES_COMMENT = "DJF settings";
    private static Properties settings = new Properties();
    private static AtomicBoolean wasRead = new AtomicBoolean(false);
    private static Lock lockReading = new ReentrantLock(false);
    private static File fileSettings = null;

    public static String getValue(String idWidget) {
        if (idWidget == null) {
            return null;
        }
        readSettingsFile();
        return settings.getProperty(idWidget);
    }

    public static void setValue(String idWidget, String value) {
        if (idWidget != null && wasRead.get()) {
            lockReading.lock();
            try {
                if (value == null) {
                    settings.remove(idWidget);
                } else {
                    settings.setProperty(idWidget, value);
                }
            } finally {
                lockReading.unlock();
            }
        }
    }

    public static void setValue(String idWidget, Integer value) {
        if (idWidget != null && wasRead.get()) {
            lockReading.lock();
            try {
                if (value == null) {
                    settings.remove(idWidget);
                } else {
                    settings.setProperty(idWidget, value.toString());
                }
            } finally {
                lockReading.unlock();
            }
        }
    }

    public static void saveSettings() {
        if (!wasRead.get()) {
            return;
        }
        if (fileSettings.exists()) {
            fileSettings.delete();
        }
        if (settings.size() == 0) {
            return;
        }
        lockReading.lock();
        try {
            OutputStream out = new FileOutputStream(fileSettings);
            settings.store(out, PROPERTIES_COMMENT);
            out.close();
        } catch (Exception e) {
            SFLogger.error("Error writing: "+FILE_SETTINGS_NAME, e);
        } finally {
            lockReading.unlock();
        }
    }

    private static void readSettingsFile() {
        if (!wasRead.get()) {
            lockReading.lock();
            try {
                if (!wasRead.get()) {
                    Properties sett = new Properties();
                    defineFileSettingsLocation();
                    if (fileSettings.exists()) {
                        InputStream inf = new FileInputStream(fileSettings);
                        sett.load(inf);
                        inf.close();
                    }
                    settings = sett;
                    wasRead.set(true);
                }
            } catch (Exception e) {
                SFLogger.error("Error reading: "+FILE_SETTINGS_NAME, e);
            } finally {
                lockReading.unlock();
            }
        }
    }

    private static void defineFileSettingsLocation() {
        String fileName = FILE_SETTINGS_NAME;
        String pathClient = System.getProperty("user.dir");
        fileSettings = new File(pathClient, fileName);
    }

}
