package ru.smartflex.djf.widget.mask;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.tool.OtherUtil;
import ru.smartflex.djf.widget.ISFHandler;
import ru.smartflex.djf.widget.ItemHandler;
import ru.smartflex.djf.widget.grid.TFCellEditor;

public class MaskFieldMaskDateFilter extends DocumentFilter implements
        ISFHandler {
    private JTextField field;
    private String maskDelimiter;
    private MaskInfo maskInfo;
    private WidgetManager wm;
    private TFCellEditor cellEditor = null;
    private boolean onlyDigit;

    public MaskFieldMaskDateFilter(WidgetManager wm, MaskInfo maskInfo,
                                   JTextField field, boolean onlyDigit) {
        this.wm = wm;
        this.field = field;
        this.maskDelimiter = maskInfo.getMaskDelimiter();
        this.maskInfo = maskInfo;
        this.onlyDigit = onlyDigit;

        OtherUtil.setFilter(field, this);
    }

    public MaskFieldMaskDateFilter(WidgetManager wm, MaskInfo maskInfo,
                                   JTextField field, TFCellEditor cellEditor, boolean onlyDigit) {
        this.wm = wm;
        this.field = field;
        this.maskDelimiter = maskInfo.getMaskDelimiter();
        this.maskInfo = maskInfo;
        this.cellEditor = cellEditor;
        this.onlyDigit = onlyDigit;

        OtherUtil.setFilter(field, this);
    }

    // BTW: insertString is never called

    @SuppressWarnings("ConstantConditions")
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
                        AttributeSet attrs) throws BadLocationException {
System.out.println("*** replace 1 string: "+text+" offset "+offset+" length "+length+" attr "+attrs+" field.getText() "+field.getText());

        // this method invoking when user press F2 (user is starting to edit)
        if (text != null && field.getText() != null) {
System.out.println("*** replace 2 string: "+text+" offset "+offset+" length "+length+" attr "+attrs);
            if (text.equals(field.getText())) {
System.out.println("*** replace 3 string: "+text+" offset "+offset+" length "+length+" attr "+attrs);
                // 03-09-2017 stop replacing instead of swing wishing
                // some additional optimization
                return;
            }
        }

        // 03-09-2017 Document doc = fb.getDocument();

        if (offset == 0 && length == maskDelimiter.length()) {
System.out.println("*** replace 4 string: "+text+" offset "+offset+" length "+length+" attr "+attrs);

            // JTextField.setText invokes this case
            super.remove(fb, offset, length);
            super.insertString(fb, offset, text, attrs);
            // After setText caret position is in the right. Moves it to the
            // left. Correct?
            ItemHandler.moveCaretToStart(field, maskDelimiter);
        } else {
System.out.println("*** replace 5 string: "+text+" offset "+offset+" length "+length+" attr "+attrs);

            if (text != null && field.getText() != null) {
System.out.println("*** replace 6 string: "+text+" offset "+offset+" length "+length+" attr "+attrs);

                if ((text.length() + field.getText().length() - 1) <= maskDelimiter.length()) {
System.out.println("*** replace 7 string: "+text+" offset "+offset+" length "+length+" attr "+attrs);
                    // 03-09-2017 prevent bug with symbol increasing in masked field
                    if (ItemHandler
                            .checkIsPossibleToInsertSymbol(maskDelimiter, offset)) {

                        // check text for digit only
                        if (onlyDigit && text != null) {
                            if (!Character.isDigit(text.charAt(0))) {
                                return;
                            }
                        }
System.out.println("*** replace 8 string: "+text+" offset "+offset+" length "+length+" attr "+attrs);
                        super.remove(fb, offset, 1);
                        super.insertString(fb, offset, text, attrs);
                    }
                }
            }

            if (offset == maskInfo.getLastCaretPosition()) {
                if (cellEditor == null) {
                    wm.moveDown(field.getName());
                } else {
                    cellEditor.stopAndValidate(true);
                }
            } else {
System.out.println("*** replace 9 string: "+text+" offset "+offset+" length "+length+" attr "+attrs);

                ItemHandler.slideCaretFromStartToRight(field, maskDelimiter);
            }

        }

    }

    @Override
    public void remove(FilterBypass fb, int offset, int length)
            throws BadLocationException {
System.out.println("*** remove  offset "+offset+" length "+length);
//todo автоматическое смещение курсора по del и backspace

        if (length <= 0) {
            return;
        }
        if (ItemHandler.checkIsPossibleToInsertSymbol(maskDelimiter, offset)) {
            super.remove(fb, offset, length); // Only one symbol can be deleted
            if (length == 1) {
                super.insertString(fb, offset, ISFMaskConstants.STRING_SPACE, null);
            } else {
                // if there is more than one symbol was selected in field to delete
                StringBuilder sb = new StringBuilder(2);
                for (int i = 0; i < length; i++) {
                    sb.append(ISFMaskConstants.STRING_SPACE);
                    // todo вставлять делимитер
                }
                super.insertString(fb, offset, sb.toString(), null);
            }
            field.setCaretPosition(offset);
        }

    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void closeHandler() {
        field = null;
        wm = null;
        maskDelimiter = null;
    }
}
