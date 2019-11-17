package ru.smartflex.djf.controller;

import java.awt.FocusTraversalPolicy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.time.DateUtils;

import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.SmartFlexException;
import ru.smartflex.djf.WidgetTypeEnum;
import ru.smartflex.djf.controller.bean.BeanFormDef;
import ru.smartflex.djf.controller.bean.BeanFormDefProperty;
import ru.smartflex.djf.controller.bean.FormBag;
import ru.smartflex.djf.controller.bean.GridColumnInfo;
import ru.smartflex.djf.controller.bean.ModelLoadResult;
import ru.smartflex.djf.controller.bean.SFPair;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.bean.UIWrapperModel;
import ru.smartflex.djf.controller.bean.tree.BeanStatusEnum;
import ru.smartflex.djf.controller.bean.tree.IBeanWrapper;
import ru.smartflex.djf.controller.bean.tree.TreeList;
import ru.smartflex.djf.controller.bean.tree.TreeListNode;
import ru.smartflex.djf.controller.bean.tree.TreeListUtils;
import ru.smartflex.djf.controller.bean.tree.WidgetTreeNode;
import ru.smartflex.djf.controller.exception.MissingException;
import ru.smartflex.djf.controller.helper.PrefixUtil;
import ru.smartflex.djf.tool.FormUtil;
import ru.smartflex.djf.widget.FocusPolicy;
import ru.smartflex.djf.widget.SFComboBox;
import ru.smartflex.djf.widget.grid.SFGrid;
import ru.smartflex.djf.widget.tgrid.SFTGrid;

public class WidgetManager {
    private List<UIWrapper> listUI = new ArrayList<UIWrapper>();
    private Map<String, WidgetTreeNode<UIWrapper>> mapUITreeNode = new HashMap<String, WidgetTreeNode<UIWrapper>>();
    private ModelLoadResult modelLoadResult = null;

    private Lock lockTree = new ReentrantLock(false);

    private FocusPolicy focus = new FocusPolicy();

    private FormBag formBag = null;

    private UIWrapper lastSelectedWrapper = null;
    private Lock lockSelected = new ReentrantLock(false);
    private UIWrapper lastSelectedScrollWrapper = null;

    // For only accessible goals during for creation
    private Map<String, Object> mapParameters;

    private boolean allowRegisterFocusLastComponent = true;

    private Map<String, UIWrapper> mapWrapper = new HashMap<String, UIWrapper>();

    private AtomicBoolean allowFocusMovement = new AtomicBoolean(false);

    public WidgetManager(Map<String, Object> mapParameters) {
        super();
        this.mapParameters = mapParameters;
    }

    void requestFocusOnLastSelectedFieldWrapper() {
        if (lastSelectedWrapper != null && !lastSelectedWrapper.isScrollWidget()) {
            focus.requestFocus(lastSelectedWrapper);
        }
    }

    void allowRegisterFocusLastComponent() {
        allowRegisterFocusLastComponent = true;
    }

    void disallowRegisterFocusLastComponent() {
        allowRegisterFocusLastComponent = false;
    }

    public UIWrapper getLastSelectedWrapper() {
        return lastSelectedWrapper;
    }

    public List<UIWrapper> getListUI() {
        return listUI;
    }

    public void registerItemUIWrapper(UIWrapper wrp) {
        listUI.add(wrp);
        focus.addItem(wrp);
        mapWrapper.put(wrp.getUiName(), wrp);
    }

    public void startPanel() {
        focus.startPanel();
    }

    public void endPanel() {
        focus.endPanel();
    }

    public FocusTraversalPolicy getFocusPolicy() {
        return focus;
    }

    void makeHierarchyUIItems(String idModel) {

        FormManagerHelper.makeHierarchyUIItems(idModel, listUI, mapUITreeNode);

    }

    public WidgetTreeNode<UIWrapper> getBeanWrapper(String treePath) {
        WidgetTreeNode<UIWrapper> ret = null;
        // root node of model
        String[] parts = TreeListUtils.getPartProperties(treePath);
        WidgetTreeNode<UIWrapper> root = mapUITreeNode.get(parts[0]);
        // scans
        List<WidgetTreeNode<UIWrapper>> orderedTreeNode = root
                .getOrderedTreeNode();
        for (WidgetTreeNode<UIWrapper> wtn : orderedTreeNode) {
            if (wtn.getWidget() instanceof GridColumnInfo) {
                // this is grid column
                continue;
            }
            if (wtn.getTreePath().equals(treePath)) {
                ret = wtn;
                break;
            }
        }
        return ret;
    }

    void checkForModel() {
        for (UIWrapper uiw : listUI) {
            if (!uiw.isScrollWidget()) {
                if (uiw.getBind() != null
                        && uiw.getModelBase().getIdModel() == null) {
                    throw new MissingException(
                            "There is unbinding widget for bind: "
                                    + uiw.getBind());
                }
            }
        }
    }

    void disableItems() {
        for (UIWrapper uiw : listUI) {
            uiw.setItemEnabled(false);
        }
    }

    WidgetTreeNode<UIWrapper> getWidgetTreeNode(String idModel) {
        return mapUITreeNode.get(idModel);
    }

    void setModelLoadResult(ModelLoadResult modelLoadResult) {
        this.modelLoadResult = modelLoadResult;
    }

    ModelLoadResult getModelLoadResult() {
        return modelLoadResult;
    }

    public int getAmountRow(UIWrapperModel uiwMod) {
        int amount = 0;

        String idModel = uiwMod.getIdModel();

        if (idModel != null) {
            TreeList treeList = modelLoadResult.getTreeList(idModel);

            lockTree.lock();
            try {
                amount = treeList.getAmountSetSize(uiwMod.getTreeNode());
            } finally {
                lockTree.unlock();
            }
        }

        return amount;
    }

    public Object getValueScrollWidget(UIWrapper uiw, int index,
                                       String property, boolean suppressExcp) {
        Object obj = null;
        String idModel = uiw.getModelBase().getIdModel();

        if (idModel != null) {
            TreeList treeList = modelLoadResult.getTreeList(idModel);

            lockTree.lock();
            IBeanWrapper bw = null;
            try {
                bw = treeList.getBeanWrapper(index, uiw.getModelBase()
                        .getTreeNode());
                obj = TreeListUtils.getPropertyValue(bw.getData(), property);
            } catch (Exception e) {
                SFLogger.error("Error by data reading: " + uiw + " "
                        + uiw.getModelBase().getTreeNode() + " " + bw, e);
                if (!suppressExcp) {
                    throw new SmartFlexException("Forward", e);
                }

            } finally {
                lockTree.unlock();
            }
        }

        return obj;
    }

    public boolean moveNode(UIWrapper uiw, int indFromTGrid, int indToTGrid) {
        boolean fok = false;

        String idModel = uiw.getModelBase().getIdModel();

        if (idModel != null) {
            TreeList treeList = modelLoadResult.getTreeList(idModel);

            lockTree.lock();
            IBeanWrapper bw = null;
            try {
                bw = treeList.getCurrentBeanWrapper(uiw
                        .getModelBase().getTreeNode());
                fok = treeList.moveNode(indFromTGrid, indToTGrid);
                if (fok) {
                    formWasChanged(uiw, bw, treeList);
                }
            } catch (Exception e) {
                SFLogger.error("Error by node moving: " + uiw + " "
                        + uiw.getModelBase().getTreeNode() + " " + bw, e);
                throw new SmartFlexException("Error by node moving", e);
            } finally {
                lockTree.unlock();
            }
        }
        return fok;
    }

    public Deque<IBeanWrapper> getObjectsToDelete(String idModel) {
        Deque<IBeanWrapper> stack = null;

        TreeList treeList = modelLoadResult.getTreeList(idModel);

        lockTree.lock();
        try {
            stack = treeList.getObjectsToDelete();
        } catch (Exception e) {
            SFLogger.error("Error by data reading", e);
        } finally {
            lockTree.unlock();
        }

        return stack;
    }

    public Object getObjectSource(String idModel) {
        TreeList treeList = modelLoadResult.getTreeList(idModel);
        return treeList.getObjectSource();
    }

    public Deque<IBeanWrapper> getObjectsToSave(String idModel) {
        Deque<IBeanWrapper> stack;

        TreeList treeList = modelLoadResult.getTreeList(idModel);
        lockTree.lock();
        try {
            stack = treeList.getObjectsToSave();
        } finally {
            lockTree.unlock();
        }

        return stack;
    }

    @SuppressWarnings("unused")
    public int getNodeAmountForFirstLevel_Debug(String idModel) {
        int amount = 0;

        TreeList treeList = modelLoadResult.getTreeList(idModel);
        lockTree.lock();
        try {
            amount = treeList.getNodeAmountForFirstLevel();
        } catch (Exception e) {
            SFLogger.error("Error by data reading", e);
        } finally {
            lockTree.unlock();
        }

        return amount;
    }

    void createNewObject(UIWrapper uiw) {
        String idModel = uiw.getModelBase().getIdModel();

        if (idModel != null) {
            TreeList treeList = modelLoadResult.getTreeList(idModel);

            lockTree.lock();
            try {
                int index = treeList.createNewObject(uiw.getModelBase().getTreeNode());

                moveToRowRepaint(uiw.getModelBase().getTreeNode(), treeList/* amount - 1
                 * ,
                 * true
                 */);

                // the code is redundant but necessary
                Object ui = uiw.getObjectUI();
                if (uiw.getWidgetType() == WidgetTypeEnum.TGRID) {
                    SFTGrid tgrid = (SFTGrid) uiw.getObjectUI();
                    tgrid.reloadTreeModel();
                }
                if (uiw.isGrid() || uiw.isTGrid()) {
                    ((SFGrid) ui).reloadModel();
                    ((SFGrid) ui).getTable().repaint();
                    ((SFGrid) ui).selectRow(index /*amount - 1*/);
                }

            } catch (Exception e) {
                SFLogger.error("Error by data reading", e);
            } finally {
                lockTree.unlock();
            }
        }

    }

    public void setValueScrollWidget(UIWrapper uiw, String property, Object obj) {
        String idModel = uiw.getModelBase().getIdModel();

        if (idModel != null) {
            TreeList treeList = modelLoadResult.getTreeList(idModel);

            lockTree.lock();
            try {
                IBeanWrapper bw = treeList.getCurrentBeanWrapper(uiw
                        .getModelBase().getTreeNode());

                Object oldValue = TreeListUtils.getPropertyValue(bw.getData(),
                        property);

                if (isDataWasChanged(oldValue, obj)) {
                    TreeListUtils.setPropertyValue(bw.getData(), property, obj);
                    formWasChanged(uiw, bw, treeList);
                }

            } catch (Exception e) {
                SFLogger.error("Error by data reading", e);
            } finally {
                lockTree.unlock();
            }
        }

    }

    private void fillProperties(TreeList treeList, IBeanWrapper bw) {
        String clazzName = bw.getClazzName();
        BeanFormDef beanDef = treeList.getBeanFormDef(clazzName);
        if (beanDef == null) {
            throw new MissingException("For this class: \"" + clazzName + "\" we have missed bean definition. Please, check bean description");
        }
        for (BeanFormDefProperty bf : beanDef.getProperties()) {
            if (bf.getFillExpression() != null) {
                String propName = bf.getName();
                Object obj = PrefixUtil.getParameterValue(bf.getFillExpression());
                TreeListUtils.setPropertyValue(bw.getData(), propName, obj);
            }
        }
    }

    private void formWasChanged(UIWrapper uiwrp, IBeanWrapper bw, TreeList treeList) {

        String idModel = uiwrp.getModelBase().getIdModel();

        if (formBag.isModelCanBeChanged(idModel)) {

            bw.setupBeanChanged();

            if (formBag.getAssistant() != null) {
                formBag.getAssistant().beanWasChanged(bw);
            }

            fillProperties(treeList, bw);

            for (UIWrapper uiw : listUI) {
                if (uiw.getModelBase().getIdModel() != null
                        && uiw.getModelBase().getIdModel().equals(idModel)) {
                    if (uiw.isGrid() || uiw.isTGrid()) {
                        Object ui = uiw.getObjectUI();
                        ((SFGrid) ui).getTable().repaint();
                    }
                }
            }
            if (!formBag.isModelCanNotBeSaved(idModel)) {
                formBag.formWasChanged();
            }
        }
    }

    public IBeanWrapper getBeanWrapper(UIWrapperModel uiwMod, int index) {
        IBeanWrapper bw = null;

        String idModel = uiwMod.getIdModel();

        if (idModel != null) {
            TreeList treeList = modelLoadResult.getTreeList(idModel);

            lockTree.lock();
            try {

                bw = treeList.getBeanWrapper(index, uiwMod.getTreeNode());
            } catch (Exception e) {
                SFLogger.error("Error by data reading", e);
            } finally {
                lockTree.unlock();
            }
            if (bw == null) {
                SFLogger.warn(WidgetManager.class, "BeanWarpper is null. index= ", index, " ", uiwMod.getTreeNode());
            }
        }
        return bw;
    }

    public Object getValueFromBeanWrapper(UIWrapperModel uiwMod,
                                          IBeanWrapper bw, boolean suppressExcp) {

        Object obj = null;
        String property = uiwMod.getProperty();
        try {

            if (WidgetManagerHelper.isWidgetInModel(uiwMod)) {
                obj = TreeListUtils.getPropertyValue(bw.getData(), property);
            } // else return null, in case if checkbox (or other widget) only belong to model

        } catch (Exception e) {
            SFLogger.error("Error by data reading: " + uiwMod + " bw=" + bw, e);
            if (!suppressExcp) {
                throw new SmartFlexException("Forward", e);
            }
        }
        return obj;
    }

    @SuppressWarnings("unused")
    public Object getValueFromObject(UIWrapperModel uiwMod, Object objSrc,
                                     boolean suppressExcp) {

        Object obj = null;
        String property = uiwMod.getProperty();
        try {

            obj = TreeListUtils.getPropertyValue(objSrc, property);
        } catch (Exception e) {
            SFLogger.error("Error by data reading: " + uiwMod + " objSrc="
                    + objSrc, e);
            if (!suppressExcp) {
                throw new SmartFlexException("Forward", e);
            }
        }
        return obj;
    }

    private boolean isDataWasChanged(Object oldValue, Object newValue) {
        boolean fok = false;

        if (oldValue == null && newValue != null) {
            fok = true;
        } else if (oldValue != null && newValue == null) {
            fok = true;
        } else if (oldValue != null) {
            if (oldValue instanceof Date) {
                if (!oldValue.equals(newValue)) {
                    // cuts ms
                    Date cutDate = DateUtils.truncate(oldValue, Calendar.DATE);
                    fok = !cutDate.equals(newValue);
                }
            } else {
                fok = !oldValue.equals(newValue);
            }
        }

        return fok;
    }

    public void setValueUsualWidget(UIWrapper uiw) {
        String idModel = uiw.getModelBase().getIdModel();

        if (uiw.getModelBase().getProperty() == null && uiw.getModelBase().isBelongToModel()) {
            // in case if checkbox only belong to model
            return;
        }

        if (idModel != null) {
            TreeList treeList = modelLoadResult.getTreeList(idModel);

            lockTree.lock();
            try {
                IBeanWrapper bw = treeList.getCurrentBeanWrapper(uiw
                        .getModelBase().getTreeNode());

                Object oldValue = getValueFromBeanWrapper(uiw.getModelBase(),
                        bw, true);

                Object newValue = uiw.getCurrentValue();

                if (isDataWasChanged(oldValue, newValue)) {
                    TreeListUtils.setPropertyValue(bw.getData(), uiw
                            .getModelBase().getProperty(), newValue);
                    formWasChanged(uiw, bw, treeList);
                }

            } catch (Exception e) {
                SFLogger.error("Error by data reading", e);
            } finally {
                lockTree.unlock();
            }
        }

    }


    public void setValueUsualWidgetComboBox(UIWrapper uiw) {
        String idModel = uiw.getModelBase().getIdModel();

        if (idModel != null) {
            TreeList treeList = modelLoadResult.getTreeList(idModel);

            lockTree.lock();
            try {
                IBeanWrapper bw = treeList.getCurrentBeanWrapper(uiw
                        .getModelBase().getTreeNode());

                Object oldValue = getValueFromBeanWrapper(uiw.getModelBase(),
                        bw, true);

                Object objectSelected = ((SFComboBox) uiw.getObjectUI())
                        .getSelectedMirrorObject();

                Object newValue = null;
                if (uiw.getPropertySet() != null) {
                    // for pSet tag in combobox
                    if (objectSelected != null) {
                        newValue = TreeListUtils.getPropertyValue(objectSelected, uiw.getPropertySet());
                    }
                } else {
                    newValue = objectSelected;
                }

                if (isDataWasChanged(oldValue, newValue)) {
                    TreeListUtils.setPropertyValue(bw.getData(), uiw
                            .getModelBase().getProperty(), newValue);
                    formWasChanged(uiw, bw, treeList);

                    if (uiw.getChildPropertyName() != null) {
                        // because parent was changed then child must be set to null
                        // today is realized only one-to-one relationship. See XSD combobox model
                        ((SFComboBox) uiw.getUiwChildCombobox().getObjectUI()).reloadLinkedModel(bw.getData());
                        TreeListUtils.setPropertyValue(bw.getData(), uiw.getChildPropertyName(), null);
                    }
                }

            } catch (Exception e) {
                SFLogger.error("Error by data reading", e);
            } finally {
                lockTree.unlock();
            }
        }

    }

    public Object getCurrentValueUsualWdiget(UIWrapper uiw) {
        Object currValue = null;
        String idModel = uiw.getModelBase().getIdModel();

        if (idModel != null) {
            TreeList treeList = modelLoadResult.getTreeList(idModel);

            lockTree.lock();
            try {
                IBeanWrapper bw = treeList.getCurrentBeanWrapper(uiw
                        .getModelBase().getTreeNode());

                currValue = getValueFromBeanWrapper(uiw.getModelBase(), bw,
                        true);

            } catch (Exception e) {
                SFLogger.error("Error by data reading", e);
            } finally {
                lockTree.unlock();
            }
        }
        return currValue;
    }

    public void moveToRow(UIWrapper uiw, int index) {
        String idModel = uiw.getModelBase().getIdModel();

        if (idModel != null) {
            TreeList treeList = modelLoadResult.getTreeList(idModel);

            lockTree.lock();
            try {
                treeList.goIndex(index, uiw.getModelBase().getTreeNode());
                moveToRowRepaint(uiw.getModelBase().getTreeNode(), treeList/*
                 * ,
                 * true
                 */);
            } finally {
                lockTree.unlock();
            }
        }
    }

    public void refreshInfoStatusToChildren(UIWrapper uiw) {
        if (uiw.getModelBase().getTreeNode().isScrollWidget()) {
            moveToRowRepaintScroll(uiw.getModelBase().getTreeNode()/* , false */);
        }

        String idModel = uiw.getModelBase().getIdModel();

        if (idModel != null) {
            TreeList treeList = modelLoadResult.getTreeList(idModel);

            lockTree.lock();
            try {
                moveToRowRepaint(uiw.getModelBase().getTreeNode(), treeList/*
                 * ,
                 * false
                 */);
            } finally {
                lockTree.unlock();
            }
        }
    }

    public void refreshWidget(UIWrapper uiw) {
        if (uiw.getModelBase().getTreeNode().isScrollWidget()) {
            moveToRowRepaintScroll(uiw.getModelBase().getTreeNode()/* , false */);
        } else {
            String idModel = uiw.getModelBase().getIdModel();

            if (idModel != null) {
                TreeList treeList = modelLoadResult.getTreeList(idModel);

                lockTree.lock();
                try {
                    int index = treeList.getCurrentIndex(uiw.getModelBase()
                            .getTreeNode());
                    WidgetManagerHelper.fillWidget(this, uiw, index);
                } finally {
                    lockTree.unlock();
                }
            }
        }
    }

    @SuppressWarnings("unused")
    public boolean isNestedExisted(UIWrapper uiw) {

        if (uiw.getNestedExisted() != null) {
            return uiw.getNestedExisted();
        } else {
            boolean fok = isNestedExisted(uiw.getModelBase().getTreeNode());
            uiw.setNestedExisted(fok);
            return fok;
        }

    }

    private boolean isNestedExisted(
            @SuppressWarnings("rawtypes") WidgetTreeNode wtn) {
        boolean fok = false;
        @SuppressWarnings({"rawtypes", "unchecked"})
        List<WidgetTreeNode> list = wtn.getChildren();

        if (list.size() == 0) {
            return false;
        }

        for (@SuppressWarnings("rawtypes")
                WidgetTreeNode wtnc : list) {
            if (((UIWrapper) wtnc.getWidget()).isWrapperInGrid()) {
                // this is grid column
                continue;
            }
            fok = true;
            break;
        }
        return fok;
    }

    private void moveToRowRepaint(
            @SuppressWarnings("rawtypes") WidgetTreeNode wtn, TreeList treeList) {

        @SuppressWarnings({"rawtypes", "unchecked"})
        List<WidgetTreeNode> list = wtn.getChildren();

        if (list.size() == 0) {
            return;
        }

        // uiw - as a rule this is grid or tgrid which have selected row already
        UIWrapper uiw = (UIWrapper) wtn.getWidget();
        int index = treeList.getCurrentIndex(uiw.getModelBase()
                .getTreeNode());

        for (@SuppressWarnings("rawtypes")
                WidgetTreeNode wtnc : list) {
            if (((UIWrapper) wtnc.getWidget()).isWrapperInGrid()) {
                // this is grid column
                continue;
            }

            if (wtnc.isScrollWidget()) {
                moveToRowRepaintScroll(wtnc);

                UIWrapper uiwNested = (UIWrapper) wtnc.getWidget();
                int indexNested = treeList.getCurrentIndex(uiwNested.getModelBase()
                        .getTreeNode());
                ((SFGrid) uiwNested.getObjectUI()).selectRowWithoutChildRefreshing(indexNested);
            } else {
                WidgetManagerHelper.fillWidget(this,
                        (UIWrapper) wtnc.getWidget(), index);
            }
            moveToRowRepaint(wtnc, treeList);
        }
    }

    private void moveToRowRepaintScroll(
            @SuppressWarnings("rawtypes") WidgetTreeNode wtnc /* boolean reload */) {
        UIWrapper uiw = (UIWrapper) wtnc.getWidget();
        if (uiw.isGrid() || uiw.isTGrid()) {
            Object obj = uiw.getObjectUI();
            ((SFGrid) obj).reloadModel();
            ((SFGrid) obj).getTable().repaint();
        }
    }

    public void moveUp(String currentUIName) {
        if (allowFocusMovement.get()) {
            focus.moveUp(currentUIName);
        }
    }

    public void moveDown(String currentUIName) {
        if (allowFocusMovement.get()) {
            focus.moveDown(currentUIName);
        }
    }

    public FormBag getFormBag() {
        return formBag;
    }

    public void setFormBag(FormBag formBag) {
        this.formBag = formBag;
    }

    public void registerSelectedWrapper(UIWrapper wrp) {
        if (allowRegisterFocusLastComponent) {
            lockSelected.lock();
            try {
                lastSelectedWrapper = wrp;
                if (wrp.isScrollWidget()) {
                    lastSelectedScrollWrapper = wrp;
                }
            } finally {
                lockSelected.unlock();
            }
        }
    }

    public IBeanWrapper getSelectedBeanWrapper() {
        IBeanWrapper bw;
        lockSelected.lock();
        try {
            bw = getSelectedBeanWrapper(lastSelectedWrapper);
        } finally {
            lockSelected.unlock();
        }
        return bw;
    }

    public UIWrapper getSelectedScrollWrapper() {
        UIWrapper uiw;

        lockSelected.lock();
        try {
            uiw = lastSelectedScrollWrapper;
        } finally {
            lockSelected.unlock();
        }

        return uiw;
    }

    SFPair<BeanStatusEnum, BeanStatusEnum> getStatusLastRowScrollWidget(
            UIWrapper uiwScroll) {
        SFPair<BeanStatusEnum, BeanStatusEnum> ret = null;
        if (uiwScroll.isGrid() || uiwScroll.isTGrid()) {
            Object ui = uiwScroll.getObjectUI();
            int amount = ((SFGrid) ui).getTable().getModel().getRowCount();
            IBeanWrapper bw = getBeanWrapper(uiwScroll.getModelBase(),
                    amount - 1);

            ret = new SFPair<BeanStatusEnum, BeanStatusEnum>(
                    bw.getCreatedStatus(), bw.getObtainedStatus());
        }
        return ret;
    }

    public boolean isLastRowScrollWidget(UIWrapper uiwScroll) {
        boolean fok = false;
        if (uiwScroll.isGrid() || uiwScroll.isTGrid()) {
            Object ui = uiwScroll.getObjectUI();
            int amount = ((SFGrid) ui).getTable().getModel().getRowCount();

            int selRow = ((SFGrid) ui).getTable().getSelectedRow();
            if (selRow != -1) {
                if (selRow == (amount - 1)) {
                    fok = true;
                }
            }
        }
        return fok;
    }

    public IBeanWrapper getSelectedBeanWrapper(UIWrapper uiw) {
        IBeanWrapper bw = null;
        String idModel = uiw.getModelBase().getIdModel();

        if (idModel != null) {
            TreeList treeList = modelLoadResult.getTreeList(idModel);

            lockTree.lock();
            try {
                bw = treeList.getCurrentBeanWrapper(uiw.getModelBase()
                        .getTreeNode());

            } catch (Exception e) {
                SFLogger.error("Error by data reading", e);
            } finally {
                lockTree.unlock();
            }
        }
        return bw;
    }

    public List<IBeanWrapper> getSelectedBeanWrapperList(UIWrapperModel uiwMod) {
        List<IBeanWrapper> list = null;

        String idModel = uiwMod.getIdModel();

        if (idModel != null) {
            TreeList treeList = modelLoadResult.getTreeList(idModel);

            lockTree.lock();
            try {
                list = treeList.getCurrentBeanWrapperList(uiwMod.getTreeNode());
            } catch (Exception e) {
                SFLogger.error("Error by data reading", e);
            } finally {
                lockTree.unlock();
            }
        }
        return list;
    }

    BeanStatusEnum getLockedStatus(UIWrapper uiw, int index) {
        BeanStatusEnum bs = null;

        String idModel = uiw.getModelBase().getIdModel();

        if (idModel != null) {
            TreeList treeList = modelLoadResult.getTreeList(idModel);

            lockTree.lock();
            try {
                bs = treeList.getLockedStatus(index, uiw.getModelBase()
                        .getTreeNode());

            } catch (Exception e) {
                SFLogger.error("Error by data reading", e);
            } finally {
                lockTree.unlock();
            }
        }
        return bs;
    }

    public BeanStatusEnum getParentLockedStatus(UIWrapper uiw) {
        BeanStatusEnum bs = null;

        String idModel = uiw.getModelBase().getIdModel();

        if (idModel != null) {
            TreeList treeList = modelLoadResult.getTreeList(idModel);

            lockTree.lock();
            try {
                bs = treeList.getParentLockedStatus(uiw.getModelBase()
                        .getTreeNode());

            } catch (Exception e) {
                SFLogger.error("Error by data reading", e);
            } finally {
                lockTree.unlock();
            }
        }
        return bs;
    }

    public Object getFormParameter(String parameterName) {
        Object obj = null;

        if (mapParameters != null && parameterName != null) {
            obj = mapParameters.get(parameterName);
        }

        return obj;
    }

    @SuppressWarnings("unused")
    public void drawTree(UIWrapper uiw) {
        String idModel = uiw.getModelBase().getIdModel();

        if (idModel != null) {
            TreeList treeList = modelLoadResult.getTreeList(idModel);

            lockTree.lock();
            try {
                SFLogger.debug(WidgetManager.class, treeList.drawTree());
            } finally {
                lockTree.unlock();
            }
        }

    }

    // some methods for TGRID
    public Object getTGridRoot(UIWrapper uiw) {
        Object obj = null;
        String idModel = uiw.getModelBase().getIdModel();
        if (idModel != null) {
            obj = modelLoadResult.getTreeList(idModel);
        }
        return obj;
    }

    public Object getTGridChild(UIWrapper uiw, Object parent, int index) {
        Object obj = null;

        String idModel = uiw.getModelBase().getIdModel();

        if (idModel != null) {
            TreeList treeList = modelLoadResult.getTreeList(idModel);

            lockTree.lock();
            try {
                if (parent instanceof TreeList) {
                    obj = treeList.getTreeListNode(null, index);
                } else if (parent instanceof TreeListNode) {
                    obj = treeList
                            .getTreeListNode((TreeListNode) parent, index);
                } else {
                    throw new SmartFlexException("Node has unknown class");
                }
            } finally {
                lockTree.unlock();
            }
        }

        return obj;
    }

    public int getTGridChildCount(UIWrapper uiw, Object parent) {
        int childAmount = 0;

        String idModel = uiw.getModelBase().getIdModel();

        if (idModel != null) {
            TreeList treeList = modelLoadResult.getTreeList(idModel);

            lockTree.lock();
            try {
                if (parent instanceof TreeList) {
                    childAmount = treeList.getNodeAmountForFirstLevel();
                } else if (parent instanceof TreeListNode) {
                    childAmount = treeList.getChildCount((TreeListNode) parent);
                } else {
                    throw new SmartFlexException("Node has unknown class");
                }
            } finally {
                lockTree.unlock();
            }
        }

        return childAmount;
    }

    public boolean isTGridLeaf(UIWrapper uiw, Object node) {
        boolean fok = false;

        int childAmount = getTGridChildCount(uiw, node);

        if (childAmount == 0) {
            fok = true;
        }

        return fok;
    }

    public int getTGridIndexOfChild(UIWrapper uiw, Object parent, Object child) {
        int index = -1;
        String idModel = uiw.getModelBase().getIdModel();

        if (idModel != null) {
            TreeList treeList = modelLoadResult.getTreeList(idModel);

            lockTree.lock();
            try {
                index = treeList.getIndexOfChild(parent, child);
            } finally {
                lockTree.unlock();
            }
        }

        return index;
    }

    public UIWrapper getUIWrapper(String name) {
        return mapWrapper.get(name);
    }

    public void doActionMethod(String name) {
        UIWrapper wrp = getUIWrapper(name);
        if (wrp != null) {
            String method = wrp.getAction();
            FormUtil.runActionMethod(this, method);
        }
    }

    void setAllowFocusMovement(boolean flag) {
        allowFocusMovement.set(flag);
    }

}
