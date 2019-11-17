package ru.smartflex.djf.controller.bean;

public class FormBagModelCRUDStatus {

    private boolean modelCanBeChanged;
    private boolean modelCanBeAppend;
    private boolean modelCanNotBeSaved;
    private boolean modelCanBeDeleted;

    FormBagModelCRUDStatus(boolean modelCanBeChanged,
                           boolean modelCanBeAppend, boolean modelCanNotBeSaved,
                           boolean modelCanBeDeleted) {
        super();
        this.modelCanBeChanged = modelCanBeChanged;
        this.modelCanBeAppend = modelCanBeAppend;
        this.modelCanNotBeSaved = modelCanNotBeSaved;
        this.modelCanBeDeleted = modelCanBeDeleted;
    }

    boolean isModelCanBeChanged() {
        return modelCanBeChanged;
    }

    boolean isModelCanBeAppend() {
        return modelCanBeAppend;
    }

    boolean isModelCanNotBeSaved() {
        return modelCanNotBeSaved;
    }

    boolean isModelCanBeDeleted() {
        return modelCanBeDeleted;
    }

    @Override
    public String toString() {
        return "FormBagModelCRUDStatus [modelCanBeChanged=" + modelCanBeChanged
                + ", modelCanBeAppend=" + modelCanBeAppend
                + ", modelCanNotBeSaved=" + modelCanNotBeSaved
                + ", modelCanBeDeleted=" + modelCanBeDeleted + "]";
    }

}
