package ru.smartflex.djf.controller.bean.tree;

import java.util.Collection;
import java.util.List;

class ChildList {

    private int currentIndex = 0;
    private List<TreeListNode> childrenList;
    @SuppressWarnings("rawtypes")
    private Class childClass = null;

    private Collection<Object> collChildSyncObject = null;

    ChildList(List<TreeListNode> childrenList) {
        super();
        this.childrenList = childrenList;
    }

    TreeListNodeIndex createNewObject(TreeList treeList, int level,
                                      INodeOperation parent) {

        if (childrenList != null) {
            TreeListNode tln = TreeListUtils.createTreeListNodeNew(treeList,
                    childClass, level + 1, parent);
            childrenList.add(tln);

            collChildSyncObject.add(tln.getData());

            int index = childrenList.size() - 1;

            return new TreeListNodeIndex(tln, index);
        }

        return null;
    }

    void setChildSyncObject(Collection<Object> coll) {
        this.collChildSyncObject = coll;
    }

    void removeNode(TreeListNode toRemove) {
        if (childrenList != null) {
            childrenList.remove(toRemove);
            toRemove.clearParentNode();
            // sync to remove object
            //collChildSyncObject.remove(toRemove.getData()); // it is not applicable because it is based on equals. But we don't know for some objects is good or not
            TreeListUtils.syncDelete(this, toRemove.getData());
        }
    }

    void addSelfObject(TreeListNode addTo) {
        collChildSyncObject.add(addTo.getData());
    }

    List<TreeListNode> getChildrenList() {
        return childrenList;
    }

    int getChildrenSize() {
        int size = 0;
        if (childrenList != null) {
            size = childrenList.size();
        }
        return size;
    }

    TreeListNode getTreeListNode(int index) {
        TreeListNode tln = null;

        if (childrenList != null) {
            tln = childrenList.get(index);
        }

        return tln;
    }

    TreeListNode getCurrentTreeListNode() {
        TreeListNode tln = null;

        if (childrenList != null) {
            tln = childrenList.get(currentIndex);
        }

        return tln;
    }

    void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    void setChildClass(@SuppressWarnings("rawtypes") Class childClass) {
        this.childClass = childClass;
    }

    int getCurrentIndex() {
        return currentIndex;
    }

    boolean isClassEquals(@SuppressWarnings("rawtypes") Class clazz) {
        boolean fok = false;

        //noinspection StringEquality
        if (childClass.getName().intern() == clazz.getName().intern()) {
            fok = true;
        }
        return fok;

    }

    int getIndexOfChild(TreeListNode child) {
        int index = -1;

        if (childrenList != null) {
            int ind = 0;
            for (TreeListNode tln : childrenList) {
                if (tln.getIdNode() == child.getIdNode()) {
                    index = ind;
                    break;
                }
                ind++;
            }
        }
        return index;
    }

    Collection<Object> getCollChildSyncObject() {
        return collChildSyncObject;
    }
}
