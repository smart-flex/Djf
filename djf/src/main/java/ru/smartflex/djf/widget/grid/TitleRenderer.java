package ru.smartflex.djf.widget.grid;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;

import ru.smartflex.djf.DjfConfigurator;
import ru.smartflex.djf.controller.bean.BeanFormDefProperty;
import ru.smartflex.djf.controller.bean.LabelBundle;
import ru.smartflex.djf.controller.helper.PrefixUtil;
import ru.smartflex.djf.model.gen.ItemGridByteType;
import ru.smartflex.djf.model.gen.ItemGridCheckboxType;
import ru.smartflex.djf.model.gen.ItemGridComboboxType;
import ru.smartflex.djf.model.gen.ItemGridDateType;
import ru.smartflex.djf.model.gen.ItemGridIntType;
import ru.smartflex.djf.model.gen.ItemGridLongType;
import ru.smartflex.djf.model.gen.ItemGridNumType;
import ru.smartflex.djf.model.gen.ItemGridPeriodType;
import ru.smartflex.djf.model.gen.ItemGridShortType;
import ru.smartflex.djf.model.gen.ItemGridTextType;
import ru.smartflex.djf.model.gen.ItemTreeGridCellType;
import ru.smartflex.djf.tool.OtherUtil;

public class TitleRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = -211823726904291514L;
    private static ImageIcon iconStatusInfo = null;

    private String title = null;

    TitleRenderer(String tips) {
        setupInfoRenderer(tips);
    }

    TitleRenderer(ItemGridTextType column, LabelBundle bundle) {
        setupHeader(column.getTitle(), column.getTips(), bundle);
    }

    TitleRenderer(ItemGridDateType column, LabelBundle bundle) {
        setupHeader(column.getTitle(), column.getTips(), bundle);
    }

    TitleRenderer(ItemGridCheckboxType column, LabelBundle bundle) {
        setupHeader(column.getTitle(), column.getTips(), bundle);
    }

    TitleRenderer(ItemGridComboboxType column, LabelBundle bundle) {
        setupHeader(column.getTitle(), column.getTips(), bundle);
    }

    TitleRenderer(ItemTreeGridCellType column, LabelBundle bundle) {
        setupHeader(column.getTitle(), column.getTips(), bundle);
    }

    TitleRenderer(ItemGridPeriodType column, LabelBundle bundle) {
        setupHeader(column.getTitle(), column.getTips(), bundle);
    }

    TitleRenderer(ItemGridByteType column, LabelBundle bundle) {
        setupHeader(column.getTitle(), column.getTips(), bundle);
    }

    TitleRenderer(ItemGridShortType column, LabelBundle bundle) {
        setupHeader(column.getTitle(), column.getTips(), bundle);
    }

    TitleRenderer(ItemGridIntType column, LabelBundle bundle) {
        setupHeader(column.getTitle(), column.getTips(), bundle);
    }

    TitleRenderer(ItemGridLongType column, LabelBundle bundle) {
        setupHeader(column.getTitle(), column.getTips(), bundle);
    }

    TitleRenderer(ItemGridNumType column, LabelBundle bundle) {
        setupHeader(column.getTitle(), column.getTips(), bundle);
    }

    static ImageIcon getIconStatusInfo() {
        return iconStatusInfo;
    }

    void markTitleAsNotNull(BeanFormDefProperty prop) {

        // override color
        if (prop != null && prop.getNotNull() != null
                && prop.getNotNull()) {
            super.setForeground(Color.BLUE);
        }

    }

    void setupBackground(boolean noEdit) {

        if (!noEdit) {
            setBackground(DjfConfigurator.GRID_COLUMN_HEAD_NOEDIT_BACKGROUND);
        } else {
            setBackground(DjfConfigurator.GRID_COLUMN_HEAD_BACKGROUND);
        }

    }

    private void setupHeader(String colTitle, String colTips, LabelBundle bundle) {
        setOpaque(true);

        setForeground(DjfConfigurator.GRID_COLUMN_HEAD_FOREGROUND);

        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);

        if (colTitle != null) {
            title = PrefixUtil.getMsg(colTitle, bundle);
            if (colTips == null) {
                setToolTipText(title);
            } else {
                String tips = PrefixUtil.getMsg(colTips, bundle);
                setToolTipText(tips);
            }
        }

    }

    private void setupInfoRenderer(String colTips) {
        setForeground(DjfConfigurator.GRID_COLUMN_HEAD_FOREGROUND);

        setBackground(DjfConfigurator.GRID_COLUMN_HEAD_NOEDIT_BACKGROUND);

        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);

        String tips = PrefixUtil.getMsg(colTips, null);
        if (tips != null) {
            setToolTipText(tips);
        }

        if (iconStatusInfo == null) {
            iconStatusInfo = OtherUtil.loadSFImages("information.png");
        }
        setIcon(iconStatusInfo);

    }

    public String getTitle() {
        return title;
    }

}
