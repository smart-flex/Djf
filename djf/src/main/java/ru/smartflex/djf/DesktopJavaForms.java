package ru.smartflex.djf;

import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

import ru.smartflex.djf.controller.FormStack;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.FormBag;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.bean.tree.BeanStatusEnum;
import ru.smartflex.djf.controller.bean.tree.IBeanWrapper;
import ru.smartflex.djf.controller.bean.tree.WidgetTreeNode;
import ru.smartflex.djf.controller.exception.MissingException;
import ru.smartflex.djf.controller.helper.PrefixUtil;
import ru.smartflex.djf.controller.helper.UIFinder;
import ru.smartflex.djf.tool.FontUtil;
import ru.smartflex.djf.widget.ISFDialog;
import ru.smartflex.djf.widget.ISFDialogHTML;
import ru.smartflex.djf.widget.SFDialogHTML;
import ru.smartflex.djf.widget.SFDialogPlain;
import ru.smartflex.djf.widget.SFGroup;
import ru.smartflex.djf.widget.TaskStatusLevelEnum;

public class DesktopJavaForms {

    DesktopJavaForms() {
    }

    public static void runForm(String xml) {
        FormStack.createForm(xml, null);
    }

    public static void runForm(String xml, Map<String, Object> mapParameters) {
        FormStack.createForm(xml, mapParameters);
    }

    public static Object getCurrentObject() {
        return getCurrentObject(null);
    }

    /**
     * Method is applicable for forms with many models.
     */
    public static Object getCurrentObject(String treePath) {
        Object obj = null;
        FormBag fb = FormStack.getCurrentFormBag();
        if (fb != null) {
            if (fb.getWidgetManager().getFormBag().isFormReady()) {
                IBeanWrapper bw;
                if (treePath == null) {
                    bw = fb.getWidgetManager().getSelectedBeanWrapper();
                } else {
                    WidgetTreeNode<UIWrapper> wtn = fb.getWidgetManager()
                            .getBeanWrapper(treePath);
                    bw = fb.getWidgetManager().getSelectedBeanWrapper(
                            wtn.getWidget());
                }
                BeanStatusEnum status = bw.getCreatedStatus();
                if (status != BeanStatusEnum.NEW) {
                    obj = bw.getData();
                }
            }
        }
        return obj;
    }

    public static void refreshFormForce() {
        FormBag fb = FormStack.getCurrentFormBag();
        fb.refeshForm();
    }

    public static void showStatusErrorMessage(String msg) {
        FrameHelper.showStatusMessage(TaskStatusLevelEnum.ERROR,
                PrefixUtil.getMsg(msg, null));
    }

    @SuppressWarnings("unused")
    public static void showStatusErrorMessageByLink(String link) {
        FrameHelper.showStatusMessage(TaskStatusLevelEnum.ERROR,
                PrefixUtil.getMsg("${" + link + "}", null));
    }

    public static void showStatusWarnMessage(String msg) {
        FrameHelper.showStatusMessage(TaskStatusLevelEnum.WARNING,
                PrefixUtil.getMsg(msg, null));
    }

    public static void showStatusInfoMessage(String msg) {
        FrameHelper.showStatusMessage(TaskStatusLevelEnum.OK,
                PrefixUtil.getMsg(msg, null));
    }

    @SuppressWarnings("unused")
    public static void showStatusInfoMessageByLink(String link) {
        FrameHelper.showStatusMessage(TaskStatusLevelEnum.OK,
                PrefixUtil.getMsg("${" + link + "}", null));
    }

    @SuppressWarnings("unused")
    public static ISFDialogHTML createSFDialogHTML(String title) {
        return new SFDialogHTML(title);
    }

    @SuppressWarnings("unused")
    public static ISFDialog createSFDialog(String title) {
        return new SFDialogPlain(title);
    }

    @SuppressWarnings("unused")
    public static void requestFocusOnWidget(String idWidget) {
        JComponent comp = findComponent(idWidget);
        comp.requestFocus();
    }

    @SuppressWarnings("unused")
    public static JComponent getWidget(String idWidget) {
        return findComponent(idWidget);
    }

    @SuppressWarnings("unused")
    public static void enableWidget(String idWidget) {
        JComponent comp = findComponent(idWidget);
        comp.setEnabled(true);
    }

    @SuppressWarnings("unused")
    public static void disableWidget(String idWidget) {
        JComponent comp = findComponent(idWidget);
        comp.setEnabled(false);
    }

    @SuppressWarnings("unused")
    public static boolean getCheckBoxStatus(String idWidget) {
        JComponent comp = findComponent(idWidget);
        if (comp instanceof JCheckBox) {
            return ((JCheckBox) comp).isSelected();
        } else {
            throw new SmartFlexException("This widget is not checkBox: \""
                    + idWidget + "\"");
        }
    }

    @SuppressWarnings("unused")
    public static void refreshWidget(String idWidget) {
        WidgetManager wm = FormStack.getCurrentFormBag().getWidgetManager();
        UIWrapper wrp = wm.getUIWrapper(idWidget);
        wm.refreshWidget(wrp);
    }

    private static JComponent findComponent(String idWidget) {
        JComponent comp = UIFinder.getControlByName(FormStack
                .getCurrentFormBag().getFormWrapper().getObjectUI(), idWidget);
        if (comp == null) {
            throw new MissingException(
                    "There is not existed component with id: \"" + idWidget
                            + "\"");
        }
        return comp;
    }

    @SuppressWarnings("unused")
    public static Object getValueBasedOnWidget(String idWidget, String val) {
        WidgetManager wm = FormStack.getCurrentFormBag().getWidgetManager();
        UIWrapper wrp = wm.getUIWrapper(idWidget);

        try {
            return wrp.getValueFromText(val);
        } catch (Exception e) {
            SFLogger.error("Parsing error: " + val, e);
            return null;
        }
    }

    @SuppressWarnings("unused")
    public static void setValueToWidget(String idWidget, Object val) {
        WidgetManager wm = FormStack.getCurrentFormBag().getWidgetManager();
        UIWrapper wrp = wm.getUIWrapper(idWidget);
        if (wrp == null) {
            throw new MissingException("There is not widget with id: \""
                    + idWidget + "\"");
        }
        String text = wrp.getFormattedData(val);
        Object ui = wrp.getObjectUI();
        if (ui instanceof JTextComponent) {
            ((JTextComponent) ui).setText(text);
        } else {
            throw new SmartFlexException("Ui widget is not text coomponent");
        }
    }

    @SuppressWarnings("unused")
    public static Object getValueFromWidget(String idWidget) {
        Object obj;
        WidgetManager wm = FormStack.getCurrentFormBag().getWidgetManager();
        UIWrapper wrp = wm.getUIWrapper(idWidget);
        if (wrp == null) {
            throw new MissingException("There is not widget with id: \""
                    + idWidget + "\"");
        }
        if (wrp.getWidgetType() == WidgetTypeEnum.GROUP) {
            SFGroup group = (SFGroup) wrp.getObjectUI();
            obj = group.getActionCommand();
        } else {
            obj = wrp.getCurrentValue();
        }

        return obj;
    }

    @SuppressWarnings("unused")
    public static String getFormattedDataBasedOnWidget(String idWidget,
                                                       Object obj) {
        WidgetManager wm = FormStack.getCurrentFormBag().getWidgetManager();
        UIWrapper wrp = wm.getUIWrapper(idWidget);
        return wrp.getFormattedData(obj);
    }

    @SuppressWarnings("unused")
    public static void setFrameMinimumSize(int width, int height) {
        DjfConfigurator.getInstance().getFrame().setMinimumSize(width, height);
    }

    public static int getIncreasedFontSize(int size) {
        return FontUtil.getIncreasedFontSize(size);
    }

}
