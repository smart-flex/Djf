package ru.smartflex.djf.demo;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.SizeFrameEnum;

public class HelloWorldSimple {
    public static void main(String[] args) {
        Djf.getConfigurator().configure(null);
        Djf.runForm("ru/smartflex/djf/demo/xml/HelloWorldSimple.frm.xml", SizeFrameEnum.HALF);
    }
}
