package ru.smartflex.djf.widget;

import javax.swing.JComboBox;

import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.widget.grid.SFTableWrapper;

public class SFComboBox extends JComboBox {

    private static final long serialVersionUID = -6425376748123446316L;

    private SFComboBoxModel model = new SFComboBoxModel();
    private UIWrapper uiw = null;
    private boolean stopAction = false;

    public SFComboBox(boolean tipsItem) {
        super();
        init(tipsItem);
    }

    @SuppressWarnings("unchecked")
    private void init(boolean tipsItem) {
        setEditable(false);
        super.setModel(model);
        if (tipsItem) {
            setRenderer(new SFComboBoxRenderer());
        }
    }

    public void setTable(SFTableWrapper table) {
        model.setTable(table);
    }

    public void reloadLinkedModel(Object rowData) {
        stopAction = true; // workarround. Because we have action event in combobox action listener
        model.reloadLinkedModel(rowData);
        stopAction = false;
    }

    public void reloadModel() {
        model.reloadModel(null);
    }

    public void setWidgetManager(WidgetManager widgetManager) {
        model.setWidgetManager(widgetManager);
    }

    public void setUIWrapper(UIWrapper uIWrapper) {
        model.setUIWrapper(uIWrapper);
        this.uiw = uIWrapper;
    }

    public Object getSelectedMirrorObject() {
        return model.getSelectedMirrorObject();
    }

    public String getChildPropertyName() {
        return uiw.getChildPropertyName();
    }

    boolean isStopAction() {
        return stopAction;
    }

    public Object getObject(String propertySet, Object valueOfSet) {
        return model.getObject(propertySet, valueOfSet);
    }
}
