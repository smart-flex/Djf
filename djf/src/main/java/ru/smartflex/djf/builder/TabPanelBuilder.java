package ru.smartflex.djf.builder;

import java.util.List;

import ru.smartflex.djf.WidgetTypeEnum;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.BeanFormDef;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.helper.AccessibleHelper;
import ru.smartflex.djf.controller.helper.ObjectCreator;
import ru.smartflex.djf.controller.helper.PrefixUtil;
import ru.smartflex.djf.model.gen.PanelType;
import ru.smartflex.djf.model.gen.TabPanelType;
import ru.smartflex.djf.model.gen.TabType;

class TabPanelBuilder {

    private TabPanelBuilder() {
    }

    static UIWrapper build(TabPanelType tabPanel, WidgetManager wm, BeanFormDef beanDef) {
        UIWrapper panelWrapper = new UIWrapper();

        TabPanelType.Tabs tabs = tabPanel.getTabs();
        if (tabs != null) {
            List<TabType> tabList = tabs.getTab();
            if (tabList != null && tabList.size() > 0) {
                Object uiTabPanel = ObjectCreator
                        .createSwing(WidgetTypeEnum.TABPANEL);
                panelWrapper.setObjectUI(uiTabPanel);

                for (TabType tt : tabList) {
                    if (AccessibleHelper.isAccessible(tt, wm)) {
                        PanelType pt = tt.getPanel();
                        UIWrapper panelTab = PanelBuilder.build(pt, wm, beanDef, null);
                        if (panelTab != null) {
                            String tips = tt.getTips();
                            if (tips != null) {
                                tips = PrefixUtil.getMsg(tips, null);
                            }

                            ((javax.swing.JTabbedPane) uiTabPanel).addTab(
                                    PrefixUtil.getMsg(tt.getTitle(), null), null,
                                    panelTab.getBasePanel(), tips);
                        }
                    }
                }
            }
        }

        return panelWrapper;
    }

}
