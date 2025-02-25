package ru.smartflex.djf.widget.mask;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

import ru.smartflex.djf.DesktopJavaForms;
import ru.smartflex.djf.FrameHelper;
import ru.smartflex.djf.WidgetTypeEnum;
import ru.smartflex.djf.controller.FormStack;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.helper.PrefixUtil;
import ru.smartflex.djf.tool.OtherUtil;
import ru.smartflex.djf.widget.ISFHandler;
import ru.smartflex.djf.widget.ItemHandler;
import ru.smartflex.djf.widget.TaskStatusLevelEnum;

public class MaskFieldFocusHandler implements FocusListener, ISFHandler {

    private JTextField field;
    private IFieldValidator validator;
    private UIWrapper uiw;
    private WidgetManager wm;
    private String valueAsIs = null;

    public MaskFieldFocusHandler(WidgetManager wm, UIWrapper uiw,
                                 JTextField field, IFieldValidator validator) {
        this.field = field;
        this.wm = wm;
        this.uiw = uiw;

        this.validator = validator;

        field.addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e) {

        if (!wm.getFormBag().isFormReady()) {
            return;
        }

        wm.registerSelectedWrapper(uiw);

        if (uiw.getMaskInfo().getMaskDelimiter() != null) {
            ItemHandler.slideCaretFromStartToRight(field, uiw.getMaskInfo()
                    .getMaskDelimiter());
        }

        switch (uiw.getWidgetType()) {
            case PERIOD:
                String value = field.getText();
                if (!OtherUtil.isStringEmpty(value)) {
                    if (validator.isValid(value) == false) {
                        String msg = PrefixUtil.getMsg("${djf.message.warn.value_dont_match_mask}", null);
                        DesktopJavaForms.showStatusWarnMessage(msg + value.toString());
                        valueAsIs = value;
                        field.setText(uiw.getMaskInfo().getMaskDelimiter());
                    }
                }
                break;
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

        if (validator != null) {
            boolean doStepNext = true;
            // возврат некорректного значения
            if (valueAsIs != null) {
                if (field.getText().equals(uiw.getMaskInfo().getMaskDelimiter())) {
                    // ничего не ввели, влзвращаем некорректное значение
                    field.setText(valueAsIs);
                } else {
                    // что-то ввели, проверяем на валидность
                    if (validator.isValid(field.getText())) {
                        doWellness();
                    } else {
                        // влзвращаем некорректное значение
                        field.setText(valueAsIs);
                    }
                    valueAsIs = null;
                }
                FrameHelper.showStatusMessage(TaskStatusLevelEnum.OK, FormStack.getCurrentFormBag().getWelcomeMessage());
                doStepNext = false;
            }

            if (doStepNext) {
                if (!validator.isValid(field.getText())) {
                    // field.requestFocus(); because focus lost event was invoked
                    // really after lost focusing
                    // wm.drawTree(uiw);

                    Object prev = wm.getCurrentValueUsualWdiget(uiw);
                    String prevAsString = uiw.getFormattedData(prev);
                    field.setText(prevAsString);

                } else {
                    doWellness();
                }
            }
        }

        wm.doActionMethod(field.getName());
    }

    private void doWellness() {
        if (uiw.getWidgetType() == WidgetTypeEnum.DATE) {
            // for type Date, because incorrect date can be translated into another date
            Object data = uiw.getCurrentValue();
            String dataAsText = uiw.getFormattedData(data);
            field.setText(dataAsText);
        }
        wm.setValueUsualWidget(uiw);
    }

    @Override
    public void closeHandler() {
        field = null;
        wm = null;
        uiw = null;
        validator = null;
    }

}
