package ru.smartflex.djf.controller.bean.tree;

public interface INodeOperation {

    void removeFromParentSelfNode(TreeListNode node);

    int getLevel();

    Object getObject();
}
