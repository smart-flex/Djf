package ru.smartflex.djf.demo.java;

import java.util.ArrayList;
import java.util.List;

public class CarFactory {

    private PojoCarType sedan = new PojoCarType("Sedan");
    private PojoCarType truck = new PojoCarType("Truck");
    private PojoCarType sportCar = new PojoCarType("Sport car");
    private PojoCarModel lancerEvo;

    public CarFactory() {
        lancerEvo = sportCar.addModel("Lancer Evo");
        sportCar.addModel("Camaro SS");
        sportCar.addModel("Porsche Cayman");

        truck.addModel("Kamaz");
    }

    @SuppressWarnings("unused")
    public List<PojoCarType> getCarType() {
        List<PojoCarType> list = new ArrayList<PojoCarType>();

        list.add(sedan);
        list.add(truck);
        list.add(sportCar);

        return list;
    }

    @SuppressWarnings("WeakerAccess")
    public PojoCarType getSportCar() {
        return sportCar;
    }

    @SuppressWarnings("WeakerAccess")
    public PojoCarModel getLancerEvo() {
        return lancerEvo;
    }

}
