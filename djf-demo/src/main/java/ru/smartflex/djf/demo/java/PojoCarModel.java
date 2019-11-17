package ru.smartflex.djf.demo.java;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class PojoCarModel implements Serializable {

    private static final long serialVersionUID = -6377136514896052463L;

    private static AtomicInteger id = new AtomicInteger(0);

    private Integer modelId;
    private String modelName = null;

    @SuppressWarnings("unused")
    public PojoCarModel() {
        super();
        this.modelId = id.incrementAndGet();
    }

    @SuppressWarnings("WeakerAccess")
    public PojoCarModel(String modelName) {
        super();
        this.modelId = id.incrementAndGet();
        this.modelName = modelName;
    }

    @SuppressWarnings("unused")
    public Integer getModelId() {
        return modelId;
    }

    @SuppressWarnings("unused")
    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    @SuppressWarnings("unused")
    public String getModelName() {
        return modelName;
    }

    @SuppressWarnings("unused")
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

}
