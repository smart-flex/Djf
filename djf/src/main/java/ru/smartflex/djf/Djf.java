package ru.smartflex.djf;

import ru.smartflex.djf.controller.FormStack;
import ru.smartflex.djf.controller.LongTaskManagerThread;
import ru.smartflex.djf.tool.FormUtil;

public class Djf extends DesktopJavaForms {

    @SuppressWarnings("unused")
    public static final String DJF_VERSION = "1.0";

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
        FormUtil.closeForm();
    }

    @SuppressWarnings("unused")
    public static void closeForm(String welcomeForParent) {
        FormUtil.closeForm(welcomeForParent);
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
}
