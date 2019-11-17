package ru.smartflex.djf.widget;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

import ru.smartflex.djf.WidgetTypeEnum;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.widget.mask.ByteValidator;
import ru.smartflex.djf.widget.mask.IFieldValidator;
import ru.smartflex.djf.widget.mask.IntValidator;
import ru.smartflex.djf.widget.mask.LongValidator;
import ru.smartflex.djf.widget.mask.NumValidator;
import ru.smartflex.djf.widget.mask.ShortValidator;

/**
 * Focus handler for text field.
 */
public class FieldFocusHandler implements FocusListener, ISFHandler {

    private JTextField field;
    private IFieldValidator validator = null;
    private UIWrapper uiw;
    private WidgetManager wm;

    FieldFocusHandler(WidgetManager wm, UIWrapper uiw, JTextField field) {
        this.field = field;
        this.wm = wm;
        this.uiw = uiw;

        assignValidator();

        field.addFocusListener(this);
    }

    private void assignValidator() {
        switch (uiw.getWidgetType()) {
            case BYTE:
                validator = new ByteValidator();
                break;
            case SHORT:
                validator = new ShortValidator();
                break;
            case INT:
                validator = new IntValidator();
                break;
            case LONG:
                validator = new LongValidator();
                break;
            case NUMERIC:
                validator = new NumValidator(uiw);
                break;
        }
    }

    @Override
    public void focusGained(FocusEvent e) {

        if (wm == null || wm.getFormBag() == null) {
            // workarround: complicated issue relation to FormUtil.exitFormViaButonStatus in case when Alt-X pressing when modal window is opened
            return;
        }

        if (!wm.getFormBag().isFormReady()) {
            return;
        }

        wm.registerSelectedWrapper(uiw);
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
            if (!validator.isValid(field.getText())) {
                // field.requestFocus();

                Object prev = wm.getCurrentValueUsualWdiget(uiw);
                String prevAsString = uiw.getFormattedData(prev);
                field.setText(prevAsString);

            } else {
                if (uiw.getWidgetType() == WidgetTypeEnum.NUMERIC) {
                    Object data = uiw.getCurrentValue();
                    String dataAsText = uiw.getFormattedData(data);
                    field.setText(dataAsText);
                }
                wm.setValueUsualWidget(uiw);
            }
        } else {
            wm.setValueUsualWidget(uiw);
        }

        wm.doActionMethod(field.getName());

    }

    @Override
    public void closeHandler() {
        field = null;
        wm = null;
        uiw = null;
        validator = null;
    }

}
