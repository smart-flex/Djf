package ru.smartflex.djf.demo.java;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ru.smartflex.djf.SmartFlexException;
import ru.smartflex.djf.controller.bean.tree.IBeanWrapper;

public class AppEmulator {

    private PojoAccPropertyType ptPrivate = new PojoAccPropertyType(1, "Private");
    private PojoAccPropertyType ptMuni = new PojoAccPropertyType(2, "Municipal");

    private List<PojoAccount> listAccount = new ArrayList<PojoAccount>();
    private byte[] listAccountStore = null;
    private boolean listAccountFilled = false;

    private AddressFactory address;
    private CarFactory car;

    public AppEmulator(AddressFactory address, CarFactory car) {
        this.address = address;
        this.car = car;
    }

    @SuppressWarnings("unused")
    public List<PojoAccount> getAccounts(String sessId) {
        return getAccounts(sessId, Boolean.FALSE);
    }

    @SuppressWarnings("unused")
    public List<PojoAccount> getAccounts(String sessId, Object filter,
                                         Boolean interrupt) {

        boolean emptyFilter = false;
        FilterAccount fa = (FilterAccount) filter;
        if (fa == null) {
            emptyFilter = true;
        } else if ((fa.getAccountNumber() == null || fa.getAccountNumber()
                .trim().length() == 0)
                && (fa.getTypeAccount() == null || fa.getTypeAccount()
                .getIdType() == 0)) {
            emptyFilter = true;
        }

        if (emptyFilter) {
            // nothing to filter
            return getAccounts(sessId, interrupt);
        } else {
            // filtering
            List<PojoAccount> la = new ArrayList<PojoAccount>();
            for (PojoAccount pa : getAccounts(sessId, interrupt)) {
                boolean toAdd = false;
                if (fa.getAccountNumber() != null
                        && fa.getAccountNumber().trim().length() > 0) {
                    if (fa.getAccountNumber().equals(pa.getAccountNumber())) {
                        toAdd = true;
                    }
                }
                if (fa.getTypeAccount() != null
                        && fa.getTypeAccount().getIdType() > 0) {
                    if (pa.getTypeAccount() != null
                            && pa.getTypeAccount().getIdType() == fa
                            .getTypeAccount().getIdType()) {
                        toAdd = true;
                    }
                }
                if (toAdd) {
                    la.add(pa);
                }
            }
            return la;
        }
    }

    @SuppressWarnings({"unchecked", "WeakerAccess"})
    public List<PojoAccount> getAccounts(@SuppressWarnings("unused") String sessId, Boolean interrupt) {

        if (interrupt) {
            throw new SmartFlexException("Network error emulation");
        }

        if (listAccountFilled) {
            listAccount = restoreFromStore(listAccountStore);
            return listAccount;
        }

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

        PojoAccount pa1 = new PojoAccount("19680201-grid", new Date(),
                null, "+7 927 220 31 17", "410000", true, null,
                null, new BigDecimal("2000.23"));
        pa1.setStreet(address.getDergachevskayaStreet());
        pa1.setBuilding(address.getBld5());
        listAccount.add(pa1);

        PojoAccount pa2 = new PojoAccount("19720202", null, "+7 845 228 46 94",
                "+7 919 825 76 58", "410000", true, null, 199912, null);
        pa2.setMilitaryRank("Captain");
        pa2.setMilitaryUnit("2014/1968");
        pa2.setHasCredit(Boolean.TRUE);
        pa2.setHasChildren(Boolean.TRUE);
        pa2.setCarType(car.getSportCar());
        pa2.setCarModel(car.getLancerEvo());
        pa2.setFired(Boolean.FALSE);
        pa2.setInnCode(6454011187094L);
        pa2.setRemark("FoxPro 2 included the \"Rushmore\" optimizing engine, which used indices to accelerate data retrieval and updating. (c) Wiki");
        listAccount.add(pa2);

        PojoAccount pa3 = new PojoAccount("19991203", new Date(), null,
                null, "410000", false, ptPrivate, null,
                new BigDecimal("5050"));
        pa3.setMilitaryRank("Major");
        listAccount.add(pa3);

        PojoAccount pa4 = new PojoAccount("19291704", new Date(),
                "+61 416 819 589", null, "410003", true, ptMuni, 196802,
                new BigDecimal("4040.44"));
        pa4.setHasCredit(Boolean.FALSE);
        pa4.setHasChildren(Boolean.TRUE);
        pa4.setStreet(address.getAltataStreet());
        pa4.setBuilding(address.getBld19l());
        listAccount.add(pa4);

        //noinspection CatchMayIgnoreException
        try {
            PojoAccount pa5 = new PojoAccount("19293205", df.parse("10.10.50"),
                    "5 30 50", "+7 495 851 30 50", "410003", true, null, 196866,
                    new BigDecimal("6060.66"));
            listAccount.add(pa5);
        } catch (ParseException e) {
        }

        PojoAccEquipment pae = new PojoAccEquipment(new BigDecimal("40.6"));
        pae.setDisabled(Boolean.FALSE);
        listAccount.get(1).getEquipment().add(pae);
        listAccount.get(1).getEquipment()
                .add(new PojoAccEquipment(new BigDecimal("2")));

        PojoAccEquipment pae3 = new PojoAccEquipment(new BigDecimal("3"));
        pae3.setDisabled(Boolean.TRUE);
        pae3.getWorkHistory().add(
                new WorkHistory("27-10-2007", "Japan car restored"));

        listAccount.get(1).getEquipment().add(pae3);
        listAccount.get(1).getEquipment()
                .add(new PojoAccEquipment(new BigDecimal("4")));
        listAccount.get(1).getEquipment()
                .add(new PojoAccEquipment(new BigDecimal("5")));
        listAccount.get(1).getEquipment()
                .add(new PojoAccEquipment(new BigDecimal("6")));

        listAccount
                .get(0)
                .getEquipment()
                .add(new PojoAccEquipment(new BigDecimal("4.025"), "g20231227anr",
                        "Equipment for account is very important thing"));

        listAccount.get(2).getEquipment()
                .add(new PojoAccEquipment(new BigDecimal("53.5")));
        listAccount.get(2).getEquipment()
                .add(new PojoAccEquipment(new BigDecimal("3")));

        listAccount.get(3).getEquipment()
                .add(new PojoAccEquipment(new BigDecimal("5")));
        listAccount.get(3).getEquipment()
                .add(new PojoAccEquipment(new BigDecimal("6")));

        pae.getWorkHistory().add(
                new WorkHistory("17-05-2013", "Was repaired surface"));
        pae.getWorkHistory().add(new WorkHistory("27-10-2013", "Tree was cut"));

        listAccountFilled = true;

        listAccountStore = saveToStore(listAccount);

        return listAccount;
    }

    @SuppressWarnings({"rawtypes"})
    private List restoreFromStore(byte[] listStore) {
        List list = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(listStore);
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(bis);
            list = (List) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private byte[] saveToStore(@SuppressWarnings("rawtypes") List list) {
        byte[] listStore = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(list);
            listStore = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return listStore;
    }

    @SuppressWarnings("unused")
    public List<PojoAccPropertyType> getAccPropertyType() {
        List<PojoAccPropertyType> list = new ArrayList<PojoAccPropertyType>();

        list.add(ptPrivate);
        list.add(ptMuni);

        return list;
    }

    public List<PojoCode> getCode() {
        List<PojoCode> list = new ArrayList<PojoCode>();

        PojoCode pc01 = new PojoCode("Cold water", "m3");
        PojoCode pc02 = new PojoCode("Sewerage of cold water", "m3");
        PojoCode pc03 = new PojoCode("Service", "m2");
        PojoCode pc04 = new PojoCode("Hot water", "m3");
        PojoCode pc05 = new PojoCode("Sewerage of hot water", "m3");
        PojoCode pc06 = new PojoCode("Central heating", "giga-cal");

        PojoCode pc03_01 = new PojoCode("Elevator", "people");
        PojoCode pc03_02 = new PojoCode("Cleaning", "m2");
        pc03.getSubCode().add(pc03_01);
        pc03.getSubCode().add(pc03_02);

        PojoCode pc03_02_01 = new PojoCode("Cleaning lift", "m2");
        PojoCode pc03_02_02 = new PojoCode("Cleaning stairs", "m2");
        pc03_02.getSubCode().add(pc03_02_01);
        pc03_02.getSubCode().add(pc03_02_02);

        PojoCodeAlg pca01_1 = new PojoCodeAlg("CW alg 1");
        PojoCodeAlg pca01_2 = new PojoCodeAlg("CW alg 2");
        PojoCodeAlg pca02 = new PojoCodeAlg("SCW alg");

        PojoCodeAlg pca03_1 = new PojoCodeAlg("Elevator alg 1");
        PojoCodeAlg pca03_2 = new PojoCodeAlg("Elevator alg 2");
        pc03_01.getHistAlg().add(pca03_1);
        pc03_01.getHistAlg().add(pca03_2);

        PojoCodeAlgParam pcap01 = new PojoCodeAlgParam(
                "Elevator param 1 for alg 1");
        PojoCodeAlgParam pcap02 = new PojoCodeAlgParam(
                "Elevator param 2 for alg 1");
        pca03_1.getParameterList().add(pcap01);
        pca03_1.getParameterList().add(pcap02);

        PojoCodeAlgParam pcap03 = new PojoCodeAlgParam("CW alg 1 parameter");
        pca01_1.getParameterList().add(pcap03);

        pc01.getHistAlg().add(pca01_1);
        pc01.getHistAlg().add(pca01_2);
        pc02.getHistAlg().add(pca02);

        list.add(pc01);
        list.add(pc02);
        list.add(pc03);
        list.add(pc04);
        list.add(pc05);
        list.add(pc06);

        return list;
    }

    private List<PojoCode> listPojoCode = new ArrayList<PojoCode>();
    private byte[] listPojoCodeStore = null;
    private boolean listPojoCodeFilled = false;

    @SuppressWarnings({"unchecked", "unused"})
    public List<PojoCode> getCodeTreeAsList() {

        if (listPojoCodeFilled) {
            listPojoCode = restoreFromStore(listPojoCodeStore);
            return listPojoCode;
        }

        PojoCode pc01 = new PojoCode("Cold water.", "m3");
        PojoCode pc02 = new PojoCode("Sewerage of cold water.", "m3");
        PojoCode pc03 = new PojoCode("Service", "m2");
        PojoCode pc04 = new PojoCode("Hot water", "m3");
        PojoCode pc05 = new PojoCode("Sewerage of hot water", "m3");
        PojoCode pc06 = new PojoCode("Central heating", "giga-cal");

        PojoCode pc03_01 = new PojoCode("Elevator", "people");
        PojoCode pc03_02 = new PojoCode("Cleaning", "m2");
        pc03.getSubCode().add(pc03_01);
        pc03.getSubCode().add(pc03_02);

        PojoCode pc03_02_01 = new PojoCode("Cleaning lift", "m2");
        PojoCode pc03_02_02 = new PojoCode("Cleaning stairs", "m2");
        pc03_02.getSubCode().add(pc03_02_01);
        pc03_02.getSubCode().add(pc03_02_02);

        PojoCodeAlg pca01_1 = new PojoCodeAlg("CW alg 1");
        PojoCodeAlg pca01_2 = new PojoCodeAlg("CW alg 2");
        PojoCodeAlg pca02 = new PojoCodeAlg("SCW alg");

        PojoCodeAlg pca03_1 = new PojoCodeAlg("Elevator alg 1");
        PojoCodeAlg pca03_2 = new PojoCodeAlg("Elevator alg 2");
        pc03_01.getHistAlg().add(pca03_1);
        pc03_01.getHistAlg().add(pca03_2);

        PojoCodeAlgParam pcap01 = new PojoCodeAlgParam(
                "Elevator param 1 for alg 1");
        PojoCodeAlgParam pcap02 = new PojoCodeAlgParam(
                "Elevator param 2 for alg 1");
        pca03_1.getParameterList().add(pcap01);
        pca03_1.getParameterList().add(pcap02);

        PojoCodeAlgParam pcap03 = new PojoCodeAlgParam("CW alg 1 parameter");
        pca01_1.getParameterList().add(pcap03);

        pc01.getHistAlg().add(pca01_1);
        pc01.getHistAlg().add(pca01_2);
        pc02.getHistAlg().add(pca02);

        listPojoCode.add(pc01);
        listPojoCode.add(pc02);
        listPojoCode.add(pc03);

        /*
         * list.add(pc03_01); list.add(pc03_02);
         *
         * list.add(pc03_02_01); list.add(pc03_02_02);
         */
        listPojoCode.add(pc04);
        listPojoCode.add(pc05);
        listPojoCode.add(pc06);

        PojoCode pc11 = new PojoCode("ID: 11", "m3");
        PojoCode pc12 = new PojoCode("ID: 12", "m3");
        PojoCode pc13 = new PojoCode("ID: 13", "m3");
        PojoCode pc14 = new PojoCode("ID: 14", "m3");
        PojoCode pc15 = new PojoCode("ID: 15", "m3");
        PojoCode pc16 = new PojoCode("ID: 16", "m3");
        PojoCode pc17 = new PojoCode("ID: 17", "m3", Boolean.TRUE);
        PojoCode pc18 = new PojoCode("ID: 18", "m3");
        PojoCode pc19 = new PojoCode("ID: 19", "m3");
        PojoCode pc20 = new PojoCode("ID: 20", "m3");

        PojoCode pc20_01 = new PojoCode("ID: 20-1", "m2");
        PojoCode pc20_02 = new PojoCode("ID: 20-2", "m2");

        pc20.getSubCode().add(pc20_01);
        pc20.getSubCode().add(pc20_02);

        PojoCode pc21 = new PojoCode("ID: 21", "m3");

        listPojoCode.add(pc11);
        listPojoCode.add(pc12);
        listPojoCode.add(pc13);
        listPojoCode.add(pc14);
        listPojoCode.add(pc15);
        listPojoCode.add(pc16);
        listPojoCode.add(pc17);
        listPojoCode.add(pc18);
        listPojoCode.add(pc19);
        listPojoCode.add(pc20);
        listPojoCode.add(pc21);

        listPojoCodeFilled = true;

        listPojoCodeStore = saveToStore(listPojoCode);

        return listPojoCode;
    }

    @SuppressWarnings("unused")
    public void saveCodes(String idSession, Deque<IBeanWrapper> toDelete,
                          Deque<IBeanWrapper> toSave) {

        listPojoCodeStore = saveToStore(listPojoCode);
    }

    @SuppressWarnings("unused")
    public CredentialInfo getCredentialInfo() {
        return new CredentialInfo(null, null);
    }

    @SuppressWarnings("unused")
    public List<TaskDemo> getTasks() {
        List<TaskDemo> list = new ArrayList<TaskDemo>();

        list.add(new TaskDemo(
                1,
                "Hello world form",
                "ru/smartflex/djf/demo/xml/HelloWorld.frm.xml",
                new String[]{"ru/smartflex/djf/demo/xml/HelloWorld.pnl.xml"},
                null, null, null));

        list.add(new TaskDemo(
                2,
                "Simple grid form /one table/",
                "ru/smartflex/djf/demo/xml/SimpleGridAccount.frm.xml",
                new String[]{"ru/smartflex/djf/demo/xml/AccountAllFieldsGrid.pnl.xml"},
                new String[]{"ru/smartflex/djf/demo/xml/PojoAccount.bean.xml"},
                new String[]{"ru/smartflex/djf/demo/java/GridFieldsAssistant.java"},
                new String[]{"ru/smartflex/djf/demo/java/PojoAccount.java",
                        "ru/smartflex/djf/demo/java/PojoCarType.java"}));

        list.add(new TaskDemo(
                3,
                "Master-detail form with grid and text fields",
                "ru/smartflex/djf/demo/xml/GridFields.frm.xml",
                new String[]{"ru/smartflex/djf/demo/xml/GridFields.pnl.xml",
                        "ru/smartflex/djf/demo/xml/AccountGrid.pnl.xml",
                        "ru/smartflex/djf/demo/xml/AccountFields.pnl.xml",
                        "ru/smartflex/djf/demo/xml/AccEquipmentGrid.pnl.xml"},
                new String[]{"ru/smartflex/djf/demo/xml/PojoAccount.bean.xml"},
                new String[]{"ru/smartflex/djf/demo/java/GridFieldsAssistant.java"},
                new String[]{"ru/smartflex/djf/demo/java/PojoAccount.java",
                        "ru/smartflex/djf/demo/java/PojoAccPropertyType.java",
                        "ru/smartflex/djf/demo/java/FilterAccount.java",
                        "ru/smartflex/djf/demo/java/PojoCarType.java",
                        "ru/smartflex/djf/demo/java/PojoStreet.java"}));

        list.get(2)
                .getLeafTaskDemo()
                .add(new TaskDemo(4,
                        "the same form with network error emulation", list
                        .get(2)));
        list.get(2)
                .getLeafTaskDemo()
                .add(new TaskDemo(5, "the same form with fields access denied",
                        list.get(2)));

        list.add(new TaskDemo(
                6,
                "Login and then invokes master-detail form",
                "ru/smartflex/djf/demo/xml/Login.frm.xml",
                new String[]{"ru/smartflex/djf/demo/xml/Login.pnl.xml"},
                null,
                new String[]{"ru/smartflex/djf/demo/java/LoginAssistant.java"},
                new String[]{"ru/smartflex/djf/demo/java/CredentialInfo.java"}));

        list.add(new TaskDemo(
                7,
                "Master-detail form with grids, text fields and tabs",
                "ru/smartflex/djf/demo/xml/GridFieldsTab.frm.xml",
                new String[]{
                        "ru/smartflex/djf/demo/xml/GridFieldsTab.pnl.xml",
                        "ru/smartflex/djf/demo/xml/AccountGrid.pnl.xml",
                        "ru/smartflex/djf/demo/xml/AccountFields.pnl.xml",
                        "ru/smartflex/djf/demo/xml/AccEquipmentGrid.pnl.xml",
                        "ru/smartflex/djf/demo/xml/EquipWorkHist.pnl.xml",
                        "ru/smartflex/djf/demo/xml/WorkHistGrid.pnl.xml"},
                new String[]{"ru/smartflex/djf/demo/xml/PojoAccount.bean.xml"},
                new String[]{"ru/smartflex/djf/demo/java/GridFieldsTabAssistant.java"},
                new String[]{"ru/smartflex/djf/demo/java/PojoAccount.java",
                        "ru/smartflex/djf/demo/java/PojoAccPropertyType.java",
                        "ru/smartflex/djf/demo/java/PojoCarType.java",
                        "ru/smartflex/djf/demo/java/PojoStreet.java"}));

        list.get(4)
                .getLeafTaskDemo()
                .add(new TaskDemo(
                        8,
                        "the same form with tab \"Account detail\" access denied",
                        list.get(4)));

        list.get(4)
                .getLeafTaskDemo()
                .add(new TaskDemo(9,
                        "the same form with work history (3 level)", list
                        .get(4)));

        list.add(new TaskDemo(11, "Simple grid with tree & DnD",
                "ru/smartflex/djf/demo/xml/TreeCode.frm.xml",
                new String[]{"ru/smartflex/djf/demo/xml/TreeCode.pnl.xml"},
                new String[]{"ru/smartflex/djf/demo/xml/PojoCode.bean.xml"},
                null,
                new String[]{"ru/smartflex/djf/demo/java/PojoCode.java"}));

        list.add(new TaskDemo(12, "Simple form with some widgets",
                "ru/smartflex/djf/demo/xml/SFWidgets.frm.xml",
                null,
                null,
                null,
                new String[]{"ru/smartflex/djf/demo/java/StepperPercentInfo.java", "ru/smartflex/djf/demo/java/PojoStreet.java"}));

        return list;
    }

    @SuppressWarnings("unused")
    public void saveAccounts(String idSession, Deque<IBeanWrapper> toDelete,
                             Deque<IBeanWrapper> toSave, Object src) {
        saveAccounts(idSession, toDelete, toSave);
    }

    @SuppressWarnings("WeakerAccess")
    public void saveAccounts(@SuppressWarnings("unused") String idSession, Deque<IBeanWrapper> toDelete,
                             @SuppressWarnings("unused") Deque<IBeanWrapper> toSave) {

        // demo sample code for saving result of edition
        Iterator<PojoAccount> iter = listAccount.iterator();
        while (iter.hasNext()) {
            PojoAccount pa = iter.next();
            if (deleteAccountAndChildren(toDelete, pa)) {
                iter.remove();
            } else {
                @SuppressWarnings("unchecked")
                Set<PojoAccEquipment> eqp = pa.getEquipment();
                if (eqp.size() > 0) {
                    Iterator<PojoAccEquipment> iterEqp = eqp.iterator();
                    while (iterEqp.hasNext()) {
                        PojoAccEquipment accEqp = iterEqp.next();
                        if (deleteAccountAndChildren(toDelete, accEqp)) {
                            iterEqp.remove();
                        }
                    }
                }
            }
        }

        listAccountStore = saveToStore(listAccount);
    }

    private boolean deleteAccountAndChildren(Deque<IBeanWrapper> toDelete,
                                             Object obj) {
        if (toDelete != null && toDelete.size() > 0) {
            for (IBeanWrapper ibw : toDelete) {
                if (ibw.getData().equals(obj)) {
                    return true;
                }
            }
        }
        return false;
    }
}
