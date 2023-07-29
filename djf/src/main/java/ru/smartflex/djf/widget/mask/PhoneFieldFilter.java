package ru.smartflex.djf.widget.mask;

import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.PhoneBag;
import ru.smartflex.djf.controller.helper.PhoneZoneUtil;
import ru.smartflex.djf.tool.OtherUtil;
import ru.smartflex.djf.widget.ISFHandler;
import ru.smartflex.djf.widget.grid.TFCellEditor;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class PhoneFieldFilter extends DocumentFilter implements ISFHandler {

    private JTextField field;
    private WidgetManager wm;
    private TFCellEditor cellEditor;

    public PhoneFieldFilter(WidgetManager wm, JTextField field) {
        this.field = field;
        this.wm = wm;

        OtherUtil.setFilter(field, this);
    }

    public PhoneFieldFilter(TFCellEditor cellEditor, WidgetManager wm) {
        this.field = (JTextField) cellEditor.getComponent();
        this.wm = wm;
        this.cellEditor = cellEditor;

        OtherUtil.setFilter(field, this);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
                        AttributeSet attrs) throws BadLocationException {

        // check single manual input text for digit only && first char may be +
        if (text != null && text.length() == 1) {
            if (!(Character.isDigit(text.charAt(0)) || (text.charAt(0) == SFConstants.PHONE_PLUS && offset == 0))) {
                return;
            }
        }

        if (text != null) {
            if (text.length() > 1) {
                // it is setText
                // nothing to format
                super.replace(fb, offset, length, text, attrs);
            } else {
                if (text.length() == 0) {
                    // Start cell editing, there we have to clear field
                    super.replace(fb, offset, length, text, attrs);
                    return;
                }

                boolean edited = false;
                if (offset < field.getText().length()) {
                    edited = true;
                }
                PhoneBag phoneBag = PhoneZoneUtil.formatPhoneWithZone(text, field.getText(), offset);
                super.replace(fb, 0, field.getText().length(), phoneBag.getPhoneFormatted(), attrs);
                if (edited) {
                    field.setCaretPosition(offset + 1);
                }
                if (phoneBag.isFullNumber()) {
                    if (cellEditor == null) {
                        wm.moveDown(field.getName());
                    } else {
                        cellEditor.stopAndValidate(true);
                    }
                }

            }
        } else {
            super.remove(fb, offset, length);
        }

    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        if (length <= 0) {
            return;
        }
        super.remove(fb, offset, length);
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    public void closeHandler() {
        field = null;
        wm = null;
        cellEditor = null;
    }
}
