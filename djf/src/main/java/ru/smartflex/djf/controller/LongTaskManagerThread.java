package ru.smartflex.djf.controller;

import javax.swing.SwingWorker;

import ru.smartflex.djf.FrameHelper;
import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.controller.helper.PrefixUtil;
import ru.smartflex.djf.tool.FormUtil;
import ru.smartflex.djf.widget.TaskStatusLevelEnum;

public class LongTaskManagerThread extends SwingWorker<Void, Void> {

    private String method;

    public LongTaskManagerThread(String method) {
        super();
        this.method = method;
    }

    @SuppressWarnings("RedundantThrows")
    @Override
    protected Void doInBackground() throws Exception {
        WidgetManager wm = FormStack.getCurrentFormBag().getWidgetManager();
        FormUtil.runActionMethod(wm, method);
        return null;
    }

    @Override
    public void done() {
        try {
            get();
        } catch (Exception e) {
            SFLogger.error("Error by action", e);
            FrameHelper.showStatusMessage(TaskStatusLevelEnum.ERROR, PrefixUtil
                    .getMsg("${djf.message.error.form.action}", null));
        }
        FrameHelper.hideWaitLongPanel();
    }

}
