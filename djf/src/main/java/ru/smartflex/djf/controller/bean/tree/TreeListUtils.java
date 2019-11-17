package ru.smartflex.djf.controller.bean.tree;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ClassUtils;

public class TreeListUtils {

    private TreeListUtils() {
    }

    @SuppressWarnings("rawtypes")
    public static Class getClass(String clazzName) {
        Class clazz;
        try {
            clazz = ClassUtils.getClass(clazzName);
        } catch (ClassNotFoundException e) {
            throw new TreeListException("Error class loading: " + clazzName, e);
        }
        return clazz;
    }

    static Object createSimplePOJOObject(String clazzName) {
        Object obj;

        @SuppressWarnings("rawtypes")
        Class clazz = getClass(clazzName);

        try {
            obj = ConstructorUtils.invokeConstructor(clazz, null);
        } catch (Exception e) {
            throw new TreeListException("Error object creation", e);
        }

        return obj;
    }

    private static Object createSimplePOJOObject(
            @SuppressWarnings("rawtypes") Class clazz) {
        Object obj;

        try {
            obj = ConstructorUtils.invokeConstructor(clazz, null);
        } catch (Exception e) {
            throw new TreeListException("Error object creation", e);
        }

        return obj;
    }

    public static Object getPropertyValue(Object bean, String propertyName) {
        Object obj;

        try {
            obj = PropertyUtils.getProperty(bean, propertyName);
        } catch (Exception e) {
            throw new TreeListException("Error get value from property name: "
                    + propertyName, e);
        }
        return obj;
    }

    public static void setPropertyValue(Object bean, String propertyName,
                                        Object val) {
        try {
            PropertyUtils.setProperty(bean, propertyName, val);
        } catch (Exception e) {
            throw new TreeListException("Error set value for property name: "
                    + propertyName, e);
        }
    }

    static TreeListNode createTreeListNodePersistent(TreeList treeList,
                                                     Object obj, int level, INodeOperation parent, String selAbleProp) {
        IBeanWrapper bw;
        if (treeList.getWrapperFactory() != null) {
            bw = treeList.getWrapperFactory().createBeanWrappper(obj,
                    BeanStatusEnum.PERSISTENT);
        } else {
            bw = new BeanWrapper(obj, BeanStatusEnum.PERSISTENT);
        }
        // working with selectAble
        if (selAbleProp != null) {
            Object oSel = getPropertyValue(obj, selAbleProp);
            if (oSel != null) {
                if (!(oSel instanceof Boolean)) {
                    throw new TreeListException(
                            "Value is not boolean  for selectable bind name: "
                                    + selAbleProp);
                }
                Boolean bSel = (Boolean) oSel;
                if (bSel) {
                    bw.changeSelectStatus();
                }
            }
        }
        return new TreeListNode(bw, treeList, level, parent);
    }

    static TreeListNode createTreeListNodeNew(TreeList treeList,
                                              String clazzName, INodeOperation parent) {
        Object obj = TreeListUtils.createSimplePOJOObject(clazzName);

        return createTreeListNodeNew(treeList, obj, TreeList.START_NODE_LEVEL_VALUE, parent, clazzName);
    }

    static TreeListNode createTreeListNodeNew(TreeList treeList,
                                              @SuppressWarnings("rawtypes") Class clazz, int level,
                                              INodeOperation parent) {
        Object obj = TreeListUtils.createSimplePOJOObject(clazz);

        return createTreeListNodeNew(treeList, obj, level, parent,
                clazz.getName());
    }

    private static TreeListNode createTreeListNodeNew(TreeList treeList,
                                                      Object obj, int level, INodeOperation parent, String clazzName) {
        IBeanWrapper bw;
        if (treeList.getWrapperFactory() != null) {
            bw = treeList.getWrapperFactory().createBeanWrappper(obj,
                    BeanStatusEnum.NEW);
        } else {
            bw = new BeanWrapper(obj, BeanStatusEnum.NEW);
        }

        if (treeList.isParentChildExisted()) {
            // there have to set parent
            String prop = treeList.getParentPropertyForClass(clazzName);
            if (prop != null) {
                Object po = parent.getObject();
                setPropertyValue(obj, prop, po);
            }
        }

        return new TreeListNode(bw, treeList, level, parent);
    }

    static String generateHyphen(int level, boolean printAsterix) {
        int size = level * 2 + 1;
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            if (printAsterix) {
                sb.append("*");
            } else {
                sb.append("-");
            }
        }
        return sb.toString();
    }

    public static String[] getPartProperties(String src) {
        return src.split("[" + ITreeConstants.PARENT_CHILD_DELIMITER + "]");
    }

    static String makePath(String[] treePath, int indexLast) {
        String path;
        StringBuilder sb = new StringBuilder(indexLast + 1);
        for (int i = 0; i <= indexLast; i++) {
            if (sb.length() > 0) {
                sb.append(ITreeConstants.PARENT_CHILD_DELIMITER);
            }
            sb.append(treePath[i]);
        }
        path = sb.toString();
        return path;
    }

    static void walkTreeDown(TreeListNode node,
                             ITreeWalkOperation oper, boolean printIfCurrent) {

        oper.beforeCycle(node, printIfCurrent);
        oper.insideCycle(node);

        Collection<ChildList> cList = node.getChildList();
        if (cList.size() > 0) {
            for (ChildList cl : cList) {
                List<TreeListNode> list = cl.getChildrenList();
                if (list != null && list.size() > 0) {
                    int it = 0;
                    for (TreeListNode tln : list) {
                        // oper.insideCycle(node);

                        boolean toPrintAsterix = false;
                        if (printIfCurrent) {
                            if (it == cl.getCurrentIndex()) {
                                toPrintAsterix = true;
                            }
                        }

                        walkTreeDown(tln, oper, toPrintAsterix);
                        it++;
                    }
                }
            }
        }
    }

    static void syncDelete(ChildList child, Object data) {
        Collection<Object> objs = child.getCollChildSyncObject();
        if (objs != null && data != null) {
            Iterator<Object> iter = objs.iterator();
            while (iter.hasNext()) {
                Object o = iter.next();
                if (o == data) {
                    // pointers are equal
                    iter.remove();
                    break;
                }
            }
        }
    }
}
