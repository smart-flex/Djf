package ru.smartflex.djf.controller.bean;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.controller.helper.UIFinder;

@SuppressWarnings("unused")
public class FrameUIItemCache {

    private static Map<String, JComponent> map = new HashMap<String, JComponent>(
            10);

    private FrameUIItemCache() {
    }

    public static JComponent getFrameItem(String name) {
        JComponent comp = map.get(name);
        if (comp == null) {

            comp = UIFinder.getControlByName(Djf.getConfigurator().getFrame(),
                    name);
            if (comp != null) {
                map.put(name, comp);
            }
        }

        return comp;
    }
}
