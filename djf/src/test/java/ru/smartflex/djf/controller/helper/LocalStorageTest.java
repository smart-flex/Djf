package ru.smartflex.djf.controller.helper;

import org.junit.Test;
import ru.smartflex.djf.tool.LocalStorage;

import static org.junit.Assert.assertEquals;

public class LocalStorageTest {

    @Test
    public void writeReadTest() {
        LocalStorage.getValue("idTest");
        LocalStorage.setValue("idTest", 10);
        String valIdTest = LocalStorage.getValue("idTest");
        LocalStorage.saveSettings();
        assertEquals("10", valIdTest);
    }
}
