package ru.smartflex.djf.widget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.SmartFlexException;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.bean.tree.IBeanWrapper;
import ru.smartflex.djf.controller.bean.tree.TreeListUtils;
import ru.smartflex.djf.widget.grid.SFTableWrapper;

public class SFComboBoxModel extends DefaultComboBoxModel {

    private static final long serialVersionUID = -7883846647062340750L;

    private WidgetManager wm = null;
    private UIWrapper uiw = null;

    private List<Object> objMirroList = new ArrayList<Object>();

    private SFTableWrapper table = null;

    void reloadLinkedModel(Object rowData) {
        if (uiw.getModelFill().getLevelUI() > 0) {
            reloadModel(rowData);
        }
    }

    @SuppressWarnings("unchecked")
    void reloadModel(Object rowData) {
        super.removeAllElements();

        objMirroList.clear();

        if (uiw.getModelFill().getLevelUI() > 0) {

            int index = 0;
            // for table
            if (table != null) {
                index = table.getCurrentPointMouseRow();
            }
            if (index == -1) {
                // not completely initialized
                return;
            }

            int rowCount = wm.getAmountRow(uiw.getModelBase());
            if (index >= rowCount) {
                // case of deletion: new GRID model contains less rows than previous
                return;
            }

            Object data;

            if (table != null) {
                IBeanWrapper bw = wm.getBeanWrapper(uiw.getModelBase(), index);
                if (bw == null) {
                    return;
                } else {
                    data = bw.getData();
                }
            } else {
                data = rowData;
            }

            addElement(SFConstants.COMBOBO_FIRST_ITEM);

            if (uiw.getParentPropertyName() == null) {
                SFLogger.warn(SFComboBoxModel.class,
                        "For combobox: " + uiw.getBind(),
                        " parent property is missed");
                return;
            }

            if (data == null) {
                SFLogger.warn(SFComboBoxModel.class,
                        "For combobox: " + uiw.getBind(),
                        " data is empty");
                return;
            }

            Object obj = TreeListUtils.getPropertyValue(data,
                    uiw.getParentPropertyName());

            if (obj != null) {
                String propColl = uiw.getModelFill().getPropParentCollection();
                @SuppressWarnings("rawtypes")
                Collection col = (Collection) TreeListUtils.getPropertyValue(
                        obj, propColl);
                if (col != null) {
                    for (Object cObj : col) {
                        Object objDisplay = TreeListUtils.getPropertyValue(
                                cObj, uiw.getModelFill().getProperty());
                        objMirroList.add(cObj);
                        addElement(objDisplay);
                    }
                }
            }

        } else {

            int rowCount = wm.getAmountRow(uiw.getModelFill());

            if (rowCount == 0)
                throw new SmartFlexException(
                        "There must be at least one row for combobox: " + uiw
                                + "\n  base=" + uiw.getModelBase()
                                + "\n  fill=" + uiw.getModelFill());

            addElement(SFConstants.COMBOBO_FIRST_ITEM);

            List<IBeanWrapper> list = wm.getSelectedBeanWrapperList(uiw
                    .getModelFill());
            for (IBeanWrapper bw : list) {
                Object obj = wm.getValueFromBeanWrapper(uiw.getModelFill(), bw,
                        true);
                addElement(obj);
                objMirroList.add(bw.getData());
            }

        }

    }

    Object getSelectedMirrorObject() {
        Object obj = null;
        JComboBox box = (JComboBox) uiw.getObjectUI();
        int index = box.getSelectedIndex();

        if (index > 0 && index < getSize()) {
            obj = objMirroList.get(index - 1);
        }

        return obj;
    }

    void setWidgetManager(WidgetManager widgetManager) {
        this.wm = widgetManager;
    }

    protected void setUIWrapper(UIWrapper uIWrapper) {
        this.uiw = uIWrapper;
    }

    protected void setTable(SFTableWrapper table) {
        this.table = table;
    }

    Object getObject(String propertySet, Object valueOfSet) {
        Object obj = null;

        for (Object o : objMirroList) {
            Object oCurr = TreeListUtils.getPropertyValue(o, propertySet);
            if (valueOfSet.equals(oCurr)) {
                obj = o;
                break;
            }

        }

        return obj;
    }

}
