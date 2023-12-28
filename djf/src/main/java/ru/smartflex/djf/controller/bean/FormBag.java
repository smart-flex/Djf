package ru.smartflex.djf.controller.bean;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;

import org.apache.commons.beanutils.MethodUtils;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.FormAssistant;
import ru.smartflex.djf.FormStepEnum;
import ru.smartflex.djf.FrameHelper;
import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.controller.FormManager;
import ru.smartflex.djf.controller.FormManagerThread;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.tree.BeanStatusEnum;
import ru.smartflex.djf.controller.bean.tree.IBeanWrapper;
import ru.smartflex.djf.controller.bean.tree.IBeanWrapperFactory;
import ru.smartflex.djf.controller.bean.tree.TreeListException;
import ru.smartflex.djf.controller.bean.tree.TreeListUtils;
import ru.smartflex.djf.controller.exception.IExceptionHumanFriendly;
import ru.smartflex.djf.controller.exception.PropertyNotFilledException;
import ru.smartflex.djf.controller.helper.ObjectCreator;
import ru.smartflex.djf.controller.helper.PrefixUtil;
import ru.smartflex.djf.model.gen.FormType;
import ru.smartflex.djf.model.gen.ModelType;
import ru.smartflex.djf.tool.FontUtil;
import ru.smartflex.djf.widget.*;
import ru.smartflex.djf.widget.grid.ICellEditor;
import ru.smartflex.djf.widget.grid.SFGrid;

public class FormBag {

    private FormType formType;
    private WidgetManager widgetManager;
    private UIWrapper formWrapper;

    private FormAssistant assistant;
    private IFormSession formSession;

    private String welcomeMessage = null;
    private boolean onlyOnceTime = false;
    private boolean formWasChanged = false;
    private boolean formWasSaved = false;
    private String description;

    private Map<String, FormBagModelCRUDStatus> modelMapFlag = new HashMap<String, FormBagModelCRUDStatus>();

    private AtomicBoolean formReady = new AtomicBoolean(false);
    private FormManager formManager = null;
    private Map<String, Object> mapParameters = null;
    private BeanFormDef beanFormDef;
    private String welcomeMessageForParentForm = null;
    private String formXml;
    private boolean closeOnSave = false;
    private boolean forceRefreshForParentForm = false;

    private TaskStatusLevelEnum taskStatusLevelEnumForParentForm = TaskStatusLevelEnum.OK;

    public FormBag(FormType formType, WidgetManager widgetManager,
                   UIWrapper formWrapper, FormAssistant assist,
                   IFormSession formSession, BeanFormDef beanDef, String formXmlBody) {
        super();
        this.formType = formType;
        this.widgetManager = widgetManager;
        this.formWrapper = formWrapper;
        this.assistant = assist;
        this.formSession = formSession;
        this.beanFormDef = beanDef;
        this.formXml = formXmlBody;

        init();

        this.widgetManager.setFormBag(this);

        if (formType.getWelcome() != null) {
            welcomeMessage = PrefixUtil.getMsg(formType.getWelcome(), null);
        }
        if (formType.isOnlyOnce() != null) {
            onlyOnceTime = formType.isOnlyOnce();
        }
        description = formType.getDescription();
        if (formType.isCloseOnSave() != null) {
            closeOnSave = formType.isCloseOnSave();
        }
    }

    private void init() {
        if (formType.getModels() == null) {
            return;
        }

        List<ModelType> modelList = formType.getModels().getModel();
        for (ModelType mt : modelList) {
            boolean fokCanBeChanged = false;
            boolean fokCanBeAppend = false;
            boolean fokNotBeSaved = false;
            boolean fokCanBeDeleted = false;

            // Dynamic settings
            Boolean assistNoAppend = formSession.isNoAppend(mt.getId());
            if (assistNoAppend != null) {
                if (assistNoAppend) {
                    //noinspection ConstantConditions
                    fokCanBeAppend = false;
                }
            } else {
                // from xml
                if (mt.isNoAppend() == null
                        || !mt.isNoAppend()) {
                    // usual behavior
                    fokCanBeAppend = true;
                }
            }

            Boolean assistReadOnly = formSession.isReadOnly(mt.getId());
            if (assistReadOnly != null) {
                if (assistReadOnly) {
                    //noinspection ConstantConditions
                    fokCanBeChanged = false;
                }
            } else {
                // from xml
                if (mt.isReadOnly() == null
                        || !mt.isReadOnly()) {
                    // usual behavior
                    fokCanBeChanged = true;
                }
            }

            Boolean assistNoSave = formSession.isNoSave(mt.getId());
            if (assistNoSave != null) {
                if (assistNoSave) {
                    fokNotBeSaved = true;
                }
            } else {
                // from xml
                if (mt.isNoSave() != null && mt.isNoSave()) {
                    fokNotBeSaved = true;
                }
            }

            Boolean assistNoDelete = formSession.isNoDelete(mt.getId());
            if (assistNoDelete != null) {
                if (!assistNoDelete) {
                    fokCanBeDeleted = true;
                }
            } else {
                if (mt.isNoDelete() == null
                        || !mt.isNoDelete()) {
                    // usual behavior
                    fokCanBeDeleted = true;
                }
            }

            // Rule
            if (!fokCanBeChanged) {
                // read only is true
                fokCanBeAppend = false;
                fokCanBeDeleted = false;
                // fokNotBeSaved = true;
            } else {
                if (fokNotBeSaved) {
                    fokCanBeAppend = false;
                    fokCanBeDeleted = false;
                }
            }
            FormBagModelCRUDStatus status = new FormBagModelCRUDStatus(
                    fokCanBeChanged, fokCanBeAppend, fokNotBeSaved,
                    fokCanBeDeleted);
            modelMapFlag.put(mt.getId(), status);

            /*
             * SFLogger.debug(FormBag.class, "Model status: " + mt.getId() + " "
             * + status);
             */
        }
    }

    public boolean isModelCanBeChanged(String idModel) {
        return modelMapFlag.get(idModel).isModelCanBeChanged();
    }

    public boolean isModelCanBeAppend(String idModel) {
        return modelMapFlag.get(idModel).isModelCanBeAppend();
    }

    public boolean isModelCanNotBeSaved(String idModel) {
        return modelMapFlag.get(idModel).isModelCanNotBeSaved();
    }

    private boolean isModelCanBeDeleted(String idModel) {
        return modelMapFlag.get(idModel).isModelCanBeDeleted();
    }

    private boolean isModelCanBeChanged() {
        boolean flag = false;

        Collection<FormBagModelCRUDStatus> coll = modelMapFlag.values();
        for (FormBagModelCRUDStatus status : coll) {
            flag = flag || status.isModelCanBeChanged();
        }
        return flag;
    }

    private boolean isModelCanBeAppend() {
        boolean flag = false;

        Collection<FormBagModelCRUDStatus> coll = modelMapFlag.values();
        for (FormBagModelCRUDStatus status : coll) {
            flag = flag || status.isModelCanBeAppend();
        }
        return flag;
    }

    private boolean isModelCanNotBeSaved() {
        boolean flag = true;

        Collection<FormBagModelCRUDStatus> coll = modelMapFlag.values();
        for (FormBagModelCRUDStatus status : coll) {
            flag = flag && status.isModelCanNotBeSaved();
        }
        return flag;
    }

    private boolean isModelCanBeDeleted() {
        boolean flag = false;

        Collection<FormBagModelCRUDStatus> coll = modelMapFlag.values();
        for (FormBagModelCRUDStatus status : coll) {
            flag = flag || status.isModelCanBeDeleted();
        }
        return flag;
    }

    public void initFrameButtons() {
        // read united flags from all models
        boolean canNotBeSaved = isModelCanNotBeSaved();
        boolean mayChanged = isModelCanBeChanged();
        boolean mayAppend = isModelCanBeAppend();
        boolean mayDelete = isModelCanBeDeleted();

        // Rule handling (as copy-past)

        if (!mayChanged) {
            // read only is true
            mayAppend = false;
            mayDelete = false;
            // canNotBeSaved = true;
        } else {
            if (canNotBeSaved) {
                mayAppend = false;
                mayDelete = false;
            }
        }

        if (mayAppend) {
            getButtonOperCache().setButtonShow(SFConstants.BUTTON_NAME_ADD);
        } else {
            getButtonOperCache().setButtonHide(SFConstants.BUTTON_NAME_ADD);
        }
        if (mayChanged && !canNotBeSaved) {
            getButtonOperCache().setButtonShow(SFConstants.BUTTON_NAME_DELETE);
            getButtonOperCache().setButtonShow(SFConstants.BUTTON_NAME_REFRESH);
            getButtonOperCache().setButtonShow(SFConstants.BUTTON_NAME_SAVE);
        } else {
            getButtonOperCache().setButtonHide(SFConstants.BUTTON_NAME_DELETE);
            getButtonOperCache().setButtonHide(SFConstants.BUTTON_NAME_REFRESH);
            getButtonOperCache().setButtonHide(SFConstants.BUTTON_NAME_SAVE);
        }
        if (mayDelete) {
            getButtonOperCache().setButtonShow(SFConstants.BUTTON_NAME_DELETE);
        } else {
            getButtonOperCache().setButtonHide(SFConstants.BUTTON_NAME_DELETE);
        }
    }

    public ButtonOperCache getButtonOperCache() {
        if (!this.isFormModal()) {
            return Djf.getConfigurator().getFrameButCache();
        } else {
            return Djf.getConfigurator().getDialogButCache();
        }
    }

    public void enableFrameButtons() {
        // boolean mayChanged = isModelCanBeChanged();
        boolean mayAppend = isModelCanBeAppend();
        boolean mayDelete = isModelCanBeDeleted();

        if (mayAppend) {
            getButtonOperCache().setButtonEnabled(SFConstants.BUTTON_NAME_ADD);
        }
        /*
         * if (mayChanged) { getButtonOperCache().setButtonEnabled(
         * SFConstants.BUTTON_NAME_DELETE); }
         */
        if (mayDelete) {
            getButtonOperCache().setButtonEnabled(
                    SFConstants.BUTTON_NAME_DELETE);
        }
        enableFrameExitButton();
    }

    public void disableFrameButtons() {
        getButtonOperCache().setButtonDisabled(SFConstants.BUTTON_NAME_ADD);
        getButtonOperCache().setButtonDisabled(SFConstants.BUTTON_NAME_DELETE);
        getButtonOperCache().setButtonDisabled(SFConstants.BUTTON_NAME_REFRESH);
        getButtonOperCache().setButtonDisabled(SFConstants.BUTTON_NAME_SAVE);
        getButtonOperCache().setButtonDisabled(SFConstants.BUTTON_NAME_EXIT);
    }

    public void markAsDelete() {
        UIWrapper uiw = getWidgetManager().getLastSelectedWrapper();
        if (uiw.getModelBase().getIdModel() == null) {
            uiw = getWidgetManager().getSelectedScrollWrapper();
        }
        if (this.isModelCanBeChanged(uiw.getModelBase().getIdModel())) {
            if (isModelCanBeDeleted(uiw.getModelBase().getIdModel())) {
                IBeanWrapper bwDelete = getWidgetManager()
                        .getSelectedBeanWrapper();
                if (bwDelete.isPossibleMarkAsDelete()) {
                    BeanStatusEnum bs = getWidgetManager()
                            .getParentLockedStatus(uiw);
                    if (bs == null) {
                        boolean fokDelete = bwDelete.changeDeleteStatus();
                        if (fokDelete) {
                            getWidgetManager().refreshInfoStatusToChildren(uiw);
                            formWasChanged();
                        }
                    } else {
                        if (bs == BeanStatusEnum.DELETED) {
                            Djf.showStatusWarnMessage("${djf.message.warn.parent_already_deleted}");
                        } else {
                            Djf.showStatusWarnMessage("${djf.message.warn.delete_is_not_allowed}");
                        }
                    }
                } else {
                    Djf.showStatusWarnMessage("${djf.message.warn.delete_is_not_allowed}");
                }
            } else {
                Djf.showStatusWarnMessage("${djf.message.warn.delete_is_not_allowed}");
            }
        } else {
            Djf.showStatusWarnMessage("${djf.message.warn.delete_is_not_allowed}");
        }
    }

    public void markRowAsSelected(UIWrapper uiw) {
        if (uiw.isSelectAble()) {
            IBeanWrapper bwSelect = getWidgetManager().getSelectedBeanWrapper();
            if (bwSelect.isPossibleMarkAsSelected()) {
                BeanStatusEnum bs = getWidgetManager().getParentLockedStatus(
                        uiw);
                if (bs == null) {
                    boolean fokSelect = bwSelect.changeSelectStatus();
                    if (fokSelect) {
                        getWidgetManager().refreshInfoStatusToChildren(uiw);
                        String bind = uiw.getSelectAbleBindProperty();
                        if (bind != null) {
                            boolean sel = bwSelect.isBeanWrapperSelected();
                            TreeListUtils.setPropertyValue(bwSelect.getData(),
                                    bind, sel);
                            formWasChanged();
                        }
                    }
                } else {
                    if (bs == BeanStatusEnum.SELECTED) {
                        Djf.showStatusWarnMessage("${djf.message.warn.parent_already_selected}");
                    } else {
                        Djf.showStatusWarnMessage("${djf.message.warn.select_is_not_allowed}");
                    }
                }
            } else {
                Djf.showStatusWarnMessage("${djf.message.warn.select_is_not_allowed}");
            }
        }
    }

    public void markGridAsSelected(UIWrapper uiw) {
        if (uiw.isSelectAble()) {
            boolean wasSelectDeselect = false;
            BeanStatusEnum bs = getWidgetManager().getParentLockedStatus(uiw);
            if (bs == null) {
                List<IBeanWrapper> list = getWidgetManager()
                        .getSelectedBeanWrapperList(uiw.getModelBase());
                for (IBeanWrapper bw : list) {
                    boolean fokSelect = bw.changeSelectStatus();
                    if (fokSelect) {
                        wasSelectDeselect = true;
                        String bind = uiw.getSelectAbleBindProperty();
                        if (bind != null) {
                            boolean sel = bw.isBeanWrapperSelected();
                            TreeListUtils.setPropertyValue(bw.getData(), bind,
                                    sel);
                        }
                    }
                }
                if (wasSelectDeselect) {
                    getWidgetManager().refreshInfoStatusToChildren(uiw);
                    formWasChanged();
                }
            } else {
                if (bs == BeanStatusEnum.SELECTED) {
                    Djf.showStatusWarnMessage("${djf.message.warn.parent_already_selected}");
                } else {
                    Djf.showStatusWarnMessage("${djf.message.warn.select_is_not_allowed}");
                }
            }
        }
    }

    public void refeshForm(String welcomeMessageFromChildrenForm, TaskStatusLevelEnum taskStatusLevelFromChildrenForm) {
        refeshFormInt(welcomeMessageFromChildrenForm, taskStatusLevelFromChildrenForm);
    }

    public void refeshForm(String welcomeMessageFromChildrenForm) {
        refeshFormInt(welcomeMessageFromChildrenForm, null);
    }

    public void refeshForm() {
        refeshFormInt(null, null);
    }

    private void refeshFormInt(String welcomeMessageFromChildrenForm, TaskStatusLevelEnum taskStatusLevelFromChildrenForm) {
        formWasChanged = false;

        setFormReady(false);

        disableFrameButtons();
        // disable item and enable item follows to winking
        // therefore disables this feature
        // getWidgetManager().disableItems();
        FrameHelper.showWaitLongPanel();

        if (welcomeMessageFromChildrenForm == null) {
            FormManagerThread fm = new FormManagerThread(getFormManager());
            fm.execute();
        } else {
            FormManagerThread fm = new FormManagerThread(getFormManager(), welcomeMessageFromChildrenForm, taskStatusLevelFromChildrenForm);
            fm.execute();
        }
    }

    public void saveForm() {
        if (formWasChanged) {

            boolean wasError = false;

            List<ModelType> modelList = getFormManager().getForm().getModels()
                    .getModel();

            String nnProps = null;

            boolean validateNotNullOk = true;
            boolean validateConfOk = true;
            // validation scam
            for (ModelType mt : modelList) {

                if (!validateNotNullOk) {
                    break;
                }
                if (!validateConfOk) {
                    break;
                }
                ModelType.Save save = mt.getSave();

                if (save != null) {
                    try {
                        @SuppressWarnings("rawtypes")
                        SFPair<Class, Object>[] pars = ObjectCreator
                                .createObjectParameters(save.getParam(),
                                        mt.getId(), widgetManager);
                        ObjectCreator.extractObject(pars);
                    } catch (PropertyNotFilledException e) {
                        SFLogger.error("Error validation bean", e);
                        validateNotNullOk = false;
                        nnProps = e.getNotNullProps();
                    } catch (TreeListException te) {
                        SFLogger.error("Error bean configuration", te);
                        validateConfOk = false;
                    }
                }
            }

            if (validateNotNullOk && validateConfOk) {
                String humanErrorMessage = null;
                boolean saveMethodWasInvoked = false;
                // saving
                for (ModelType mt : modelList) {
                    ModelType.Save save = mt.getSave();

                    if (save != null) {
                        String method;

                        Object[] pars = ObjectCreator
                                .extractObject(ObjectCreator
                                        .createObjectParameters(
                                                save.getParam(), mt.getId(),
                                                widgetManager));
                        method = save.getMethod();

                        SFPair<String, Object> pair = PrefixUtil
                                .getMethodDefinition(method);

                        if (pair != null) {
                            try {
                                MethodUtils.invokeMethod(pair.getValue(),
                                        pair.getName(), pars);
                                saveMethodWasInvoked = true;
                            } catch (Exception e) {
                                if (pair.getName() != null) {
                                    SFLogger.error("Error by save bean: " + pair.getName(), e);
                                } else {
                                    SFLogger.error("Error by save bean (no name existed)", e);
                                }
                                wasError = true;
                                humanErrorMessage = digHumanMessage(e);
                                break;
                            }
                        }
                    }

                }
                if (!saveMethodWasInvoked) {
                    SFLogger.warn(FormBag.class,
                            "No one save method was invoked");
                }
                if (wasError) {
                    if (humanErrorMessage != null) {
                        Djf.showStatusErrorMessage(humanErrorMessage);
                    } else {
                        Djf.showStatusErrorMessage("${label.djf.message.error.form.save}");
                    }
                } else {
                    Djf.showStatusInfoMessage("${label.djf.message.info.form.save}");
                    formWasSaved = true;
                    refeshForm();
                }
            } else {
                if (!validateNotNullOk) {
                    String msg = PrefixUtil.getMsg("${label.djf.message.warn.validate_not_null}", null) + " " + nnProps;
                    Djf.showStatusWarnMessage(msg);
                }
                if (!validateConfOk) {
                    Djf.showStatusWarnMessage("${label.djf.message.warn.sf.message.warn.validate_conf}");
                }
            }
        }
    }

    private String digHumanMessage(Throwable thr) {
        if (thr == null) {
            return null;
        } else if (thr instanceof IExceptionHumanFriendly) {
            return ((IExceptionHumanFriendly) thr).getHumanErrorMessage();
        } else {
            return digHumanMessage(thr.getCause());
        }
    }

    public void formWasChanged() {
        if (!formWasChanged) {

            getButtonOperCache().setButtonEnabled(
                    SFConstants.BUTTON_NAME_REFRESH);
            getButtonOperCache().setButtonEnabled(SFConstants.BUTTON_NAME_SAVE);
            getButtonOperCache()
                    .setButtonDisabled(SFConstants.BUTTON_NAME_EXIT);

            formWasChanged = true;

            SFLogger.debug(FormBag.class, PrefixUtil.getMsg(
                    "${djf.message.info.form.was_changed}", null));
        }
    }

    private void enableFrameExitButton() {
        getButtonOperCache().setButtonEnabled(SFConstants.BUTTON_NAME_EXIT);
    }

    public FormType getFormType() {
        return formType;
    }

    public WidgetManager getWidgetManager() {
        return widgetManager;
    }

    public void registerErrorOnForm() {
        formReady.set(false);
        // this.wasError = true;
        enableFrameExitButton();
    }

    public UIWrapper getFormWrapper() {
        return formWrapper;
    }

    public FormAssistant getAssistant() {
        return assistant;
    }

    public IBeanWrapperFactory getWrapperFactory() {
        return assistant;
    }

    public String getWelcomeMessage() {
        String msg = welcomeMessage;
        if (welcomeMessage == null) {
            msg = PrefixUtil.getMsg("${djf.message.info.form.ok}", null);
        }
        return msg;
    }

    public boolean isOnlyOnceTime() {
        return onlyOnceTime;
    }

    public boolean isFormReady() {
        return formReady.get();
    }

    public void setFormReady(boolean formReady) {
        this.formReady.set(formReady);
    }

    public void invokeBeforeRemoveStep() {
        if (this.assistant != null) {
            try {
                assistant.step(FormStepEnum.BEFORE_REMOVE, formSession);
            } catch (Exception e) {
                SFLogger.error("Error by form removing", e);
            }
        }
    }

    public void closeForm() {

        for (UIWrapper uiw : this.widgetManager.getListUI()) {
            Object ui = uiw.getObjectUI();
            if (ui instanceof JButton) {
                closeHandler(((JButton) ui).getActionListeners());
                closeHandler(((JButton) ui).getKeyListeners());
                closeHandler(((JButton) ui).getMouseListeners());
            } else if (ui instanceof JTextField) {
                closeHandler(((JTextField) ui).getFocusListeners());
                closeHandler(((JTextField) ui).getKeyListeners());
                closeHandler(((JTextField) ui).getMouseListeners());
                // closeHandler(((JTextField) ui).getActionListeners());
                Document doc = ((JTextField) ui).getDocument();
                if (doc instanceof AbstractDocument) {
                    DocumentFilter docFilter = ((AbstractDocument)doc).getDocumentFilter();
                    if (docFilter instanceof ISFHandler) {
                        ((ISFHandler)docFilter).closeHandler();
                    }
                }
            } else if (ui instanceof SFGrid) {
                SFGrid grid = (SFGrid) ui;
                // SFGridModel model = ((SFGrid) ui).getModel();
                for (int i = 0; i < grid.getColumnCount(); i++) {
                    GridColumnInfo cInfo = grid.getGridColumnInfo(i);
                    ICellEditor cedit = cInfo.getCellEditor();
                    if (cedit != null) {
                        cedit.removeCellEditorListener();
                        JComponent comp = cedit.getComponent();
                        if (comp instanceof JTextField) {
                            closeHandler(comp
                                    .getFocusListeners());
                            closeHandler(comp.getKeyListeners());
                        } else //noinspection StatementWithEmptyBody
                            if (comp instanceof JCheckBox) {
                                // nothing to remove yet
                            } else if (comp instanceof JComboBox) {
                                // nothing to remove yet
                                closeHandler(((JComboBox) comp)
                                        .getPopupMenuListeners());
                            } else //noinspection StatementWithEmptyBody
                                if (comp instanceof JTree) {
                                    // nothing to remove yet
                                }
                    }

                }
                ((SFGrid) ui).removeSFGridListeners();
            } else if (ui instanceof JCheckBox) {
                closeHandler(((JCheckBox) ui).getItemListeners());
                closeHandler(((JCheckBox) ui).getFocusListeners());
                closeHandler(((JCheckBox) ui).getKeyListeners());
                closeHandler(((JCheckBox) ui).getActionListeners());
            } else if (ui instanceof JComboBox) {
                closeHandler(((JComboBox) ui).getFocusListeners());
                closeHandler(((JComboBox) ui).getActionListeners());
                closeHandler(((JComboBox) ui).getKeyListeners());
                closeHandler(((JComboBox) ui).getPopupMenuListeners());
            } else if (ui instanceof JLabel) {
                closeHandler(((JLabel) ui).getMouseListeners());
            } else if (ui instanceof JTextComponent) {
                closeHandler(((JTextComponent) ui).getFocusListeners());
                closeHandler(((JTextComponent) ui).getKeyListeners());
            } else if (ui instanceof SFFileChooser) {
                closeHandler(((SFFileChooser) ui).getKeyListeners());
                closeHandler(((SFFileChooser) ui).getActionListeners());
            } else if (ui instanceof SFGroup) {
                ((SFGroup) ui).closeHandler();
            } else if (ui instanceof JTabbedPane) {
                closeHandler(((JTabbedPane) ui).getChangeListeners());
            } else if (ui instanceof SFStepperPercent) {
                ((SFStepperPercent)ui).closeHandler();
            }

        }
    }

    public static void closeHandler(EventListener[] lsnr) {
        if (lsnr != null && lsnr.length > 0) {
            for (EventListener ev : lsnr) {
                if (ev instanceof ISFHandler) {
                    ISFHandler hndlr = (ISFHandler) ev;
                    hndlr.closeHandler();
                }
            }
        }
    }

    private FormManager getFormManager() {
        return formManager;
    }

    public void setFormManager(FormManager formManager) {
        this.formManager = formManager;
    }

    public boolean isFormWasChanged() {
        return formWasChanged;
    }

    public void setMapParameters(Map<String, Object> mapParameters) {
        this.mapParameters = mapParameters;
    }

    public Object getFormParameter(String parameterName) {
        Object obj = null;

        if (mapParameters != null && parameterName != null) {
            obj = mapParameters.get(parameterName);
        }

        return obj;
    }

    public BeanFormDef getBeanFormDef() {
        return beanFormDef;
    }

    public void showDialog() {
        Object uiObject = formWrapper.getObjectUI();
        if (uiObject instanceof ISFDialog) {
            ((ISFDialog) uiObject).show(null);
        }
    }

    public boolean isFormModal() {
        if (formType.isModal() != null) {
            return formType.isModal();
        }
        return false;
    }

    public Dimension getSizeModalForm() {
        if (isFormModal()) {
            if (formType.getSize() != null) {
                Integer[] pixel = new Integer[2];
                String[] percent = new String[2];

                int amount = 0;
                StringTokenizer st = new StringTokenizer(formType.getSize(),
                        ",");
                while (st.hasMoreTokens()) {
                    if (amount >= pixel.length) {
                        break;
                    }
                    //noinspection CatchMayIgnoreException
                    try {
                        String token = st.nextToken().trim();
                        if (token.endsWith("%")) {
                            percent[amount] = token;
                        } else {
                            int wd = Integer.parseInt(token);
                            pixel[amount] = wd;
                        }

                        amount++;
                    } catch (Exception e) {
                    }
                }
                if (amount == 2) {
                    if (pixel[0] == null && pixel[1] == null) {
                        if (percent[0] != null && percent[1] != null) {
                            int perWidth = 0;
                            int perHeight = 0;
                            //noinspection CatchMayIgnoreException
                            try {
                                perWidth = Integer.parseInt(percent[0]
                                        .substring(0, percent[0].length() - 1));
                                perHeight = Integer.parseInt(percent[1]
                                        .substring(0, percent[1].length() - 1));
                            } catch (Exception e) {
                            }
                            if (perWidth > 0 && perWidth <= 100
                                    && perHeight > 0 && perHeight <= 100) {
                                Rectangle rect = Djf.getConfigurator()
                                        .getFrame().getFormUISCreenBound();
                                int width = (int) (rect.getWidth() * (perWidth / 100.0));
                                int height = (int) (rect.getHeight() * (perHeight / 100.0));
                                return new Dimension(width, height);
                            }
                        }
                    } else if (pixel[0] != null && pixel[1] != null) {
                        int width = FontUtil.getIncreasedWidth(pixel[0]);
                        int height = FontUtil.getIncreasedHeight(pixel[1]);
                        return new Dimension(width, height);
                    }

                    SFLogger.warn(FormBag.class, "Size for form is incorrect");
                }
            }
        }

        return null;
    }

    public boolean isParentHasToBeRefreshed() {
        return formType.isParRefresh() != null
                && formType.isParRefresh();
    }

    public boolean isFormWasSaved() {
        return formWasSaved;
    }

    public void setFormWasSaved(boolean formWasSaved) {
        this.formWasSaved = formWasSaved;
    }

    public IFormSession getFormSession() {
        return formSession;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWelcomeMessageForParentForm() {
        return welcomeMessageForParentForm;
    }

    public void setWelcomeMessageForParentForm(String welcomeMessageForParentForm) {
        this.welcomeMessageForParentForm = welcomeMessageForParentForm;
    }

    public String getFormXml() {
        return formXml;
    }

    public boolean isCloseOnSave() {
        return closeOnSave;
    }

    public boolean isForceRefreshForParentForm() {
        return forceRefreshForParentForm;
    }

    public void setForceRefreshForParentForm(boolean forceRefreshForParentForm) {
        this.forceRefreshForParentForm = forceRefreshForParentForm;
    }

    public TaskStatusLevelEnum getTaskStatusLevelEnumForParentForm() {
        return taskStatusLevelEnumForParentForm;
    }

    public void setTaskStatusLevelEnumForParentForm(TaskStatusLevelEnum taskStatusLevelEnumForParentForm) {
        this.taskStatusLevelEnumForParentForm = taskStatusLevelEnumForParentForm;
    }
}
