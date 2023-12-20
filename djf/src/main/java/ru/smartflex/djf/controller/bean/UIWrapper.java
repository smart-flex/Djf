package ru.smartflex.djf.controller.bean;

import java.awt.Component;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

import ru.smartflex.djf.AlignTypeEnum;
import ru.smartflex.djf.Djf;
import ru.smartflex.djf.FormAssistant;
import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.WidgetTypeEnum;
import ru.smartflex.djf.controller.FormStack;
import ru.smartflex.djf.controller.bean.tree.ITreeConstants;
import ru.smartflex.djf.controller.bean.tree.TreeListUtils;
import ru.smartflex.djf.controller.bean.tree.WidgetTreeNode;
import ru.smartflex.djf.controller.exception.MissingException;
import ru.smartflex.djf.controller.exception.ObjectCreationException;
import ru.smartflex.djf.controller.helper.ConverterUtil;
import ru.smartflex.djf.widget.ITextArea;
import ru.smartflex.djf.widget.SFPassword;
import ru.smartflex.djf.widget.SFStepperPercent;
import ru.smartflex.djf.widget.grid.SFGrid;
import ru.smartflex.djf.widget.mask.MaskInfo;
import ru.smartflex.djf.widget.tgrid.SFTGrid;

public class UIWrapper implements Comparable<UIWrapper> {

    private Object objectUI = null;
    // This level is based on bind parsing: pa.idAccount for example. In this
    // case both grid and field have the same level.
    // But field related from grid.

    private String bind = null;

    private WidgetTypeEnum widgetType = null;

    private DateFormat dateFormat = null;

    private String uiName = null;

    private MaskInfo maskInfo = null;

    private int order = 0;

    private String action = null;

    private String formXMLName = null;

    private boolean selectAble = false;
    private boolean gridInfoColumnAllowed = true;

    private int length = -1;

    // for button, checkbox
    private String belongToModel = null;

    private boolean appendAble = true;

    // JScrollPane
    private Component basePanel = null;
    // for combobox
    private String fill = null;
    private String propertySet = null;

    private UIWrapperModel modelBase = new UIWrapperModel();
    private UIWrapperModel modelFill = new UIWrapperModel();

    private BeanFormDef beanFormDef = null;
    private boolean wrapperInGrid = false;

    private AlignTypeEnum align = null;

    // these 2 properties for combobox one-to-one relationship
    private String parentPropertyName = null;
    private String childPropertyName = null;
    private UIWrapper uiwChildCombobox = null;

    // cashed property
    private Boolean nestedExisted = null;

    private String mouseDoubleClickAction = null;

    private boolean includedIntoWidgetTree = false;

    private String bindPrefix = null;
    private Boolean getBindFromMap = null;
    private BeanFormDef beanOfModel = null;
    private String idModelSelectAble = null;
    private boolean actionLong = false;
    private String actionLongMessage = null;
    private String selAction = null;
    private UIWrapperEnabledState enabledState = null;

    String getSelectAbleBindProperty() {

        if (bindPrefix != null) {
            if (getBindFromMap == null) {
                String idModel = bindPrefix;
                String[] parts = TreeListUtils.getPartProperties(bindPrefix);
                if (parts.length > 1) {
                    getBindFromMap = Boolean.TRUE;
                    idModel = parts[0];
                } else {
                    getBindFromMap = Boolean.FALSE;
                }
                idModelSelectAble = idModel;
                beanOfModel = beanFormDef.getModelBean(idModel);
            }
            if (getBindFromMap) {
                return beanOfModel.getSelectableSetProperty(bindPrefix);
            } else {
                return beanOfModel.getSelectablePropertyOfRootModel();
            }
        }

        return null;
    }

    @SuppressWarnings("unused")
    public String getIdModelSelectAble() {
        return idModelSelectAble;
    }

    public String getFormattedData(Object obj) {
        String ret = null;

        if (widgetType == WidgetTypeEnum.DATE) {
            ret = (String) ConverterUtil.getFormattedData(widgetType,
                    dateFormat, maskInfo.getMaskDelimiter(), obj);
        } else if (widgetType == WidgetTypeEnum.PERIOD) {
            ret = (String) ConverterUtil.getFormattedData(maskInfo, obj);
        } else {
            if (obj != null) {
                ret = obj.toString();
            }
        }

        return ret;
    }

    public Object getFormattedDataForGrid(Object obj) {
        Object ret = null;

        if (widgetType == WidgetTypeEnum.DATE) {
            ret = ConverterUtil.getFormattedData(widgetType,
                    dateFormat, maskInfo.getMaskDelimiter(), obj);
        } else if (widgetType == WidgetTypeEnum.PERIOD) {
            ret = ConverterUtil.getFormattedData(maskInfo, obj);
        } else {
            if (obj != null) {
                ret = obj;
            }
        }
        return ret;
    }

    public BeanFormDefProperty getBeanFormDefPropertyFromBind() {
        return beanFormDef
                .getPropertyFromLinearMap(getBind());
    }

    public Object getCurrentValue() {
        // after validation
        Object obj;

        if (widgetType == WidgetTypeEnum.DATE) {
            obj = ConverterUtil.getValue(widgetType, dateFormat,
                    (JTextComponent) objectUI, this);
        } else {
            obj = ConverterUtil.getValue(widgetType, null,
                    (JComponent) objectUI, this);
        }

        return obj;
    }

    public Object getValueFromText(String val) {
        return ConverterUtil.getValue(widgetType, dateFormat, val, this);
    }

    public void setBind(String bind) {
        this.bind = bind;
        this.modelBase.setBindPath(bind);
    }

    public void setBind(String bind, String bindPrefix) {
        if (bindPrefix == null) {
            setBind(bind);
        } else {
            setBind(bindPrefix + "." + bind);
        }
    }

    public String getBind() {
        return bind;
    }

    public Object getObjectUI() {
        return objectUI;
    }

    public void setObjectUI(Object objectUI) {
        this.objectUI = objectUI;
    }

    public int getLevelUI(String idModel) {
        return UIWrapperModel.getLevelUI(idModel, this);
    }

    public UIWrapperModel getWrapperModel(String idModel) {
        return UIWrapperModel.getWrapperModel(idModel, this);
    }

    /**
     * Setup static or dynamic behavior for enable/disable mode of item
     */
    public void setEnableBehavior(String info, boolean enabledByMouseClick) {
        enabledState = UIWrapperEnabledState.defineInitialStatus(info, enabledByMouseClick);
        boolean currentState = enabledState.getCurrentState(this);

        setItemEnabledInt(currentState, false, true);
    }

    public void setEditableStaticOffBehavior() {
        enabledState = UIWrapperEnabledState.getEditableStaticOffBehavior();
        boolean currentState = enabledState.getCurrentState(this);

        setItemEnabledInt(currentState, false, true);
    }

    public static Boolean translateStringToBoolean(String info, boolean enabledByMouseClick) {
        Boolean ret = null;

        if (info != null) {
            String infoLower = info.toLowerCase();
            if (infoLower.equals(SFConstants.YES)) {
                ret = Boolean.TRUE;
            } else if (infoLower.equals(SFConstants.TRUE)) {
                ret = Boolean.TRUE;
            } else if (infoLower.equals(SFConstants.NO)) {
                ret = Boolean.FALSE;
            } else if (infoLower.equals(SFConstants.FALSE)) {
                ret = Boolean.FALSE;
            } else if (infoLower.equals(SFConstants.BY_CLICK)) {
                if (enabledByMouseClick) {
                    ret = Boolean.FALSE;
                }
            }
        }

        return ret;
    }

    public boolean isEnabledDisabledStaticBehavior() {
        return enabledState.getCurrentState(this);
    }

    public void setItemEnabled(boolean flag) {
        setItemEnabledInt(flag, false, false);
    }

    public void setItemDisabledDueToAbend() {
        setItemEnabledInt(false /* этот параметр при force==true не имеет значения*/, true, false);
    }

    /**
     *
     * @param flag - true - возвращаем изначальный статус, false - отключаем
     * @param force если true - принудительно переводим виджет в disable в виду аварии
     * @param onInit - true означает начальную инициализацию
     */
    private void setItemEnabledInt(boolean flag, boolean force, boolean onInit) {

        boolean currentState = enabledState.getCurrentState(this);

        JComponent comp;
        switch (widgetType) {
            case BUTTON:
            case COMBOBOX:
            case RUN:
            case CHECKBOX:
            case PASSWORD:
                comp = (JComponent) objectUI;
                if (force) {
                    comp.setEnabled(false);
                } else {
                    if (onInit) {
                        if (!flag) {
                            comp.setEnabled(false);
                        }
                    } else {
                        // регулярный проход
                        if (flag) {
                            comp.setEnabled(currentState);
                        } else {
                            comp.setEnabled(false);
                        }
                    }
                }
                break;
            case DATE:
            case PERIOD:
            case BYTE:
            case SHORT:
            case INT:
            case LONG:
            case TEXT:
            case PHONE:
            case NUMERIC:
                comp = (JComponent) objectUI;
                if (force) {
                    comp.setEnabled(false);
                } else {
                    JTextComponent compText = (JTextComponent) objectUI;
                    if (onInit) {
                        if (!flag) {
                            compText.setEditable(false);
                        }
                    } else {
                        if (flag) {
                            compText.setEditable(currentState);
                        } else {
                            compText.setEditable(false);
                        }
                    }
                }
                break;
            case STEPPER_PERCENT:
                SFStepperPercent stepper = (SFStepperPercent) objectUI;
                if (force) {
                    stepper.setItemDisabledDueToAbend();
                } else {
                    if (onInit) {
                        // ничего не делаем, т.к. по дефолту уже все выставлено как надо
                    } else {
                        if (flag) {
                            stepper.enableAction();
                        } else {
                            stepper.disableAction();
                        }
                    }
                }
                break;
            case GRID:
                SFGrid grid = (SFGrid) objectUI;
                comp = grid.getTable();
                if (force) {
                    comp.setEnabled(false);
                } else {
                    if (!onInit) {
                        // регулярный проход
                        comp.setEnabled(flag);
                    }
                }
                break;
            case TGRID:
                SFTGrid tgrid = (SFTGrid) objectUI;
                comp = tgrid.getTable();
                if (force) {
                    comp.setEnabled(false);
                } else {
                    if (!onInit) {
                        // регулярный проход
                        comp.setEnabled(flag);
                    }
                }
                break;
            case TEXTAREA:
                ITextArea tArea = (ITextArea) objectUI;
                if (force) {
                    tArea.setEnabled(false);
                } else {
                    if (onInit) {
                        if (!flag) {
                            tArea.setEditable(false);
                        }
                    } else {
                        if (flag) {
                            tArea.setEditable(currentState);
                        } else {
                            tArea.setEditable(false);
                        }
                    }
                }
                break;
        }
    }

    public boolean equalsBaseTreePath(UIWrapper uiw) {
        boolean fok = false;
        String myTreePath = modelBase.getTreePath();
        if (myTreePath != null) {
            String treePath = uiw.getModelBase().getTreePath();
            if (treePath != null) {
                fok = myTreePath.equals(treePath);
            }
        }
        return fok;
    }

    public boolean isScrollWidget() {

        return WidgetTypeEnum.isScrollAbleInMDStyle(widgetType);

    }

    public boolean isGrid() {
        boolean fok = false;
        if (widgetType == WidgetTypeEnum.GRID) {
            fok = true;
        }
        return fok;
    }

    public boolean isTGrid() {
        boolean fok = false;
        if (widgetType == WidgetTypeEnum.TGRID) {
            fok = true;
        }
        return fok;
    }

    public String getTreePath(String idModel) {
        return UIWrapperModel.getTreePath(idModel, this);
    }

    public boolean isItemInModel(String idModel) {
        boolean fok;

        String search = idModel + ITreeConstants.PARENT_CHILD_DELIMITER;

        switch (widgetType) {
            case COMBOBOX:
                fok = this.modelBase.isBindPathBelongModel(idModel);
                if (fok) {
                    modelFill.handleBelongModelFillForce();
                }
                break;
            case GRID:
            case TGRID:
                boolean columnBelongModel = false;
                boolean allColsInModel = true;
                int level = Integer.MAX_VALUE;
                String colBindMinimalLevel = null;

                SFGrid grid = (SFGrid) this.getObjectUI();
                List<GridColumnInfo> list = grid.getListColumnInfo();
                for (GridColumnInfo gci : list) {
                    if (gci.getColumnDefinition() == null) {
                        // info column; skip it
                        continue;
                    }
                    String colBind = gci.getBind();
                    if (colBind != null) {
                        int ind = colBind.indexOf(search);
                        if (ind != 0) {
                            allColsInModel = false;
                        } else {
                            columnBelongModel = true;
                        }
                        if (columnBelongModel) {
                            int colLevel = UIWrapperModel.getLevel(colBind);
                            if (colLevel != -1) {
                                if (level > colLevel) {
                                    // get minimal level value
                                    level = colLevel;

                                    colBindMinimalLevel = colBind;
                                }
                            }
                        }
                    }
                    boolean fokBase = gci.getModelBase().isBindPathBelongModel(
                            idModel);
                    if (fokBase) {
                        if (gci.getWidgetType() == WidgetTypeEnum.COMBOBOX) {
                            // fills modelFill forced
                            gci.getModelFill().handleBelongModelFillForce();
                        }
                    }
                }

                if (!allColsInModel) {
                    if (columnBelongModel) {
                        throw new MissingException(
                                "Not all scroll widget column belong model: "
                                        + idModel);
                    }
                }
                fok = allColsInModel;
                if (fok) {
                    this.modelBase.setLevelUI(level);
                    this.modelBase.setIdModel(idModel);
                    this.modelBase.defineTreePath(colBindMinimalLevel);
                }

                break;
            default:
                fok = this.modelBase.isBindPathBelongModel(idModel);
        }

        return fok;
    }

    public WidgetTreeNode<?> getTreeNode(String id) {
        return UIWrapperModel.getTreeNode(id, this);
    }

    public void setTreeNode(WidgetTreeNode<?> treeNode, String id) {
        UIWrapperModel.setTreeNode(treeNode, id, this);
    }

    @Override
    public String toString() {
        String ret = "UIWrapper [item=" + widgetType + " bind=" + getBind()
                + " disableBehavior=" + isEnabledDisabledStaticBehavior();
        if (widgetType == WidgetTypeEnum.COMBOBOX) {
            ret += " fill=" + fill;
        }
        ret += " uiName " + this.getUiName() + "]";
        return ret;
    }

    public void setWidgetType(WidgetTypeEnum widgetType) {
        this.widgetType = widgetType;
    }

    public WidgetTypeEnum getWidgetType() {
        return widgetType;
    }

    public String getUiName() {
        return uiName;
    }

    public void setupUIName(WidgetTypeEnum type, String id) {
        if (id != null) {
            uiName = id;
        } else {
            uiName = Djf.getConfigurator().getNextUIId(type);
        }

        switch (type) {
            case PASSWORD:
                ((SFPassword)objectUI).getPasswordField().setName(uiName);
                break;
            default:
                ((java.awt.Component) objectUI).setName(uiName);
                break;
        }
    }

    public boolean isFocusable() {
        return WidgetTypeEnum.isFocusAble(widgetType);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uiName == null) ? 0 : uiName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UIWrapper other = (UIWrapper) obj;
        if (uiName == null) {
            //noinspection RedundantIfStatement
            if (other.uiName != null)
                return false;
        } else //noinspection RedundantIfStatement
            if (!uiName.equals(other.uiName))
                return false;
        return true;
    }

    public void setMaskInfo(MaskInfo maskInfo) {
        this.maskInfo = maskInfo;

        if (widgetType == WidgetTypeEnum.DATE) {
            dateFormat = new SimpleDateFormat(maskInfo.getMask());
        }
    }

    public java.text.Format getDateFormat() {
        return dateFormat;
    }

    public MaskInfo getMaskInfo() {
        return maskInfo;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(BigInteger order) {
        if (order != null) {
            if (order.intValue() <= 0) {
                throw new ObjectCreationException(
                        "Order must be more than zero");
            }
            this.order = order.intValue();
        } else {
            this.order = Djf.getConfigurator().getNextUIOrder();
        }
    }

    @Override
    public int compareTo(UIWrapper o) {
        int ret = 0;

        int ord = o.getOrder();
        if (ord < order) {
            ret = 1;
        } else if (ord > order) {
            ret = -1;
        }

        return ret;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFormXMLName() {
        return formXMLName;
    }

    public void setFormXMLName(String formXMLName) {
        this.formXMLName = formXMLName;
    }

    boolean isSelectAble() {
        return selectAble;
    }

    public void setSelectAble(boolean selectAble) {
        this.selectAble = selectAble;
    }

    public boolean isGridInfoColumnAllowed() {
        return gridInfoColumnAllowed;
    }

    public void setGridInfoColumnAllowed(boolean gridInfoColumnAllowed) {
        this.gridInfoColumnAllowed = gridInfoColumnAllowed;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    /**
     * This property is intended for focus policy and status management
     */
    public void setBelongToModel(String belongToModel) {
        if (belongToModel != null) {
            this.belongToModel = belongToModel; // a little duplicate
            this.modelBase.setBindPath(belongToModel);
            this.modelBase.setFlagOfBelongToModel(); // a little duplicate
        }
    }

    public String getBelongToModel() {
        return belongToModel;
    }

    public boolean isAppendAble() {
        return appendAble;
    }

    public void setAppendAble(boolean appendAble) {
        this.appendAble = appendAble;
    }

    public Component getBasePanel() {
        return basePanel;
    }

    public void setBasePanel(Component basePanel) {
        this.basePanel = basePanel;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
        this.modelFill.setBindPath(fill);
    }

    public UIWrapperModel getModelBase() {
        return modelBase;
    }

    public UIWrapperModel getModelFill() {
        return modelFill;
    }

    @SuppressWarnings("unused")
    public BeanFormDef getBeanFormDef() {
        return beanFormDef;
    }

    public void setBeanFormDef(BeanFormDef beanFormDef) {
        this.beanFormDef = beanFormDef;
        beanFormDef.bindRegisterFromWidget(bind);
    }

    public boolean isWrapperInGrid() {
        return wrapperInGrid;
    }

    public void setWrapperInGrid(boolean wrapperInGrid) {
        this.wrapperInGrid = wrapperInGrid;
    }

    public AlignTypeEnum getAlign() {
        return align;
    }

    public void setAlign(AlignTypeEnum align) {
        this.align = align;
    }

    public String getParentPropertyName() {
        return parentPropertyName;
    }

    public void setParentPropertyName(String parentPropertyName) {
        this.parentPropertyName = parentPropertyName;
    }

    public String getChildPropertyName() {
        return childPropertyName;
    }

    public void setChildPropertyName(String childPropertyName) {
        this.childPropertyName = childPropertyName;
    }

    public UIWrapper getUiwChildCombobox() {
        return uiwChildCombobox;
    }

    public void setUiwChildCombobox(UIWrapper uiwChildCombobox) {
        this.uiwChildCombobox = uiwChildCombobox;
    }

    public Boolean getNestedExisted() {
        return nestedExisted;
    }

    public void setNestedExisted(Boolean nestedExisted) {
        this.nestedExisted = nestedExisted;
    }

    public String getMouseDoubleClickAction() {
        return mouseDoubleClickAction;
    }

    public void setMouseDoubleClickAction(String mouseDoubleClickAction) {
        this.mouseDoubleClickAction = mouseDoubleClickAction;
    }

    public boolean isIncludedIntoWidgetTree() {
        return includedIntoWidgetTree;
    }

    public void setIncludedIntoWidgetTree(boolean includedIntoWidgetTree) {
        this.includedIntoWidgetTree = includedIntoWidgetTree;
    }

    public String getPropertySet() {
        return propertySet;
    }

    public void setPropertySet(String propertySet) {
        this.propertySet = propertySet;
    }

    public void setBindPrefix(String bindPrefix) {
        this.bindPrefix = bindPrefix;
    }

    public boolean isActionLong() {
        return actionLong;
    }

    public void setActionLong(boolean actionLong) {
        this.actionLong = actionLong;
    }

    public String getActionLongMessage() {
        return actionLongMessage;
    }

    public void setActionLongMessage(String actionLongMessage) {
        this.actionLongMessage = actionLongMessage;
    }

    public String getSelAction() {
        return selAction;
    }

    public void setSelAction(String selAction) {
        this.selAction = selAction;
    }
}
