package ru.smartflex.djf.widget.grid;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.TreePath;

import ru.smartflex.djf.WidgetTypeEnum;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.widget.ISFHandler;
import ru.smartflex.djf.widget.tgrid.SFTGrid;

/**
 * Listener for grid row and column selection
 */
public class SFGridSelListener implements ListSelectionListener, ISFHandler {

    private UIWrapper uiw;
    private WidgetManager wm;
    private boolean listenForColumn = false;
    private int selectedIndex = 0;

    SFGridSelListener(WidgetManager wm, UIWrapper uiw) {
        this.wm = wm;
        this.uiw = uiw;
    }

    SFGridSelListener(WidgetManager wm, UIWrapper uiw,
                      boolean listenForColumn) {
        this.wm = wm;
        this.uiw = uiw;
        this.listenForColumn = listenForColumn;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting())
            return;

        if (!wm.getFormBag().isFormReady()) {
            return;
        }

        ListSelectionModel lsm = (ListSelectionModel) e.getSource();
        if (!lsm.isSelectionEmpty()) {
            if (!listenForColumn) {
                hadleRowSelection(lsm);
            } else {
                hadleColumnSelection(lsm);
            }
        }
    }

    private void hadleColumnSelection(ListSelectionModel lsm) {
        selectedIndex = lsm.getMinSelectionIndex();
        wm.registerSelectedWrapper(uiw);
        SFGrid grid = (SFGrid) uiw.getObjectUI();
        grid.setPreviousSelectedColumn(selectedIndex);
    }

    private void hadleRowSelection(ListSelectionModel lsm) {
        selectedIndex = lsm.getMinSelectionIndex();

        wm.moveToRow(uiw, selectedIndex);
        wm.registerSelectedWrapper(uiw);

        SFGrid grid = (SFGrid) uiw.getObjectUI();
        grid.setPreviousSelectedRow(selectedIndex);
        int selectedColumn = grid.getTable().getSelectedColumn();
        grid.setPreviousSelectedColumn(selectedColumn);

        if (uiw.getWidgetType() == WidgetTypeEnum.TGRID) {
            SFTGrid treeGrid = (SFTGrid) uiw.getObjectUI();

            // workarround: remove previous setted paths
            // bug is risen when you added new line
            TreePath[] selectedPaths = treeGrid.getTreeGridWidget()
                    .getSelectionModel().getSelectionPaths();
            if (selectedPaths != null && selectedPaths.length > 0) {
                treeGrid.getTreeGridWidget().getSelectionModel()
                        .removeSelectionPaths(selectedPaths);
            }

            TreePath selPath = treeGrid.getTreeGridWidget().getPathForRow(
                    selectedIndex);
            if (selPath != null) {
                treeGrid.getTreeGridWidget().getSelectionModel()
                        .setSelectionPath(selPath);
            }
            treeGrid.repaint();
        }
    }

    @Override
    public void closeHandler() {
        uiw = null;
        wm = null;
    }

    int getSelectedIndex() {
        return selectedIndex;
    }

}
