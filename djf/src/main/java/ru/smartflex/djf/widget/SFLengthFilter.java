package ru.smartflex.djf.widget;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;

import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.widget.grid.TFCellEditor;

public class SFLengthFilter extends DocumentFilter implements ISFHandler {

    private JTextComponent textComponent;
    private WidgetManager wm;
    private int maxLength;
    private TFCellEditor cellEditor = null;

    public SFLengthFilter(WidgetManager wm, int length, JTextComponent field) {
        this.wm = wm;
        this.textComponent = field;

        maxLength = length;

        AbstractDocument doc = (AbstractDocument) field.getDocument();
        doc.setDocumentFilter(this);
    }

    SFLengthFilter(WidgetManager wm, int length, JTextField field) {
        this.wm = wm;
        this.textComponent = field;

        maxLength = length;

        AbstractDocument doc = (AbstractDocument) field.getDocument();
        doc.setDocumentFilter(this);
    }

    SFLengthFilter(WidgetManager wm, int length, JTextField field,
                   TFCellEditor cellEditor) {
        this.wm = wm;
        this.textComponent = field;
        this.cellEditor = cellEditor;

        maxLength = length;

        AbstractDocument doc = (AbstractDocument) field.getDocument();
        doc.setDocumentFilter(this);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
                        AttributeSet attrs) throws BadLocationException {

        Document doc = fb.getDocument();
        int calcLength = doc.getLength() - length;
        if (text != null) {
            calcLength += text.length();
        }
        if (calcLength <= maxLength) {
            super.replace(fb, offset, length, text, attrs);
        } else {
            if (cellEditor == null) {
                wm.moveDown(textComponent.getName());
            } else {
                cellEditor.stopAndValidate(true);
            }
        }
    }

    @Override
    public void closeHandler() {
        textComponent = null;
        wm = null;
    }

}
