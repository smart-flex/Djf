package ru.smartflex.djf;

import ru.smartflex.djf.controller.FormStack;
import ru.smartflex.djf.controller.LongTaskManagerThread;
import ru.smartflex.djf.tool.FormUtil;
import ru.smartflex.djf.tool.FrameUtil;
import ru.smartflex.djf.widget.TaskStatusLevelEnum;

import java.awt.*;

public class Djf extends DesktopJavaForms {

    @SuppressWarnings("unused")
    public static final String DJF_VERSION = "1.6";

    private static DjfSession session = new DjfSession();

    private Djf() {
        super();
    }

    public static DjfConfigurator getConfigurator() {
        return DjfConfigurator.getInstance();
    }

    public static DjfSession getSession() {
        return session;
    }

    /**
     * Primary form run.
     */
    public static void runForm(String xml, SizeFrameEnum size) {

        if (size != null) {
            getConfigurator().getFrame().expand(size);
        }
        // do to front always
        getConfigurator().getFrame().toFront();

        runForm(xml);
    }

    public static void closeFrame() {
        getConfigurator().getFrame().closeFrame();
    }

    @SuppressWarnings("unused")
    public static void exitForm() {
        FormUtil.exitForm();
    }

    @SuppressWarnings("unused")
    public static void closeForm() {
        FormUtil.closeForm(null, false, null);
    }

    @SuppressWarnings("unused")
    public static void closeForm(String welcomeForParent) {
        FormUtil.closeForm(welcomeForParent, false, null);
    }

    public static void closeFormWarning(String warningForParent) {
        FormUtil.closeForm(warningForParent, false, TaskStatusLevelEnum.WARNING);
    }

    public static void closeFormError(String errorForParent) {
        FormUtil.closeForm(errorForParent, false, TaskStatusLevelEnum.ERROR);
    }

    @SuppressWarnings("unused")
    public static void closeForm(String welcomeForParent, boolean forceRefresh) {
        FormUtil.closeForm(welcomeForParent, forceRefresh, null);
    }

    @SuppressWarnings("unused")
    public static void exitFormViaButonStatus() {
        FormUtil.exitFormViaButonStatus();
    }

    public static void saveForm() {
        FormUtil.saveForm();
    }

    @SuppressWarnings("unused")
    public static void doLongAssistantMethod(String longMethod, String longMessage) {

        if (FormStack.getCurrentFormBag() != null
                && FormStack.getCurrentFormBag().isFormWasChanged()) {
            Djf.showStatusWarnMessage("${label.djf.message.warn.but_no_actn_allow_formwaschanged}");
            return;
        }

        FrameHelper.showWaitLongPanel(longMessage);

        LongTaskManagerThread task = new LongTaskManagerThread(longMethod);
        task.execute();
    }

    @SuppressWarnings("unused")
    public static void centerOnFrame(Component c) {
        FrameUtil.centerOnFrame(c);
    }

    @SuppressWarnings("unused")
    public static boolean isCurrentFormWasChanged() {
        boolean fok = false;

        if (FormStack.getCurrentFormBag() != null
                && FormStack.getCurrentFormBag().isFormWasChanged()) {
            fok = true;
        }

        return fok;
    }
}
