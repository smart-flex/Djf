package ru.smartflex.djf.widget;

import ru.smartflex.djf.controller.WidgetManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SFStepperPercentActionListener implements ActionListener, ISFHandler {

    private WidgetManager widgetManager;
    private SFStepperPercent stepper;
    private SFStepperPercent.Action action;

    public SFStepperPercentActionListener(SFStepperPercent stepper, SFStepperPercent.Action action, WidgetManager widgetManager) {
        this.stepper = stepper;
        this.action = action;
        this.widgetManager = widgetManager;
        init();
    }

    private void init() {
        switch (action) {
            case INCREMENT:
                stepper.getButtonPlus().addActionListener(this);
                break;
            case DECREMENT:
                stepper.getButtonMinus().addActionListener(this);
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (widgetManager == null) {
            return;
        }
        if (!widgetManager.getFormBag().isFormReady()) {
            return;
        }

        stepper.doStep(action);
    }

    @Override
    public void closeHandler() {
        switch (action) {
            case INCREMENT:
                stepper.getButtonPlus().removeActionListener(this);
                break;
            case DECREMENT:
                stepper.getButtonMinus().removeActionListener(this);
                break;
        }
        stepper = null;
        action = null;
        widgetManager = null;
    }
}
