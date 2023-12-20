package ru.smartflex.djf.builder;

import java.awt.Component;
import java.awt.LayoutManager;
import java.io.InputStream;
import java.util.List;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.BeanFormDef;
import ru.smartflex.djf.controller.bean.LabelBundle;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.exception.MissingException;
import ru.smartflex.djf.controller.helper.AccessibleHelper;
import ru.smartflex.djf.controller.helper.ObjectCreator;
import ru.smartflex.djf.controller.helper.PrefixUtil;
import ru.smartflex.djf.controller.helper.UnMarshalHelper;
import ru.smartflex.djf.model.gen.*;
import ru.smartflex.djf.tool.OtherUtil;
import ru.smartflex.djf.widget.SFPanel;

class PanelBuilder {

    private PanelBuilder() {
    }

    static UIWrapper build(PanelType panel, WidgetManager wm,
                           BeanFormDef beanDef, LabelBundle parentBundle) {
        UIWrapper panelWrapper = new UIWrapper();

        if (panel == null) {
            throw new MissingException("Not found panel (probably for tab)");
        }
        String includeBindPref = null;
        if (panel.getInclude() != null) {
            includeBindPref = panel.getIncludeBindPref();

            String formInclude = (String) PrefixUtil.getFormParameter(
                    panel.getInclude(), wm);
            if (formInclude == null) {
                // Form parameter was not filled. Attempts get default value
                formInclude = panel.getDefault();
            }
            if (formInclude == null) {
                throw new MissingException("No found any panel name for: "
                        + panel.getInclude());
            }
            InputStream is = OtherUtil.getBodyAsStream(Djf.getConfigurator()
                    .getPathToPanel(), formInclude);
            panel = UnMarshalHelper.unmarshalPanel(is);
        }

        if (!AccessibleHelper.isAccessible(panel, wm)) {
            return null;
        }

        SFPanel sfPanel = ObjectCreator.createSwingPanel(panel);

        Object uiPanel = sfPanel.getPanel();

        panelWrapper.setObjectUI(uiPanel);
        panelWrapper.setBasePanel(sfPanel.getBasePanel());

        UIWrapper layout = LayoutBuilder.build(panel.getLayout(), wm);
        if (layout.getObjectUI() != null) {
            ((java.awt.Container) uiPanel).setLayout((LayoutManager) layout
                    .getObjectUI());
        }

        if (parentBundle == null) {
            parentBundle = ObjectCreator.createLabelBundle(panel.getLabel());
        }
        sfPanel.setBundle(parentBundle);

        // items handling

        wm.startPanel();

        PanelType.Items items = panel.getItems();
        if (items != null) {
            String bindPref = items.getBindPref();
            if (includeBindPref != null) {
                bindPref = includeBindPref;
            }
            if (AccessibleHelper.isAccessible(items, wm)) {
                List<Object> itemList = items.getGridOrDateOrButton();
                if (itemList != null && itemList.size() > 0) {
                    for (Object it : itemList) {
                        if (it instanceof ItemButtonType) {
                            ItemBuilder.build((ItemButtonType) it, wm, sfPanel);
                        } else if (it instanceof ItemComboboxType) {
                            ItemBuilder.build((ItemComboboxType) it, wm,
                                    (java.awt.Container) uiPanel, beanDef,
                                    bindPref);
                        } else if (it instanceof ItemLabelType) {
                            ItemBuilder.build((ItemLabelType) it, wm, sfPanel);

                        } else if (it instanceof ItemTextType) {
                            ItemBuilder.build((ItemTextType) it, wm, sfPanel,
                                    beanDef, bindPref);
                        } else if (it instanceof ItemCheckboxType) {
                            ItemBuilder.build((ItemCheckboxType) it, wm,
                                    sfPanel, beanDef, bindPref);
                        } else if (it instanceof ItemGridType) {
                            ItemBuilder.build((ItemGridType) it, wm, sfPanel,
                                    beanDef);
                        } else if (it instanceof ItemDateType) {
                            ItemBuilder.build((ItemDateType) it, wm, sfPanel,
                                    beanDef, bindPref);
                        } else if (it instanceof ItemPasswordType) {
                            ItemBuilder.build((ItemPasswordType) it, wm,
                                    sfPanel, beanDef, bindPref);
                        } else if (it instanceof ItemButtonRunType) {
                            ItemBuilder.build((ItemButtonRunType) it, wm,
                                    sfPanel);
                        } else if (it instanceof ItemPeriodType) {
                            ItemBuilder.build((ItemPeriodType) it, wm, sfPanel,
                                    beanDef, bindPref);
                        } else if (it instanceof ItemByteType) {
                            ItemBuilder.build((ItemByteType) it, wm, sfPanel,
                                    beanDef, bindPref);
                        } else if (it instanceof ItemShortType) {
                            ItemBuilder.build((ItemShortType) it, wm, sfPanel,
                                    beanDef, bindPref);
                        } else if (it instanceof ItemIntType) {
                            ItemBuilder.build((ItemIntType) it, wm, sfPanel,
                                    beanDef, bindPref);
                        } else if (it instanceof ItemLongType) {
                            ItemBuilder.build((ItemLongType) it, wm, sfPanel,
                                    beanDef, bindPref);
                        } else if (it instanceof ItemNumType) {
                            ItemBuilder.build((ItemNumType) it, wm, sfPanel,
                                    beanDef, bindPref);
                        } else if (it instanceof ItemOperatorType) {
                            ItemBuilder.build((ItemOperatorType) it, wm,
                                    sfPanel);
                        } else if (it instanceof SeparatorType) {
                            WidgetBuilder.build((SeparatorType) it, sfPanel, wm);
                        } else if (it instanceof ItemTextAreaType) {
                            ItemBuilder.build((ItemTextAreaType) it, wm,
                                    sfPanel, beanDef, bindPref);
                        } else if (it instanceof ItemFileType) {
                            ItemBuilder.build((ItemFileType) it, wm, sfPanel);
                        } else if (it instanceof ItemGroupType) {
                            ItemBuilder.build((ItemGroupType) it, wm, sfPanel);
                        } else if (it instanceof ItemPhoneType) {
                            ItemBuilder.build((ItemPhoneType) it, wm, sfPanel,
                                    beanDef, bindPref);
                        } else if (it instanceof ItemStepperPercentType) {
                            ItemBuilder.build((ItemStepperPercentType) it, wm, sfPanel, beanDef, bindPref);
                        }
                    }
                }
            }
        }

        wm.endPanel();

        List<PanelType> panelList = panel.getPanel();

        buildPanels(uiPanel, panelList, wm, beanDef, parentBundle);

        List<TabPanelType> tptList = panel.getTabpanel();
        if (tptList != null && tptList.size() > 0) {
            for (TabPanelType tpt : tptList) {
                if (AccessibleHelper.isAccessible(tpt, wm)) {
                    UIWrapper tabPanel = TabPanelBuilder.build(tpt, wm, beanDef);

                    ((java.awt.Container) uiPanel)
                            .add((Component) tabPanel.getObjectUI(),
                                    tpt.getConstraint());
                }
            }
        }
        return panelWrapper;
    }

    static void buildPanels(Object uiParent, List<PanelType> panelList,
                            WidgetManager wm, BeanFormDef beanDef, LabelBundle parentBundle) {
        if (panelList != null && panelList.size() > 0) {
            for (PanelType pt : panelList) {
                UIWrapper panelWrapperChild = PanelBuilder.build(pt, wm,
                        beanDef, parentBundle);
                if (panelWrapperChild != null) {
                    ((java.awt.Container) uiParent).add(
                            panelWrapperChild.getBasePanel(),
                            pt.getConstraint());
                }
            }
        }
    }
}
