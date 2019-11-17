package ru.smartflex.djf.controller.bean;

import ru.smartflex.djf.controller.bean.tree.ITreeConstants;
import ru.smartflex.djf.controller.bean.tree.TreeListUtils;
import ru.smartflex.djf.controller.bean.tree.WidgetTreeNode;

public class UIWrapperModel {

    private String bindPath = null;
    private int levelUI = -1;
    private String property = null;
    private String idModel = null;
    private String treePath = null;
    private WidgetTreeNode<?> treeNode = null;
    private String propParentCollection = null;
    private boolean belongToModel = false;

    boolean isBindPathBelongModel(String idModel) {
        boolean fok = false;
        if (bindPath != null) {
            boolean belongToModel = false;
            if (!bindPath.contains(ITreeConstants.PARENT_CHILD_DELIMITER)) {
                // case when button is belong to model
                belongToModel = true;
            }
            String search = idModel;
            if (!belongToModel) {
                search += ITreeConstants.PARENT_CHILD_DELIMITER;
            }
            int indBind = bindPath.indexOf(search);
            if (indBind == 0) {
                fok = true;
                this.idModel = idModel;
                if (belongToModel) {
                    levelUI = getLevelBelong(bindPath);
                } else {
                    levelUI = getLevel(bindPath);

                    String prop = null;
                    String[] bindProperty = TreeListUtils
                            .getPartProperties(bindPath);
                    if (bindProperty.length > 0) {
                        prop = bindProperty[bindProperty.length - 1];
                    }
                    property = prop;
                }
                defineTreePath(bindPath);
            }
        }
        return fok;
    }

    void handleBelongModelFillForce() {
        String[] parts = TreeListUtils.getPartProperties(bindPath);
        String idModelFill = parts[0];
        isBindPathBelongModel(idModelFill);
    }

    private boolean checkBelongModel(String idModel) {
        boolean fok = false;
        if (this.idModel != null) {
            fok = this.idModel.equals(idModel);
        }
        return fok;
    }

    private void setTreeNode(WidgetTreeNode<?> treeNode) {
        this.treeNode = treeNode;
    }

    public WidgetTreeNode<?> getTreeNode() {
        return treeNode;
    }

    static WidgetTreeNode<?> getTreeNode(String idModel, UIWrapper uiw) {
        WidgetTreeNode<?> treeNode = null;

        UIWrapperModel m1 = uiw.getModelBase();
        UIWrapperModel m2 = uiw.getModelFill();
        if (m1.checkBelongModel(idModel)) {
            treeNode = m1.getTreeNode();
        } else if (m2.checkBelongModel(idModel)) {
            treeNode = m2.getTreeNode();
        }

        return treeNode;
    }

    static void setTreeNode(WidgetTreeNode<?> treeNode, String idModel,
                            UIWrapper uiw) {

        UIWrapperModel m1 = uiw.getModelBase();
        UIWrapperModel m2 = uiw.getModelFill();
        if (m1.checkBelongModel(idModel)) {
            m1.setTreeNode(treeNode);
        } else if (m2.checkBelongModel(idModel)) {
            m2.setTreeNode(treeNode);
        }

    }

    static int getLevelUI(String idModel, UIWrapper uiw) {
        int lev = -1;
        UIWrapperModel m1 = uiw.getModelBase();
        UIWrapperModel m2 = uiw.getModelFill();
        if (m1.checkBelongModel(idModel)) {
            lev = m1.levelUI;
        } else if (m2.checkBelongModel(idModel)) {
            lev = m2.levelUI;
        }
        return lev;
    }

    static UIWrapperModel getWrapperModel(String idModel, UIWrapper uiw) {
        UIWrapperModel ret = null;

        UIWrapperModel m1 = uiw.getModelBase();
        UIWrapperModel m2 = uiw.getModelFill();
        if (m1.checkBelongModel(idModel)) {
            ret = m1;
        } else if (m2.checkBelongModel(idModel)) {
            ret = m2;
        }

        return ret;
    }

    static String getTreePath(String idModel, UIWrapper uiw) {
        String tree = null;

        UIWrapperModel m1 = uiw.getModelBase();
        UIWrapperModel m2 = uiw.getModelFill();
        if (m1.checkBelongModel(idModel)) {
            tree = m1.treePath;
        } else if (m2.checkBelongModel(idModel)) {
            tree = m2.treePath;
        }

        return tree;
    }

    void setLevelUI(int levelUI) {
        this.levelUI = levelUI;
    }

    public void setIdModel(String idModel) {
        this.idModel = idModel;
    }

    void setBindPath(String bindPath) {
        this.bindPath = bindPath;
    }

    @SuppressWarnings("unused")
    public String getBindPath() {
        return bindPath;
    }

    void defineTreePath(String bindPath) {
        if (bindPath != null) {
            if (treePath == null) {
                treePath = extractTreePath(bindPath);
            }
        }
    }

    private String extractTreePath(String bind) {
        StringBuilder sb = new StringBuilder();

        String[] parts = TreeListUtils.getPartProperties(bind);
        if (parts.length > 0) {

            for (int i = 0; i <= parts.length; i++) {
                if (i <= levelUI) {
                    if (sb.length() > 0) {
                        sb.append(ITreeConstants.PARENT_CHILD_DELIMITER);
                    }
                    sb.append(parts[i]);
                }
                if (i == levelUI) {
                    propParentCollection = parts[i];
                }
            }
            return sb.toString();
        }

        return null;
    }

    static int getLevel(String bind) {
        int level = -1;

        if (bind != null) {
            String[] parts = TreeListUtils.getPartProperties(bind);
            level = parts.length - 2;
        }

        return level;
    }

    private int getLevelBelong(String belong) {
        int level = -1;

        if (belong != null) {
            String[] parts = TreeListUtils.getPartProperties(belong);
            level = parts.length - 1;
        }

        return level;
    }

    public String getIdModel() {
        return idModel;
    }

    public String getProperty() {
        return property;
    }

    public String getTreePath() {
        return treePath;
    }

    public int getLevelUI() {
        return levelUI;
    }

    @Override
    public String toString() {
        return "UIWrapperModel [bindPath=" + bindPath + ", levelUI=" + levelUI
                + ", property=" + property + ", idModel=" + idModel
                + ", treePath=" + treePath
                + ", propParentCollection=" + propParentCollection
                + ", treeNode=" + treeNode
                + "]";
    }

    public String getPropParentCollection() {
        return propParentCollection;
    }

    public boolean isBelongToModel() {
        return belongToModel;
    }

    void setFlagOfBelongToModel() {
        this.belongToModel = true;
    }

}
