package ru.smartflex.djf.widget;

import javax.swing.*;
import javax.swing.table.TableColumn;

import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.WidgetTypeEnum;
import ru.smartflex.djf.builder.ItemBuilder;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.BeanFormDefProperty;
import ru.smartflex.djf.controller.bean.GridColumnInfo;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.widget.grid.GridCellFocusListener;
import ru.smartflex.djf.widget.grid.GridCellListener;
import ru.smartflex.djf.widget.grid.TFCellEditor;
import ru.smartflex.djf.widget.mask.*;

public class ItemHandler {

    private ItemHandler() {
    }

    public static void setupMaskHandlerToField(UIWrapper uiw, WidgetManager wm) {

        JTextField field = (JTextField) uiw.getObjectUI();

        switch (uiw.getWidgetType()) {
            case DATE:
                validateMask(uiw.getMaskInfo());

                field.setText(uiw.getMaskInfo().getMaskDelimiter());

                new MaskFieldKeyHandler(field,
                        uiw.getMaskInfo().getMaskDelimiter(), wm, uiw);
                new MaskFieldMaskDateFilter(wm, uiw.getMaskInfo(), field, false);
                new MaskFieldFocusHandler(wm, uiw, field, new DateValidator(
                        uiw.getMaskInfo()));
                new MaskFieldMouseHandler(field, uiw.getMaskInfo()
                        .getMaskDelimiter());

                break;
            case PERIOD:
                validateMask(uiw.getMaskInfo());

                field.setText(uiw.getMaskInfo().getMaskDelimiter());

                new MaskFieldKeyHandler(field,
                        uiw.getMaskInfo().getMaskDelimiter(), wm, uiw);
                new MaskFieldMaskDateFilter(wm, uiw.getMaskInfo(), field, true);
                new MaskFieldFocusHandler(wm, uiw, field, new PeriodValidator(
                        uiw.getMaskInfo()));
                new MaskFieldMouseHandler(field, uiw.getMaskInfo()
                        .getMaskDelimiter());

                break;
            default:
                throw new SmartFlexMaskException("No handlers for this type: "
                        + uiw.getWidgetType());
        }
        BeanFormDefProperty prop = uiw.getBeanFormDefPropertyFromBind();
        if (prop != null && prop.getNotNull() != null
                && prop.getNotNull()) {
            field.setBackground(SFConstants.FIELD_REQUIRED_BACKGROUND_COLOR);
        }
        if (uiw.getWidgetType() == WidgetTypeEnum.DATE || uiw.getWidgetType() == WidgetTypeEnum.PERIOD) {
            ItemHandler.moveCaretToStart(field, uiw.getMaskInfo()
                    .getMaskDelimiter());
        }
    }

    public static void setupHandlerToColumn(TableColumn tableColumn,
                                            WidgetManager wm, UIWrapper uiw, GridColumnInfo colInfo) {

        switch (colInfo.getWidgetType()) {
            case DATE:
            case PERIOD:
            case BYTE:
            case SHORT:
            case INT:
            case LONG:
            case NUMERIC:
            case PHONE:
                setupHandlerToColumnWithValidator(tableColumn, wm, uiw, colInfo);
                break;
            default:
                throw new SmartFlexMaskException("No handlers for this type: "
                        + colInfo.getWidgetType());
        }
    }

    private static void setupHandlerToColumnWithValidator(TableColumn tableColumn,
                                                          WidgetManager wm, UIWrapper uiw, GridColumnInfo colInfo) {

        if (colInfo.getWidgetType() == WidgetTypeEnum.PERIOD
                || colInfo.getWidgetType() == WidgetTypeEnum.DATE) {
            validateMask(colInfo.getMaskInfo());
        }

        JTextField field = (JTextField) colInfo.getObjectUI();

        TFCellEditor cellEditor = new TFCellEditor(field, uiw, wm, colInfo);
        new GridCellListener(cellEditor);

        if (colInfo.getWidgetType() == WidgetTypeEnum.PERIOD
                || colInfo.getWidgetType() == WidgetTypeEnum.DATE) {
            field.setText(colInfo.getMaskInfo().getMaskDelimiter());
        }

        field.setHorizontalAlignment(ItemBuilder
                .getSwingAligmentConstant(colInfo.getAlign()));

        boolean onlyDigit = false;
        if (colInfo.getWidgetType() == WidgetTypeEnum.PERIOD) {
            onlyDigit = true;
        }

        if (colInfo.getWidgetType() == WidgetTypeEnum.PERIOD
                || colInfo.getWidgetType() == WidgetTypeEnum.DATE) {

            new MaskFieldKeyHandler(field, colInfo.getMaskInfo()
                    .getMaskDelimiter(), wm, uiw, cellEditor);
            new MaskFieldMaskDateFilter(wm, colInfo.getMaskInfo(), field,
                    cellEditor, onlyDigit);
        } else if (colInfo.getWidgetType() == WidgetTypeEnum.PHONE) {
            new PhoneFieldFilter(cellEditor, wm);
        }

        IFieldValidator validator = null;

        switch (colInfo.getWidgetType()) {
            case DATE:
                validator = new DateValidator(colInfo.getMaskInfo());
                break;
            case PERIOD:
                validator = new PeriodValidator(colInfo.getMaskInfo());
                break;
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
                validator = new NumValidator(colInfo);
                break;
            case PHONE:
                validator = new PhoneValidator();
                break;
        }
        cellEditor.setValidator(validator);

        colInfo.setCellEditor(cellEditor);

        tableColumn.setCellEditor(cellEditor);

        // workaround: because when user start editing by press F2, for empty
        // value there field caret position is on right side
        if (colInfo.getWidgetType() == WidgetTypeEnum.PERIOD
                || colInfo.getWidgetType() == WidgetTypeEnum.DATE) {
            ItemHandler.moveCaretToStart(field, colInfo.getMaskInfo()
                    .getMaskDelimiter());
        }
    }

    public static void setupHandlerToColumn(TableColumn tableColumn,
                                            WidgetTypeEnum typeHandler, WidgetManager wm, UIWrapper uiw,
                                            GridColumnInfo colInfo, BeanFormDefProperty prop, JTable table) {

        switch (typeHandler) {
            case CHECKBOX:
                JCheckBox checkBox = (JCheckBox) colInfo.getObjectUI();
                checkBox.setHorizontalAlignment(SwingConstants.CENTER);

                if (prop != null && prop.getNotNull() != null
                        && prop.getNotNull()) {
                    checkBox.setBackground(SFConstants.FIELD_REQUIRED_BACKGROUND_COLOR);
                }

                TFCellEditor cellEditorCheck = new TFCellEditor(checkBox, uiw, wm,
                        colInfo, table);
                new GridCellListener(cellEditorCheck);
                colInfo.setCellEditor(cellEditorCheck);

                tableColumn.setCellEditor(cellEditorCheck);

                break;
            case COMBOBOX:
                SFComboBox box = (SFComboBox) colInfo.getObjectUI();

                if (prop != null && prop.getNotNull() != null
                        && prop.getNotNull()) {
                    box.setBackground(SFConstants.FIELD_REQUIRED_BACKGROUND_COLOR);
                }

                TFCellEditor cellEditorBox = new TFCellEditor(box, uiw, wm,
                        colInfo, table);
                new GridCellListener(cellEditorBox);
                colInfo.setCellEditor(cellEditorBox);

                tableColumn.setCellEditor(cellEditorBox);

                break;
            case TEXT:
            case TGRID_TREE_FIELD:
                setupHandlerToColumnText(tableColumn, wm, uiw, colInfo, prop);
                break;
        }
    }

    private static void setupHandlerToColumnText(TableColumn tableColumn,
                                                 WidgetManager wm, UIWrapper uiw, GridColumnInfo colInfo,
                                                 BeanFormDefProperty prop) {
        JTextField field = (JTextField) colInfo.getObjectUI();

        if (prop != null && prop.getNotNull() != null
                && prop.getNotNull()) {
            field.setBackground(SFConstants.FIELD_REQUIRED_BACKGROUND_COLOR);
        }

        TFCellEditor cellEditor = new TFCellEditor(field, uiw, wm, colInfo);
        new GridCellListener(cellEditor);
        new GridCellFocusListener(field, cellEditor);

        if (colInfo.getMaskInfo() == null) {
            if (colInfo.getLength() > 0) {
                new SFLengthFilter(wm, colInfo.getLength(), field,
                        cellEditor);
            }
        }

        colInfo.setCellEditor(cellEditor);

        tableColumn.setCellEditor(cellEditor);
    }

    public static void setupHandlerToButton(JButton button, UIWrapper uiw,
                                            WidgetManager wm) {

        new ActionListenerWidget(button, uiw, wm);
        new ButtonKeyHandler(button, wm);

        button.setFocusTraversalKeysEnabled(false);

    }

    public static void setupHandlerToRunButton(JButton button, UIWrapper uiw,
                                               WidgetManager wm) {

        new ActionListenerRunButton(button, uiw, wm);
        new ButtonKeyHandler(button, wm);
    }

    public static void setupHandlerToPasswordField(UIWrapper uiw, WidgetManager wm) {

        SFPassword password = (SFPassword) uiw.getObjectUI();
        JPasswordField field = password.getPasswordField();
        field.setFocusTraversalKeysEnabled(false);

        new FieldKeyHandler(field, wm);
        new FieldFocusHandler(wm, uiw, field);

        if (uiw.getLength() > 0) {
            new SFLengthFilter(wm, uiw.getLength(), field);
        }

        BeanFormDefProperty prop = uiw.getBeanFormDefPropertyFromBind();

        if (prop != null && prop.getNotNull() != null
                && prop.getNotNull()) {
            field.setBackground(SFConstants.FIELD_REQUIRED_BACKGROUND_COLOR);
        }
    }

    public static void setupHandlerToTextField(UIWrapper uiw, WidgetManager wm) {

        JTextField field = (JTextField) uiw.getObjectUI();
        field.setFocusTraversalKeysEnabled(false);

        if (uiw.getMaskInfo() == null) {
            new FieldKeyHandler(field, wm);
            new FieldFocusHandler(wm, uiw, field);

            if (uiw.getLength() > 0) {
                new SFLengthFilter(wm, uiw.getLength(), field);
            }
            switch (uiw.getWidgetType()) {
                case PHONE:
                    new PhoneFieldFilter(wm, field);
                    break;
            }
        }

        BeanFormDefProperty prop = uiw.getBeanFormDefPropertyFromBind();

        if (prop != null && prop.getNotNull() != null
                && prop.getNotNull()) {
            field.setBackground(SFConstants.FIELD_REQUIRED_BACKGROUND_COLOR);
        }
    }

    public static void setupHandlerToField(UIWrapper uiw, WidgetManager wm) {

        JCheckBox field = (JCheckBox) uiw.getObjectUI();

        FieldFocusListener ffl = new FieldFocusListener(field, uiw, wm);
        new ItemListenerCheckBox(field, uiw, wm, ffl);
        new SFCheckBoxKeyHandler(field, wm);
        new ActionListenerWidget(field, uiw, wm);

        field.setFocusTraversalKeysEnabled(false);
    }

    public static void setupHandlerToFieldCombobox(UIWrapper uiw,
                                                   WidgetManager wm) {

        SFComboBox field = (SFComboBox) uiw.getObjectUI();

        new FieldFocusListener(field, uiw, wm);
        new ActionListenerComboBox(wm, uiw, field);
        new SFComboBoxKeyHandler(field, wm);

        field.setFocusTraversalKeysEnabled(false);

    }

    private static void validateMask(MaskInfo maskInfo) {
        if (maskInfo.getMaskDelimiter() == null) {
            throw new SmartFlexMaskException("Mask is required for this type");
        }

        boolean wasSpace = false;
        int lastCaret = -1;
        for (int i = 0; i < maskInfo.getMaskDelimiter().length(); i++) {
            char symbol = maskInfo.getMaskDelimiter().charAt(i);
            if (symbol == ISFMaskConstants.CHAR_SPACE) {
                wasSpace = true;
                lastCaret = i;
            }
        }
        if (!wasSpace) {
            throw new SmartFlexMaskException("Mask must contains a space: "
                    + maskInfo.getMaskDelimiter());
        }
        maskInfo.setLastCaretPosition(lastCaret);
    }

    public static boolean checkIsPossibleToInsertSymbol(String mask, int index) {
        boolean fok = false;
        if (index >= 0 && index < mask.length()) {
            if (mask.charAt(index) == ISFMaskConstants.CHAR_SPACE) {
                fok = true;
            }
        }
        return fok;
    }

    public static void slideCaretFromEndToLeft(JTextField field, String mask) {
        int startCaret;

        for (startCaret = mask.length() - 1; startCaret >= 0; startCaret--) {
            char symbol = mask.charAt(startCaret);
            if (symbol == ISFMaskConstants.CHAR_SPACE) {
                break;
            }
        }
        startCaret++;
        field.setCaretPosition(startCaret);

    }

    public static void moveCaretToStart(JTextField field, String mask) {
        field.setCaretPosition(0);
        if (mask != null) {
            slideCaretFromStartToRight(field, mask);
        }
    }

    public static void slideCaretFromStartToRight(JTextField field, String mask) {
        int startCaret = field.getCaretPosition();
        if (startCaret < mask.length()) {
            char symbol = mask.charAt(startCaret);
            if (symbol != ISFMaskConstants.CHAR_SPACE) {
                int indexSpace = -1;
                // slide to right
                for (int i = startCaret + 1; i < mask.length(); i++) {
                    symbol = mask.charAt(i);
                    if (symbol == ISFMaskConstants.CHAR_SPACE) {
                        indexSpace = i;
                        break; // ok
                    }
                }
                if (indexSpace == -1) {
                    // slide from start to 0. In case if there many delimiters
                    for (int i = startCaret; i >= 0; i--) {
                        symbol = mask.charAt(i);
                        if (symbol == ISFMaskConstants.CHAR_SPACE) {
                            indexSpace = i;
                            break; // ok
                        }
                    }
                    indexSpace++; // to stay closely to delimiter
                }
                field.setCaretPosition(indexSpace);
            }

        }  // else { out of edge. Nothing to do
    }

    public static void setupHandlerToTabPanel(JTabbedPane tabpanel, UIWrapper uiw,
                                              WidgetManager wm) {

        new ChangeListenerTabPanel(tabpanel, uiw, wm);

    }

}
