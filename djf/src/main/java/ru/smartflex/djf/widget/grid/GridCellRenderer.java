package ru.smartflex.djf.widget.grid;

import javax.swing.table.DefaultTableCellRenderer;

import ru.smartflex.djf.AlignTypeEnum;
import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.builder.ItemBuilder;
import ru.smartflex.djf.controller.bean.BeanFormDefProperty;

class GridCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 2310792351131962620L;

    GridCellRenderer() {
    }

    @SuppressWarnings("MagicConstant")
    void setHorAligment(AlignTypeEnum type) {
        setHorizontalAlignment(ItemBuilder.getSwingAligmentConstant(type));
    }

    void setUpBackGroundAsNotNull(BeanFormDefProperty prop) {

        if (prop != null && prop.getNotNull() != null
                && prop.getNotNull()) {
            super.setBackground(SFConstants.FIELD_REQUIRED_BACKGROUND_COLOR);
        }
    }
}
