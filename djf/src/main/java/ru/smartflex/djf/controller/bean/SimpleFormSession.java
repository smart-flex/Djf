package ru.smartflex.djf.controller.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.smartflex.djf.controller.exception.MissingException;
import ru.smartflex.djf.model.gen.FormType;
import ru.smartflex.djf.model.gen.ModelType;

public class SimpleFormSession implements IFormSession {

    private FormType form;
    private Map<String, Boolean> readOnlyMap = new HashMap<String, Boolean>(2);
    private Map<String, Boolean> noAppendMap = new HashMap<String, Boolean>(2);
    private Map<String, Boolean> noSaveMap = new HashMap<String, Boolean>(2);
    private Map<String, Boolean> noDeleteMap = new HashMap<String, Boolean>(2);
    private Map<String, Object> formParameters;
    private Map<String, Object> modelBeans = new HashMap<String, Object>();

    public SimpleFormSession(FormType form, Map<String, Object> formParameters) {
        this.form = form;
        this.formParameters = formParameters;
    }

    @Override
    public String getIdForm() {
        return form.getIdForm();
    }

    @Override
    public void setReadOnly(String idModel) {
        if (check(idModel)) {
            readOnlyMap.put(idModel, Boolean.TRUE);
        }
    }

    @Override
    public void setNoAppend(String idModel) {
        if (check(idModel)) {
            noAppendMap.put(idModel, Boolean.TRUE);
        }
    }

    private boolean check(String idModel) {
        boolean fok = false;
        if (idModel != null) {
            List<ModelType> modelList = form.getModels().getModel();
            for (ModelType mt : modelList) {
                if (mt.getId() != null && mt.getId().equals(idModel)) {
                    fok = true;
                    break;
                }
            }
            if (!fok) {
                throw new MissingException("Model: " + idModel
                        + " is not found");
            }
        }
        return fok;
    }

    @Override
    public Boolean isReadOnly(String idModel) {
        return readOnlyMap.get(idModel);
    }

    @Override
    public Boolean isNoAppend(String idModel) {
        return noAppendMap.get(idModel);
    }

    @Override
    public void setNoSave(String idModel) {
        if (check(idModel)) {
            noSaveMap.put(idModel, Boolean.TRUE);
        }
    }

    @Override
    public Boolean isNoSave(String idModel) {
        return noSaveMap.get(idModel);
    }

    @Override
    public void setNoDelete(String idModel) {
        if (check(idModel)) {
            noDeleteMap.put(idModel, Boolean.TRUE);
        }
    }

    @Override
    public Boolean isNoDelete(String idModel) {
        return noDeleteMap.get(idModel);
    }

    @Override
    public Map<String, Object> getFormParameters() {
        return formParameters;
    }

    @Override
    public void addBean(String idModel, Object bean) {
        modelBeans.put(idModel, bean);
    }

    @Override
    public Object getBean(String idModel) {
        return modelBeans.get(idModel);
    }
}
