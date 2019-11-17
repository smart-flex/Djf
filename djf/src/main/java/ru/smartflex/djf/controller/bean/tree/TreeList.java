package ru.smartflex.djf.controller.bean.tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.SmartFlexException;
import ru.smartflex.djf.controller.FormStack;
import ru.smartflex.djf.controller.bean.BeanFormDef;
import ru.smartflex.djf.controller.bean.BeanNameProperty;
import ru.smartflex.djf.controller.exception.PropertyNotFilledException;

/**
 * Represents structure of parent-child relationships
 */
public class TreeList implements INodeOperation {

    static final int START_NODE_LEVEL_VALUE = 0;
    private static final int TREE_LIST_LEVEL_VALUE = -1;

    private String rootClazzName = null;
    private Object objectOrList = null;

    private List<TreeListNode> rootList = new ArrayList<TreeListNode>();
    private List<TreeListPair<String, String>> childPropertyPair = null;
    private int maxChildLevel = 0;

    private int currentIndex = 0;

    private IBeanWrapperFactory wrapperFactory = null;

    private String propertyNameSelf = null;
    @SuppressWarnings("rawtypes")
    private Class roozClazz = null;
    private BeanFormDef beanOfModel = null;

    private AtomicBoolean setInfoConstructed = new AtomicBoolean(false);
    private Lock lockSetInfoConstructed = new ReentrantLock(false);
    private Map<String, List<SetInfo>> nestedInfo = new HashMap<String, List<SetInfo>>();

    private AtomicInteger nodeOrder = new AtomicInteger(0);

    private IBeanWrapper emptybeanWrapper = null;

    // collection of original objects. (for synchronized changes)
    private Collection<Object> collSyncObject = null;
    private Map<String, BeanNestedPropInfo> propertyInfoForNestedAndRootClass = new HashMap<String, BeanNestedPropInfo>();

    @SuppressWarnings("unused")
    public TreeList() {
    }

    public TreeList(IBeanWrapperFactory wrapperFactory) {
        this.wrapperFactory = wrapperFactory;
    }

    IBeanWrapperFactory getWrapperFactory() {
        return wrapperFactory;
    }

    public void addSetDefinition(BeanFormDef beanDef) {

        String path = beanDef.getTreePath();
        String childClazzName = beanDef.getClazz();
        String parent = beanDef.getParent();

        BeanNestedPropInfo bInfo = new BeanNestedPropInfo();
        bInfo.setParentPropertyName(parent);
        propertyInfoForNestedAndRootClass.put(childClazzName, bInfo);

        if (path != null && childClazzName != null) {

            String[] split = TreeListUtils.getPartProperties(path);
            if (split.length < 2) {
                throw new TreeListException(
                        "There must be at least one delimeter: " + path);
            }
            if ((split.length - 1) > maxChildLevel) {
                maxChildLevel = split.length - 1;
            }
            if (childPropertyPair == null) {
                childPropertyPair = new ArrayList<TreeListPair<String, String>>();
            }
            TreeListPair<String, String> tlp = new TreeListPair<String, String>(
                    path, childClazzName);
            childPropertyPair.add(tlp);

            bInfo.setBeanDef(beanDef);
        }

        setNotNullDefProperties(beanDef);

    }

    public BeanFormDef getBeanFormDef(String clazzName) {
        BeanNestedPropInfo bInfo = propertyInfoForNestedAndRootClass
                .get(clazzName);
        if (bInfo != null) {
            return bInfo.getBeanDef();
        }
        return null;
    }

    String getParentPropertyForClass(String clazzName) {
        BeanNestedPropInfo bInfo = propertyInfoForNestedAndRootClass
                .get(clazzName);
        if (bInfo != null) {
            return bInfo.getParentPropertyName();
        }
        return null;
    }

    boolean isParentChildExisted() {
        boolean flag = false;
        if (childPropertyPair != null) {
            flag = true;
        }
        return flag;
    }

    @SuppressWarnings("rawtypes")
    Class getRoozClazz() {
        if (roozClazz == null) {
            roozClazz = TreeListUtils.getClass(rootClazzName);
        }
        return roozClazz;
    }

    List<SetInfo> getNestedInfoByParentCLass(String clazz) {

        if (!setInfoConstructed.get()) {
            lockSetInfoConstructed.lock();

            try {
                for (int level = 1; level <= maxChildLevel; level++) {
                    fillNestedInfo(level);
                }
                setInfoConstructed.set(true);
            } finally {
                lockSetInfoConstructed.unlock();
            }
        }

        return nestedInfo.get(clazz);
    }

    private void fillNestedInfo(int level) {
        for (TreeListPair<String, String> tlp : childPropertyPair) {
            String[] split = TreeListUtils.getPartProperties(tlp
                    .getKey());
            if ((split.length - 1) == level) {
                @SuppressWarnings("rawtypes")
                Class clazz = TreeListUtils.getClass(tlp.getValue());
                SetInfo si = new SetInfo(split[level], tlp.getKey(), clazz);

                String classParent = getParentClassName(tlp);
                List<SetInfo> list = nestedInfo.get(classParent);
                if (list == null) {
                    list = new ArrayList<SetInfo>();
                    nestedInfo.put(classParent, list);
                }

                list.add(si);
            }

        }
    }

    private String getParentClassName(TreeListPair<String, String> tlpChild) {
        String parent = this.rootClazzName;
        String[] splitChild = TreeListUtils.getPartProperties(tlpChild
                .getKey());

        if (splitChild.length > 2) {
            for (TreeListPair<String, String> tlp : childPropertyPair) {
                String[] split = TreeListUtils.getPartProperties(tlp
                        .getKey());

                if ((split.length + 1) == splitChild.length) {
                    if (tlpChild.getKey().indexOf(tlp.getKey()) == 0) {
                        // found parent
                        parent = tlp.getValue();
                    }
                }
            }
        }
        return parent;
    }

    @SuppressWarnings("unused")
    public void fillTreeList(Object objectOrList) {
        this.objectOrList = objectOrList;
        fillHandle();
    }

    public void fillTreeList(Object objectOrList, String rootClazzName) {
        this.objectOrList = objectOrList;
        this.rootClazzName = rootClazzName;
        fillHandle();
    }

    private void fillHandle() {
        fillValidateEmptyObject();
        fillFromSingleObject();
        fillFromCollection();
    }

    private void fillFromSingleObject() {
        if (objectOrList == null) {
            createNewObject();
        } else if (!(objectOrList instanceof Collection)) {
            String selAbleProp = beanOfModel.getSelectablePropertyOfRootModel();
            TreeListNode tln = TreeListUtils.createTreeListNodePersistent(this,
                    objectOrList, START_NODE_LEVEL_VALUE, this, selAbleProp);
            rootList.add(tln);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void fillFromCollection() {
        if (objectOrList == null) {
            return;
        }
        if (objectOrList instanceof Collection) {
            collSyncObject = (Collection) objectOrList;
            String selAbleProp = beanOfModel.getSelectablePropertyOfRootModel();
            for (Object obj : collSyncObject) {
                TreeListNode tln = TreeListUtils.createTreeListNodePersistent(
                        this, obj, START_NODE_LEVEL_VALUE, this, selAbleProp);
                rootList.add(tln);
            }
            if (rootList.size() == 0) {
                createNewObject();
            }
        }
    }

    private void createNewObject() {
        TreeListNode tln = TreeListUtils.createTreeListNodeNew(this,
                rootClazzName, this);
        rootList.add(tln);
    }

    public int getNodeAmountForFirstLevel() {
        return rootList.size();
    }

    @SuppressWarnings("rawtypes")
    private void fillValidateEmptyObject() {
        if (objectOrList == null) {
            if (rootClazzName == null) {
                throw new TreeListException("Root class name is empty");
            }
        } else {
            if (objectOrList instanceof Collection) {
                if (((Collection) objectOrList).size() == 0) {
                    if (rootClazzName == null) {
                        throw new TreeListException("Root class name is empty");
                    }
                }
            }
        }
    }

    /**
     * Draws a tree in text mode (for testing purposes only).
     *
     * @return String representation of tree
     */
    public String drawTree() {

        final StringBuffer sb = new StringBuffer();
        sb.append(SFConstants.NEW_LINE);
        sb.append(SFConstants.NEW_LINE);
        sb.append("*** Bean tree. **** Maximum child level is: ");
        sb.append(this.maxChildLevel);
        sb.append(SFConstants.NEW_LINE);

        ITreeWalkOperation oper = new ITreeWalkOperation() {

            @Override
            public void beforeCycle(TreeListNode node, boolean printAsterix) {
                sb.append("|");
                sb.append(TreeListUtils.generateHyphen(node.getLevel(),
                        printAsterix));
                sb.append(node.getLevel());
                sb.append("L ID: ");
                sb.append(node.getIdNode());
                sb.append(" DEL: ");
                sb.append(node.getBeanWrapper().isBeanWrapperDeleted());
                sb.append(" ");
                String str = node.getBeanWrapper().getData().toString();
                sb.append(str);
                sb.append(SFConstants.NEW_LINE);
            }

            @Override
            public void insideCycle(TreeListNode node) {
            }

        };
        int it = 0;
        for (TreeListNode tln : rootList) {
            TreeListUtils.walkTreeDown(tln, oper,
                    it == this.currentIndex);
            it++;
        }

        return sb.toString();
    }

    public BeanStatusEnum getParentLockedStatus(
            @SuppressWarnings("rawtypes") WidgetTreeNode wtn) {
        BeanStatusEnum[] status = new BeanStatusEnum[1];
        TreeListNode node = getCurrentRootTreeListNode();
        getParentLockedStatus(wtn, node, 1, status);
        return status[0];
    }

    private void getParentLockedStatus(
            @SuppressWarnings("rawtypes") WidgetTreeNode wtn,
            TreeListNode node, int indexPath, BeanStatusEnum[] status) {
        boolean searchFinished = false;
        if ((indexPath + 1) >= wtn.getTreePathLength()) {
            // search is finished.
            if (indexPath == 1 && wtn.getTreePathLength() == 1) {
                // first level
                return;
            } else {
                searchFinished = true;
            }
        }

        BeanStatusEnum bs = node.getBeanWrapper().getObtainedStatus();
        if (bs != null || searchFinished) {
            status[0] = bs;
        } else {
            String path = wtn.getTreePathByLevel(indexPath);
            ChildList cl = node.getChildList(path);
            TreeListNode nextNode = cl.getCurrentTreeListNode();
            getParentLockedStatus(wtn, nextNode, indexPath + 1, status);
        }
    }

    @SuppressWarnings("unused")
    public String printObjectsMarkAsDeleted() {
        StringBuilder sb = new StringBuilder();
        sb.append(SFConstants.NEW_LINE);
        sb.append("Objects mark as deleted:");
        sb.append(SFConstants.NEW_LINE);

        Deque<IBeanWrapper> stack = getObjectsToDelete();
        while (!stack.isEmpty()) {
            Object data = stack.pop().getData();
            sb.append(SFConstants.MARK_DEL);
            sb.append(data.toString());
            sb.append(SFConstants.NEW_LINE);
        }

        return sb.toString();
    }

    @SuppressWarnings("unused")
    public String printObjectsMarkAsChanged() {
        StringBuilder sb = new StringBuilder();
        sb.append(SFConstants.NEW_LINE);
        sb.append("Objects mark as changed:");
        sb.append(SFConstants.NEW_LINE);

        Deque<IBeanWrapper> stack = getObjectsToSave();
        while (!stack.isEmpty()) {
            IBeanWrapper bw = stack.pop();
            if (bw.getCreatedStatus() == BeanStatusEnum.NEW) {
                sb.append(SFConstants.MARK_INS);
            } else {
                sb.append(SFConstants.MARK_UPD);
            }
            sb.append(bw.getData().toString());
            sb.append(SFConstants.NEW_LINE);
        }

        return sb.toString();
    }

    public Deque<IBeanWrapper> getObjectsToSave() {
        Deque<IBeanWrapper> stack = new ArrayDeque<IBeanWrapper>();

        for (TreeListNode tln : rootList) {
            collectedMarkAsChanged(tln, stack);
        }

        return stack;
    }

    private void checkForNotNull(TreeListNode node) {
        String clazzName = node.getDataClazzName();

        BeanNestedPropInfo bInfo = propertyInfoForNestedAndRootClass
                .get(clazzName);

        BeanNameProperty[] nProps = bInfo.getNotNullProperties();
        if (nProps == null) {
            return;
        }
        boolean hasToBeFilled = false;
        StringBuilder sb = new StringBuilder();
        for (BeanNameProperty npp : nProps) {
            Object value = TreeListUtils.getPropertyValue(node.getData(), npp.getName());
            if (FormStack.getCurrentFormBag().getBeanFormDef()
                    .isPropertyExistsInBindsDefinition(clazzName, npp.getName())) {
                if (value == null) {
                    hasToBeFilled = true;
                    if (sb.length() > 0) {
                        sb.append("; ");
                    }
                    if (npp.getHumanName() != null) {
                        sb.append(npp.getHumanName());
                    } else {
                        sb.append(npp.getName());
                    }

                }
            }
        }
        if (hasToBeFilled) {
            PropertyNotFilledException pnfe = new PropertyNotFilledException(
                    "There is not filled property: \"" + sb.toString()
                            + "\" for class: \"" + clazzName + "\"");
            pnfe.setNotNullProps(sb.toString());
            throw pnfe;
        }
    }

    private void collectedMarkAsChanged(TreeListNode node,
                                        Deque<IBeanWrapper> stack) {

        if (node.getBeanWrapper().isBeanWrapperChanged()) {
            // it enough for hibernate return only parent object
            checkForNotNull(node);
            stack.push(node.getBeanWrapper());
            removeNewSubNodes(node);
        } else {
            Collection<ChildList> cList = node.getChildList();
            if (cList.size() > 0) {
                for (ChildList cl : cList) {
                    List<TreeListNode> list = cl.getChildrenList();
                    if (list != null && list.size() > 0) {
                        for (TreeListNode tln : list) {
                            collectedMarkAsChanged(tln, stack);
                        }
                    }
                }
            }
        }
    }

    private void removeNewSubNodes(TreeListNode node) {
        Collection<ChildList> cList = node.getChildList();
        if (cList.size() > 0) {
            for (ChildList cl : cList) {
                List<TreeListNode> list = cl.getChildrenList();
                if (list != null && list.size() > 0) {
                    ListIterator<TreeListNode> iter = list.listIterator();
                    while (iter.hasNext()) {
                        TreeListNode tln = iter.next();
                        if (tln.getBeanWrapper().getCreatedStatus() == BeanStatusEnum.NEW && !tln.getBeanWrapper().isBeanWrapperChanged()) {
                            Object data = tln.getBeanWrapper().getData();
                            iter.remove();
                            TreeListUtils.syncDelete(cl, data);
                        } else {
                            removeNewSubNodes(tln);
                        }
                    }
                }
            }
        }

    }

    public Deque<IBeanWrapper> getObjectsToDelete() {
        Deque<IBeanWrapper> stack = new ArrayDeque<IBeanWrapper>();

        for (TreeListNode tln : rootList) {
            boolean markDel = tln.getBeanWrapper().isBeanWrapperDeleted();
            collectedMarkAsDeleted(tln, stack, markDel);
        }
        return stack;
    }

    private void collectedMarkAsDeleted(TreeListNode node,
                                        Deque<IBeanWrapper> stack, boolean parentMarkAsDeleted) {

        if (parentMarkAsDeleted) {
            stack.push(node.getBeanWrapper());
        }

        Collection<ChildList> cList = node.getChildList();
        if (cList.size() > 0) {
            for (ChildList cl : cList) {
                List<TreeListNode> list = cl.getChildrenList();
                if (list != null && list.size() > 0) {
                    for (TreeListNode tln : list) {
                        if (parentMarkAsDeleted) {
                            collectedMarkAsDeleted(tln, stack,
                                    true);
                        } else {
                            boolean markDel = tln.getBeanWrapper()
                                    .isBeanWrapperDeleted();
                            collectedMarkAsDeleted(tln, stack, markDel);
                        }
                    }
                }
            }
        }

    }

    public int getAmountSetSize(@SuppressWarnings("rawtypes") WidgetTreeNode wtn) {
        int amount = 0;
        if (wtn.getTreePathLength() == 1) {
            if (propertyNameSelf == null) {
                amount = rootList.size();
            } else {
                for (TreeListNode tln : rootList) {
                    amount++;
                    amount = recursiveCountBeanWithNameSelfForTGRID(tln, amount);
                }
                if (ITreeConstants.SHOW_ROOT_NODE) {
                    amount++;
                }
            }
        } else {
            TreeListNode tln = getCurrentRootTreeListNode();
            amount = getAmountSetSize(tln, wtn, 1);
        }

        return amount;
    }

    private int recursiveCountBeanWithNameSelfForTGRID(TreeListNode tln,
                                                       int amount) {
        ChildList cl = tln.getChildList(propertyNameSelf);
        if (cl != null) {
            List<TreeListNode> tlnSubList = cl.getChildrenList();
            for (TreeListNode slTln : tlnSubList) {
                amount++;
                amount = recursiveCountBeanWithNameSelfForTGRID(slTln, amount);
            }
        }
        return amount;
    }

    private int getAmountSetSize(TreeListNode node,
                                 @SuppressWarnings("rawtypes") WidgetTreeNode wtn, int indexPath) {
        int amount;
        String path = wtn.getTreePathByLevel(indexPath);
        if ((indexPath + 1) == wtn.getTreePathLength()) {
            amount = node.getChildrenSize(path);
        } else {
            ChildList cl = node.getChildList(path);
            TreeListNode nodeNext = cl.getCurrentTreeListNode();
            amount = getAmountSetSize(nodeNext, wtn, indexPath + 1);
        }
        return amount;
    }

    public IBeanWrapper getBeanWrapper(int index,
                                       @SuppressWarnings("rawtypes") WidgetTreeNode wtn) {
        IBeanWrapper bw;

        if (wtn.getTreePathLength() == 1) {
            bw = getBeanWrapperWhenPathLengthEqualOne(index);
        } else {
            TreeListNode tln = getCurrentRootTreeListNode();
            String path = wtn.getTreePathByLevel(1);
            ChildList cl = tln.getChildList(path);
            if (cl == null) {
                throw new SmartFlexException(
                        "May be is omitted \"set tag\" description in bean definition, because there is no child for this path: "
                                + path);
            }
            bw = getBeanWrapper(cl, index, wtn, 1);
        }
        return bw;
    }

    private IBeanWrapper getBeanWrapperWhenPathLengthEqualOne(int index) {
        IBeanWrapper bw = null;
        if (propertyNameSelf == null) {
            // usual GRID
            bw = rootList.get(index).getBeanWrapper();
        } else {
            if (index <= 0) {
                if (ITreeConstants.SHOW_ROOT_NODE) {
                    if (emptybeanWrapper == null) {
                        createEmptyObjectForRootNodeSimulate();
                    }
                    return emptybeanWrapper; // root node
                }
            }
            TreeListNode node = getRootTreeListNodeByIndex(index);
            if (node != null) {
                bw = node.getBeanWrapper();
            }
        }
        return bw;
    }

    public List<IBeanWrapper> getCurrentBeanWrapperList(
            @SuppressWarnings("rawtypes") WidgetTreeNode wtn) {

        List<IBeanWrapper> list = new ArrayList<IBeanWrapper>();

        if (wtn.getTreePathLength() == 1) {
            copyCollectionsBW(list, rootList);
        } else {
            TreeListNode tln = getCurrentRootTreeListNode();
            String path = wtn.getTreePathByLevel(1);
            ChildList cl = tln.getChildList(path);
            getCurrentBeanWrapperList(list, cl, wtn, 1);
        }

        return list;
    }

    private void getCurrentBeanWrapperList(
            List<IBeanWrapper> dest, ChildList cList,
            @SuppressWarnings("rawtypes") WidgetTreeNode wtn, int indexPath) {
        if ((indexPath + 1) == wtn.getTreePathLength()) {
            copyCollectionsBW(dest, cList.getChildrenList());
        } else {
            String path = wtn.getTreePathByLevel(indexPath + 1);
            TreeListNode nodeCurr = cList.getCurrentTreeListNode();
            ChildList cl = nodeCurr.getChildList(path);
            getCurrentBeanWrapperList(dest, cl, wtn, indexPath + 1);
        }
    }

    private void copyCollectionsBW(List<IBeanWrapper> dest,
                                   Collection<TreeListNode> src) {
        for (TreeListNode tln : src) {
            dest.add(tln.getBeanWrapper());
        }
    }

    public IBeanWrapper getCurrentBeanWrapper(
            @SuppressWarnings("rawtypes") WidgetTreeNode wtn) {
        IBeanWrapper bw;

        if (wtn.getTreePathLength() == 1) {
            bw = getBeanWrapperWhenPathLengthEqualOne(currentIndex);
        } else {
            TreeListNode tln = getCurrentRootTreeListNode();
            String path = wtn.getTreePathByLevel(1);
            ChildList cl = tln.getChildList(path);
            bw = getCurrentBeanWrapper(cl, wtn, 1);
        }
        return bw;
    }

    public int getCurrentIndex(@SuppressWarnings("rawtypes") WidgetTreeNode wtn) {
        int index;

        if (wtn.getTreePathLength() == 1) {
            index = currentIndex;
        } else {
            TreeListNode tln = getCurrentRootTreeListNode();
            String path = wtn.getTreePathByLevel(1);
            ChildList cl = tln.getChildList(path);
            index = getCurrentIndex(cl, wtn, 1);
        }

        return index;
    }

    private int getCurrentIndex(ChildList cList,
                                @SuppressWarnings("rawtypes") WidgetTreeNode wtn, int indexPath) {
        int index;

        if ((indexPath + 1) == wtn.getTreePathLength()) {
            index = cList.getCurrentIndex();
        } else {
            String path = wtn.getTreePathByLevel(indexPath + 1);
            TreeListNode nodeCurr = cList.getCurrentTreeListNode();
            ChildList cl = nodeCurr.getChildList(path);
            index = getCurrentIndex(cl, wtn, indexPath + 1);
        }
        return index;
    }

    public int createNewObject(@SuppressWarnings("rawtypes") WidgetTreeNode wtn) {

        int index;

        if (propertyNameSelf == null) {
            // usual grid
            if (wtn.getTreePathLength() == 1) {
                index = addToEndOfList();
            } else {
                index = addToUsualSubList(wtn);
            }
        } else {
            // tgrid
            if (wtn.getTreePathLength() == 1) {
                if (ITreeConstants.SHOW_ROOT_NODE) {
                    if (currentIndex == 0) {
                        index = addToEndOfList();
                    } else {
                        index = addToSelfSubList();
                    }
                } else {
                    index = addToSelfSubList();
                }
            } else {
                index = addToUsualSubList(wtn);
            }
        }

        return index;
    }

    private TreeListNode getCurrentRootTreeListNode() {
        return getRootTreeListNodeByIndex(currentIndex);
    }

    private TreeListNode getRootTreeListNodeByIndex(int index) {
        if (propertyNameSelf == null) {
            return rootList.get(index);
        } else {
            // tgrid
            TreeListNode[] nodeFound = new TreeListNode[1];
            slideOnTree(nodeFound, index, null);
            return nodeFound[0];
        }
    }

    private int slideOnTree(TreeListNode[] nodeFound, int _currentIndex,
                            TreeListNode nodeToFind) {
        int ind = 0;
        if (ITreeConstants.SHOW_ROOT_NODE) {
            ind = 1;
        }
        for (TreeListNode tln : rootList) {
            ind = getNodesBasedOnRowIndex(tln, ind, nodeFound, _currentIndex,
                    nodeToFind);
            if (nodeFound != null && nodeFound[0] != null) {
                break;
            }
            ind++;
        }
        if (_currentIndex == -1 && nodeToFind == null) {
            ind--;
        }
        return ind;
    }

    private int addToSelfSubList() {
        TreeListNode[] nodeFound = new TreeListNode[1];
        slideOnTree(nodeFound, currentIndex, null);

        ChildList cl = nodeFound[0].getChildList(propertyNameSelf);
        if (cl != null) {
            TreeListNodeIndex nodeIndex = cl.createNewObject(this,
                    nodeFound[0].getLevel(), nodeFound[0]);
            nodeFound[0] = null;
            return slideOnTree(nodeFound, -1, nodeIndex.getNode());
        }

        return 0;
    }

    private int getNodesBasedOnRowIndex(TreeListNode node, int currentInd,
                                        TreeListNode[] nodeFound, int _currentIndex, TreeListNode nodeToFind) {

        if (_currentIndex == currentInd) {
            nodeFound[0] = node;
            return currentInd;
        }
        if (nodeToFind != null && nodeToFind.getIdNode() == node.getIdNode()) {
            nodeFound[0] = node;
            return currentInd;
        }
        ChildList cl = node.getChildList(propertyNameSelf);
        if (cl != null) {
            List<TreeListNode> tlnSubList = cl.getChildrenList();
            if (tlnSubList.size() > 0) {
                for (TreeListNode slTln : tlnSubList) {
                    currentInd++;
                    currentInd = getNodesBasedOnRowIndex(slTln, currentInd,
                            nodeFound, _currentIndex, nodeToFind);
                    if (nodeFound != null && nodeFound[0] != null) {
                        break;
                    }
                }
            }
        }

        return currentInd;
    }

    private int addToUsualSubList(
            @SuppressWarnings("rawtypes") WidgetTreeNode wtn) {
        TreeListNode tln = rootList.get(currentIndex);
        String path = wtn.getTreePathByLevel(1);
        ChildList cl = tln.getChildList(path);
        return createNewObject(cl, wtn, 1, tln);
    }

    private int addToEndOfList() {
        int index;
        TreeListNode tln = TreeListUtils.createTreeListNodeNew(this,
                rootClazzName, this);
        rootList.add(tln);
        collSyncObject.add(tln.getData());
        if (propertyNameSelf == null) {
            index = rootList.size() - 1;
        } else {
            // to find last index
            index = slideOnTree(null, -1, null);
        }
        return index;
    }

    private int createNewObject(ChildList cList,
                                @SuppressWarnings("rawtypes") WidgetTreeNode wtn, int indexPath,
                                INodeOperation parent) {
        int index = 0;
        if ((indexPath + 1) == wtn.getTreePathLength()) {
            TreeListNode tln = cList.getCurrentTreeListNode();
            TreeListNodeIndex nodeIndex = cList.createNewObject(this,
                    tln.getLevel() - 1, parent);
            if (nodeIndex != null) {
                index = nodeIndex.getIndex();
            }
        } else {
            String path = wtn.getTreePathByLevel(indexPath + 1);
            TreeListNode nodeCurr = cList.getCurrentTreeListNode();
            ChildList cl = nodeCurr.getChildList(path);
            createNewObject(cl, wtn, indexPath + 1, nodeCurr);
        }
        return index;
    }

    public BeanStatusEnum getLockedStatus(int index,
                                          @SuppressWarnings("rawtypes") WidgetTreeNode wtn) {
        BeanStatusEnum bs;

        if (wtn.getTreePathLength() == 1) {
            IBeanWrapper bw = getBeanWrapperWhenPathLengthEqualOne(index);
            bs = lockedStatus(bw);
        } else {
            TreeListNode tln = rootList.get(currentIndex);
            if (lockedStatus(tln.getBeanWrapper()) == null) {
                // if parent status is not locked then go on search to the end
                // from
                // top of tree
                String path = wtn.getTreePathByLevel(1);
                ChildList cl = tln.getChildList(path);
                bs = getLockedStatus(cl, index, wtn, 1);
            } else {
                bs = lockedStatus(tln.getBeanWrapper());
            }
        }

        return bs;
    }

    private BeanStatusEnum lockedStatus(IBeanWrapper bw) {
        BeanStatusEnum bs = null;

        if (bw.getObtainedStatus() != null) {
            switch (bw.getObtainedStatus()) {
                case DELETED:
                    bs = BeanStatusEnum.DELETED;
                    break;
                case LOCKED:
                    bs = BeanStatusEnum.LOCKED;
                    break;
                case SELECTED:
                    bs = BeanStatusEnum.SELECTED;
                    break;
            }
        }
        return bs;
    }

    private BeanStatusEnum getLockedStatus(ChildList cList, int index,
                                           @SuppressWarnings("rawtypes") WidgetTreeNode wtn, int indexPath) {
        BeanStatusEnum bs;
        if ((indexPath + 1) == wtn.getTreePathLength()) {
            TreeListNode tln = cList.getTreeListNode(index);
            IBeanWrapper bw = tln.getBeanWrapper();
            bs = lockedStatus(bw);
        } else {
            String path = wtn.getTreePathByLevel(indexPath + 1);
            TreeListNode nodeCurr = cList.getCurrentTreeListNode();
            if (lockedStatus(nodeCurr.getBeanWrapper()) == null) {
                ChildList cl = nodeCurr.getChildList(path);
                bs = getLockedStatus(cl, index, wtn, indexPath + 1);
            } else {
                bs = lockedStatus(nodeCurr.getBeanWrapper());
            }
        }
        return bs;
    }

    public void goIndex(int index,
                        @SuppressWarnings("rawtypes") WidgetTreeNode wtn) {
        if (wtn.getTreePathLength() == 1) {
            int amount = 0;
            if (propertyNameSelf == null) {
                amount = rootList.size();
            } else {
                for (TreeListNode tln : rootList) {
                    amount++;
                    amount = recursiveCountBeanWithNameSelfForTGRID(tln, amount);
                }
                if (ITreeConstants.SHOW_ROOT_NODE) {
                    amount++;
                }
            }
            if (index >= 0 && index < amount) {
                currentIndex = index;
            }
        } else {
            TreeListNode tln = rootList.get(currentIndex);
            String path = wtn.getTreePathByLevel(1);
            ChildList cl = tln.getChildList(path);
            goIndex(cl, index, wtn, 1);
        }
    }

    private void goIndex(ChildList cList, int index,
                         @SuppressWarnings("rawtypes") WidgetTreeNode wtn, int indexPath) {
        if ((indexPath + 1) == wtn.getTreePathLength()) {
            if (index >= 0 && index < cList.getChildrenSize()) {
                cList.setCurrentIndex(index);
            }
        } else {
            String path = wtn.getTreePathByLevel(indexPath + 1);
            TreeListNode nodeCurr = cList.getCurrentTreeListNode();
            ChildList cl = nodeCurr.getChildList(path);
            goIndex(cl, index, wtn, indexPath + 1);
        }
    }

    public boolean moveNode(int indFromTGrid, int indToTGrid) {

        TreeListPair<TreeListNode, TreeListNode> nodePair = new TreeListPair<TreeListNode, TreeListNode>();

        int ind = 0;
        if (ITreeConstants.SHOW_ROOT_NODE) {
            ind = 1;
        }
        for (TreeListNode tln : rootList) {
            ind = getNodesBasedOnRowIndex(tln, ind, indFromTGrid, indToTGrid,
                    nodePair);
            ind++;
            if (nodePair.isFilled()) {
                break;
            }
        }
        if (nodePair.isFilled()) {
            // Moving nodes
            TreeListNode nodeTo = nodePair.getValue();
            TreeListNode nodeFrom = nodePair.getKey();
            if (propertyNameSelf != null) {
                ChildList cl = nodeTo.getChildList(propertyNameSelf);
                if (cl != null) {
                    nodeFrom.removeFromParentSelfNode();
                    nodeFrom.setParentNode(nodeTo);

                    List<TreeListNode> tlnSubList = cl.getChildrenList();
                    tlnSubList.add(nodeFrom);

                    cl.addSelfObject(nodeFrom);

                    return true;
                }
            }
        } else {
            if (indToTGrid == 0 && nodePair.getKey() != null) {
                // moving node on the first level (to the root)
                if (propertyNameSelf != null) {
                    TreeListNode nodeFrom = nodePair.getKey();
                    if (nodeFrom.getLevel() > 0) {
                        nodeFrom.removeFromParentSelfNode();
                        nodeFrom.setParentNode(this);

                        rootList.add(nodeFrom);

                        collSyncObject.add(nodeFrom.getData());

                        return true;
                    }
                }
            }
        }

        return false;
    }

    private int getNodesBasedOnRowIndex(TreeListNode node, int currentInd,
                                        int indFromTGrid, int indToTGrid,
                                        TreeListPair<TreeListNode, TreeListNode> nodePair) {

        if (indFromTGrid == currentInd) {
            nodePair.setKey(node);
        }
        if (indToTGrid == currentInd) {
            nodePair.setValue(node);
        }
        if (propertyNameSelf != null) {
            ChildList cl = node.getChildList(propertyNameSelf);
            if (cl != null) {
                List<TreeListNode> tlnSubList = cl.getChildrenList();
                for (TreeListNode slTln : tlnSubList) {
                    currentInd++;
                    currentInd = getNodesBasedOnRowIndex(slTln, currentInd,
                            indFromTGrid, indToTGrid, nodePair);
                }
            }
        }

        return currentInd;
    }

    private IBeanWrapper getBeanWrapper(ChildList cList, int index,
                                        @SuppressWarnings("rawtypes") WidgetTreeNode wtn, int indexPath) {
        IBeanWrapper bw;

        if ((indexPath + 1) == wtn.getTreePathLength()) {
            TreeListNode tln = cList.getTreeListNode(index);
            bw = tln.getBeanWrapper();
        } else {
            String path = wtn.getTreePathByLevel(indexPath + 1);
            TreeListNode nodeCurr = cList.getCurrentTreeListNode();
            ChildList cl = nodeCurr.getChildList(path);
            bw = getBeanWrapper(cl, index, wtn, indexPath + 1);
        }
        return bw;
    }

    private IBeanWrapper getCurrentBeanWrapper(ChildList cList,
                                               @SuppressWarnings("rawtypes") WidgetTreeNode wtn, int indexPath) {
        IBeanWrapper bw;
        if ((indexPath + 1) == wtn.getTreePathLength()) {
            TreeListNode tln = cList.getCurrentTreeListNode();
            bw = tln.getBeanWrapper();
        } else {
            String path = wtn.getTreePathByLevel(indexPath + 1);
            TreeListNode nodeCurr = cList.getCurrentTreeListNode();
            ChildList cl = nodeCurr.getChildList(path);
            bw = getCurrentBeanWrapper(cl, wtn, indexPath + 1);
        }
        return bw;
    }

    public String getPropertyNameSelf() {
        return propertyNameSelf;
    }

    public void setRootDefProperties(BeanFormDef beanDef) {
        this.beanOfModel = beanDef;
        this.propertyNameSelf = beanDef.getPropertyNameSelf();

        // in opposite case we have lost validation for not-null properties of
        // root bean
        BeanNestedPropInfo bInfo = new BeanNestedPropInfo();
        bInfo.setBeanDef(beanDef);
        propertyInfoForNestedAndRootClass.put(beanDef.getClazz(), bInfo);

        setNotNullDefProperties(beanDef);
    }

    private void setNotNullDefProperties(BeanFormDef beanDef) {
        BeanNameProperty[] nnpa = beanDef.getNotNulProperties();
        if (nnpa != null) {
            BeanNestedPropInfo bInfo = propertyInfoForNestedAndRootClass
                    .get(beanDef.getClazz());
            if (bInfo != null) {
                bInfo.setNotNullProperties(nnpa);
            }
        }
    }

    /*
     * public void setPropertyNameSelf(String propertyNameSelf) {
     * this.propertyNameSelf = propertyNameSelf; }
     */
    private void createEmptyObjectForRootNodeSimulate() {
        Object emptyData = TreeListUtils.createSimplePOJOObject(rootClazzName);
        emptybeanWrapper = new BeanWrapper(emptyData, BeanStatusEnum.PERSISTENT);
    }

    @SuppressWarnings("unused")
    public String getRootClazzName() {
        return rootClazzName;
    }

    public TreeListNode getTreeListNode(TreeListNode parent, int index) {
        TreeListNode tln;

        if (parent == null) {
            // root
            tln = rootList.get(index);
        } else {
            tln = parent.getSelfTreeListNode(index);
        }

        return tln;
    }

    public int getChildCount(TreeListNode parent) {
        return parent.getSelfChildrenSize();
    }

    int getNextNodeID() {
        return nodeOrder.incrementAndGet();
    }

    public int getIndexOfChild(Object parent, Object child) {
        int index = -1;

        if (parent != null && child != null) {
            TreeListNode tlnChild = (TreeListNode) child;
            if (parent instanceof TreeList) {
                int ind = 0;
                for (TreeListNode tln : rootList) {
                    if (tln.getIdNode() == tlnChild.getIdNode()) {
                        index = ind;
                        break;
                    }
                    ind++;
                }
            } else if (parent instanceof TreeListNode) {
                index = ((TreeListNode) parent).getSelfIndex(tlnChild);
            } else {
                throw new SmartFlexException(
                        "There is unknown class for tree node");
            }
        }

        return index;
    }

    @Override
    public void removeFromParentSelfNode(TreeListNode node) {

        if (rootList.remove(node)) {
            node.clearParentNode();
            collSyncObject.remove(node.getData());
        }

    }

    @Override
    public int getLevel() {
        return TREE_LIST_LEVEL_VALUE;
    }

    @Override
    public Object getObject() {
        return null;
    }

    BeanFormDef getBeanOfModel() {
        return beanOfModel;
    }

    public Object getObjectSource() {
        return objectOrList;
    }

}
