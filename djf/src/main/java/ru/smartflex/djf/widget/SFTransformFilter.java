package ru.smartflex.djf.widget;

import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.model.gen.AttrTransformType;
import ru.smartflex.djf.tool.OtherUtil;
import ru.smartflex.djf.widget.grid.TFCellEditor;

import javax.swing.*;
import javax.swing.text.*;

public class SFTransformFilter extends DocumentFilter implements ISFHandler {

    private JTextComponent textComponent;
    private WidgetManager wm;
    private AttrTransformType transformType;
    private TFCellEditor cellEditor = null;

    public SFTransformFilter(WidgetManager wm, JTextField field, AttrTransformType transformType) {
        this.wm = wm;
        this.textComponent = field;
        this.transformType = transformType;

        OtherUtil.setFilter(field, this);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
                        AttributeSet attrs) throws BadLocationException {

        String transformedText = OtherUtil.transformText(transformType, text);
        super.replace(fb, offset, length, transformedText, attrs);
    }

    @Override
    public void closeHandler() {
        textComponent = null;
        wm = null;
        cellEditor = null;
    }
}
