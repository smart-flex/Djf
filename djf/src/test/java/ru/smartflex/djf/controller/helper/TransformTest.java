package ru.smartflex.djf.controller.helper;

import org.junit.Test;
import ru.smartflex.djf.model.gen.AttrTransformType;
import ru.smartflex.djf.tool.OtherUtil;

import static org.junit.Assert.assertEquals;

public class TransformTest {

    @Test
    public void testRuEng() {
        String ru = "ПриВет!123";
        String eng = OtherUtil.transformText(AttrTransformType.RU_ENG, ru);
        assertEquals("GhbDtn!123", eng);
    }
}
