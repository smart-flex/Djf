package ru.smartflex.djf.controller.bean.tree;

public class TreeListNodeIndex {

    private TreeListNode node = null;
    private int index = -1;

    @SuppressWarnings("unused")
    public TreeListNodeIndex() {
        super();
    }

    TreeListNodeIndex(TreeListNode node, int index) {
        super();
        this.node = node;
        this.index = index;
    }

    public TreeListNode getNode() {
        return node;
    }

    public void setNode(TreeListNode node) {
        this.node = node;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @SuppressWarnings("unused")
    public boolean incrementAndEqual(int ind) {
        index++;
        return index == ind;
    }
}
