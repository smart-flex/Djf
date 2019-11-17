package ru.smartflex.djf.widget;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import ru.smartflex.djf.controller.bean.LabelBundle;

public class SFPanel {

    private Component panel;
    private Component basePanel;
    private LabelBundle bundle = null;

    public SFPanel(boolean scrollable, Border border) {
        if (!scrollable) {
            panel = new JPanel();
            ((JPanel) panel).setBorder(border);
            basePanel = panel;
        } else {
            panel = new JPanel();
            JScrollPane base = new JScrollPane();
            base.getViewport().add(panel, null);
            basePanel = base;
        }
    }

    public Component getPanel() {
        return panel;
    }

    public Component getBasePanel() {
        return basePanel;
    }

    public LabelBundle getBundle() {
        return bundle;
    }

    public void setBundle(LabelBundle bundle) {
        this.bundle = bundle;
    }

}
