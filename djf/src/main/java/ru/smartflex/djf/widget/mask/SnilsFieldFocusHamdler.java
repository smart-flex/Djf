package ru.smartflex.djf.widget.mask;

import ru.smartflex.djf.DesktopJavaForms;
import ru.smartflex.djf.FrameHelper;
import ru.smartflex.djf.controller.FormStack;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.SnilsBag;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.helper.PrefixUtil;
import ru.smartflex.djf.controller.helper.SnilsUtil;
import ru.smartflex.djf.tool.OtherUtil;
import ru.smartflex.djf.widget.ISFHandler;
import ru.smartflex.djf.widget.TaskStatusLevelEnum;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SnilsFieldFocusHamdler implements FocusListener, ISFHandler {

    private JTextField field;
    private IFieldValidator validator;
    private UIWrapper uiw;
    private WidgetManager wm;
    private String valueAsIs = null;

    public SnilsFieldFocusHamdler(WidgetManager wm, UIWrapper uiw, JTextField field) {
        this.field = field;
        this.wm = wm;
        this.uiw = uiw;

        this.validator = new SnilsValidator();

        field.addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent focusEvent) {
        if (!wm.getFormBag().isFormReady()) {
            return;
        }

        valueAsIs = null;
        wm.registerSelectedWrapper(uiw);

        String value = field.getText();
        if (!OtherUtil.isStringEmpty(value)) {

            boolean result = validator.isValid(value);
            if (result) {
                // проверяем на формат
                SnilsBag snilsBag = SnilsUtil.formatSnils(field.getText());
                if (snilsBag != null) {
                    String snilsFormatted = snilsBag.getSnilsFormatted();
                    result = snilsFormatted.equals(value);
                } else {
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
    public void focusLost(FocusEvent focusEvent) {
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
                String snilsFormatted = null;
                boolean result = validator.isValid(field.getText());
                if (result) {
                    // совпало по правилам номера
                    SnilsBag snilsBag = SnilsUtil.formatSnils(field.getText());
                    if (snilsBag != null) {
                        snilsFormatted = snilsBag.getSnilsFormatted();
                    } else {
                        // мало ли
                        result = false;
                    }
                }
                if (result) {
                    field.setText(snilsFormatted);
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
                SnilsBag snilsBag = SnilsUtil.formatSnils(field.getText());
                if (snilsBag != null) {
                    field.setText(snilsBag.getSnilsFormatted());
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
