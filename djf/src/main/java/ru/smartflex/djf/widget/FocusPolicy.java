package ru.smartflex.djf.widget;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import ru.smartflex.djf.WidgetTypeEnum;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.exception.ObjectCreationException;
import ru.smartflex.djf.controller.helper.UIFinder;

public class FocusPolicy extends FocusTraversalPolicy {

    private List<UIWrapper> formItemList = new ArrayList<UIWrapper>();

    private Map<String, UIWrapper> mapUI = new HashMap<String, UIWrapper>();

    private List<UIWrapper> panelItemList = new ArrayList<UIWrapper>();

    public void startPanel() {
        panelItemList.clear();
    }

    public void endPanel() {
        // validation
        boolean wasOrdered = false;
        for (UIWrapper wrp : panelItemList) {
            if (wrp.getOrder() > 0) {
                wasOrdered = true;
                break;
            }
        }
        if (wasOrdered) {
            for (UIWrapper wrp : panelItemList) {
                if (wrp.getOrder() < 0) {
                    if (WidgetTypeEnum.isOrderAble(wrp.getWidgetType())) {
                        throw new ObjectCreationException(
                                "All items in group \"items\" must have order attribute (or no one)");
                    }
                }
            }
            Collections.sort(panelItemList);
        }
        formItemList.addAll(panelItemList);
    }

    public void addItem(UIWrapper wrp) {
        panelItemList.add(wrp);
        if (mapUI.get(wrp.getUiName()) != null) {
            throw new ObjectCreationException(
                    "For current form is found duplicate widget ID: \""
                            + wrp.getUiName() + "\"");
        }
        mapUI.put(wrp.getUiName(), wrp);
    }

    public void requestFocus(UIWrapper wrp) {
        if (wrp == null) {
            return;
        }
        JComponent jcomp = (JComponent) wrp.getObjectUI();
        requestFocus(jcomp);
    }

    public void moveDown(String currentUIName) {
        JComponent jcomp = getDownComponent(currentUIName);
        requestFocus(jcomp);
    }

    private void requestFocus(JComponent jcomp) {
        if (jcomp != null) {

            UIFinder.getUpAndTabSelection(jcomp, null);

            if (jcomp instanceof IRequestFocus) {
                ((IRequestFocus) jcomp).requestFocusOnNestedWidget();
            } else {
                jcomp.requestFocus();
            }
        }
    }

    private JComponent getDownComponent(String currentUIName) {
        JComponent jcomp = null;

        UIWrapper wrp = mapUI.get(currentUIName);
        int i = formItemList.indexOf(wrp);
        if (i < (formItemList.size() + 1)) {
            i++;
            for (; i < formItemList.size(); i++) {
                jcomp = findFocusable(i, wrp);
                if (jcomp != null) {
                    break;
                }
            }
        }

        if (jcomp == null) {
            // reach last component. Cycling
            jcomp = getFirstComponentInternal(wrp);
        }
        return jcomp;
    }

    public void moveUp(String currentUIName) {
        JComponent jcomp = getUpComponent(currentUIName);
        requestFocus(jcomp);
    }

    public Component getComponentBefore(Container focusCycleRoot,
                                        Component aComponent) {
        String uiName = aComponent.getName();
        return getUpComponent(uiName);
    }

    private JComponent getUpComponent(String currentUIName) {
        JComponent jcomp = null;

        UIWrapper wrp = mapUI.get(currentUIName);
        int i = formItemList.indexOf(wrp);
        if (i > 0) {
            i--;
            for (; i >= 0; i--) {
                jcomp = findFocusable(i, wrp);
                if (jcomp != null) {
                    break;
                }
            }
        }

        if (jcomp == null) {
            // reach top component. Cycling
            jcomp = getLastComponentInternal(wrp);
        }

        return jcomp;
    }

    public Component getComponentAfter(Container focusCycleRoot,
                                       Component aComponent) {
        String uiName = aComponent.getName();
        return getDownComponent(uiName);
    }

    @Override
    public Component getFirstComponent(Container aContainer) {
        return getFirstComponentInternal(null);
    }

    public Component getFirstFocusableComponent() {
        return getFirstComponentInternal(null);
    }

    @Override
    public Component getLastComponent(Container aContainer) {
        return getLastComponentInternal(null);
    }

    @Override
    public Component getDefaultComponent(Container aContainer) {
        return getFirstComponentInternal(null);
    }

    private JComponent getFirstComponentInternal(UIWrapper startUIW) {
        JComponent jcomp = null;
        for (int i = 0; i < formItemList.size(); i++) {
            jcomp = findFocusable(i, startUIW);
            if (jcomp != null) {
                break;
            }
        }
        return jcomp;
    }

    private JComponent getLastComponentInternal(UIWrapper startUIW) {
        JComponent jcomp = null;
        for (int i = formItemList.size() - 1; i >= 0; i--) {
            jcomp = findFocusable(i, startUIW);
            if (jcomp != null) {
                break;
            }
        }
        return jcomp;
    }

    private JComponent findFocusable(int i, UIWrapper startUIW) {
        JComponent jcomp = null;

        UIWrapper w = formItemList.get(i);
        if (w.getModelBase().getIdModel() != null) { // for cycle focusing only model's items
            boolean nextComparison = true;
            if (startUIW != null) {
                if (startUIW.getModelBase().getIdModel() != null) {
                    nextComparison = startUIW.getModelBase().getIdModel().equals(w.getModelBase().getIdModel());
                } else {
                    nextComparison = false;
                }
            }
            if (nextComparison) {
                if (w.isFocusable()) {
                    if (((JComponent) w.getObjectUI()).isRequestFocusEnabled()) {
                        if (((JComponent) w.getObjectUI()).isEnabled()) {
                            jcomp = (JComponent) w.getObjectUI();
                        }
                    }
                }
            }
        } else {
            // for form without model, but widgets have order
            if (w.getOrder() > 0) {
                if (((JComponent) w.getObjectUI()).isEnabled()) {
                    jcomp = (JComponent) w.getObjectUI();
                }
            }
        }

        return jcomp;
    }
}
