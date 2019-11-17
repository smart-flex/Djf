package ru.smartflex.djf.builder;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import ru.smartflex.djf.WidgetTypeEnum;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.helper.AccessibleHelper;
import ru.smartflex.djf.controller.helper.ObjectCreator;
import ru.smartflex.djf.model.gen.SeparatorType;
import ru.smartflex.djf.widget.SFPanel;

class WidgetBuilder {

    private WidgetBuilder() {
    }

    static void build(SeparatorType separator, SFPanel sfPanel, WidgetManager wm) {

        if (AccessibleHelper.isAccessible(separator, wm)) {
            Object ui = ObjectCreator.createSwing(WidgetTypeEnum.SEPARATOR);

            if (separator.isVertical() != null && separator.isVertical()) {
                JSeparator s = (JSeparator) ui;
                s.setOrientation(SwingConstants.VERTICAL);
            }
            java.awt.Container uiPanel = (Container) sfPanel.getPanel();

            uiPanel.add((Component) ui, separator.getConstraint());
        }
    }

}
