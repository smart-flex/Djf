package ru.smartflex.djf.widget.grid;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.TreeModel;

import ru.smartflex.djf.SmartFlexException;
import ru.smartflex.djf.WidgetTypeEnum;
import ru.smartflex.djf.builder.ItemBuilder;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.WidgetManagerHelper;
import ru.smartflex.djf.controller.bean.BeanFormDef;
import ru.smartflex.djf.controller.bean.GridColumnInfo;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.bean.tree.BeanStatusEnum;
import ru.smartflex.djf.controller.bean.tree.IBeanWrapper;
import ru.smartflex.djf.controller.bean.tree.TreeListUtils;
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
import ru.smartflex.djf.model.gen.ItemInputType;
import ru.smartflex.djf.model.gen.ItemPeriodBaseType;
import ru.smartflex.djf.model.gen.ItemTreeGridCellType;
import ru.smartflex.djf.tool.OtherUtil;
import ru.smartflex.djf.widget.SFComboBox;
import ru.smartflex.djf.widget.tgrid.SFTGridCellWidget;

public class SFGridModel extends DefaultTableModel {

    private static final long serialVersionUID = 2374706119835094354L;

    private List<GridColumnInfo> titleList = new ArrayList<GridColumnInfo>();

    private WidgetManager wm = null;
    private UIWrapper uiw = null;

    private static ImageIcon iconLocked = null;
    private static ImageIcon iconDeleted = null;
    private static ImageIcon iconNew = null;
    private static ImageIcon iconChanged = null;
    private static ImageIcon iconSelected = null;

    private boolean treeExisted = false;
    private boolean comboboxChildExisted = false;

    public SFGridModel() {
    }

    protected void init() {
        if (iconLocked == null) {
            iconLocked = OtherUtil.loadSFImages("padlock_closed.png");
            iconDeleted = OtherUtil.loadSFImages("cut.png");
            iconNew = OtherUtil.loadSFImages("new.png");
            iconChanged = OtherUtil.loadSFImages("edit.png");
            iconSelected = OtherUtil.loadSFImages("bullet_green.png");
        }
    }

    GridColumnInfo getGridColumnInfo(int index) {
        return titleList.get(index);
    }

    List<GridColumnInfo> getListColumnInfo() {
        return titleList;
    }

    void reloadModel() {
        int rowCount = wm.getAmountRow(uiw.getModelBase());
        if (rowCount == 0) {
            throw new SmartFlexException(
                    "There must be at least one row. Perhaps, there is missed set tag definition for bean.");
        }

        // tree cell model initialization code
        for (GridColumnInfo gci : titleList) {
            if (gci.getWidgetType() == WidgetTypeEnum.TGRID_TREE_FIELD) {
                SFTGridCellWidget tree = (SFTGridCellWidget) gci
                        .getCelRenderer();
                if (tree != null) {
                    if (tree.getModel() == null) {
                        tree.setModel((TreeModel) this);
                    }
                }
            }
        }

        super.setRowCount(rowCount);
    }

    @SuppressWarnings({"rawtypes"})
    public Class getColumnClass(int c) {

        if (c == 0) {
            if (uiw.isGridInfoColumnAllowed()) {
                return ImageIcon.class; // first column is info column
            } else {
                return Object.class;
            }
        } else {
            return Object.class;
        }
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (!wm.getFormBag().isFormReady()) {
            return null;
        }

        if ((rowIndex >= 0 && rowIndex < getRowCount())
                && (columnIndex >= 0 && columnIndex < titleList.size())) {

            if (columnIndex == 0) {
                if (uiw.isGridInfoColumnAllowed()) {
                    ImageIcon status = null;
                    IBeanWrapper bw = wm.getBeanWrapper(uiw.getModelBase(),
                            rowIndex);
                    BeanStatusEnum bs = WidgetManagerHelper
                            .isBeanWrapperLocked(wm, uiw, bw, rowIndex);
                    if (bs != null) {
                        switch (bs) {
                            case LOCKED:
                                status = iconLocked;
                                break;
                            case DELETED:
                                status = iconDeleted;
                                break;
                            case SELECTED:
                                status = iconSelected;
                                break;
                        }
                    } else {
                        BeanStatusEnum cStatus = bw.getCreatedStatus();
                        if (cStatus == BeanStatusEnum.NEW) {
                            status = iconNew;
                        } else if (cStatus == BeanStatusEnum.PERSISTENT) {
                            BeanStatusEnum oStatus = bw.getObtainedStatus();
                            if (oStatus != null) {
                                if (oStatus == BeanStatusEnum.CHANGED) {
                                    status = iconChanged;
                                }
                            }
                        }
                    }
                    return status;
                } else {
                    return returnCellObject(rowIndex, columnIndex);

                }
            } else {
                return returnCellObject(rowIndex, columnIndex);
            }

        }

        return null;
    }

    private Object returnCellObject(int rowIndex, int columnIndex) {
        Object obj;

        obj = wm.getValueScrollWidget(this.uiw, rowIndex,
                titleList.get(columnIndex).getModelBase().getProperty(), true);

        GridColumnInfo cmb = titleList.get(columnIndex);
        if (obj != null
                && cmb.getWidgetType() == WidgetTypeEnum.COMBOBOX) {

            if (cmb.getPropertySet() != null) {
                obj = ((SFComboBox) cmb.getObjectUI()).getObject(cmb.getPropertySet(), obj);
            }

            String prop = titleList.get(columnIndex).getModelFill()
                    .getProperty();
            if (prop != null) {
                obj = TreeListUtils.getPropertyValue(obj, prop);
            }
        }
        obj = titleList.get(columnIndex).getFormattedDataForGrid(obj);

        return obj;
    }

    public String getPropertyNameOfModelBase(int columnIndex) {
        return titleList.get(columnIndex).getModelBase().getProperty();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (!uiw.isEnabledDisabledStaticBehavior()) {
            return false;
        } else {
            if (rowIndex == 0 && treeExisted) {
                // closes root node tree
                return false;
            } else {
                if (columnIndex == 0) {
                    if (uiw.isGridInfoColumnAllowed()) {
                        return false;
                    } else {
                        return isUsualCellEditable(rowIndex, columnIndex);
                    }
                } else {
                    return isUsualCellEditable(rowIndex, columnIndex);
                }
            }
        }
    }

    private boolean isUsualCellEditable(int rowIndex, int columnIndex) {
        boolean fok = false;
        if ((rowIndex >= 0 && rowIndex < getRowCount())
                && (columnIndex >= 0 && columnIndex < titleList.size())) {

            UIWrapper uiwColumn = titleList.get(columnIndex);
            if (!uiwColumn.isEnabledDisabledStaticBehavior()) {
                return false;
            }

            IBeanWrapper bw = wm.getBeanWrapper(uiw.getModelBase(), rowIndex);
            if (WidgetManagerHelper.isBeanWrapperLocked(wm, uiw, bw, rowIndex) == null) {
                fok = true;
            }
        }
        return fok;
    }

    void addColumnInfo() {

        GridColumnInfo titleInfo = new GridColumnInfo();
        titleInfo.setWrapperInGrid(true);

        titleList.add(titleInfo);
    }

    GridColumnInfo addColumn(WidgetTypeEnum type,
                             Object columnDefinition, BeanFormDef beanDef, String bindPrefix) {

        ColumnWidth columnWidth = null;
        Boolean noResize = null;

        GridColumnInfo colInfo = new GridColumnInfo();
        colInfo.setWrapperInGrid(true);

        switch (type) {
            case TEXT:
                ItemBuilder
                        .fillTextBase(colInfo, type,
                                (ItemInputType) columnDefinition, beanDef, true,
                                bindPrefix, false);

                columnWidth = new ColumnWidth(
                        ((ItemGridTextType) columnDefinition).getWidth());
                noResize = UIWrapper
                        .translateStringToBoolean(((ItemGridTextType) columnDefinition)
                                .getNoResize(), false);
                break;
            case DATE:
                ItemBuilder.fillDateBase(colInfo,
                        (ItemGridDateType) columnDefinition, beanDef, bindPrefix);

                columnWidth = new ColumnWidth(
                        ((ItemGridDateType) columnDefinition).getWidth());
                noResize = UIWrapper
                        .translateStringToBoolean(((ItemGridDateType) columnDefinition)
                                .getNoResize(), false);
                break;
            case CHECKBOX:
                ItemBuilder.fillCheckBase(colInfo,
                        (ItemGridCheckboxType) columnDefinition, beanDef,
                        bindPrefix);

                columnWidth = new ColumnWidth(
                        ((ItemGridCheckboxType) columnDefinition).getWidth());
                noResize = UIWrapper
                        .translateStringToBoolean(((ItemGridCheckboxType) columnDefinition)
                                .getNoResize(), false);
                break;
            case COMBOBOX:
                ItemBuilder.fillComboboxBase(colInfo,
                        (ItemGridComboboxType) columnDefinition, beanDef,
                        bindPrefix);

                columnWidth = new ColumnWidth(
                        ((ItemGridComboboxType) columnDefinition).getWidth());
                noResize = UIWrapper
                        .translateStringToBoolean(((ItemGridComboboxType) columnDefinition)
                                .getNoResize(), false);
                if (colInfo.getParentPropertyName() != null) {
                    comboboxChildExisted = true;
                }
                break;
            case TGRID_TREE_FIELD:
                treeExisted = true;

                ItemBuilder.fillTgridTreeFieldBase(
                        colInfo, type, (ItemTreeGridCellType) columnDefinition,
                        beanDef, true, bindPrefix);

                columnWidth = new ColumnWidth(
                        ((ItemTreeGridCellType) columnDefinition).getWidth());
                noResize = UIWrapper
                        .translateStringToBoolean(((ItemTreeGridCellType) columnDefinition)
                                .getNoResize(), false);
                break;
            case PERIOD:
                ItemBuilder.fillPeriodBase(colInfo,
                        (ItemPeriodBaseType) columnDefinition, beanDef, bindPrefix);

                columnWidth = new ColumnWidth(
                        ((ItemGridPeriodType) columnDefinition).getWidth());
                noResize = UIWrapper
                        .translateStringToBoolean(((ItemGridPeriodType) columnDefinition)
                                .getNoResize(), false);
                break;

            case BYTE:
                ItemBuilder.fillTextBase(colInfo, type,
                        (ItemInputType) columnDefinition, beanDef, false,
                        bindPrefix, false);

                columnWidth = new ColumnWidth(
                        ((ItemGridByteType) columnDefinition).getWidth());
                noResize = UIWrapper
                        .translateStringToBoolean(((ItemGridByteType) columnDefinition)
                                .getNoResize(), false);
                break;

            case INT:
                ItemBuilder.fillTextBase(colInfo, type,
                        (ItemInputType) columnDefinition, beanDef, false,
                        bindPrefix, false);

                columnWidth = new ColumnWidth(
                        ((ItemGridIntType) columnDefinition).getWidth());
                noResize = UIWrapper
                        .translateStringToBoolean(((ItemGridIntType) columnDefinition)
                                .getNoResize(), false);
                break;

            case SHORT:
                ItemBuilder.fillTextBase(colInfo, type,
                        (ItemInputType) columnDefinition, beanDef, false,
                        bindPrefix, false);

                columnWidth = new ColumnWidth(
                        ((ItemGridShortType) columnDefinition).getWidth());
                noResize = UIWrapper
                        .translateStringToBoolean(((ItemGridShortType) columnDefinition)
                                .getNoResize(), false);
                break;

            case LONG:
                ItemBuilder.fillTextBase(colInfo, type,
                        (ItemInputType) columnDefinition, beanDef, false,
                        bindPrefix, false);

                columnWidth = new ColumnWidth(
                        ((ItemGridLongType) columnDefinition).getWidth());
                noResize = UIWrapper
                        .translateStringToBoolean(((ItemGridLongType) columnDefinition)
                                .getNoResize(), false);
                break;

            case NUMERIC:
                ItemBuilder.fillTextBase(colInfo,
                        WidgetTypeEnum.NUMERIC, (ItemGridNumType) columnDefinition,
                        beanDef, false, bindPrefix, false);

                columnWidth = new ColumnWidth(
                        ((ItemGridNumType) columnDefinition).getWidth());
                noResize = UIWrapper
                        .translateStringToBoolean(((ItemGridNumType) columnDefinition)
                                .getNoResize(), false);
                break;
        }

        // only for defines this is info column or not
        colInfo.setColumnDefinition(columnDefinition);
        colInfo.setColumnWidth(columnWidth);
        colInfo.setNoResize(noResize);

        titleList.add(colInfo);

        return colInfo;
    }

    public String getColumnName(int column) {
        String columnName = null;
        int colAmn = titleList.size();
        if (column >= 0 && column < colAmn) {
            columnName = titleList.get(column).getTitle();
        }
        return columnName;
    }

    public int getColumnCount() {
        return titleList.size();
    }

    void setWidgetManager(WidgetManager widgetManager) {
        this.wm = widgetManager;
    }

    public void setUIWrapper(UIWrapper uIWrapper) {
        this.uiw = uIWrapper;
    }

    public WidgetManager getWidgetManager() {
        return wm;
    }

    public UIWrapper getUIWrapper() {
        return uiw;
    }

    boolean isComboboxChildExisted() {
        return comboboxChildExisted;
    }

}
