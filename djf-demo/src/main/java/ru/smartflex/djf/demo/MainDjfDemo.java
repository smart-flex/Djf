package ru.smartflex.djf.demo;

import java.util.Properties;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.SimpleAccessible;
import ru.smartflex.djf.SizeFrameEnum;
import ru.smartflex.djf.demo.java.AddressFactory;
import ru.smartflex.djf.demo.java.AppEmulator;
import ru.smartflex.djf.demo.java.CarFactory;

public class MainDjfDemo {

    public static void run(String frm) {
        Properties props = new Properties();
        props.put(SFConstants.PROPERTY_FORM_CLASS, "ru.smartflex.djf.widget.template.FormUI");
        props.put(SFConstants.PROPERTY_FRAME_CLASS, "ru.smartflex.djf.widget.template.FrameUI");
        props.put(SFConstants.PROPERTY_LABEL_RB_PATH, "ru/smartflex/djf/demo");

        props.put(SFConstants.PROPERTY_MASK_DATE, "dd-MM-yyyy");

        props.put(SFConstants.PROPERTY_FONT_SIZE, "14"); //12
        props.put(SFConstants.PROPERTY_FONT_TEXT_INPUT_RATE_INCREASING, "0"); // 1.5

        // Setup for application type

        // Before start you can to define restrict labels
        // Moreover: if "sfSpecialService" will be removed then on the form these all related items will be lost
        SimpleAccessible saMarker = new SimpleAccessible("sfAll", "sfSpecialService", "sfBank");
        Djf.getConfigurator().setAccessibleHandler(saMarker);

        Djf.getConfigurator().configure(props);

        AddressFactory address = new AddressFactory();
        CarFactory car = new CarFactory();

        Djf.getSession().addBean("app", new AppEmulator(address, car));
        Djf.getSession().addBean("adr", address);
        Djf.getSession().addBean("car", car);
        Djf.getSession().addBean("sessId", "testSessionId");

        Djf.runForm(frm, SizeFrameEnum.HALF);
    }

    public static void main(String[] args) {
        MainDjfDemo.run("ru/smartflex/djf/demo/xml/Tasks.frm.xml");
    }

}
