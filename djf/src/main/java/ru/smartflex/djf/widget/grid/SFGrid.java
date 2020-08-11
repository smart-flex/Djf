package ru.smartflex.djf.widget.grid;

import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.GridColumnInfo;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.widget.IRequestFocus;
import ru.smartflex.djf.widget.SFArrowKnob;

public class SFGrid extends JScrollPane implements IRequestFocus {

    private static final long serialVersionUID = -3512402297166865474L;

    private SFTableWrapper table = new SFTableWrapper(this);
    private SFGridModel model = new SFGridModel();
    private boolean firstInit = true;
    private SFGridSelListener listenerRow = null;
    private SFGridSelListener listenerColumn = null;
    private GridKeyHandler keyHandler = null;
    private UIWrapper uiw;
    private GridFocusListener gridFocus = null;
    private SFArrowKnob rightCorner = null;
    private WidgetManager wm;
    private SFGridMouseMotionListener mouse = null;

    private int previousSelectedRow = -1;
    private int previousSelectedColumn = -1;
    private int initRow = -1;

    public SFGrid(WidgetManager wm, UIWrapper uiw) {
        this.wm = wm;
        this.uiw = uiw;
        initSFGrid();

        table.setModel(model);
    }

    public SFGrid(WidgetManager wm, UIWrapper uiw, SFGridModel model1) {
        this.wm = wm;
        this.uiw = uiw;
        this.model = model1;
        initSFGrid();

        table.setModel(model1);
    }

    private void selectFirstRow() {
        selectRowInternal(previousSelectedRow, previousSelectedColumn, true, false);
    }

    private void selectRowInternal(int row, int col, boolean requestFocus, boolean noRefreshing) {
        if (row == -1) {
            row = 0; // suppress invalid value
        }
        if (col == -1) {
            col = 0; // suppress invalid value
        }

        if (uiw.isGridInfoColumnAllowed()) {
            if (row >= model.getRowCount()) {
                row = model.getRowCount() - 1;
            }
            if (col == 0) {
                col = 1;
            }
        }

        if (!noRefreshing) {
            wm.moveToRow(uiw, row);
        }
        table.changeSelection(row, col, false, false);
        if (requestFocus) {
            table.requestFocus();
        }
    }

    public void restoreCurrentSelection(boolean firstTime) {
        int row = listenerRow.getSelectedIndex();
        int col = listenerColumn.getSelectedIndex();

        if (firstTime && initRow > -1) {
            row = initRow;
        }
        selectRowInternal(row, col, true, false);
    }

    public void selectRow(int row) {
        selectRowInternal(row, previousSelectedColumn, true, false);
    }

    public void selectRowWithoutChildRefreshing(int row) {
        selectRowInternal(row, previousSelectedColumn, false, true);
    }

    /*
     * unsafe way public SFGridModel getModel() { return model; }
     */
    public void requestGridFocus() {
        if (!table.isFocusOwner()) {
            table.requestFocus();
            if (table.getSelectedRow() == -1) {
                selectFirstRow();
            }
        }
    }

    public List<GridColumnInfo> getListColumnInfo() {
        return model.getListColumnInfo();
    }

    public int getColumnCount() {
        return model.getColumnCount();
    }

    public GridColumnInfo getGridColumnInfo(int index) {
        return model.getGridColumnInfo(index);
    }

    private void initSFGrid() {
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.getViewport().add(table, null);

        model.init();

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        // stop cell editing when focus lost
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        // setup righr corner
        rightCorner = new SFArrowKnob(table, wm);
        setCorner(JScrollPane.UPPER_RIGHT_CORNER, rightCorner);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public SFTableWrapper getTable() {
        return table;
    }

    public void repaint() {
        if (table != null) {
            table.repaint();
        }
    }

    public void setWidgetManager(WidgetManager widgetManager) {
        model.setWidgetManager(widgetManager);
    }

    public void setUIWrapper(UIWrapper uIWrapper) {
        model.setUIWrapper(uIWrapper);
    }

    public void reloadModel() {
        model.reloadModel();
        initSFGridNextStep();
    }

    private void initSFGridNextStep() {
        if (firstInit) {

            listenerRow = new SFGridSelListener(model.getWidgetManager(),
                    model.getUIWrapper());
            table.getSelectionModel().addListSelectionListener(listenerRow);

            listenerColumn = new SFGridSelListener(model.getWidgetManager(),
                    model.getUIWrapper(), true);
            table.getColumnModel().getSelectionModel()
                    .addListSelectionListener(listenerColumn);

            keyHandler = new GridKeyHandler(model.getWidgetManager(),
                    model.getUIWrapper());
            table.addKeyListener(keyHandler);

            gridFocus = new GridFocusListener(model.getWidgetManager(),
                    model.getUIWrapper());
            table.addFocusListener(gridFocus);

            mouse = new SFGridMouseMotionListener(table);
            table.addMouseMotionListener(mouse);

            firstInit = false;
        }
    }

    public void removeSFGridListeners() {
        if (listenerRow != null) {
            table.getSelectionModel().removeListSelectionListener(listenerRow);
            listenerRow.closeHandler();
        }
        if (keyHandler != null) {
            table.removeKeyListener(keyHandler);
            keyHandler.closeHandler();
        }
        if (gridFocus != null) {
            table.removeFocusListener(gridFocus);
            gridFocus.closeHandler();
        }
        if (rightCorner != null) {
            rightCorner.closeHandler();
        }
        if (listenerColumn != null) {
            table.getColumnModel().getSelectionModel()
                    .removeListSelectionListener(listenerColumn);
            listenerColumn.closeHandler();
        }
        if (mouse != null) {
            table.removeMouseMotionListener(mouse);
        }
        table.closeSFTableWrapper();
    }

    void setPreviousSelectedRow(int previousSelectedRow) {
        this.previousSelectedRow = previousSelectedRow;
    }

    void setPreviousSelectedColumn(int previousSelectedColumn) {
        this.previousSelectedColumn = previousSelectedColumn;
    }

    public UIWrapper getUIWrapper() {
        return uiw;
    }

    public WidgetManager getWidgetManager() {
        return wm;
    }

    public void setInitRow(Integer initRow) {
        if (initRow != null && initRow >= 0) {
            this.initRow = initRow;
        }
    }

    @Override
    public void requestFocusOnNestedWidget() {
        requestGridFocus();
    }

}
