package ru.smartflex.djf.controller.bean;

import ru.smartflex.djf.widget.grid.ColumnWidth;
import ru.smartflex.djf.widget.grid.ICellEditor;
import ru.smartflex.djf.widget.grid.TitleRenderer;

public class GridColumnInfo extends UIWrapper {
    private String title = "";

    private ICellEditor cellEditor = null;

    private Object columnDefinition = null;

    private TitleRenderer renderer = null;

    private boolean showComboBoxItemTips = false;

    private Object celRenderer = null;

    private Boolean noResize = null;

    private ColumnWidth columnWidth = null;

    public GridColumnInfo() {
        super();
    }

    public String getTitle() {
        return title;
    }

    ICellEditor getCellEditor() {
        return cellEditor;
    }

    public void setCellEditor(ICellEditor cellEditor) {
        this.cellEditor = cellEditor;
    }

    public Object getColumnDefinition() {
        return columnDefinition;
    }

    public void setColumnDefinition(Object columnDefinition) {
        this.columnDefinition = columnDefinition;
    }

    @SuppressWarnings("unused")
    public TitleRenderer getRenderer() {
        return renderer;
    }

    @SuppressWarnings("unused")
    public boolean isShowComboBoxItemTips() {
        return showComboBoxItemTips;
    }

    @SuppressWarnings("unused")
    public void setShowComboBoxItemTips(boolean showComboBoxItemTips) {
        this.showComboBoxItemTips = showComboBoxItemTips;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getCelRenderer() {
        return celRenderer;
    }

    public void setCelRenderer(Object celRenderer) {
        this.celRenderer = celRenderer;
    }

    @Override
    public String toString() {
        return "GridColumnInfo [widgetType=" + getWidgetType() + " bind=" + getBind() + "]";
    }

    public Boolean getNoResize() {
        return noResize;
    }

    public void setNoResize(Boolean noResize) {
        this.noResize = noResize;
    }

    public ColumnWidth getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(ColumnWidth columnWidth) {
        this.columnWidth = columnWidth;
    }

}
