package ru.smartflex.djf.widget.mask;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

import ru.smartflex.djf.WidgetTypeEnum;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.widget.ISFHandler;
import ru.smartflex.djf.widget.ItemHandler;

public class MaskFieldFocusHandler implements FocusListener, ISFHandler {

    private JTextField field;
    private IFieldValidator validator;
    private UIWrapper uiw;
    private WidgetManager wm;

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

    }

    @Override
    public void focusLost(FocusEvent e) {

        if (!wm.getFormBag().isFormReady()) {
            return;
        }

        if (validator != null) {
            if (!validator.isValid(field.getText())) {
                // field.requestFocus(); because focus lost event was invoked
                // really after lost focusing
                // wm.drawTree(uiw);

                Object prev = wm.getCurrentValueUsualWdiget(uiw);
                String prevAsString = uiw.getFormattedData(prev);
                field.setText(prevAsString);

            } else {
                if (uiw.getWidgetType() == WidgetTypeEnum.DATE) {
                    // for type Date, because incorrect date can be translated into another date
                    Object data = uiw.getCurrentValue();
                    String dataAsText = uiw.getFormattedData(data);
                    field.setText(dataAsText);
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
    }

}
