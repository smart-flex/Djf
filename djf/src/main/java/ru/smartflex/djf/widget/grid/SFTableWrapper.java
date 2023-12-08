package ru.smartflex.djf.widget.grid;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import ru.smartflex.djf.WidgetTypeEnum;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.BeanFormDef;
import ru.smartflex.djf.controller.bean.BeanFormDefProperty;
import ru.smartflex.djf.controller.bean.GridColumnInfo;
import ru.smartflex.djf.controller.bean.LabelBundle;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.bean.tree.TreeListUtils;
import ru.smartflex.djf.controller.exception.MissingException;
import ru.smartflex.djf.controller.helper.AccessibleHelper;
import ru.smartflex.djf.model.gen.*;
import ru.smartflex.djf.tool.FontUtil;
import ru.smartflex.djf.widget.ItemHandler;
import ru.smartflex.djf.widget.SFComboBox;
import ru.smartflex.djf.widget.tgrid.SFTGrid;
import ru.smartflex.djf.widget.tgrid.SFTGridCellWidget;

public class SFTableWrapper extends JTable {

    private static final long serialVersionUID = -4351650553503317419L;

    private SFGridModel model = null;
    private static final int addWidthToColumnInfo = 4;
    private SFGrid grid;

    // for case of determine row and col for linked combobox
    private int currentPointMouseRow = -1;

    private Map<Integer, ColumnSettings> mapColSetting = new HashMap<Integer, ColumnSettings>();
    private GridResizeListener gridResizeListener;

    SFTableWrapper(SFGrid grid) {
        this.grid = grid;
        gridResizeListener = new GridResizeListener(this);
        this.addComponentListener(gridResizeListener);
    }

    public void closeSFTableWrapper() {
        this.removeComponentListener(gridResizeListener);
        mapColSetting.clear();
        gridResizeListener.closeGridResizeListener();
    }

    void determineCurrentPointMouse(java.awt.Point point) {
        // it is necessary because in opposite we have strange bug when there are two grids with combobox and we switch on other
        // grid by mouse clicking on combobox. As a result we have series events: focus gain and focus lost and then we got invoking
        // method setValueAt setValue
        SFGridMouseMotionFlag.setMotionFlag(true);

        grid.requestFocusOnNestedWidget();

        if (model != null && !model.isComboboxChildExisted()) {
            // to exclude unnecessary calculations
            return;
        }
        currentPointMouseRow = this.rowAtPoint(point);
        int currentPointMouseCol = this.columnAtPoint(point);

        // if columns were re-arranged
        int col = this.convertColumnIndexToModel(currentPointMouseCol);

        if (model != null
                && model.getListColumnInfo().get(col).getWidgetType() == WidgetTypeEnum.COMBOBOX) {

            SFComboBox box = (SFComboBox) model.getListColumnInfo().get(col)
                    .getObjectUI();
            box.reloadLinkedModel(null);
        }

        SFGridMouseMotionFlag.setMotionFlag(false);
    }

    @SuppressWarnings("DuplicateBranchesInSwitch")
    public void addColumns(List<Object> list, WidgetManager wm, UIWrapper uiw,
                           BeanFormDef beanDef, LabelBundle bundle, String bindPrefix) {

        double titleHeight = FontUtil.getIncreasedGridRowHeight() + 5;

        List<TitleRenderer> titleRenderers = new ArrayList<TitleRenderer>();

        // info column
        TitleRenderer trInfo = null;
        if (uiw.isGridInfoColumnAllowed()) {
            trInfo = new TitleRenderer("${djf.grid.info.tips}");
            titleRenderers.add(trInfo);
            model.addColumnInfo();
        }

        for (Object ct : list) {

            if (!AccessibleHelper.isAccessible(ct, wm)) {
                continue;
            }

            TitleRenderer tr = null;
            WidgetTypeEnum type = null;

            if (ct instanceof ItemGridTextType) {
                tr = new TitleRenderer((ItemGridTextType) ct, bundle);
                type = WidgetTypeEnum.TEXT;

            } else if (ct instanceof ItemGridDateType) {
                tr = new TitleRenderer((ItemGridDateType) ct, bundle);
                type = WidgetTypeEnum.DATE;

            } else if (ct instanceof ItemGridCheckboxType) {
                tr = new TitleRenderer((ItemGridCheckboxType) ct, bundle);
                type = WidgetTypeEnum.CHECKBOX;

            } else if (ct instanceof ItemGridComboboxType) {
                tr = new TitleRenderer((ItemGridComboboxType) ct, bundle);
                type = WidgetTypeEnum.COMBOBOX;

            } else if (ct instanceof ItemTreeGridCellType) {
                tr = new TitleRenderer((ItemTreeGridCellType) ct, bundle);
                type = WidgetTypeEnum.TGRID_TREE_FIELD;

            } else if (ct instanceof ItemGridPeriodType) {
                tr = new TitleRenderer((ItemGridPeriodType) ct, bundle);
                type = WidgetTypeEnum.PERIOD;

            } else if (ct instanceof ItemGridByteType) {
                tr = new TitleRenderer((ItemGridByteType) ct, bundle);
                type = WidgetTypeEnum.BYTE;

            } else if (ct instanceof ItemGridShortType) {
                tr = new TitleRenderer((ItemGridShortType) ct, bundle);
                type = WidgetTypeEnum.SHORT;

            } else if (ct instanceof ItemGridIntType) {
                tr = new TitleRenderer((ItemGridIntType) ct, bundle);
                type = WidgetTypeEnum.INT;

            } else if (ct instanceof ItemGridLongType) {
                tr = new TitleRenderer((ItemGridLongType) ct, bundle);
                type = WidgetTypeEnum.LONG;

            } else if (ct instanceof ItemGridNumType) {
                tr = new TitleRenderer((ItemGridNumType) ct, bundle);
                type = WidgetTypeEnum.NUMERIC;

            } else if (ct instanceof ItemGridPhoneType) {
                tr = new TitleRenderer((ItemGridPhoneType) ct, bundle);
                type = WidgetTypeEnum.PHONE;

            }

            //noinspection ConstantConditions
            GridColumnInfo colInfo = model.addColumn(type, ct, beanDef, bindPrefix);
            //noinspection ConstantConditions
            tr.setupBackground(colInfo.isEnabledDisabledStaticBehavior());

            if (colInfo.getBind() == null) {
                throw new MissingException(
                        "There is missed bind definition for table column");
            }
            titleRenderers.add(tr);
            colInfo.setTitle(tr.getTitle());

            if (tr.getPreferredSize().getHeight() > titleHeight) {
                titleHeight = tr.getPreferredSize().getHeight();
            }

        }

        super.setModel(model);

        if (titleHeight < 34) {
            // Workaround with arrows in right corner
            titleHeight = 34;
        }

        int indColumn = 0;

        if (uiw.isGridInfoColumnAllowed()) {
            if (TitleRenderer.getIconStatusInfo().getIconHeight() > titleHeight) {
                titleHeight = TitleRenderer.getIconStatusInfo().getIconHeight();
            }
            //noinspection ConstantConditions
            trInfo.setPreferredSize(new Dimension(TitleRenderer
                    .getIconStatusInfo().getIconWidth(), (int) titleHeight));
            TableColumn tableColumnInfo = getColumnModel().getColumn(0);
            tableColumnInfo.setHeaderRenderer(trInfo);
            int widthInfo = TitleRenderer.getIconStatusInfo().getIconWidth()
                    + addWidthToColumnInfo;
            tableColumnInfo.setPreferredWidth(widthInfo);
            tableColumnInfo.setMinWidth(widthInfo);
            tableColumnInfo.setMaxWidth(widthInfo);
            mapColSetting.put(indColumn, new ColumnSettings(widthInfo, widthInfo));
            indColumn = 1;
        }

        List<GridColumnInfo> titleList = model.getListColumnInfo();
        for (GridColumnInfo gci : titleList) {
            Object ct = gci.getColumnDefinition();
            if (ct == null) {
                // skip info column
                continue;
            }
            TableColumn tableColumn = null;
            TableCellRenderer cr = null;

            ColumnWidth columnWidth = gci.getColumnWidth();

            switch (gci.getWidgetType()) {
                case TEXT: {
                    cr = new GridCellRenderer();

                    tableColumn = getColumnModel().getColumn(indColumn);

                    BeanFormDefProperty prop = gci.getBeanFormDefPropertyFromBind();

                    ItemHandler.setupHandlerToColumn(tableColumn,
                            WidgetTypeEnum.TEXT, wm, uiw, gci, prop, null);

                    ((GridCellRenderer) cr).setHorAligment(gci.getAlign());
                    ((GridCellRenderer) cr).setUpBackGroundAsNotNull(prop);

                    titleRenderers.get(indColumn).markTitleAsNotNull(prop);
                }
                break;
                case DATE: {
                    cr = new GridCellRenderer();
                    tableColumn = getColumnModel().getColumn(indColumn);

                    BeanFormDefProperty prop = gci.getBeanFormDefPropertyFromBind();

                    ItemHandler.setupHandlerToColumn(tableColumn, wm, uiw, gci);

                    ((GridCellRenderer) cr).setHorAligment(gci.getAlign());
                    ((GridCellRenderer) cr).setUpBackGroundAsNotNull(prop);
                    titleRenderers.get(indColumn).markTitleAsNotNull(prop);
                }
                break;
                case CHECKBOX: {
                    cr = new CheckBoxCellRenderer(wm);

                    tableColumn = getColumnModel().getColumn(indColumn);
                    tableColumn.setCellRenderer(cr);

                    BeanFormDefProperty prop = gci.getBeanFormDefPropertyFromBind();

                    ItemHandler.setupHandlerToColumn(tableColumn,
                            WidgetTypeEnum.CHECKBOX, wm, uiw, gci, prop, this);

                    ((CheckBoxCellRenderer) cr).setUpBackGroundAsNotNull(prop);
                    titleRenderers.get(indColumn).markTitleAsNotNull(prop);
                }
                break;
                case COMBOBOX: {
                    cr = new ComboBoxCellRenderer(wm);

                    tableColumn = getColumnModel().getColumn(indColumn);
                    tableColumn.setCellRenderer(cr);

                    BeanFormDefProperty prop = gci.getBeanFormDefPropertyFromBind();

                    SFComboBox box = (SFComboBox) gci.getObjectUI();
                    box.setTable(this);

                    ItemHandler.setupHandlerToColumn(tableColumn,
                            WidgetTypeEnum.COMBOBOX, wm, uiw, gci, prop, this);

                    ((GridCellRenderer) cr).setUpBackGroundAsNotNull(prop);
                    titleRenderers.get(indColumn).markTitleAsNotNull(prop);
                }
                break;
                case TGRID_TREE_FIELD: {
                    // ранее до 8-12-2023 передавался (int) maxHeight. Заменен внутри на table.getRowHeight()
                    cr = new SFTGridCellWidget(this, wm, model, indColumn, columnWidth);
                    gci.setCelRenderer(cr);

                    tableColumn = getColumnModel().getColumn(indColumn);
                    tableColumn.setCellRenderer(cr);

                    ItemHandler.setupHandlerToColumn(tableColumn,
                            WidgetTypeEnum.TGRID_TREE_FIELD, wm, uiw, gci, null,
                            null);

                    SFTGrid treeGrid = (SFTGrid) grid;
                    treeGrid.setTreeGridWidget((SFTGridCellWidget) cr);
                }
                break;
                case PERIOD: {
                    cr = new GridCellRenderer();
                    tableColumn = getColumnModel().getColumn(indColumn);

                    BeanFormDefProperty prop = gci.getBeanFormDefPropertyFromBind();

                    ItemHandler.setupHandlerToColumn(tableColumn, wm, uiw, gci);

                    ((GridCellRenderer) cr).setHorAligment(gci.getAlign());
                    ((GridCellRenderer) cr).setUpBackGroundAsNotNull(prop);
                    titleRenderers.get(indColumn).markTitleAsNotNull(prop);
                }
                break;
                case BYTE:
                case SHORT:
                case INT:
                case LONG: {
                    cr = new GridCellRenderer();
                    tableColumn = getColumnModel().getColumn(indColumn);

                    BeanFormDefProperty prop = gci.getBeanFormDefPropertyFromBind();

                    ItemHandler.setupHandlerToColumn(tableColumn, wm, uiw, gci);

                    ((GridCellRenderer) cr).setHorAligment(gci.getAlign());
                    ((GridCellRenderer) cr).setUpBackGroundAsNotNull(prop);
                    titleRenderers.get(indColumn).markTitleAsNotNull(prop);
                }
                break;
                case NUMERIC: {
                    cr = new GridCellRenderer();
                    tableColumn = getColumnModel().getColumn(indColumn);

                    BeanFormDefProperty prop = gci.getBeanFormDefPropertyFromBind();
                    ItemHandler.setupHandlerToColumn(tableColumn, wm, uiw, gci);

                    ((GridCellRenderer) cr).setHorAligment(gci.getAlign());
                    ((GridCellRenderer) cr).setUpBackGroundAsNotNull(prop);
                    titleRenderers.get(indColumn).markTitleAsNotNull(prop);
                }
                break;
                case PHONE: {
                    cr = new GridCellRenderer();
                    tableColumn = getColumnModel().getColumn(indColumn);

                    BeanFormDefProperty prop = gci.getBeanFormDefPropertyFromBind();

                    ItemHandler.setupHandlerToColumn(tableColumn, wm, uiw, gci);

                    ((GridCellRenderer) cr).setHorAligment(gci.getAlign());
                    ((GridCellRenderer) cr).setUpBackGroundAsNotNull(prop);
                    titleRenderers.get(indColumn).markTitleAsNotNull(prop);
                }
                break;

            }

            TitleRenderer tr = titleRenderers.get(indColumn);
            tr.setPreferredSize(new Dimension(columnWidth.getPrefWidth(),
                    (int) titleHeight));

            //noinspection ConstantConditions
            tableColumn.setHeaderRenderer(tr);

            ColumnSettings cs;
            if (columnWidth.getPrefWidth() > 0) {
                tableColumn.setPreferredWidth(columnWidth.getPrefWidth());
                cs = new ColumnSettings(columnWidth.getMinWidth(), columnWidth.getPrefWidth());
            } else {
                cs = new ColumnSettings(columnWidth.getMinWidth());
            }
            tableColumn.setMinWidth(columnWidth.getMinWidth());
            mapColSetting.put(indColumn, cs);

            // last column is to not set to maximum width
            // if (index < titleList.size()) {
            // tableColumn.setMaxWidth(width);
            // }
            if (gci.getNoResize() != null && gci.getNoResize()) {
                tableColumn.setMaxWidth(Math.max(columnWidth.getMinWidth(),
                        columnWidth.getPrefWidth()));
                tableColumn.setResizable(false);
            }

            tableColumn.setCellRenderer(cr);
            indColumn++;
        }
    }

    public void setModel(SFGridModel model) {
        this.model = model;
    }

    public void setValueAt(Object aValue, int row, int column) {
        GridColumnInfo gci = model.getListColumnInfo().get(column);
        //noinspection SwitchStatementWithTooFewBranches
        switch (gci.getWidgetType()) {
            case COMBOBOX:
                // because action listener and cell listener sets value into
                // previous row :(
                if (((SFComboBox) gci.getObjectUI()).getSelectedIndex() == -1) {
                    // workarround: case when mouse pointer moved in other row
                    // contained combobox
                    return;
                }
                Object val = ((SFComboBox) gci.getObjectUI())
                        .getSelectedMirrorObject();

                if (gci.getPropertySet() != null) {
                    // for pSet tag in combobox
                    if (val != null) {
                        val = TreeListUtils.getPropertyValue(val, gci.getPropertySet());
                    }
                }
                grid.getWidgetManager().setValueScrollWidget(grid.getUIWrapper(),
                        gci.getModelBase().getProperty(), val);

                String childPropertyName = ((SFComboBox) gci.getObjectUI())
                        .getChildPropertyName();
                if (childPropertyName != null) {
                    // because parent was changed then child must be set to null
                    // today is realized only one-to-one relationship. See XSD
                    // combobox model
                    grid.getWidgetManager().setValueScrollWidget(
                            grid.getUIWrapper(), childPropertyName, null);
                }

                break;
            default:
                super.setValueAt(aValue, row, column);
                break;
        }
    }

    public int getCurrentPointMouseRow() {
        return currentPointMouseRow;
    }

    class ColumnSettings {
        private Integer minWidth;
        private Integer prefWidth = null;

        public ColumnSettings(Integer minWidth) {
            this.minWidth = minWidth;
        }

        public ColumnSettings(Integer minWidth, Integer prefWidth) {
            this.minWidth = minWidth;
            this.prefWidth = prefWidth;
        }

        public Integer getMinWidth() {
            return minWidth;
        }

        public Integer getPrefWidth() {
            return prefWidth;
        }

        @Override
        public String toString() {
            return "ColumnSettings{" +
                    "minWidth=" + minWidth +
                    ", prefWidth=" + prefWidth +
                    '}';
        }
    }

    class GridResizeListener extends ComponentAdapter {
        private SFTableWrapper table;

        public GridResizeListener(SFTableWrapper table) {
            this.table = table;
        }
        public void closeGridResizeListener() {
            table = null;
        }

        public void componentResized(ComponentEvent e) {

            // если уже установлен AUTO_RESIZE_OFF то смена на другие режимы уже не работает, мало того приводит к мерцанию (зацикливанию)
            if (table.getAutoResizeMode() == JTable.AUTO_RESIZE_OFF) {
                return;
            }
            int colsWidthTotal = 0;

            for (int i = 0; i < getColumnCount(); i++) {
                ColumnSettings cs = mapColSetting.get(i);
                colsWidthTotal += cs.getMinWidth();
            }

            // выставляем режим, в зависимости от ширины колонок
            if (table.getWidth() < colsWidthTotal) {
                // включаем скроллинг колонок
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            }
        }
    }
}
