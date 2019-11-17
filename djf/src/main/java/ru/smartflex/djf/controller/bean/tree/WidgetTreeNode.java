package ru.smartflex.djf.controller.bean.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.smartflex.djf.SFConstants;

public class WidgetTreeNode<T> {

    private T widget = null;
    private List<WidgetTreeNode<T>> children = new ArrayList<WidgetTreeNode<T>>();
    private boolean scrollWidget = false;
    private String treePath = null;
    private int depthNode = 0;

    private List<WidgetTreeNode<T>> orderedTreeNode = null;
    private String[] treePathArray = null;
    private Map<Integer, String> treePathMap = new HashMap<Integer, String>();
    private List<WidgetTreeNode<T>> childrenFilledModel = new ArrayList<WidgetTreeNode<T>>();

    public WidgetTreeNode() {
        super();
    }

    public WidgetTreeNode(T widget, boolean scrollWidget, String treePath) {
        super();
        this.widget = widget;
        this.scrollWidget = scrollWidget;
        this.treePath = treePath;
    }

    public WidgetTreeNode<T> add(WidgetTreeNode<T> node, boolean baseModel) {
        if (baseModel) {
            children.add(node);
        } else {
            childrenFilledModel.add(node);
        }
        node.setDepthNode(this.getDepthNode() + 1);
        return node;
    }

    public WidgetTreeNode<T> getParentScrollNodeByChildPath(int levelUI,
                                                            String childTreePath, boolean baseModel) {

        // traversal of the tree from the root
        WidgetTreeNode<T> nodeFound = null;
        for (WidgetTreeNode<T> node : (baseModel ? children : childrenFilledModel)) {
            nodeFound = compareNode(node, levelUI, childTreePath);
            if (nodeFound != null) {
                break;
            }
        }

        return nodeFound;
    }

    private WidgetTreeNode<T> compareNode(WidgetTreeNode<T> node, int levelUI,
                                          String childTreePath) {
        WidgetTreeNode<T> nodeFound = null;
        if (node.getDepthNode() == levelUI) {
            if (node.isScrollWidget()) {
                if (childTreePath.indexOf(node.getTreePath()) == 0) {
                    nodeFound = node;
                }
            }
        }
        if (nodeFound == null) {
            for (WidgetTreeNode<T> childrenNode : node.getChildren()) {
                nodeFound = compareNode(childrenNode, levelUI, childTreePath);
                if (nodeFound != null) {
                    break;
                }
            }
        }
        return nodeFound;
    }

    private int getDepthNode() {
        return depthNode;
    }

    private void setDepthNode(int depthNode) {
        this.depthNode = depthNode;
    }

    public boolean isScrollWidget() {
        return scrollWidget;
    }

    public String getTreePath() {
        return treePath;
    }

    int getTreePathLength() {
        int ln = 0;

        String[] pathArray = getTreePathArray();
        if (pathArray != null) {
            ln = pathArray.length;
        }

        return ln;
    }

    private String[] getTreePathArray() {
        if (treePathArray == null) {
            if (treePath != null) {
                treePathArray = TreeListUtils.getPartProperties(treePath);
            }
        }
        return treePathArray;
    }

    String getTreePathByLevel(int level) {
        String path;

        Integer key = level;
        path = treePathMap.get(key);
        if (path == null) {
            String[] pathArray = getTreePathArray();
            if (pathArray != null && pathArray.length > level) {
                path = TreeListUtils.makePath(pathArray, level);
                treePathMap.put(key, path);
            }
        }

        return path;
    }

    public List<WidgetTreeNode<T>> getChildren() {
        return children;
    }

    @SuppressWarnings("unused")
    public String drawWidgetTree() {
        StringBuilder sb = new StringBuilder();

        sb.append(SFConstants.NEW_LINE);
        sb.append("Widget tree");
        sb.append(SFConstants.NEW_LINE);

        for (WidgetTreeNode<T> childrenNode : children) {
            drawWidgetNode(childrenNode, sb);
        }

        return sb.toString();
    }

    private void drawWidgetNode(WidgetTreeNode<T> node, StringBuilder sb) {
        sb.append("|");
        sb.append(TreeListUtils.generateHyphen(node.getDepthNode(), false));
        sb.append(node.getDepthNode());
        sb.append("L ");
        String str = node.getTreePath();
        sb.append(str);
        sb.append(" ");
        sb.append(" ").append(node.getWidget());
        sb.append(SFConstants.NEW_LINE);

        for (WidgetTreeNode<T> childrenNode : node.getChildren()) {
            drawWidgetNode(childrenNode, sb);
        }
    }

    public List<WidgetTreeNode<T>> getOrderedTreeNode() {
        if (orderedTreeNode == null) {
            orderedTreeNode = new ArrayList<WidgetTreeNode<T>>();
            for (int iDepth = 1; iDepth < Integer.MAX_VALUE; iDepth++) {
                int sizePrev = orderedTreeNode.size();
                recursiveArrayMaker(this, iDepth, orderedTreeNode);
                int sizeNew = orderedTreeNode.size();
                if (sizePrev == sizeNew) {
                    break;
                }
            }
        }
        return orderedTreeNode;
    }

    private void recursiveArrayMaker(WidgetTreeNode<T> wtn, int currentDepth,
                                     List<WidgetTreeNode<T>> orderedList) {

        for (WidgetTreeNode<T> childrenNode : wtn.getChildren()) {
            if (childrenNode.getDepthNode() == currentDepth) {
                orderedList.add(childrenNode);
            } else {
                recursiveArrayMaker(childrenNode, currentDepth, orderedList);
            }
        }
    }

    public T getWidget() {
        return widget;
    }

    @Override
    public String toString() {
        if (widget == null) {
            return "WidgetTreeNode ROOT";
        } else {
            return "WidgetTreeNode [scrollWidget=" + scrollWidget
                    + ", treePath=" + treePath + ", depthNode=" + depthNode
                    + "]";
        }
    }

}
