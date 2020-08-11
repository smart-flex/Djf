package ru.smartflex.djf.controller;

import java.awt.Component;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.WidgetTypeEnum;
import ru.smartflex.djf.controller.bean.GridColumnInfo;
import ru.smartflex.djf.controller.bean.ModelLoadResult;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.bean.UIWrapperModel;
import ru.smartflex.djf.controller.bean.tree.BeanStatusEnum;
import ru.smartflex.djf.controller.bean.tree.IBeanWrapper;
import ru.smartflex.djf.controller.bean.tree.TreeList;
import ru.smartflex.djf.controller.bean.tree.TreeListUtils;
import ru.smartflex.djf.controller.bean.tree.WidgetTreeNode;
import ru.smartflex.djf.controller.exception.MissingException;
import ru.smartflex.djf.model.gen.ModelType;
import ru.smartflex.djf.widget.FocusPolicy;
import ru.smartflex.djf.widget.SFComboBox;
import ru.smartflex.djf.widget.ITextArea;
import ru.smartflex.djf.widget.grid.SFGrid;
import ru.smartflex.djf.widget.grid.SFGridMouseMotionFlag;
import ru.smartflex.djf.widget.grid.TitleRenderer;
import ru.smartflex.djf.widget.tgrid.SFTGrid;

public class WidgetManagerHelper {

    private WidgetManagerHelper() {
        super();
    }

    /**
     * Validates model's properties
     */
    static void checksGetters(FormManager fm, ModelLoadResult data) {
        if (data.isWasLoadError()) {
            return;
        }

        fm.getWidgetManager().setModelLoadResult(data);

        List<ModelType> modelList = fm.getForm().getModels().getModel();
        for (ModelType mt : modelList) {
            String idModel = mt.getId();

            WidgetManager wm = fm.getWidgetManager();
            WidgetTreeNode<UIWrapper> wtn = wm.getWidgetTreeNode(idModel);
            List<WidgetTreeNode<UIWrapper>> orderedList = wtn
                    .getOrderedTreeNode();
            for (WidgetTreeNode<UIWrapper> wtno : orderedList) {

                if (wtno.getWidget().getBelongToModel() != null) {
                    continue;
                }
                if (wtno.isScrollWidget()) {
                    Object uiObject = wtno.getWidget().getObjectUI();
                    if (uiObject instanceof SFGrid) {
                        if (uiObject instanceof SFTGrid) {
                            // check for existing self properties
                            TreeList tree = data.getTreeList(idModel);
                            if (tree.getPropertyNameSelf() == null) {
                                throw new MissingException("If you have in grid tree column then in bean definition you must have filled \"selfSet\" attribute");
                            }
                        }
                        List<GridColumnInfo> titleList = ((SFGrid) uiObject)
                                .getListColumnInfo();
                        for (int i = 0; i < titleList.size(); i++) {
                            if (i == 0) {
                                if (wtno.getWidget().isGridInfoColumnAllowed()) {
                                    continue;
                                }
                            }
                            String property = titleList.get(i).getModelBase()
                                    .getProperty();
                            wm.getValueScrollWidget(wtno.getWidget(), 0,
                                    property, false);
                        }
                    }
                } else {
                    UIWrapperModel model = wtno.getWidget().getWrapperModel(
                            idModel);
                    IBeanWrapper bw = wm.getBeanWrapper(model, 0);
                    wm.getValueFromBeanWrapper(model, bw, false);
                }
            }
        }
    }

    static void restoreSelectionOnScrollableWidgets(FormManager fm, ModelLoadResult data, boolean firstTime) {
        if (data.isWasLoadError()) {
            return;
        }

        // in other case, after form was initialized we can not add record on grid because Djf does not know
        // which grid has requested focus
        SFGridMouseMotionFlag.setMotionFlag(false);

        List<ModelType> modelList = fm.getForm().getModels().getModel();
        for (ModelType mt : modelList) {
            String idModel = mt.getId();

            WidgetManager wm = fm.getWidgetManager();
            WidgetTreeNode<UIWrapper> wtn = wm.getWidgetTreeNode(idModel);
            List<WidgetTreeNode<UIWrapper>> orderedList = wtn
                    .getOrderedTreeNode();
            for (WidgetTreeNode<UIWrapper> wtno : orderedList) {
                if (wtno.isScrollWidget() /* && wtno.getDepthNode() == 1 */) {
                    Object uiObject = wtno.getWidget().getObjectUI();
                    if (wtno.getWidget().getWidgetType() == WidgetTypeEnum.GRID || wtno.getWidget().getWidgetType() == WidgetTypeEnum.TGRID) {
                        // scan grid columns
                        ((SFGrid) uiObject).restoreCurrentSelection(firstTime);
                        ((SFGrid) uiObject).repaint();
                    }
                }
            }
        }

        requestFocustOnFirstComponent(fm);

    }

    static void requestFocustOnFirstComponent(FormManager fm) {
        FocusPolicy focus = (FocusPolicy) fm.getWidgetManager()
                .getFocusPolicy();
        Component comp = focus.getFirstFocusableComponent();
        if (comp != null) {
            if (comp instanceof SFGrid) {
                ((SFGrid) comp).requestGridFocus();
            } else {
                comp.requestFocus();
            }
        }
    }

    static void initialFilling(FormManager fm, ModelLoadResult data) {

        if (data.isWasLoadError()) {
            return;
        }

        List<ModelType> modelList = fm.getForm().getModels().getModel();
        for (ModelType mt : modelList) {
            String idModel = mt.getId();

            WidgetManager wm = fm.getWidgetManager();
            WidgetTreeNode<UIWrapper> wtn = wm.getWidgetTreeNode(idModel);
            List<WidgetTreeNode<UIWrapper>> orderedList = wtn
                    .getOrderedTreeNode();
            for (WidgetTreeNode<UIWrapper> wtno : orderedList) {

                if (wtno.isScrollWidget() /* && wtno.getDepthNode() == 1 */) {
                    Object uiObject = wtno.getWidget().getObjectUI();
                    if (wtno.getWidget().getWidgetType() == WidgetTypeEnum.GRID || wtno.getWidget().getWidgetType() == WidgetTypeEnum.TGRID) {

                        // scan grid columns
                        SFGrid grid = (SFGrid) uiObject;
                        List<GridColumnInfo> listCI = grid.getListColumnInfo();
                        for (GridColumnInfo gci : listCI) {

                            if (gci.getColumnDefinition() != null) {
                                if (gci.getWidgetType() == WidgetTypeEnum.COMBOBOX) {
                                    reloadComboBoxModel(
                                            (SFComboBox) gci.getObjectUI(), wm,
                                            gci);
                                }
                            }
                        }

                        // reload grid model
                        ((SFGrid) uiObject).setWidgetManager(fm
                                .getWidgetManager());
                        ((SFGrid) uiObject).setUIWrapper(wtno.getWidget());
                        ((SFGrid) uiObject).reloadModel();

                    }
                    if (wtno.getWidget().getWidgetType() == WidgetTypeEnum.TGRID) {
                        // there is not refreshed in opposite
                        ((SFTGrid) uiObject).reloadTreeModel();
                    }
                } else {
                    if (wtno.getWidget().getWidgetType() == WidgetTypeEnum.COMBOBOX) {
                        reloadComboBoxModel((SFComboBox) wtno.getWidget()
                                .getObjectUI(), wm, wtno.getWidget());
                    }
                    fillWidget(fm.getWidgetManager(), wtno.getWidget(), 0);
                }
            }
        }

    }

    private static void reloadComboBoxModel(SFComboBox ui, WidgetManager wm,
                                            UIWrapper uiw) {
        ui.setWidgetManager(wm);
        ui.setUIWrapper(uiw);
        ui.reloadModel();
    }

    @SuppressWarnings("RedundantIfStatement")
    static boolean isWidgetInModel(UIWrapperModel uiwMod) {

        if (uiwMod.getProperty() == null && uiwMod.isBelongToModel()) {
            return false; // null, in case if checkbox and others only belong to model
        }

        return true;
    }

    @SuppressWarnings("DuplicateBranchesInSwitch")
    static void fillWidget(WidgetManager wm, UIWrapper uiw, int index) {
        if (uiw.isWrapperInGrid()) {
            // exclude grid column
            return;
        }
        if (uiw.getWidgetType() == WidgetTypeEnum.BUTTON) {

            IBeanWrapper bw = wm.getBeanWrapper(uiw.getModelBase(), index);
            BeanStatusEnum bs = isBeanWrapperLocked(wm, uiw, bw, index);
            if (bs != null) {
                uiw.setItemEnabled(false);
            } else {
                uiw.setItemEnabled(true);
            }

        } else if (uiw.getWidgetType() == WidgetTypeEnum.CHECKBOX) {
            IBeanWrapper bw = wm.getBeanWrapper(uiw.getModelBase(), index);
            if (isWidgetInModel(uiw.getModelBase())) {
                Object obj = wm.getValueFromBeanWrapper(uiw.getModelBase(), bw,
                        true);
                boolean selected = false;
                if (obj != null) {
                    selected = (Boolean) obj;
                }
                ((JCheckBox) uiw.getObjectUI()).setSelected(selected);
            }
            BeanStatusEnum bs = isBeanWrapperLocked(wm, uiw, bw, index);
            if (bs == null) {
                uiw.setItemEnabled(true);
            } else {
                uiw.setItemEnabled(false);
            }
        } else if (uiw.getWidgetType() == WidgetTypeEnum.COMBOBOX) {
            IBeanWrapper bw = wm.getBeanWrapper(uiw.getModelBase(), index);

            if (uiw.getBind() != null) {
                if (isWidgetInModel(uiw.getModelBase())) {
                    Object obj = wm.getValueFromBeanWrapper(uiw.getModelBase(), bw,
                            true);

                    ((SFComboBox) uiw.getObjectUI()).reloadLinkedModel(bw.getData());

                    if (obj == null) {
                        ((JComboBox) uiw.getObjectUI())
                                .setSelectedItem(SFConstants.COMBOBO_FIRST_ITEM);
                    } else {
                        if (uiw.getPropertySet() != null) {
                            obj = ((SFComboBox) uiw.getObjectUI()).getObject(uiw.getPropertySet(), obj);
                        }
                        Object objItem = TreeListUtils.getPropertyValue(obj, uiw
                                .getModelFill().getProperty());
                        ((JComboBox) uiw.getObjectUI()).setSelectedItem(objItem);
                    }
                }
            }

            // locking
            BeanStatusEnum bs = isBeanWrapperLocked(wm, uiw, bw, index);
            if (bs == null) {
                uiw.setItemEnabled(true);
            } else {
                uiw.setItemEnabled(false);
            }
        } else {

            IBeanWrapper bw = wm.getBeanWrapper(uiw.getModelBase(), index);

            if (isWidgetInModel(uiw.getModelBase())) {
                Object obj = wm.getValueFromBeanWrapper(uiw.getModelBase(), bw,
                        true);

                switch (uiw.getWidgetType()) {
                    case PASSWORD:
                    case PHONE:
                    case TEXT:
                        ((JTextField) uiw.getObjectUI()).setText((String) obj);
                        break;
                    case DATE:
                        ((JTextField) uiw.getObjectUI()).setText(uiw
                                .getFormattedData(obj));
                        break;
                    case PERIOD:
                        ((JTextField) uiw.getObjectUI()).setText(uiw
                                .getFormattedData(obj));
                        break;
                    case BYTE:
                    case SHORT:
                    case INT:
                    case LONG:
                        ((JTextField) uiw.getObjectUI()).setText(uiw
                                .getFormattedData(obj));
                        break;
                    case NUMERIC:
                        ((JTextField) uiw.getObjectUI()).setText(uiw
                                .getFormattedData(obj));
                        break;
                    case TEXTAREA:
                        ((ITextArea) uiw.getObjectUI()).setText(uiw
                                .getFormattedData(obj));
                        break;
                }
            }

            BeanStatusEnum bs = isBeanWrapperLocked(wm, uiw, bw, index);
            if (bs == null) {
                uiw.setItemEnabled(true);
            } else {
                uiw.setItemEnabled(false);
            }

        }
    }

    public static BeanStatusEnum isBeanWrapperLocked(WidgetManager wm,
                                                     UIWrapper uiw, IBeanWrapper bw, int index) {
        BeanStatusEnum bs;

        boolean fok = wm.getFormBag().isModelCanBeChanged(
                uiw.getModelBase().getIdModel());
        if (!fok) {
            bs = BeanStatusEnum.LOCKED;
        } else {
            // next check
            if (bw.isBeanWrapperLocked() || bw.isBeanWrapperDeleted()
                    || bw.isBeanWrapperSelected()) {
                bs = bw.getObtainedStatus();
            } else {
                bs = wm.getLockedStatus(uiw, index);
            }
        }
        return bs;
    }
}
