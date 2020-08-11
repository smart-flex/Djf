package ru.smartflex.djf.controller.helper;

import java.awt.Color;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.EnumUtils;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.WidgetTextAreaTypeEnum;
import ru.smartflex.djf.WidgetTypeEnum;
import ru.smartflex.djf.controller.FormStack;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.BeanFormDef;
import ru.smartflex.djf.controller.bean.LabelBundle;
import ru.smartflex.djf.controller.bean.SFPair;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.exception.MissingException;
import ru.smartflex.djf.controller.exception.ObjectCreationException;
import ru.smartflex.djf.model.gen.AttrBorderType;
import ru.smartflex.djf.model.gen.ItemGridType;
import ru.smartflex.djf.model.gen.ItemTreeGridCellType;
import ru.smartflex.djf.model.gen.MethParamType;
import ru.smartflex.djf.model.gen.PanelType;
import ru.smartflex.djf.model.gen.ParameterType;
import ru.smartflex.djf.widget.ActionListenerCommonButton;
import ru.smartflex.djf.widget.IForm;
import ru.smartflex.djf.widget.IFrame;
import ru.smartflex.djf.widget.ITextArea;
import ru.smartflex.djf.widget.SFComboBox;
import ru.smartflex.djf.widget.SFDialogForm;
import ru.smartflex.djf.widget.SFFileChooser;
import ru.smartflex.djf.widget.SFGroup;
import ru.smartflex.djf.widget.SFOperator;
import ru.smartflex.djf.widget.SFPanel;
import ru.smartflex.djf.widget.SFTextAreaHTMLView;
import ru.smartflex.djf.widget.SFTextAreaPlain;
import ru.smartflex.djf.widget.SFTextAreaSyntax;
import ru.smartflex.djf.widget.grid.SFGrid;
import ru.smartflex.djf.widget.tgrid.SFTGrid;

public class ObjectCreator {

    private ObjectCreator() {
        super();
    }

    @SuppressWarnings("DuplicateBranchesInSwitch")
    public static Object createSwing(WidgetTypeEnum type) {
        Object object = null;
        switch (type) {
            case BUTTON:
            case RUN:
                object = new JButton();
                break;
            case LABEL:
                object = new JLabel();
                break;
            case TEXT:
            case DATE:
            case PERIOD:
            case BYTE:
            case SHORT:
            case INT:
            case LONG:
            case NUMERIC:
            case PHONE:
                object = new JTextField();
                break;
            case CHECKBOX:
                object = new JCheckBox();
                break;
            case TABPANEL:
                object = new JTabbedPane();
                break;
            case PASSWORD:
                object = new JPasswordField();
                break;
            case TGRID_TREE_FIELD:
                object = new JTextField();
                break;
            case OPERATOR:
                object = new SFOperator();
                break;
            case SEPARATOR:
                object = new JSeparator();
                break;
            case FILE:
                object = new SFFileChooser();
                break;
            case GROUP:
                object = new SFGroup();
                break;
        }

        return object;
    }

    public static SFComboBox createSwingComboBox(boolean tipsItem) {
        return new SFComboBox(tipsItem);
    }

    public static SFPanel createSwingPanel(PanelType panel) {
        SFPanel object;

        Border border = createBorder(panel.getBorder());

        if (panel.isScroll() != null && panel.isScroll()) {
            object = new SFPanel(true, border);
        } else {
            object = new SFPanel(false, border);
        }

        return object;
    }

    public static LabelBundle createLabelBundle(String labelBundleFileName) {
        LabelBundle lb;

        if (labelBundleFileName == null) {
            return null;
        }
        lb = new LabelBundle();
        lb.load(labelBundleFileName);

        return lb;
    }

    private static Border createBorder(AttrBorderType bType) {
        Border border = null;

        if (bType != null) {
            switch (bType) {
                case LINE:
                    border = BorderFactory.createLineBorder(Color.black);
                    break;
                case RAISED:
                    border = BorderFactory.createRaisedBevelBorder();
                    break;
            }
        }
        return border;
    }

    public static Object createSwingGrid(ItemGridType item, WidgetManager wm,
                                         UIWrapper wrapper, BeanFormDef beanDef, LabelBundle bundle) {

        List<Object> list = null;
        if (item.getCols() != null) {
            list = item.getCols().getTextOrCheckboxOrDate();
        }

        if (list == null || list.size() == 0) {
            throw new ObjectCreationException(
                    "There is no column definition for grid");
        }

        // validation
        int amountTreeCell = 0;
        for (Object object : list) {
            if (object instanceof ItemTreeGridCellType) {
                amountTreeCell++;
            }
        }
        if (amountTreeCell > 1) {
            throw new ObjectCreationException(
                    "There is too many tree column definition for grid");
        }

        if (amountTreeCell == 0) {
            SFGrid grid = new SFGrid(wm, wrapper);
            grid.setInitRow(item.getInitRow());

            wrapper.setObjectUI(grid);
            wrapper.setWidgetType(WidgetTypeEnum.GRID);
            wrapper.setupUIName(WidgetTypeEnum.GRID, item.getId());

            grid.getTable().addColumns(list, wm, wrapper, beanDef, bundle,
                    item.getBindPref());

            return grid;
        } else {
            SFTGrid tgrid = new SFTGrid(wm, wrapper);
            tgrid.setInitRow(item.getInitRow());

            wrapper.setObjectUI(tgrid);
            wrapper.setWidgetType(WidgetTypeEnum.TGRID);
            wrapper.setupUIName(WidgetTypeEnum.TGRID, item.getId());

            tgrid.getTable().addColumns(list, wm, wrapper, beanDef, bundle,
                    item.getBindPref());

            return tgrid;
        }
    }

    public static IForm createForm(Boolean modal) {
        IForm iForm;

        if (modal == null || !modal) {
            String formClassName = Djf.getConfigurator().getFormClassName();
            if (formClassName == null) {
                throw new MissingException(
                        "There is no class name for form creation");
            } else {
                iForm = (IForm) createObjectNoParameter(formClassName);
            }
        } else {
            iForm = new SFDialogForm();
        }

        return iForm;
    }

    public static IFrame createFrame() {
        IFrame iFrame;

        String frameClassName = Djf.getConfigurator().getFrameClassName();
        if (frameClassName == null) {
            throw new MissingException(
                    "There is no class name for frame creation");
        } else {
            iFrame = (IFrame) createObjectNoParameter(frameClassName);

            // exit
            JButton butExit = (JButton) UIFinder.getControlByName(iFrame,
                    SFConstants.BUTTON_NAME_EXIT);
            if (butExit == null) {
                throw new MissingException(
                        "There is no exit button with name: "
                                + SFConstants.BUTTON_NAME_EXIT);
            }
            butExit.setToolTipText(PrefixUtil.getMsg(
                    "${djf.oper_panel.exit.tips}", null));
            Djf.getConfigurator().getFrameButCache()
                    .setButton(SFConstants.BUTTON_NAME_EXIT, butExit);
            butExit.addActionListener(new ActionListenerCommonButton(
                    SFConstants.BUTTON_NAME_EXIT));

            // save
            JButton butSave = (JButton) UIFinder.getControlByName(iFrame,
                    SFConstants.BUTTON_NAME_SAVE);
            if (butSave == null) {
                throw new MissingException(
                        "There is no save button with name: "
                                + SFConstants.BUTTON_NAME_SAVE);
            }
            butSave.setToolTipText(PrefixUtil.getMsg(
                    "${djf.oper_panel.save.tips}", null));
            Djf.getConfigurator().getFrameButCache()
                    .setButton(SFConstants.BUTTON_NAME_SAVE, butSave);
            butSave.addActionListener(new ActionListenerCommonButton(
                    SFConstants.BUTTON_NAME_SAVE));

            // delete
            JButton butDelete = (JButton) UIFinder.getControlByName(iFrame,
                    SFConstants.BUTTON_NAME_DELETE);
            if (butDelete == null) {
                throw new MissingException(
                        "There is no delete button with name: "
                                + SFConstants.BUTTON_NAME_DELETE);
            }
            butDelete.setToolTipText(PrefixUtil.getMsg(
                    "${djf.oper_panel.delete.tips}", null));
            Djf.getConfigurator().getFrameButCache()
                    .setButton(SFConstants.BUTTON_NAME_DELETE, butDelete);
            butDelete.addActionListener(new ActionListenerCommonButton(
                    SFConstants.BUTTON_NAME_DELETE));

            // refresh
            JButton butRefresh = (JButton) UIFinder.getControlByName(iFrame,
                    SFConstants.BUTTON_NAME_REFRESH);
            if (butRefresh == null) {
                throw new MissingException(
                        "There is no refresh button with name: "
                                + SFConstants.BUTTON_NAME_REFRESH);
            }
            butRefresh.setToolTipText(PrefixUtil.getMsg(
                    "${djf.oper_panel.refresh.tips}", null));
            Djf.getConfigurator().getFrameButCache()
                    .setButton(SFConstants.BUTTON_NAME_REFRESH, butRefresh);
            butRefresh.addActionListener(new ActionListenerCommonButton(
                    SFConstants.BUTTON_NAME_REFRESH));

            // add
            JButton butAdd = (JButton) UIFinder.getControlByName(iFrame,
                    SFConstants.BUTTON_NAME_ADD);
            if (butAdd == null) {
                throw new MissingException("There is no add button with name: "
                        + SFConstants.BUTTON_NAME_ADD);
            }
            butAdd.setToolTipText(PrefixUtil.getMsg(
                    "${djf.oper_panel.add.tips}", null));
            Djf.getConfigurator().getFrameButCache()
                    .setButton(SFConstants.BUTTON_NAME_ADD, butAdd);
            butAdd.addActionListener(new ActionListenerCommonButton(
                    SFConstants.BUTTON_NAME_ADD));

        }

        return iFrame;
    }

    private static Object createObjectNoParameter(String className) {
        Object object;
        Object[] pars = null;
        @SuppressWarnings("rawtypes")
        Class someClazz;
        try {
            someClazz = ClassUtils.getClass(className);
        } catch (ClassNotFoundException e1) {
            String err = "Class: " + className;
            SFLogger.error(err, e1);
            throw new ObjectCreationException(err, e1);
        }
        try {
            //noinspection ConstantConditions
            object = ConstructorUtils.invokeConstructor(someClazz, pars);
        } catch (Exception e) {
            String err = "Class: " + className;
            SFLogger.error(err, e);
            throw new ObjectCreationException(err, e);
        }
        return object;
    }

    public static Object createObjectWithoutParameter(String className) {
        if (className == null) {
            return null;
        }
        return createObjectNoParameter(className);
    }

    @SuppressWarnings("rawtypes")
    public static Object[] extractObject(SFPair<Class, Object>[] pars) {
        Object[] o = null;
        if (pars != null && pars.length > 0) {
            o = new Object[pars.length];
            int ind = 0;
            for (SFPair<Class, Object> par : pars) {
                o[ind++] = par.getValue();
            }
        }
        return o;
    }

    @SuppressWarnings("rawtypes")
    public static Class[] extractClass(SFPair<Class, Object>[] pars) {
        Class[] o = null;
        if (pars != null && pars.length > 0) {
            o = new Class[pars.length];
            int ind = 0;
            for (SFPair<Class, Object> par : pars) {
                o[ind++] = par.getName();
            }
        }
        return o;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean isAccesible(MethParamType lpt, WidgetManager wm) {
        boolean fok = true;
        if (lpt.getAccessible() != null) {
            String[] infos = PrefixUtil.getFormParameterAccessible(
                    lpt.getAccessible(), wm);
            fok = Djf.getConfigurator().isAccessible(infos);
        }
        return fok;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static SFPair<Class, Object>[] createObjectParameters(
            List<MethParamType> list, String idModel, WidgetManager wm) {
        SFPair<Class, Object>[] pars = null;

        int parSize = 0;
        if (list != null && list.size() > 0) {
            // check for accessible
            for (MethParamType lpt : list) {
                if (!isAccesible(lpt, wm)) {
                    continue;
                }
                parSize++;
            }
        }

        if (list != null && list.size() > 0 && parSize > 0) {

            pars = new SFPair[parSize];
            int index = 0;
            for (MethParamType lpt : list) {

                if (!isAccesible(lpt, wm)) {
                    continue;
                }

                Object value = null;
                Class type = null;

                if (lpt.getType() == null && lpt.getClazz() != null) {
                    try {
                        type = ClassUtils.getClass(lpt.getClazz());
                        value = PrefixUtil.getParameterValue(lpt.getValue());
                    } catch (Exception e) {
                        String err = "Class: " + lpt.getClazz();
                        SFLogger.error(err, e);
                        throw new ObjectCreationException(err, e);
                    }
                } else if (lpt.getType() != null
                        && lpt.getType() == ParameterType.ENUM) {
                    try {
                        if (PrefixUtil.isValueDynamic(lpt.getValue())) {
                            value = PrefixUtil.getFormParameter(lpt.getValue(),
                                    FormStack.getCurrentFormBag()
                                            .getWidgetManager());
                            type = value.getClass();
                        } else {
                            SFPair<String, String> pair = PrefixUtil
                                    .extractEnumClassAndName(lpt.getValue());
                            Class enumClass = ClassUtils.getClass(pair
                                    .getName());
                            value = EnumUtils.getEnum(enumClass,
                                    pair.getValue());
                            type = enumClass;
                        }
                    } catch (Exception e) {
                        String err = "Class: " + lpt.getType();
                        SFLogger.error(err, e);
                        throw new ObjectCreationException(err, e);
                    }
                } else {
                    try {
                        if (lpt.getType() != null
                                && lpt.getType().value() != null) {
                            type = ClassUtils.getClass(MnemoNameConverter
                                    .convert(lpt.getType().value()));
                        }
                    } catch (ClassNotFoundException e) {
                        String err = "Class: " + lpt.getType();
                        SFLogger.error(err, e);
                        throw new ObjectCreationException(err, e);
                    }
                    try {
                        if (type != null) { // class was defined
                            if (lpt.getType()
                                    .value()
                                    .equals(MnemoNameConverter.STACK_MNEMO_DELETE)) {
                                value = wm.getObjectsToDelete(idModel);
                            } else if (lpt
                                    .getType()
                                    .value()
                                    .equals(MnemoNameConverter.STACK_MNEMO_SAVE)) {
                                value = wm.getObjectsToSave(idModel);
                            } else if (lpt
                                    .getType()
                                    .value()
                                    .equals(MnemoNameConverter.OBJECT_MNEMO_SOURCE)) {
                                value = wm.getObjectSource(idModel);
                            } else {
                                Object val = PrefixUtil.getParameterValue(
                                        lpt.getValue());

                                if (val != null) {
                                    value = ConvertUtils.convert(val, type);
                                } else {
                                    // value is empty, checks default value
                                    String defVal = lpt.getDefault();
                                    if (defVal != null) {
                                        value = ConvertUtils.convert(defVal,
                                                type);
                                    }
                                }
                            }
                        } else {
                            // the case when parameter type is wrong or equals
                            // null
                            value = PrefixUtil.getParameterValue(
                                    lpt.getValue());
                        }
                    } catch (ConversionException e) {
                        String err = "Class: " + type;
                        SFLogger.error(err, e);
                        throw new ObjectCreationException(err, e);
                    }
                }
                SFPair<Class, Object> poc = new SFPair<Class, Object>();
                poc.setValue(value);
                if (type == null) {
                    throw new MissingException(
                            "Type parameter is wrong or empty. Check it.");
                }
                poc.setName(type);
                pars[index++] = poc;
            }
        }

        return pars;
    }

    public static ITextArea createSwingTextArea(WidgetTextAreaTypeEnum type,
                                                String syntax) {
        switch (type) {
            case PLAIN:
                return new SFTextAreaPlain();
            case R_SYNTAX_TEXT_AREA:
                return new SFTextAreaSyntax(syntax);
            case HTML_VIEW:
                return new SFTextAreaHTMLView();
        }
        return null;
    }

}
