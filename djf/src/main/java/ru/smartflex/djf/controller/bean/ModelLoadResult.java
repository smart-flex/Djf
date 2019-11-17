package ru.smartflex.djf.controller.bean;

import java.util.HashMap;
import java.util.Map;

import ru.smartflex.djf.SmartFlexException;
import ru.smartflex.djf.controller.bean.tree.TreeList;

public class ModelLoadResult {
    private boolean wasLoadError = false;

    private Map<String, TreeList> modelList = new HashMap<String, TreeList>();

    private Exception exception = null;

    public boolean isWasLoadError() {
        return wasLoadError;
    }

    public void setWasLoadError(boolean wasLoadError) {
        this.wasLoadError = wasLoadError;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public void setTreeList(String idModel, TreeList treeList) {
        modelList.put(idModel, treeList);
    }

    public TreeList getTreeList(String idModel) {
        TreeList tree = modelList.get(idModel);

        if (tree == null) {
            throw new SmartFlexException("There is no tree for model: "
                    + idModel);
        }

        return tree;
    }

}
