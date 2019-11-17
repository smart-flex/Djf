package ru.smartflex.djf.widget.tgrid;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.tree.ITreeConstants;
import ru.smartflex.djf.widget.grid.ColumnWidth;
import ru.smartflex.djf.widget.grid.SFGridModel;

public class SFTGridCellWidget extends JTree implements TableCellRenderer {

    private static final long serialVersionUID = -1712804398642362671L;

    /**
     * Last table/tree row asked to renderer.
     */
    private int visibleRow;

    private JTable table;

    private SFTreeCellRenderer cell = new SFTreeCellRenderer();

    private int indexTreeColumn;

    public SFTGridCellWidget(JTable table, WidgetManager widgetManager,
                             SFGridModel model, int indColumn,
                             ColumnWidth columnWidth, int height) {
        this.table = table;
        this.indexTreeColumn = indColumn;

        init(widgetManager, model, indColumn, columnWidth, height);

        super.setModel(null); // to set off default model. In this case renderer
        // start to draw tree
        // super.setModel((TreeModel) model); // this does not draw tree because
        // in th start of works we have not data for drawing
    }

    private void init(WidgetManager widgetManager, SFGridModel model,
                      int indColumn, ColumnWidth columnWidth, int height) {
        super.setRootVisible(false);
        // super.setVisibleRowCount(0); // does not work

        cell.setTable(table);
        cell.setWidgetManager(widgetManager);
        cell.setModel(model);
        cell.setIndColumn(indColumn);
        cell.setCellParam(columnWidth, height);

        // set up cell renderer for tree widget (that located in table cell)
        super.setCellRenderer(cell);

        super.setRootVisible(ITreeConstants.SHOW_ROOT_NODE);

        super.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);

    }

    /**
     * Sets the row height of the tree, and forwards the row height to the
     * table.
     */
    public void setRowHeight(int rowHeight) {
        if (rowHeight > 0) {
            super.setRowHeight(rowHeight);
            if (table.getRowHeight() != rowHeight) {
                table.setRowHeight(getRowHeight());
            }
        }
    }

    /**
     * This method is mandatory. It is affected view of tgrid selection
     */
    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x, 0, w, table.getHeight());
    }

    public void paint(Graphics g) {
        // g.translate(0, -(visibleRow+1) * getRowHeight()); // hide root node
        // of Tree, because SF doesn't need it
        g.translate(0, -visibleRow * getRowHeight()); // original
        // g.translate(0, 0); // original
        super.paint(g);
    }

    /**
     * TreeCellRenderer method. Overridden to update the visible row.
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        if (isSelected)
            setBackground(table.getSelectionBackground());
        else
            setBackground(table.getBackground());

        visibleRow = row;
        return this;
    }

    private void expandAllNodes() {
        for (int i = 0; i < super.getRowCount(); i++) {
            super.expandRow(i);
        }
    }

    @Override
    public void treeDidChange() {
        super.treeDidChange();
        // setModel in JTree invokes this method
        expandAllNodes();
    }

    @SuppressWarnings("unused")
    public int getIndexTreeColumn() {
        return indexTreeColumn;
    }

}
