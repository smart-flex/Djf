package ru.smartflex.djf.controller.bean.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeListNode implements INodeOperation {

    private IBeanWrapper beanWrapper;
    private TreeList treeList;
    private int level;

    private Map<String, ChildList> mapChildren = new HashMap<String, ChildList>();

    private ChildList selfChildList = null;

    private int idNode;

    private INodeOperation parentNode;

    public TreeListNode(IBeanWrapper bw, TreeList treeList, int level,
                        INodeOperation parent) {
        super();
        this.beanWrapper = bw;
        this.treeList = treeList;
        this.level = level;
        this.parentNode = parent;

        this.idNode = treeList.getNextNodeID();

        addNestedNodesSelf();
        addNestedNodes();
    }

    ChildList getChildList(String property) {
        return mapChildren.get(property);
    }

    int getChildrenSize(String property) {
        int size = 0;
        ChildList cl = mapChildren.get(property);
        if (cl != null) {
            size = cl.getChildrenSize();
        }
        return size;
    }

    public int getLevel() {
        return level;
    }

    IBeanWrapper getBeanWrapper() {
        return beanWrapper;
    }

    public Object getData() {
        return beanWrapper.getData();
    }

    String getDataClazzName() {
        return beanWrapper.getClazzName();
    }

    Collection<ChildList> getChildList() {
        return mapChildren.values();
    }

    private void addNestedNodesSelf() {
        if (treeList.getPropertyNameSelf() != null) {
            if (!beanWrapper.getData().getClass()
                    .equals(treeList.getRoozClazz())) {
                return;
            }
            Object val = TreeListUtils.getPropertyValue(beanWrapper.getData(),
                    treeList.getPropertyNameSelf());
            if (val != null) {
                // collection referred to itself is existed

                if (!(val instanceof Collection)) {
                    throw new TreeListException(
                            "This property is not Collection: "
                                    + treeList.getPropertyNameSelf());
                }

                List<TreeListNode> childrenList = new ArrayList<TreeListNode>();

                ChildList childList = new ChildList(childrenList);
                childList.setChildClass(treeList.getRoozClazz());

                mapChildren.put(treeList.getPropertyNameSelf(), childList);

                @SuppressWarnings({"rawtypes", "unchecked"})
                Collection<Object> coll = (Collection) val;
                childList.setChildSyncObject(coll);

                String selAbleProp = treeList.getBeanOfModel()
                        .getSelectablePropertyOfRootModel();

                for (Object data : coll) {
                    TreeListNode tln = TreeListUtils
                            .createTreeListNodePersistent(treeList, data,
                                    level + 1, this, selAbleProp);
                    // TreeList.START_LEVEL_VALUE
                    childrenList.add(tln);
                }

            } // else doesn't create a empty row

        }
    }

    @SuppressWarnings("unchecked")
    private void addNestedNodes() {
        if (treeList.isParentChildExisted()) {

            List<SetInfo> listSetInfo = treeList
                    .getNestedInfoByParentCLass(beanWrapper.getData()
                            .getClass().getName());

            if (listSetInfo != null) {
                for (SetInfo si : listSetInfo) {
                    String pName = si.getSetPropertyName();
                    Object val = TreeListUtils.getPropertyValue(
                            beanWrapper.getData(), pName);
                    if (!(val instanceof Collection)) {
                        throw new TreeListException(
                                "This property is not Collection: " + pName);
                    }
                    List<TreeListNode> childrenList = new ArrayList<TreeListNode>();

                    ChildList childList = new ChildList(childrenList);
                    childList.setChildClass(si.getBeanClassInSet());

                    mapChildren.put(si.getSetPropertyPathName(), childList);

                    @SuppressWarnings("rawtypes")
                    Collection coll = (Collection) val;
                    childList.setChildSyncObject(coll);

                    String selAbleProp = treeList.getBeanOfModel()
                            .getSelectableSetProperty(
                                    si.getSetPropertyPathName());

                    for (Object data : coll) {
                        TreeListNode tln = TreeListUtils
                                .createTreeListNodePersistent(treeList, data,
                                        level + 1, this, selAbleProp);
                        childrenList.add(tln);
                    }
                    if (childrenList.size() == 0) {
                        // adds empty usual node
                        TreeListNode tln = TreeListUtils.createTreeListNodeNew(
                                treeList, si.getBeanClassInSet(), level + 1,
                                this);
                        childrenList.add(tln);
                        // because initial filling from hibernate is empty. Then
                        // we have to add 1 object to each collection.
                        coll.add(tln.getData());
                    }
                }
            }
        }
    }

    TreeListNode getSelfTreeListNode(int index) {
        TreeListNode tln = null;

        ChildList cl = getSelfChildList();
        if (cl != null) {
            tln = cl.getTreeListNode(index);
        }

        return tln;
    }

    private ChildList getSelfChildList() {
        if (selfChildList == null) {
            Collection<ChildList> childList = getChildList();
            for (ChildList cl : childList) {
                if (cl.isClassEquals(treeList.getRoozClazz())) {
                    selfChildList = cl;
                    break;
                }
            }
        }
        return selfChildList;
    }

    int getSelfChildrenSize() {
        int size = 0;
        ChildList cl = getSelfChildList();
        if (cl != null) {
            size = cl.getChildrenSize();
        }
        return size;
    }

    int getIdNode() {
        return this.idNode;
    }

    int getSelfIndex(TreeListNode child) {
        int index = -1;
        if (child != null) {
            ChildList cl = getSelfChildList();
            if (cl != null) {
                index = cl.getIndexOfChild(child);
            }
        }
        return index;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + idNode;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TreeListNode other = (TreeListNode) obj;
        //noinspection RedundantIfStatement
        if (idNode != other.idNode)
            return false;
        return true;
    }

    void removeFromParentSelfNode() {
        parentNode.removeFromParentSelfNode(this);
    }

    @Override
    public void removeFromParentSelfNode(TreeListNode node) {
        if (treeList.getPropertyNameSelf() != null) {
            ChildList childList = mapChildren.get(treeList
                    .getPropertyNameSelf());
            childList.removeNode(node);
        }
    }

    void clearParentNode() {
        parentNode = null;
    }

    private void changeLevel(int delta) {
        level += delta;
    }

    void setParentNode(INodeOperation node) {
        parentNode = node;
        // changes level for current and children nodes
        final int deltaLevel = node.getLevel() + 1 - getLevel();
        if (deltaLevel != 0) {

            ITreeWalkOperation oper = new ITreeWalkOperation() {
                @Override
                public void beforeCycle(TreeListNode node, boolean printAsterix) {
                }

                @Override
                public void insideCycle(TreeListNode node) {
                    node.changeLevel(deltaLevel);
                }
            };

            TreeListUtils.walkTreeDown(this, oper, false);

        }
    }

    @Override
    public Object getObject() {
        if (beanWrapper != null) {
            return beanWrapper.getData();
        }
        return null;
    }

}
