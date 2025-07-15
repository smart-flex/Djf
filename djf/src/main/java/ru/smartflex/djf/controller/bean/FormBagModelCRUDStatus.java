package ru.smartflex.djf.controller.bean;

public class FormBagModelCRUDStatus {

    private String idModel;
    private boolean modelCanBeChanged;
    private boolean modelCanBeAppend;
    private boolean modelCanNotBeSaved;
    private boolean modelCanBeDeleted;
    private boolean modelMayBeRefreshed;

    FormBagModelCRUDStatus(String idModel, boolean modelCanBeChanged,
                           boolean modelCanBeAppend, boolean modelCanNotBeSaved,
                           boolean modelCanBeDeleted, boolean modelMayBeRefreshed) {
        super();
        this.idModel = idModel;
        this.modelCanBeChanged = modelCanBeChanged;
        this.modelCanBeAppend = modelCanBeAppend;
        this.modelCanNotBeSaved = modelCanNotBeSaved;
        this.modelCanBeDeleted = modelCanBeDeleted;
        this.modelMayBeRefreshed = modelMayBeRefreshed;
    }

    /**
     * Модель закрыта от изменения и потому нет смысла ее рефрешить (перечитывать)
     * @return
     */
    boolean isModelNotBeRefreshedByDefault() {
        boolean fok = modelCanNotBeSaved || (!modelCanBeChanged);
        return fok;
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

    boolean isModelMayBeRefreshed() {
        return modelMayBeRefreshed;
    }

    String getIdModel() {
        return idModel;
    }

    @Override
    public String toString() {
        return "FormBagModelCRUDStatus [modelCanBeChanged=" + modelCanBeChanged
                + ", modelCanBeAppend=" + modelCanBeAppend
                + ", modelCanNotBeSaved=" + modelCanNotBeSaved
                + ", modelCanBeDeleted=" + modelCanBeDeleted
                + ", modelMayBeRefreshed=" + modelMayBeRefreshed + "]";
    }

}
