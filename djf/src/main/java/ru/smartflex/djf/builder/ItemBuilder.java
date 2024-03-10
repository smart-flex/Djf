package ru.smartflex.djf.builder;

import java.awt.*;
import java.math.BigInteger;

import javax.swing.*;

import ru.smartflex.djf.AlignTypeEnum;
import ru.smartflex.djf.Djf;
import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.WidgetTextAreaTypeEnum;
import ru.smartflex.djf.WidgetTypeEnum;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.BeanFormDef;
import ru.smartflex.djf.controller.bean.BeanFormDefProperty;
import ru.smartflex.djf.controller.bean.LabelBundle;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.exception.MissingException;
import ru.smartflex.djf.controller.helper.AccessibleHelper;
import ru.smartflex.djf.controller.helper.ObjectCreator;
import ru.smartflex.djf.controller.helper.PrefixUtil;
import ru.smartflex.djf.model.gen.*;
import ru.smartflex.djf.tool.ColorUtil;
import ru.smartflex.djf.tool.FontUtil;
import ru.smartflex.djf.tool.LocalStorage;
import ru.smartflex.djf.tool.OtherUtil;
import ru.smartflex.djf.widget.*;
import ru.smartflex.djf.widget.mask.MaskInfo;

public class ItemBuilder {

    private ItemBuilder() {
    }

    static void build(ItemGridType item, WidgetManager wm,
                      SFPanel sfPanel, BeanFormDef beanDef) {

        if (AccessibleHelper.isAccessible(item, wm)) {
            UIWrapper wrapper = new UIWrapper();

            if (item.isNoInfoColumn() != null) {
                wrapper.setGridInfoColumnAllowed(!item.isNoInfoColumn()
                );
            }

            Object ui = ObjectCreator.createSwingGrid(item, wm, wrapper,
                    beanDef, sfPanel.getBundle());
            wrapper.setOrder(item.getOrder());

            if (item.getSelectAble() != null) {
                Boolean selAble = UIWrapper.translateStringToBoolean(
                        item.getSelectAble(), false);
                if (selAble != null) {
                    wrapper.setSelectAble(selAble);
                } else {
                    // selectAble also and has binding property
                    // check it
                    if (item.getBindPref() == null) {
                        throw new MissingException(
                                "For selectable bind property: \""
                                        + item.getSelectAble()
                                        + "\" you have to fill \"bindPref\" attribute.");
                    }
                    beanDef.addSelectableProperty(item.getBindPref(),
                            item.getSelectAble());
                    wrapper.setSelectAble(true);
                }
            }

            if (item.isAppend() != null) {
                wrapper.setAppendAble(item.isAppend());
            }

            wrapper.setEnableBehavior(item.getEnabled(), false);

            // only for selectable works
            wrapper.setBeanFormDef(beanDef);
            wrapper.setBindPrefix(item.getBindPref());

            wm.registerItemUIWrapper(wrapper);

            java.awt.Container uiPanel = (Container) sfPanel.getPanel();

            uiPanel.add((Component) ui, item.getConstraint());

        }

    }

    static void build(ItemComboboxType item, WidgetManager wm,
                      java.awt.Container uiPanel, BeanFormDef beanDef, String bindPrefix) {

        // servant of two masters

        if (AccessibleHelper.isAccessible(item, wm)) {

            UIWrapper wrapper = fillComboboxBase(new UIWrapper(), item,
                    beanDef, bindPrefix);
            wrapper.setOrder(item.getOrder());
            wrapper.setupUIName(WidgetTypeEnum.COMBOBOX, item.getId());
            wrapper.setBelongToModel(item.getBelong());

            wm.registerItemUIWrapper(wrapper);

            ItemHandler.setupHandlerToFieldCombobox(wrapper, wm);
            doAdditionalComboboxBehavior(item, wrapper);

            uiPanel.add((Component) wrapper.getObjectUI(), item.getConstraint());
        }
    }

    private static void doAdditionalComboboxBehavior(ItemComboboxType item, UIWrapper wrapper) {
        ((JComboBox)wrapper.getObjectUI()).addPopupMenuListener(new SFComboBoxPopupMenuListener());
        if (item.getCols() == null || item.getCols().intValue() <= 0 || item.getCols().intValue() > 1024) {
            return;
        }
        if (item.getCols().intValue() - 1 > 0) {
            // невозможно выставить длину combobox'a по длине JtextField.setCols
//            String fillByGali = new String(new char[item.getCols().intValue() - 1]).replace('\0', 'Г');
//            ((JComboBox)wrapper.getObjectUI()).setPrototypeDisplayValue(fillByGali);
            int width = Math.round((item.getCols().intValue() + 1) * FontUtil.getIncreasedSymbolWidth()) + 3;
            Dimension d = new Dimension(width, FontUtil.getIncreasedGridRowHeight() + 1);
            ((JComboBox)wrapper.getObjectUI()).setMinimumSize(d);
            ((JComboBox)wrapper.getObjectUI()).setMaximumSize(d);
            ((JComboBox)wrapper.getObjectUI()).setPreferredSize(d);
        }
    }

    public static UIWrapper fillComboboxBase(UIWrapper wrapper,
                                             ItemComboboxBaseType item, BeanFormDef beanDef, String bindPrefix) {

        // validates
        if (item.getFill() == null) {
            SFLogger.warn(
                    ItemBuilder.class,
                    "Combobox must be filled with parameter: \"fill\"");
        }

        WidgetTypeEnum widgetType = WidgetTypeEnum.COMBOBOX;

        boolean tipsItem = true;
        if (item.isTipsItem() != null) {
            tipsItem = item.isTipsItem();
        }

        Object ui = ObjectCreator.createSwingComboBox(tipsItem);
        wrapper.setObjectUI(ui);
        wrapper.setWidgetType(widgetType);
        wrapper.setBind(item.getBind(), bindPrefix);
        wrapper.setupUIName(widgetType, null);
        wrapper.setFill(item.getFill());

        wrapper.setBeanFormDef(beanDef);
        wrapper.setEnableBehavior(item.getEnabled(), false);

        wrapper.setParentPropertyName(item.getParent());

        wrapper.setPropertySet(item.getPSet());

        return wrapper;
    }

    static void build(ItemButtonType item, WidgetManager wm,
                      SFPanel sfPanel) {

        if (AccessibleHelper.isAccessible(item, wm)) {
            UIWrapper wrapper = new UIWrapper();

            Object ui = ObjectCreator.createSwing(WidgetTypeEnum.BUTTON);
            wrapper.setObjectUI(ui);
            wrapper.setWidgetType(WidgetTypeEnum.BUTTON);

            String text = item.getText();
            if (text != null) {
                text = PrefixUtil.getMsg(text, sfPanel.getBundle());
                ((javax.swing.JButton) wrapper.getObjectUI()).setText(text);
            }

            String tips = item.getTips();
            if (tips != null) {
                tips = PrefixUtil.getMsg(tips, sfPanel.getBundle());
                ((javax.swing.JButton) wrapper.getObjectUI())
                        .setToolTipText(tips);
            }

            if (item.getIcon() != null) {
                ImageIcon play = OtherUtil.loadSFImages(item.getIcon());
                ((javax.swing.JButton) wrapper.getObjectUI()).setIcon(play);
            }

            wrapper.setupUIName(WidgetTypeEnum.BUTTON, item.getId());
            wrapper.setOrder(item.getOrder());

            wrapper.setBelongToModel(item.getBelong()); // for only focus policy
            // and status management

            String action = item.getAction();
            if (action == null) {
                action = item.getActionLong();
                if (action != null) {
                    wrapper.setActionLong(true);
                    wrapper.setActionLongMessage(item.getActionLongMsg());
                }
            }

            wrapper.setAction(action);
            if (!wrapper.isActionLong()) {
                wrapper.setFormXMLName(item.getForm());
            }

            wrapper.setEnableBehavior(item.getEnabled(), true);

            if (item.getEnabled() != null
                    && item.getEnabled().toLowerCase()
                    .equals(SFConstants.BY_CLICK)) {
                new MouseListenerButton((JButton) ui);
            }

            wm.registerItemUIWrapper(wrapper);

            ItemHandler.setupHandlerToButton((JButton) ui, wrapper, wm);

            java.awt.Container uiPanel = (Container) sfPanel.getPanel();

            uiPanel.add((Component) ui, item.getConstraint());
        }
    }

    static void build(ItemLabelType item, WidgetManager wm,
                      SFPanel sfPanel) {

        if (AccessibleHelper.isAccessible(item, wm)) {

            UIWrapper wrapper = new UIWrapper();

            Object ui = ObjectCreator.createSwing(WidgetTypeEnum.LABEL);
            wrapper.setObjectUI(ui);
            wrapper.setWidgetType(WidgetTypeEnum.LABEL);

            String text = item.getText();
            if (text != null) {
                text = PrefixUtil.getMsg(text, sfPanel.getBundle());
                ((javax.swing.JLabel) wrapper.getObjectUI()).setText(text);
            }

            wrapper.setupUIName(WidgetTypeEnum.LABEL, item.getId());
            wrapper.setOrder(null);

            setToolTipText(wrapper, item.getTips(), sfPanel.getBundle());

            wm.registerItemUIWrapper(wrapper);

            AlignTypeEnum type = getAlignType(item.getAlign(),
                    WidgetTypeEnum.DATE);
            setLabelAlign(ui, type);

            new MouseListenerLabel((JLabel) ui, wrapper, wm);
            wrapper.setMouseDoubleClickAction(item.getMdClickAction());

            java.awt.Container uiPanel = (Container) sfPanel.getPanel();

            if (item.getFont() != null) {
                ((JLabel) ui).setFont(FontUtil.getFont(item.getFont()));
            }
            Color fground = ColorUtil.getColor(item.getFground());
            if (fground != null) {
                ((JLabel) ui).setForeground(fground);
            }
            Color bground = ColorUtil.getColor(item.getBground());
            if (bground != null) {
                ((JLabel) ui).setBackground(bground);
            }

            uiPanel.add((Component) ui, item.getConstraint());

        }
    }

    static void build(ItemTextType item, WidgetManager wm,
                      SFPanel sfPanel, BeanFormDef beanDef, String bindPrefix) {

        if (AccessibleHelper.isAccessible(item, wm)) {

            UIWrapper wrapper = fillTextBase(new UIWrapper(), WidgetTypeEnum.TEXT, item, beanDef, true, bindPrefix, true);

            wrapper.setOrder(item.getOrder());
            wrapper.setAction(item.getAction());
            wrapper.setBelongToModel(item.getBelong());

            setToolTipText(wrapper, item.getTips(), sfPanel.getBundle());

            if (item.getEnabled() != null
                    && item.getEnabled().toLowerCase()
                    .equals(SFConstants.BY_CLICK)) {
                new MouseListenerText((JTextField) wrapper.getObjectUI());
            }

            wm.registerItemUIWrapper(wrapper);

            ItemHandler.setupHandlerToTextField(wrapper, wm);
            setColsAttributeForJTextField(wrapper.getObjectUI(), item.getCols());
            if (item.getTransform() != null) {
                new SFTransformFilter(wm, (JTextField)wrapper.getObjectUI(), item.getTransform());
            }

            java.awt.Container uiPanel = (Container) sfPanel.getPanel();

            uiPanel.add((Component) wrapper.getObjectUI(), item.getConstraint());
        }

    }

    static void build(ItemByteType item, WidgetManager wm,
                      SFPanel sfPanel, BeanFormDef beanDef, String bindPrefix) {

        buildIntType(item, wm, sfPanel, beanDef, WidgetTypeEnum.BYTE,
                item.getOrder(), item.getAction(),
                item.getConstraint(), bindPrefix, item.getBelong(), item.getCols());
    }

    static void build(ItemShortType item, WidgetManager wm,
                      SFPanel sfPanel, BeanFormDef beanDef, String bindPrefix) {

        buildIntType(item, wm, sfPanel, beanDef, WidgetTypeEnum.SHORT,
                item.getOrder(), item.getAction(),
                item.getConstraint(), bindPrefix, item.getBelong(), item.getCols());
    }

    static void build(ItemIntType item, WidgetManager wm,
                      SFPanel sfPanel, BeanFormDef beanDef, String bindPrefix) {

        buildIntType(item, wm, sfPanel, beanDef, WidgetTypeEnum.INT,
                item.getOrder(), item.getAction(),
                item.getConstraint(), bindPrefix, item.getBelong(), item.getCols());
    }

    static void build(ItemLongType item, WidgetManager wm,
                      SFPanel sfPanel, BeanFormDef beanDef, String bindPrefix) {

        buildIntType(item, wm, sfPanel, beanDef, WidgetTypeEnum.LONG,
                item.getOrder(), item.getAction(),
                item.getConstraint(), bindPrefix, item.getBelong(), item.getCols());
    }

    private static void buildIntType(ItemInputType item, WidgetManager wm,
                                     SFPanel sfPanel, BeanFormDef beanDef, WidgetTypeEnum wType,
                                     BigInteger order, String action, String constraint,
                                     String bindPrefix, String belong, BigInteger cols) {

        if (AccessibleHelper.isAccessible(item, wm)) {

            UIWrapper wrapper = fillTextBase(new UIWrapper(), wType, item, beanDef, false, bindPrefix, true);

            wrapper.setOrder(order);
            wrapper.setAction(action);
            wrapper.setBelongToModel(belong);

            setToolTipText(wrapper, item.getTips(), sfPanel.getBundle());
            wm.registerItemUIWrapper(wrapper);

            ItemHandler.setupHandlerToTextField(wrapper, wm);
            setColsAttributeForJTextField(wrapper.getObjectUI(), cols);

            java.awt.Container uiPanel = (Container) sfPanel.getPanel();

            uiPanel.add((Component) wrapper.getObjectUI(), constraint);
        }
    }

    static void build(ItemNumType item, WidgetManager wm,
                      SFPanel sfPanel, BeanFormDef beanDef, String bindPrefix) {

        if (AccessibleHelper.isAccessible(item, wm)) {

            UIWrapper wrapper = fillTextBase(new UIWrapper(), WidgetTypeEnum.NUMERIC, item, beanDef, false, bindPrefix,true);

            wrapper.setOrder(item.getOrder());
            wrapper.setAction(item.getAction());
            wrapper.setBelongToModel(item.getBelong());

            setToolTipText(wrapper, item.getTips(), sfPanel.getBundle());
            wm.registerItemUIWrapper(wrapper);

            ItemHandler.setupHandlerToTextField(wrapper, wm);
            setColsAttributeForJTextField(wrapper.getObjectUI(), item.getCols());

            java.awt.Container uiPanel = (Container) sfPanel.getPanel();

            uiPanel.add((Component) wrapper.getObjectUI(), item.getConstraint());

        }

    }

    private static String getWidgetId(WidgetTypeEnum widgetType, ItemInputType item) {
        String id = null;
        switch (widgetType) {
            case TEXT:
                if (item instanceof ItemTextType) {
                    id = ((ItemTextType)item).getId();
                }
                break;
            case BYTE:
                if (item instanceof ItemByteType) {
                    id = ((ItemByteType)item).getId();
                }
                break;
            case SHORT:
                if (item instanceof ItemShortType) {
                    id = ((ItemShortType)item).getId();
                }
                break;
            case INT:
                if (item instanceof ItemIntType) {
                    id = ((ItemIntType)item).getId();
                }
                break;
            case LONG:
                if (item instanceof ItemLongType) {
                    id = ((ItemLongType)item).getId();
                }
                break;
            case NUMERIC:
                if (item instanceof ItemNumType) {
                    id = ((ItemNumType)item).getId();
                }
                break;
            case PERIOD:
                if (item instanceof ItemPeriodType) {
                    id = ((ItemPeriodType)item).getId();
                }
                break;
            case DATE:
                if (item instanceof ItemDateType) {
                    id = ((ItemDateType)item).getId();
                }
                break;
            case PHONE:
                if (item instanceof ItemPhoneType) {
                    id = ((ItemPhoneType)item).getId();
                }
                break;
        }

        return id;
    }
    public static UIWrapper fillTextBase(UIWrapper wrapper,
                                         WidgetTypeEnum widgetType, ItemInputType item, BeanFormDef beanDef,
                                         boolean setupLength, String bindPrefix, boolean enabledByMouseClick) {
        Object ui = ObjectCreator.createSwing(widgetType);
        wrapper.setObjectUI(ui);
        wrapper.setWidgetType(widgetType);
        wrapper.setBind(item.getBind(), bindPrefix);
        wrapper.setupUIName(widgetType, getWidgetId(widgetType, item));
        wrapper.setBeanFormDef(beanDef);

        if (setupLength) {
            BeanFormDefProperty prop = wrapper.getBeanFormDefPropertyFromBind();
            if (prop != null && prop.getLength() != null) {
                wrapper.setLength(prop.getLength());
            }
        }

        wrapper.setEnableBehavior(item.getEnabled(), enabledByMouseClick);

        AlignTypeEnum type = getAlignType(item.getAlign(), widgetType);
        wrapper.setAlign(type);

        setTextFieldAlign(ui, type);

        return wrapper;
    }

    public static void fillTgridTreeFieldBase(UIWrapper wrapper,
                                              WidgetTypeEnum widgetType, ItemTreeGridCellType item,
                                              BeanFormDef beanDef, boolean setupLength, String bindPrefix) {
        Object ui = ObjectCreator.createSwing(widgetType);
        wrapper.setObjectUI(ui);
        wrapper.setWidgetType(widgetType);
        wrapper.setBind(item.getBind(), bindPrefix);
        wrapper.setupUIName(widgetType, null);
        wrapper.setBeanFormDef(beanDef);

        if (setupLength) {
            BeanFormDefProperty prop = wrapper.getBeanFormDefPropertyFromBind();
            if (prop != null && prop.getLength() != null) {
                wrapper.setLength(prop.getLength());
            }
        }

        wrapper.setEnableBehavior(item.getEnabled(), false);
    }

    private static void setToolTipText(UIWrapper wrapper, String text,
                                       LabelBundle bundle) {
        if (text != null) {
            text = PrefixUtil.getMsg(text, bundle);
            ((javax.swing.JComponent) wrapper.getObjectUI())
                    .setToolTipText(text);
        }
    }

    static void build(ItemPasswordType item, WidgetManager wm,
                      SFPanel sfPanel, BeanFormDef beanDef, String bindPrefix) {

        if (AccessibleHelper.isAccessible(item, wm)) {
            UIWrapper wrapper = new UIWrapper();

            Object ui = ObjectCreator.createSwing(WidgetTypeEnum.PASSWORD);

            wrapper.setObjectUI(ui);
            wrapper.setWidgetType(WidgetTypeEnum.PASSWORD);
            wrapper.setBind(item.getBind(), bindPrefix);

            wrapper.setupUIName(WidgetTypeEnum.PASSWORD, item.getId());
            wrapper.setOrder(item.getOrder());
            wrapper.setBeanFormDef(beanDef);
            wrapper.setBelongToModel(item.getBelong());

            wrapper.setEnableBehavior(item.getEnabled(), false);

            wm.registerItemUIWrapper(wrapper);

            BeanFormDefProperty prop = beanDef.getPropertyFromLinearMap(item
                    .getBind());
            if (prop != null && prop.getLength() != null) {
                wrapper.setLength(prop.getLength());
            }

            ItemHandler.setupHandlerToPasswordField(wrapper, wm);
            setColsAttributeForJTextField(wrapper.getObjectUI(), item.getCols());
            if (item.getTransform() != null) {
                SFPassword password = (SFPassword) ui;
                new SFTransformFilter(wm, password.getPasswordField(), item.getTransform());
          }

            setToolTipText(wrapper, item.getTips(), sfPanel.getBundle());
            java.awt.Container uiPanel = (Container) sfPanel.getPanel();

            uiPanel.add((Component) ui, item.getConstraint());
        }
    }

    static void build(ItemCheckboxType item, WidgetManager wm,
                      SFPanel sfPanel, BeanFormDef beanDef, String bindPrefix) {

        if (AccessibleHelper.isAccessible(item, wm)) {
            UIWrapper wrapper = fillCheckBase(new UIWrapper(), item, beanDef,
                    bindPrefix);

            String text = item.getText();
            if (text != null) {
                text = PrefixUtil.getMsg(text, sfPanel.getBundle());
                ((javax.swing.JCheckBox) wrapper.getObjectUI()).setText(text);
            }
            wrapper.setOrder(item.getOrder());
            wrapper.setupUIName(WidgetTypeEnum.CHECKBOX, item.getId());
            wrapper.setBelongToModel(item.getBelong()); // for only focus policy
            // and status management

            setToolTipText(wrapper, item.getTips(), sfPanel.getBundle());

            wrapper.setAction(item.getAction());

            wm.registerItemUIWrapper(wrapper);

            ItemHandler.setupHandlerToField(wrapper, wm);

            java.awt.Container uiPanel = (Container) sfPanel.getPanel();

            uiPanel.add((Component) wrapper.getObjectUI(), item.getConstraint());
        }
    }

    public static UIWrapper fillCheckBase(UIWrapper wrapper,
                                          ItemCheckboxBaseType item, BeanFormDef beanDef, String bindPrefix) {

        WidgetTypeEnum widgetType = WidgetTypeEnum.CHECKBOX;

        Object ui = ObjectCreator.createSwing(widgetType);
        wrapper.setObjectUI(ui);
        wrapper.setWidgetType(widgetType);
        wrapper.setBind(item.getBind(), bindPrefix);
        wrapper.setupUIName(widgetType, null);

        wrapper.setBeanFormDef(beanDef);
        wrapper.setEnableBehavior(item.getEnabled(), false);

        return wrapper;
    }

    private static AlignTypeEnum getAlignType(AttrAlignType aType,
                                              WidgetTypeEnum wType) {
        AlignTypeEnum type = null;
        if (aType != null) {
            if (aType == AttrAlignType.LEFT) {
                type = AlignTypeEnum.LEFT;
            } else if (aType == AttrAlignType.RIGHT) {
                type = AlignTypeEnum.RIGHT;
            } else if (aType == AttrAlignType.CENTER) {
                type = AlignTypeEnum.CENTER;
            }
        } else {
            type = Djf.getConfigurator().getDefaultAlign(wType);
        }
        return type;
    }

    public static UIWrapper fillPeriodBase(UIWrapper wrapper,
                                           ItemPeriodBaseType item, BeanFormDef beanDef, String bindPrefix, boolean enabledByMouseClick) {

        fillTextBase(wrapper, WidgetTypeEnum.PERIOD, item, beanDef, false, bindPrefix, enabledByMouseClick);

        String maskDelimiter = null;
        String mask = null;

        if (item instanceof ItemPeriodType) {
            maskDelimiter = Djf.getConfigurator().getMaskPeriodOnlyDelimiter();
            mask = Djf.getConfigurator().getMaskPeriod();
        } else if (item instanceof ItemGridPeriodType) {
            maskDelimiter = Djf.getConfigurator().getMaskPeriodOnlyDelimiter();
            mask = Djf.getConfigurator().getMaskPeriod();
        }
        MaskInfo maskInfo = new MaskInfo(mask, maskDelimiter,
                WidgetTypeEnum.PERIOD);
        wrapper.setMaskInfo(maskInfo);

        return wrapper;
    }

    static void build(ItemPeriodType item, WidgetManager wm,
                      SFPanel sfPanel, BeanFormDef beanDef, String bindPrefix) {

        if (AccessibleHelper.isAccessible(item, wm)) {

            UIWrapper wrapper = fillPeriodBase(new UIWrapper(), item, beanDef, bindPrefix, true);

            wrapper.setOrder(item.getOrder());
            wrapper.setAction(item.getAction());
            wrapper.setBelongToModel(item.getBelong());

            wm.registerItemUIWrapper(wrapper);

            ItemHandler.setupMaskHandlerToField(wrapper, wm);

            setToolTipText(wrapper, item.getTips(), sfPanel.getBundle());
            java.awt.Container uiPanel = (Container) sfPanel.getPanel();

            //+1 убирает проблему движения данных в TextField влево после ввода последнего символа
            ((JTextField)wrapper.getObjectUI()).setColumns(7 + 1);

            uiPanel.add((Component) wrapper.getObjectUI(), item.getConstraint());
        }

    }

    public static UIWrapper fillDateBase(UIWrapper wrapper, ItemInputType item, BeanFormDef beanDef, String bindPrefix, boolean enabledByMouseClick) {

        fillTextBase(wrapper, WidgetTypeEnum.DATE, item, beanDef, false, bindPrefix, enabledByMouseClick);

        String maskDelimiter = null;
        String mask = null;

        if (item instanceof ItemDateType) {
            maskDelimiter = Djf.getConfigurator().getMaskDateOnlyDelimiter(
                    ((ItemDateType) item).getMask());
            mask = Djf.getConfigurator().getMaskDate(
                    ((ItemDateType) item).getMask());
        } else if (item instanceof ItemGridDateType) {
            maskDelimiter = Djf.getConfigurator().getMaskDateOnlyDelimiter(
                    ((ItemGridDateType) item).getMask());
            mask = Djf.getConfigurator().getMaskDate(
                    ((ItemGridDateType) item).getMask());
        }
        MaskInfo maskInfo = new MaskInfo(mask, maskDelimiter,
                WidgetTypeEnum.DATE);
        wrapper.setMaskInfo(maskInfo);

        return wrapper;
    }

    static void build(ItemDateType item, WidgetManager wm,
                      SFPanel sfPanel, BeanFormDef beanDef, String bindPrefix) {

        if (AccessibleHelper.isAccessible(item, wm)) {

            UIWrapper wrapper = fillDateBase(new UIWrapper(), item, beanDef, bindPrefix, true);

            wrapper.setOrder(item.getOrder());
            wrapper.setAction(item.getAction());
            wrapper.setBelongToModel(item.getBelong());

            wm.registerItemUIWrapper(wrapper);

            ItemHandler.setupMaskHandlerToField(wrapper, wm);
            ((JTextField)wrapper.getObjectUI()).setColumns(10 + 1);

            setToolTipText(wrapper, item.getTips(), sfPanel.getBundle());

            java.awt.Container uiPanel = (Container) sfPanel.getPanel();

            uiPanel.add((Component) wrapper.getObjectUI(), item.getConstraint());
        }

    }

    private static void setTextFieldAlign(Object ui, AlignTypeEnum type) {
        if (type == null)
            return;

        ((JTextField) ui)
                .setHorizontalAlignment(getSwingAligmentConstant(type));
    }

    public static int getSwingAligmentConstant(AlignTypeEnum type) {
        int ret = -1;

        if (type == AlignTypeEnum.LEFT) {
            ret = javax.swing.SwingConstants.LEFT;
        } else if (type == AlignTypeEnum.RIGHT) {
            ret = javax.swing.SwingConstants.RIGHT;
        } else if (type == AlignTypeEnum.CENTER) {
            ret = javax.swing.SwingConstants.CENTER;
        }

        return ret;
    }

    private static void setLabelAlign(Object ui, AlignTypeEnum type) {
        if (type == null)
            return;

        ((JLabel) ui).setHorizontalAlignment(getSwingAligmentConstant(type));
    }

    static void build(ItemButtonRunType item, WidgetManager wm,
                      SFPanel sfPanel) {

        if (AccessibleHelper.isAccessible(item, wm)) {
            UIWrapper wrapper = new UIWrapper();

            if (item.getForm() == null && item.getAction() == null) {
                throw new MissingException(
                        "For run button attributes \"form\" or \"action\" must be filled");
            } else {
                wrapper.setFormXMLName(item.getForm());
            }

            Object ui = ObjectCreator.createSwing(WidgetTypeEnum.RUN);
            wrapper.setObjectUI(ui);
            wrapper.setWidgetType(WidgetTypeEnum.RUN);

            wrapper.setupUIName(WidgetTypeEnum.RUN, item.getId());
            wrapper.setOrder(item.getOrder());

            ImageIcon play = OtherUtil.loadSFImages("play.png");
            ((javax.swing.JButton) wrapper.getObjectUI()).setIcon(play);

            wm.registerItemUIWrapper(wrapper);

            if (item.getAction() != null) {
                wrapper.setAction(item.getAction());
                ItemHandler.setupHandlerToButton((JButton) ui, wrapper, wm);
            } else {
                ItemHandler.setupHandlerToRunButton((JButton) ui, wrapper, wm);
            }

            setToolTipText(wrapper, item.getTips(), sfPanel.getBundle());
            java.awt.Container uiPanel = (Container) sfPanel.getPanel();

            uiPanel.add((Component) ui, item.getConstraint());
        }
    }

    static void build(ItemOperatorType item, WidgetManager wm,
                      SFPanel sfPanel) {

        UIWrapper wrapper = new UIWrapper();
        Object ui = ObjectCreator.createSwing(WidgetTypeEnum.OPERATOR);
        wrapper.setObjectUI(ui);
        wrapper.setWidgetType(WidgetTypeEnum.OPERATOR);

        wrapper.setupUIName(WidgetTypeEnum.OPERATOR, null);

        wm.registerItemUIWrapper(wrapper);

        java.awt.Container uiPanel = (Container) sfPanel.getPanel();

        uiPanel.add((Component) ui, item.getConstraint());
    }

    static void build(ItemTextAreaType item, WidgetManager wm,
                      SFPanel sfPanel, BeanFormDef beanDef, String bindPrefix) {

        if (AccessibleHelper.isAccessible(item, wm)) {

            SFTextAreaPlain textAreaPlain = null;

            UIWrapper wrapper = new UIWrapper();
            ITextArea ui;
            if (item.getSyntax() != null
                    && item.getSyntax().equals(SFConstants.TEXT_VIEW_MODE_HTML)) {
                ui = ObjectCreator.createSwingTextArea(
                        WidgetTextAreaTypeEnum.HTML_VIEW, null);
            } else {
                String rSyntaxStyle = TextAreaSyntaxTransformer
                        .getRSyntaxStyleType(item.getSyntax());
                if (rSyntaxStyle == null) {
                    ui = ObjectCreator.createSwingTextArea(
                            WidgetTextAreaTypeEnum.PLAIN, null);
                    textAreaPlain = (SFTextAreaPlain)ui;
                } else {
                    ui = ObjectCreator.createSwingTextArea(
                            WidgetTextAreaTypeEnum.R_SYNTAX_TEXT_AREA,
                            rSyntaxStyle);
                }
            }
            wrapper.setObjectUI(ui);
            wrapper.setWidgetType(WidgetTypeEnum.TEXTAREA);
            wrapper.setBind(item.getBind(), bindPrefix);
            wrapper.setupUIName(WidgetTypeEnum.TEXTAREA, item.getId());
            wrapper.setBeanFormDef(beanDef);
            wrapper.setBelongToModel(item.getBelong());

            wrapper.setEnableBehavior(item.getEnabled(), false);

            wrapper.setOrder(item.getOrder());

            setToolTipText(wrapper, item.getTips(), sfPanel.getBundle());
            wm.registerItemUIWrapper(wrapper);

            BeanFormDefProperty prop = wrapper.getBeanFormDefPropertyFromBind();
            if (prop != null && prop.getLength() != null) {
                wrapper.setLength(prop.getLength());
            }

            if (ui != null) {
                new TextAreaKeyHandler(ui.getJTextComponent(), wm,
                        wrapper.getUiName());
                new TextAreaFocusHandler(ui.getJTextComponent(), wrapper, wm);

                if (wrapper.getLength() > 0) {
                    new SFLengthFilter(wm, wrapper.getLength(),
                            ui.getJTextComponent());
                }
            }

            if (textAreaPlain != null) {
                if (item.getCols() != null) {
                    textAreaPlain.setColumns(item.getCols().intValue() + 1);
                }
                if (item.getRows() != null) {
                    textAreaPlain.setRows(item.getRows().intValue());
                }
            }

            java.awt.Container uiPanel = (Container) sfPanel.getPanel();

            uiPanel.add((Component) wrapper.getObjectUI(), item.getConstraint());

        }

    }

    static void build(ItemFileType item, WidgetManager wm,
                      SFPanel sfPanel) {

        if (AccessibleHelper.isAccessible(item, wm)) {
            UIWrapper wrapper = new UIWrapper();

            Object ui = ObjectCreator.createSwing(WidgetTypeEnum.FILE);
            wrapper.setObjectUI(ui);
            wrapper.setWidgetType(WidgetTypeEnum.FILE);

            String tips = item.getTips();
            if (tips != null) {
                tips = PrefixUtil.getMsg(tips, sfPanel.getBundle());
                SFFileChooser fc = (SFFileChooser) wrapper.getObjectUI();
                fc.setToolTipText(tips);
            }

            wrapper.setupUIName(WidgetTypeEnum.FILE, item.getId());
            wrapper.setOrder(item.getOrder());

            wrapper.setBelongToModel(item.getBelong()); // for only focus policy
            // and status management

            wrapper.setEnableBehavior(item.getEnabled(), true);

            wm.registerItemUIWrapper(wrapper);
            ((SFFileChooser) wrapper.getObjectUI()).setColsAttribute(item.getCols());

            ((SFFileChooser) ui).createKeyHandler(wm);
            ((SFFileChooser) ui).setPanel((JComponent) sfPanel.getPanel());

            java.awt.Container uiPanel = (Container) sfPanel.getPanel();

            uiPanel.add((Component) ui, item.getConstraint());
        }
    }

    static void build(ItemGroupType item, WidgetManager wm,
                      SFPanel sfPanel) {

        if (AccessibleHelper.isAccessible(item, wm)) {
            UIWrapper wrapper = new UIWrapper();

            SFGroup ui = (SFGroup) ObjectCreator
                    .createSwing(WidgetTypeEnum.GROUP);
            wrapper.setObjectUI(ui);
            wrapper.setWidgetType(WidgetTypeEnum.GROUP);

            wrapper.setupUIName(WidgetTypeEnum.GROUP, item.getId());

            wm.registerItemUIWrapper(wrapper);

            for (ItemRadioType rd : item.getRadio()) {
                boolean sel = false;
                if (rd.isSelected() != null) {
                    sel = rd.isSelected();
                }
                if (sel) {
                    ui.setActionCommand(rd.getCommand());
                }
                JRadioButton btn = ui.addRadio(rd.getText(), rd.getCommand(),
                        sel);
                new ButtonKeyHandler(btn, wm);
                new ActionListenerRadioButton(ui, btn);

                UIWrapper wrp = new UIWrapper();
                wrp.setObjectUI(btn);
                wrp.setWidgetType(WidgetTypeEnum.RADIO);
                wrp.setupUIName(WidgetTypeEnum.RADIO, null);
                wrp.setOrder(rd.getOrder());
                wrp.setBelongToModel(item.getBelong());
                wm.registerItemUIWrapper(wrp);
            }

            java.awt.Container uiPanel = (Container) sfPanel.getPanel();

            uiPanel.add(ui, item.getConstraint());
        }
    }

    static void build(ItemPhoneType item, WidgetManager wm,
                      SFPanel sfPanel, BeanFormDef beanDef, String bindPrefix) {

        if (AccessibleHelper.isAccessible(item, wm)) {

            UIWrapper wrapper = fillTextBase(new UIWrapper(), WidgetTypeEnum.PHONE, item, beanDef,false, bindPrefix, true);

            wrapper.setOrder(item.getOrder());
            wrapper.setAction(item.getAction());
            wrapper.setBelongToModel(item.getBelong());

            wm.registerItemUIWrapper(wrapper);

            ItemHandler.setupHandlerToTextField(wrapper, wm);
            ((JTextField)wrapper.getObjectUI()).setColumns(16 + 1);

            setToolTipText(wrapper, item.getTips(), sfPanel.getBundle());

            java.awt.Container uiPanel = (Container) sfPanel.getPanel();

            uiPanel.add((Component) wrapper.getObjectUI(), item.getConstraint());
        }

    }

    static void build(ItemStepperPercentType item, WidgetManager wm, SFPanel sfPanel, BeanFormDef beanDef, String bindPrefix) {

        if (AccessibleHelper.isAccessible(item, wm)) {
            UIWrapper wrapper = new UIWrapper();

            SFStepperPercent ui = ObjectCreator.createStepperPercent(wm, wrapper);
            wrapper.setObjectUI(ui);
            wrapper.setWidgetType(WidgetTypeEnum.STEPPER_PERCENT);
            wrapper.setBind(item.getBind(), bindPrefix);
            wrapper.setBeanFormDef(beanDef);
            wrapper.setEditableStaticOffBehavior();

            String tips = item.getTips();
            if (tips != null) {
                tips = PrefixUtil.getMsg(tips, sfPanel.getBundle());
                ui.setTips(tips);
            }
            Boolean persist = item.isPersist();
            handleAttrPersistType(wrapper, item.getId(), persist);

            wrapper.setupUIName(WidgetTypeEnum.STEPPER_PERCENT, item.getId());
            wm.registerItemUIWrapper(wrapper);

            if (item.getDelta() != null && item.getDelta().intValue() > 0) {
                ui.setDelta(item.getDelta().intValue());
            }
            if (item.getLow() != null) {
                ui.setLow(item.getLow().intValue());
            }
            if (item.getHigh() != null) {
                ui.setHigh(item.getHigh().intValue());
            }

            java.awt.Container uiPanel = (Container) sfPanel.getPanel();
            uiPanel.add(ui, item.getConstraint());
        }
    }

    static void handleAttrPersistType(UIWrapper wrapper, String id, Boolean val) {
        if (val != null && val.booleanValue()) {
            if (id == null) {
                SFLogger.warn(ItemBuilder.class,"The id attribute must be filled for stepPercent widget");
                return;
            }
            String value = LocalStorage.getValue(id);
            if (!OtherUtil.isStringEmpty(value)) {
                wrapper.setPersistValue(value);
            }
        }
    }

    private static void setColsAttributeForJTextField(Object ui, BigInteger cols) {
        if (cols != null) {
            if (ui instanceof JTextField) {
                JTextField jtf = (JTextField) ui;
                jtf.setColumns(cols.intValue() + 1);
            } else if (ui instanceof SFPassword) {
                ((SFPassword)ui).getPasswordField().setColumns(cols.intValue() + 1);
            }
        }
    }

}
