package ru.smartflex.djf.widget;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.controller.DataManager;
import ru.smartflex.djf.controller.FormStack;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.ButtonStatus;
import ru.smartflex.djf.controller.bean.FormBag;
import ru.smartflex.djf.tool.FormUtil;

public class ActionListenerCommonButton implements ActionListener, ISFHandler {

    private String typeButton;

    public ActionListenerCommonButton(String typeButton) {
        this.typeButton = typeButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (typeButton.equals(SFConstants.BUTTON_NAME_EXIT)) {
            handleExitAction();
        } else if (typeButton.equals(SFConstants.BUTTON_NAME_ADD)) {
            handleAddAction();
        } else if (typeButton.equals(SFConstants.BUTTON_NAME_REFRESH)) {
            handleRefreshAction();
        } else if (typeButton.equals(SFConstants.BUTTON_NAME_DELETE)) {
            handleDeleteAction();
        } else if (typeButton.equals(SFConstants.BUTTON_NAME_SAVE)) {
            handleSaveAction();
        }

    }

    private void handleSaveAction() {
        FormUtil.saveForm();
    }

    private void handleDeleteAction() {
        ButtonStatus bs = Djf.getConfigurator().getButtonCache()
                .getButtonStatus(SFConstants.BUTTON_NAME_DELETE);
        if (bs.isVisible() && bs.isEnable()) {
            FormBag fb = FormStack.getCurrentFormBag();
            fb.markAsDelete();
        }
    }

    private void handleRefreshAction() {
        ButtonStatus bs = Djf.getConfigurator().getButtonCache()
                .getButtonStatus(SFConstants.BUTTON_NAME_REFRESH);
        if (bs.isVisible() && bs.isEnable()) {
            FormBag fb = FormStack.getCurrentFormBag();
            fb.refeshForm();
        }
    }

    private void handleAddAction() {
        ButtonStatus bs = Djf.getConfigurator().getButtonCache()
                .getButtonStatus(SFConstants.BUTTON_NAME_ADD);
        if (bs.isVisible() && bs.isEnable()) {
            FormBag fb = FormStack.getCurrentFormBag();
            WidgetManager wm = fb.getWidgetManager();
            DataManager.createNewObject(wm);
        }
    }

    private void handleExitAction() {
        FormUtil.exitFormViaButonStatus();
    }

    @Override
    public void closeHandler() {
    }

}
