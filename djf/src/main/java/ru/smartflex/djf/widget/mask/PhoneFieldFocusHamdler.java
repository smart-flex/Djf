package ru.smartflex.djf.widget.mask;

import ru.smartflex.djf.DesktopJavaForms;
import ru.smartflex.djf.FrameHelper;
import ru.smartflex.djf.controller.FormStack;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.PhoneBag;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.helper.PhoneZoneUtil;
import ru.smartflex.djf.controller.helper.PrefixUtil;
import ru.smartflex.djf.tool.OtherUtil;
import ru.smartflex.djf.widget.ISFHandler;
import ru.smartflex.djf.widget.TaskStatusLevelEnum;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class PhoneFieldFocusHamdler implements FocusListener, ISFHandler {

    private JTextField field;
    private IFieldValidator validator;
    private UIWrapper uiw;
    private WidgetManager wm;
    private String valueAsIs = null;

    public PhoneFieldFocusHamdler(WidgetManager wm, UIWrapper uiw,  JTextField field) {
        this.field = field;
        this.wm = wm;
        this.uiw = uiw;

        this.validator = new PhoneValidator();

        field.addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e) {

        if (!wm.getFormBag().isFormReady()) {
            return;
        }

        wm.registerSelectedWrapper(uiw);

        String value = field.getText();
        if (!OtherUtil.isStringEmpty(value)) {

            boolean result = validator.isValid(value);
            if (result) {
                // совпало по правилам номера
                PhoneBag phoneBag = PhoneZoneUtil.formatPhoneWithZone(value);
                if (phoneBag != null) {
                    String phoneFormatted = phoneBag.getPhoneFormatted();
                    result = phoneFormatted.equals(value);
                } else {
                    // мало ли
                    result = false;
                }
            }
            if (!result) {
                String msg = PrefixUtil.getMsg("${djf.message.warn.value_dont_match_mask}", null);
                DesktopJavaForms.showStatusWarnMessage(msg + value.toString());
                valueAsIs = value;
                field.setText("");
            }
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (wm == null || wm.getFormBag() == null) {
            // workarround for SFDialogForm.closeDialog() ->dispose();
            return;
        }

        if (!wm.getFormBag().isFormReady()) {
            return;
        }

        boolean doStepNext = true;
        if (valueAsIs != null) {
            if (field.getText().trim().length() == 0) {
                // ничего не ввели, возвращаем некорректное значение
                field.setText(valueAsIs);
            } else {
                // что то ввели
                String phoneFormatted = null;
                boolean result = validator.isValid(field.getText());
                if (result) {
                    // совпало по правилам номера
                    PhoneBag phoneBag = PhoneZoneUtil.formatPhoneWithZone(field.getText());
                    if (phoneBag != null) {
                        phoneFormatted = phoneBag.getPhoneFormatted();
                    } else {
                        // мало ли
                        result = false;
                    }
                }
                if (result) {
                    field.setText(phoneFormatted);
                } else {
                    field.setText(valueAsIs);
                }
            }
            FrameHelper.showStatusMessage(TaskStatusLevelEnum.OK, FormStack.getCurrentFormBag().getWelcomeMessage());
            valueAsIs = null;
            doStepNext = false;
            wm.setValueUsualWidget(uiw);
        }

        if (doStepNext) {
            if (!validator.isValid(field.getText())) {
                Object prev = wm.getCurrentValueUsualWdiget(uiw);
                String prevAsString = uiw.getFormattedData(prev);
                field.setText(prevAsString);
            } else {
                PhoneBag phoneBag = PhoneZoneUtil.formatPhoneWithZone(field.getText());
                if (phoneBag != null) {
                    field.setText(phoneBag.getPhoneFormatted());
                }
                wm.setValueUsualWidget(uiw);
            }
        }

        wm.doActionMethod(field.getName());
    }

    @Override
    public void closeHandler() {
        field = null;
        wm = null;
        uiw = null;
        validator = null;
        valueAsIs = null;
    }

}

