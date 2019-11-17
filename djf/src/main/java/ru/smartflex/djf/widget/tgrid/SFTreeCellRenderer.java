package ru.smartflex.djf.widget.tgrid;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import ru.smartflex.djf.DjfConfigurator;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.tree.TreeList;
import ru.smartflex.djf.controller.bean.tree.TreeListNode;
import ru.smartflex.djf.controller.bean.tree.TreeListUtils;
import ru.smartflex.djf.widget.grid.ColumnWidth;
import ru.smartflex.djf.widget.grid.SFGridModel;

public class SFTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = -4558724253651848808L;

    private JTable table = null;
    private String property = null;
    private WidgetManager widgetManager = null;
    private SFGridModel model = null;
    private int indColumn = -1;
    private ColumnWidth columnWidth = null;
    private int height = -1;
    private boolean initWidthDone = false;
    private boolean initFontDone = false;

    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean sel, boolean expanded, boolean leaf, int row,
                                                  boolean hasFocus) {

        String text = null;

        if (value instanceof TreeList) {
            // root node -> nothing to draw
            text = null;
        }

        if (value instanceof TreeListNode) {
            TreeListNode tln = (TreeListNode) value;
            Object data = tln.getData();

            if (data != null) {
                if (widgetManager.getFormBag().isFormReady()) {
                    if (property == null) {
                        property = model.getPropertyNameOfModelBase(indColumn);
                    }
                    Object objProperty = TreeListUtils.getPropertyValue(data,
                            property);

                    if (objProperty == null) {
                        text = "";
                    } else if (objProperty instanceof String) {
                        text = (String) objProperty;
                    } else {
                        // in case of value is not String
                        text = data.toString();
                    }
                } else {
                    text = data.toString();
                }
            }
        }

        if (table != null) {
            if (table.getSelectedRow() != -1) {
                if (table.getSelectedRow() == row) {
                    sel = true;
                }
            }
        }

        // workarround: to avoid cutting symbols in JLabel (-->> in Tree, in
        // Table)

        if (!initWidthDone) {
            initWidthDone = true;
            setMinimumSize(new Dimension(this.columnWidth.getMinWidth(),
                    this.height));
            if (columnWidth.getPrefWidth() > 0) {
                setPreferredSize(new Dimension(this.columnWidth.getPrefWidth(),
                        this.height));
            }
        }

        Component comp = super.getTreeCellRendererComponent(tree, text, sel,
                expanded, leaf, row, hasFocus);

        if (!initFontDone) {
            initFontDone = true;
            comp.setFont(DjfConfigurator.getFontTextInput());
        }

        return comp;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    void setWidgetManager(WidgetManager widgetManager) {
        this.widgetManager = widgetManager;
    }

    public void setModel(SFGridModel model) {
        this.model = model;
    }

    void setIndColumn(int indColumn) {
        this.indColumn = indColumn;
    }

    void setCellParam(ColumnWidth columnWidth, int height) {
        this.columnWidth = columnWidth;
        this.height = height;
    }

}
