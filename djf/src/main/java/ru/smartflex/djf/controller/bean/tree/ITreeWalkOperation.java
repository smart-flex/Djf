package ru.smartflex.djf.controller.bean.tree;

public interface ITreeWalkOperation {

    void beforeCycle(TreeListNode node, boolean printIfCurrent);

    void insideCycle(TreeListNode node);
}
