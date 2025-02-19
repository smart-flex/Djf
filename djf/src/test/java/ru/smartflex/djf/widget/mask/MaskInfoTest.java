package ru.smartflex.djf.widget.mask;

import org.junit.Test;
import ru.smartflex.djf.Djf;
import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.WidgetTypeEnum;

import static org.junit.Assert.assertEquals;

public class MaskInfoTest {

    @Test
    public void alignTest(){
        MaskInfo mi = new MaskInfo(SFConstants.DEFAULT_MASK_PERIOD,
                            Djf.getConfigurator().extractDelimitersFromPeriodMask(SFConstants.DEFAULT_MASK_PERIOD),
                            WidgetTypeEnum.PERIOD);

        String align1 = mi.alignTextToMask("202333", 0);
        assertEquals("2023-33", align1);
        String align2 = mi.alignTextToMask("2023", 0);
        assertEquals("2023", align2);
        String align3 = mi.alignTextToMask("2312", 2);
        assertEquals("23-12", align3);

    }
}
