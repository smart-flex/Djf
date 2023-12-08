package ru.smartflex.djf.widget.tgrid;

import javax.swing.DropMode;
import javax.swing.tree.TreeModel;

import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.tool.FontUtil;
import ru.smartflex.djf.widget.grid.SFGrid;

public class SFTGrid extends SFGrid {

    private static final long serialVersionUID = -893474004726636286L;
    private SFTGridCellWidget treeGridWidget = null;

    public SFTGrid(WidgetManager wm, UIWrapper uiw) {
        super(wm, uiw, new SFTGridModel());

        // increase row height
        int h = getTable().getRowHeight();
        getTable().setRowHeight(h + 1);
        // DnD
        getTable().setDragEnabled(true);
        getTable().setDropMode(DropMode.INSERT_ROWS);
        // getTable().setDropMode(DropMode.ON_OR_INSERT_ROWS);
        getTable().setTransferHandler(new TableRowTransferHandler(this));
    }

    public SFTGridCellWidget getTreeGridWidget() {
        return treeGridWidget;
    }

    public void setTreeGridWidget(SFTGridCellWidget treeGridWidget) {
        this.treeGridWidget = treeGridWidget;
    }

    public void reloadTreeModel() {
        if (treeGridWidget != null) {
            TreeModel model = treeGridWidget.getModel();
            treeGridWidget.setModel(null);
            treeGridWidget.setModel(model);

            treeGridWidget.treeDidChange();
        }
    }
}
