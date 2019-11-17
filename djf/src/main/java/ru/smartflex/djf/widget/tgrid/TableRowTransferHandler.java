package ru.smartflex.djf.widget.tgrid;

import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSource;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import ru.smartflex.djf.WidgetTypeEnum;
import ru.smartflex.djf.controller.bean.GridColumnInfo;
import ru.smartflex.djf.controller.bean.tree.ITreeConstants;

public class TableRowTransferHandler extends TransferHandler {

    private static final long serialVersionUID = 8337863930556707627L;

    private final DataFlavor localObjectFlavor = new ActivationDataFlavor(
            Integer.class, DataFlavor.javaJVMLocalObjectMimeType,
            "Integer Row Index");
    private JTable table;
    private SFTGrid tGrid;

    TableRowTransferHandler(SFTGrid tGrid) {
        this.tGrid = tGrid;
        this.table = tGrid.getTable();
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        assert (c == table);
        return new DataHandler(table.getSelectedRow(),
                localObjectFlavor.getMimeType());
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport info) {
        boolean fok = info.getComponent() == table && info.isDrop()
                && info.isDataFlavorSupported(localObjectFlavor);

        int row = -1;
        if (fok) {
            row = table.getSelectedRow();
            if (row == 0) {
                if (ITreeConstants.SHOW_ROOT_NODE) {
                    // dropping root node is forbidden
                    fok = false;
                }
            }
        }
        if (fok) {
            fok = false;
            int col = table.getSelectedColumn();
            if (col < tGrid.getColumnCount()) {
                int colIndex = table.convertColumnIndexToModel(col);
                GridColumnInfo colInfo = tGrid.getGridColumnInfo(colIndex);
                if (colInfo.getWidgetType() == WidgetTypeEnum.TGRID_TREE_FIELD) {
                    // if allowed to edit tgrid column then allowed DnD
                    boolean tgridCellEditable = table.getModel().isCellEditable(row, colIndex);
                    if (tgridCellEditable) {
                        fok = true;
                        table.setCursor(DragSource.DefaultMoveDrop);
                    }
                }
            }
        }

        return fok;
    }

    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY_OR_MOVE;
//		return TransferHandler.MOVE;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport info) {
        JTable target = (JTable) info.getComponent();
        JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
        int index = dl.getRow();
        int max = table.getModel().getRowCount();
        if (index < 0 || index > max)
            index = max;
        target.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        try {
            int rowFrom = (Integer) info.getTransferable().getTransferData(
                    localObjectFlavor);
            if (rowFrom != -1 && rowFrom != index) {

                // ((Reorderable)table.getModel()).reorder(rowFrom, index);
                if (index > rowFrom)
                    index--;

                if (index != rowFrom) { // in other case we have incorrect node moving and after that NPE
                    if (tGrid.getWidgetManager().moveNode(tGrid.getUIWrapper(), rowFrom, index)) {

                        target.getSelectionModel().addSelectionInterval(index, index);

                        // tricks. model must be refreshed in JTree
                        tGrid.reloadTreeModel();
                        return true;
                    }
                }

                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void exportDone(JComponent c, Transferable t, int act) {
        if (act == TransferHandler.MOVE) {
            table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

}