package ru.smartflex.djf.demo.java;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PojoCarType implements Serializable {

    private static final long serialVersionUID = -4758555263566808251L;

    private static AtomicInteger id = new AtomicInteger(0);

    private Integer carId;
    private String carName = null;
    private List<PojoCarModel> modelList = new ArrayList<PojoCarModel>();

    @SuppressWarnings("unused")
    public PojoCarType() {
        this.carId = id.incrementAndGet();
    }

    @SuppressWarnings("WeakerAccess")
    public PojoCarType(String carName) {
        super();
        this.carId = id.incrementAndGet();
        this.carName = carName;
    }

    @SuppressWarnings("WeakerAccess")
    public PojoCarModel addModel(String name) {
        PojoCarModel model = new PojoCarModel(name);
        modelList.add(model);
        return model;
    }

    @SuppressWarnings("unused")
    public Integer getCarId() {
        return carId;
    }

    @SuppressWarnings("unused")
    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    @SuppressWarnings("unused")
    public String getCarName() {
        return carName;
    }

    @SuppressWarnings("unused")
    public void setCarName(String carName) {
        this.carName = carName;
    }

    @SuppressWarnings("unused")
    public List<PojoCarModel> getModelList() {
        return modelList;
    }

    @SuppressWarnings("unused")
    public void setModelList(List<PojoCarModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((carId == null) ? 0 : carId.hashCode());
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
        PojoCarType other = (PojoCarType) obj;
        if (carId == null) {
            //noinspection RedundantIfStatement
            if (other.carId != null)
                return false;
        } else //noinspection RedundantIfStatement
            if (!carId.equals(other.carId))
                return false;
        return true;
    }

}
