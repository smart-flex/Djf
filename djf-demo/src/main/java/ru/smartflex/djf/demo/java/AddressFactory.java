package ru.smartflex.djf.demo.java;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import ru.smartflex.djf.SmartFlexException;
import ru.smartflex.djf.controller.bean.tree.IBeanWrapper;

public class AddressFactory {

    private List<PojoStreet> listAddress = new ArrayList<PojoStreet>();
    private byte[] listAddressStore = null;
    private boolean listAddressFilled = false;

    private PojoStreet dergachevskayaStreet = new PojoStreet(1, "Dergachevskaya street");
    private PojoBuilding bld5 = new PojoBuilding("5");
    private PojoStreet altataStreet = new PojoStreet(2, "Altata river street");
    private PojoBuilding bld19l = new PojoBuilding("19l");

    public AddressFactory() {
        dergachevskayaStreet.getBuildings().add(new PojoBuilding("1"));
        dergachevskayaStreet.getBuildings().add(new PojoBuilding("2"));
        dergachevskayaStreet.getBuildings().add(bld5);
        dergachevskayaStreet.getBuildings().add(new PojoBuilding("7"));
        dergachevskayaStreet.getBuildings().add(new PojoBuilding("10"));
        dergachevskayaStreet.getBuildings().add(new PojoBuilding("12a"));

        altataStreet.getBuildings().add(new PojoBuilding("2g"));
        altataStreet.getBuildings().add(new PojoBuilding("12a"));
        altataStreet.getBuildings().add(bld19l);
        altataStreet.getBuildings().add(new PojoBuilding("68i"));
    }

    @SuppressWarnings("WeakerAccess")
    public PojoStreet getDergachevskayaStreet() {
        return dergachevskayaStreet;
    }

    @SuppressWarnings("WeakerAccess")
    public PojoBuilding getBld5() {
        return bld5;
    }

    @SuppressWarnings("WeakerAccess")
    public PojoStreet getAltataStreet() {
        return altataStreet;
    }

    @SuppressWarnings("WeakerAccess")
    public PojoBuilding getBld19l() {
        return bld19l;
    }

    @SuppressWarnings({"unchecked", "unused"})
    public List<PojoStreet> getAddress() {

        if (listAddressFilled) {
            listAddress = FactoryHelper.restoreFromStore(listAddressStore);
            return listAddress;
        }

        initialFilling();

        listAddressFilled = true;

        listAddressStore = FactoryHelper.saveToStore(listAddress);

        return listAddress;
    }

    private void initialFilling() {
        listAddress.add(dergachevskayaStreet);
        listAddress.add(altataStreet);

    }

    @SuppressWarnings({"unchecked", "unused"})
    public List<PojoStreet> getAddress(Boolean interrupt) {

        if (interrupt) {
            throw new SmartFlexException("Network error emulation");
        }

        if (listAddressFilled) {
            listAddress = FactoryHelper.restoreFromStore(listAddressStore);
            return listAddress;
        }

        initialFilling();

        listAddressFilled = true;

        listAddressStore = FactoryHelper.saveToStore(listAddress);

        return listAddress;
    }

    @SuppressWarnings("unused")
    public void saveAddress(String idSession, Deque<IBeanWrapper> toSave) {

        int i = 1;
        for (PojoStreet ps : listAddress) {
            if (ps.getIdStreet() == 0) {
                ps.setIdStreet(i);
            }
            i++;
        }
        listAddressStore = FactoryHelper.saveToStore(listAddress);
    }

}
