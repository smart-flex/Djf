package ru.smartflex.djf.controller.helper;

import org.junit.Test;
import ru.smartflex.djf.controller.bean.PhoneBag;

import static org.junit.Assert.assertEquals;

public class PhoneZoneUtilTest {

    @Test
    public void testOnZones() {

        PhoneBag phoneBag = null;

        phoneBag = PhoneZoneUtil.formatPhoneWithZone("8", "+1919 825 76 5",14);
        assertEquals("+1 919 825 76 58", phoneBag.getPhoneFormatted());
        assertEquals("US", phoneBag.getPhoneZone().getMainZone());

        phoneBag = PhoneZoneUtil.formatPhoneWithZone("8", "+1919825765",11);
        assertEquals("+1 919 825 76 58", phoneBag.getPhoneFormatted());

        phoneBag = PhoneZoneUtil.formatPhoneWithZone("8", "+1242825765",11);
        assertEquals("+1242 825 76 58", phoneBag.getPhoneFormatted());

        phoneBag = PhoneZoneUtil.formatPhoneWithZone("8", "+1242",5);
        assertEquals("+1242 8", phoneBag.getPhoneFormatted());

        // insert into 2 position
        phoneBag = PhoneZoneUtil.formatPhoneWithZone("2", "+1428",2);
        assertEquals("+1242 8", phoneBag.getPhoneFormatted());
        assertEquals("BS", phoneBag.getPhoneZone().getMainZone());

        phoneBag = PhoneZoneUtil.formatPhoneWithZone("8", "+61416",6);
        assertEquals("+61 416 8", phoneBag.getPhoneFormatted());
        assertEquals("AU", phoneBag.getPhoneZone().getMainZone());

        phoneBag = PhoneZoneUtil.formatPhoneWithZone("+", "",0);
        assertEquals("+", phoneBag.getPhoneFormatted());

        phoneBag = PhoneZoneUtil.formatPhoneWithZone("54315");
        assertEquals("5 43 15", phoneBag.getPhoneFormatted());

        phoneBag = PhoneZoneUtil.formatPhoneWithZone("284694");
        assertEquals("28 46 94", phoneBag.getPhoneFormatted());

        phoneBag = PhoneZoneUtil.formatPhoneWithZone("8513050");
        assertEquals("851 30 50", phoneBag.getPhoneFormatted());

        phoneBag = PhoneZoneUtil.formatPhoneWithZone("9198257658");
        assertEquals("+7 919 825 76 58", phoneBag.getPhoneFormatted());

        phoneBag = PhoneZoneUtil.formatPhoneWithZone("79198257658");
        assertEquals("+7 919 825 76 58", phoneBag.getPhoneFormatted());

        phoneBag = PhoneZoneUtil.formatPhoneWithZone("89172094006");
        assertEquals("+7 917 209 40 06", phoneBag.getPhoneFormatted());

        System.out.println(phoneBag.getPhoneFormatted() + " " + phoneBag.getPhoneZone()) ;
    }
}
