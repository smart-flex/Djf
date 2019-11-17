package ru.smartflex.djf.controller;

import javax.swing.SwingWorker;

import ru.smartflex.djf.FrameHelper;
import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.helper.PrefixUtil;
import ru.smartflex.djf.widget.TaskStatusLevelEnum;


public class ActionManagerThread extends SwingWorker<Void, Void> {

    private WidgetManager wm;
    private UIWrapper uiw;

    ActionManagerThread(WidgetManager wm, UIWrapper uiw) {
        super();
        this.wm = wm;
        this.uiw = uiw;
    }

    @SuppressWarnings("RedundantThrows")
    @Override
    protected Void doInBackground() throws Exception {
        ActionManager.doActionInt(wm, uiw);
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
