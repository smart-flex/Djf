package ru.smartflex.djf.controller.helper;

import org.junit.Test;
import ru.smartflex.djf.controller.bean.SnilsBag;

import static org.junit.Assert.assertEquals;

public class SnilsUtilTest {

    @Test
    public void testSnils() {
        SnilsBag snilsBag = null;

        snilsBag = SnilsUtil.formatSnils("0", "", 0);
        assertEquals("0", snilsBag.getSnilsFormatted());

        snilsBag = SnilsUtil.formatSnils("0", null, 0);
        assertEquals("0", snilsBag.getSnilsFormatted());

        snilsBag = SnilsUtil.formatSnils("5", "04", 1);
        assertEquals("054-", snilsBag.getSnilsFormatted());

        snilsBag = SnilsUtil.formatSnils("3", "044-926-593 8", 13);
        assertEquals("044-926-593 83", snilsBag.getSnilsFormatted());

        snilsBag = SnilsUtil.formatSnils("044-926-593 83");
        assertEquals("044-926-593 83", snilsBag.getSnilsFormatted());
    }
}
