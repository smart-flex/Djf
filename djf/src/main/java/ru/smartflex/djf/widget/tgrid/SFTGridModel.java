package ru.smartflex.djf.widget.tgrid;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import ru.smartflex.djf.widget.grid.SFGridModel;

public class SFTGridModel extends SFGridModel implements TreeModel {

    private static final long serialVersionUID = 7254639340338199885L;

    @Override
    public Object getRoot() {
        if (super.getWidgetManager() == null) {
            return null;
        }
        return super.getWidgetManager().getTGridRoot(super.getUIWrapper());
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (super.getWidgetManager() == null) {
            return null;
        }
        return super.getWidgetManager().getTGridChild(super.getUIWrapper(),
                parent, index);
    }

    @Override
    public int getChildCount(Object parent) {
        if (super.getWidgetManager() == null) {
            return 0;
        }
        return super.getWidgetManager().getTGridChildCount(
                super.getUIWrapper(), parent);
    }

    @Override
    public boolean isLeaf(Object node) {
        if (super.getWidgetManager() == null) {
            return true;
        }
        return super.getWidgetManager().isTGridLeaf(super.getUIWrapper(), node);
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (super.getWidgetManager() == null) {
            return 0;
        }
        return super.getWidgetManager().getTGridIndexOfChild(
                super.getUIWrapper(), parent, child);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
    }
}
