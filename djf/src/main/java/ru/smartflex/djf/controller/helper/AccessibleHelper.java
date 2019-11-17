package ru.smartflex.djf.controller.helper;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.model.gen.ItemButtonRunType;
import ru.smartflex.djf.model.gen.ItemButtonType;
import ru.smartflex.djf.model.gen.ItemCheckboxBaseType;
import ru.smartflex.djf.model.gen.ItemComboboxBaseType;
import ru.smartflex.djf.model.gen.ItemFileType;
import ru.smartflex.djf.model.gen.ItemGridType;
import ru.smartflex.djf.model.gen.ItemGroupType;
import ru.smartflex.djf.model.gen.ItemInputType;
import ru.smartflex.djf.model.gen.ItemLabelType;
import ru.smartflex.djf.model.gen.ItemPasswordType;
import ru.smartflex.djf.model.gen.ItemTextAreaType;
import ru.smartflex.djf.model.gen.PanelType;
import ru.smartflex.djf.model.gen.SeparatorType;
import ru.smartflex.djf.model.gen.TabPanelType;
import ru.smartflex.djf.model.gen.TabType;

public class AccessibleHelper {

    private AccessibleHelper() {
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean isAccessible(Object item, WidgetManager wm) {

        if (item instanceof ItemInputType) {
            return isAccessible((ItemInputType) item, wm);
        } else if (item instanceof ItemPasswordType) {
            return isAccessible((ItemPasswordType) item, wm);
        } else if (item instanceof ItemLabelType) {
            return isAccessible((ItemLabelType) item, wm);
        } else if (item instanceof ItemGridType) {
            return isAccessible((ItemGridType) item, wm);
        } else if (item instanceof ItemComboboxBaseType) {
            return isAccessible((ItemComboboxBaseType) item, wm);
        } else if (item instanceof ItemCheckboxBaseType) {
            return isAccessible((ItemCheckboxBaseType) item, wm);
        } else if (item instanceof ItemButtonType) {
            return isAccessible((ItemButtonType) item, wm);
        } else if (item instanceof ItemButtonRunType) {
            return isAccessible((ItemButtonRunType) item, wm);
        } else if (item instanceof PanelType.Items) {
            return isAccessible((PanelType.Items) item, wm);
        } else if (item instanceof TabType) {
            return isAccessible((TabType) item, wm);
        } else if (item instanceof ItemTextAreaType) {
            return isAccessible((ItemTextAreaType) item, wm);
        } else if (item instanceof SeparatorType) {
            return isAccessible((SeparatorType) item, wm);
        } else if (item instanceof PanelType) {
            return isAccessible((PanelType) item, wm);
        } else if (item instanceof ItemFileType) {
            return isAccessible((ItemFileType) item, wm);
        } else if (item instanceof ItemGroupType) {
            return isAccessible((ItemGroupType) item, wm);
        } else if (item instanceof TabPanelType) {
            return isAccessible((TabPanelType) item, wm);
        } else {
            return true;
        }
    }

    private static boolean isAccessible(TabPanelType item, WidgetManager wm) {
        boolean fok = true;

        if (item.getAccessible() != null) {
            String[] infos = PrefixUtil.getFormParameterAccessible(
                    item.getAccessible(), wm);
            fok = Djf.getConfigurator().isAccessible(infos);
        }
        return fok;
    }

    private static boolean isAccessible(ItemGroupType item, WidgetManager wm) {
        boolean fok = true;

        if (item.getAccessible() != null) {
            String[] infos = PrefixUtil.getFormParameterAccessible(
                    item.getAccessible(), wm);
            fok = Djf.getConfigurator().isAccessible(infos);
        }
        return fok;
    }

    private static boolean isAccessible(ItemFileType item, WidgetManager wm) {
        boolean fok = true;

        if (item.getAccessible() != null) {
            String[] infos = PrefixUtil.getFormParameterAccessible(
                    item.getAccessible(), wm);
            fok = Djf.getConfigurator().isAccessible(infos);
        }
        return fok;
    }

    private static boolean isAccessible(PanelType item, WidgetManager wm) {
        boolean fok = true;

        if (item.getAccessible() != null) {
            String[] infos = PrefixUtil.getFormParameterAccessible(
                    item.getAccessible(), wm);
            fok = Djf.getConfigurator().isAccessible(infos);
        }
        return fok;
    }

    private static boolean isAccessible(SeparatorType item, WidgetManager wm) {
        boolean fok = true;

        if (item.getAccessible() != null) {
            String[] infos = PrefixUtil.getFormParameterAccessible(
                    item.getAccessible(), wm);
            fok = Djf.getConfigurator().isAccessible(infos);
        }
        return fok;
    }

    private static boolean isAccessible(ItemInputType item, WidgetManager wm) {
        boolean fok = true;

        if (item.getAccessible() != null) {
            String[] infos = PrefixUtil.getFormParameterAccessible(
                    item.getAccessible(), wm);
            fok = Djf.getConfigurator().isAccessible(infos);
        }
        return fok;
    }

    private static boolean isAccessible(ItemPasswordType item, WidgetManager wm) {
        boolean fok = true;

        if (item.getAccessible() != null) {
            String[] infos = PrefixUtil.getFormParameterAccessible(
                    item.getAccessible(), wm);
            fok = Djf.getConfigurator().isAccessible(infos);
        }
        return fok;
    }

    private static boolean isAccessible(ItemLabelType item, WidgetManager wm) {
        boolean fok = true;

        if (item.getAccessible() != null) {
            String[] infos = PrefixUtil.getFormParameterAccessible(
                    item.getAccessible(), wm);
            fok = Djf.getConfigurator().isAccessible(infos);
        }
        return fok;
    }

    private static boolean isAccessible(ItemGridType item, WidgetManager wm) {
        boolean fok = true;

        if (item.getAccessible() != null) {
            String[] infos = PrefixUtil.getFormParameterAccessible(
                    item.getAccessible(), wm);
            fok = Djf.getConfigurator().isAccessible(infos);
        }
        return fok;
    }

    private static boolean isAccessible(ItemComboboxBaseType item,
                                        WidgetManager wm) {
        boolean fok = true;

        if (item.getAccessible() != null) {
            String[] infos = PrefixUtil.getFormParameterAccessible(
                    item.getAccessible(), wm);
            fok = Djf.getConfigurator().isAccessible(infos);
        }
        return fok;
    }

    private static boolean isAccessible(ItemCheckboxBaseType item,
                                        WidgetManager wm) {
        boolean fok = true;

        if (item.getAccessible() != null) {
            String[] infos = PrefixUtil.getFormParameterAccessible(
                    item.getAccessible(), wm);
            fok = Djf.getConfigurator().isAccessible(infos);
        }
        return fok;
    }

    private static boolean isAccessible(ItemButtonType item, WidgetManager wm) {
        boolean fok = true;

        if (item.getAccessible() != null) {
            String[] infos = PrefixUtil.getFormParameterAccessible(
                    item.getAccessible(), wm);
            fok = Djf.getConfigurator().isAccessible(infos);
        }
        return fok;
    }

    private static boolean isAccessible(ItemButtonRunType item, WidgetManager wm) {
        boolean fok = true;

        if (item.getAccessible() != null) {
            String[] infos = PrefixUtil.getFormParameterAccessible(
                    item.getAccessible(), wm);
            fok = Djf.getConfigurator().isAccessible(infos);
        }
        return fok;
    }

    private static boolean isAccessible(PanelType.Items item, WidgetManager wm) {
        boolean fok = true;

        if (item.getAccessible() != null) {
            String[] infos = PrefixUtil.getFormParameterAccessible(
                    item.getAccessible(), wm);
            fok = Djf.getConfigurator().isAccessible(infos);
        }
        return fok;
    }

    private static boolean isAccessible(TabType tab, WidgetManager wm) {
        boolean fok = true;

        if (tab.getAccessible() != null) {
            String[] infos = PrefixUtil.getFormParameterAccessible(
                    tab.getAccessible(), wm);
            fok = Djf.getConfigurator().isAccessible(infos);
        }
        return fok;
    }

    private static boolean isAccessible(ItemTextAreaType item, WidgetManager wm) {
        boolean fok = true;

        if (item.getAccessible() != null) {
            String[] infos = PrefixUtil.getFormParameterAccessible(
                    item.getAccessible(), wm);
            fok = Djf.getConfigurator().isAccessible(infos);
        }
        return fok;
    }

}
