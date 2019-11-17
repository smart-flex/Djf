package ru.smartflex.djf.widget.mask;

import java.awt.event.KeyEvent;

import javax.swing.JTextField;

import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.widget.ISFHandler;
import ru.smartflex.djf.widget.ItemHandler;
import ru.smartflex.djf.widget.grid.TFCellEditor;

public class MaskFieldKeyHandler extends java.awt.event.KeyAdapter implements
        ISFHandler {
    private JTextField field;
    private String mask;
    private WidgetManager wm;
    private UIWrapper uiw;
    private TFCellEditor cellEditor = null;

    public MaskFieldKeyHandler(JTextField field, String mask, WidgetManager wm,
                               UIWrapper uiw) {
        this.field = field;
        this.mask = mask;
        this.wm = wm;
        this.uiw = uiw;

        field.addKeyListener(this);
    }

    public MaskFieldKeyHandler(JTextField field, String mask, WidgetManager wm,
                               UIWrapper uiw, TFCellEditor cellEditor) {
        this.field = field;
        this.mask = mask;
        this.wm = wm;
        this.uiw = uiw;
        this.cellEditor = cellEditor;

        field.addKeyListener(this);
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                shiftRight(e, false);
                break;
            case KeyEvent.VK_LEFT:
                shiftLeft(e);
                break;
            case KeyEvent.VK_ENTER:
                goNextSectionOrItem(e);
                break;
            case KeyEvent.VK_HOME:
                e.consume();
                field.setCaretPosition(0);
                ItemHandler.slideCaretFromStartToRight(field, mask);
                break;
            case KeyEvent.VK_END:
                e.consume();
                ItemHandler.slideCaretFromEndToLeft(field, mask);
                break;
            case KeyEvent.VK_UP:
                if (cellEditor == null) {
                    e.consume();
                    wm.moveUp(field.getName());
                }
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_TAB:
                if (cellEditor == null) {
                    e.consume();
                    wm.moveDown(field.getName());
                }
                break;
        }
    }

    private void goNextSectionOrItem(KeyEvent e) {

        int startCaret = field.getCaretPosition();
        int saveCaretPosition = startCaret;
        if (startCaret < mask.length()) {
            startCaret = goNearestDelimiter();
            char startSymbol = mask.charAt(startCaret);
            if (startSymbol != ISFMaskConstants.CHAR_SPACE) {
                startCaret = shiftRight(e, true);
            }
            if (startCaret == saveCaretPosition) {
                // nothing to move, then has to go next ui item
                if (cellEditor == null) {
                    wm.moveDown(uiw.getUiName());
                }
            }
        } else {
            // last section (caret), go to next ui item
            if (cellEditor == null) {
                wm.moveDown(uiw.getUiName());
            }
        }

    }

    private int goNearestDelimiter() {
        int startCaret = field.getCaretPosition();
        int delimiterCaret = -1;
        if (startCaret < mask.length()) {
            char startSymbol = mask.charAt(startCaret);
            if (startSymbol == ISFMaskConstants.CHAR_SPACE) {
                int nextCaret = startCaret + 1;
                if (nextCaret < mask.length()) {
                    char symbol;
                    for (; nextCaret < mask.length(); nextCaret++) {
                        symbol = mask.charAt(nextCaret);
                        if (symbol == ISFMaskConstants.CHAR_SPACE) {
                            if (delimiterCaret != -1) {
                                startCaret = delimiterCaret;
                                field.setCaretPosition(delimiterCaret);
                                break; // found space after delimiter
                            }
                        } else {
                            if (delimiterCaret == -1) {
                                delimiterCaret = nextCaret;
                            }
                        }
                    }

                }
            }
        }
        return startCaret;
    }

    private void shiftLeft(KeyEvent e) {
        int startCaret = field.getCaretPosition();
        int nextCaret = startCaret - 1;
        if (nextCaret >= 0) {
            char nextSymbol = mask.charAt(nextCaret);
            if (nextSymbol != ISFMaskConstants.CHAR_SPACE) {
                boolean wasShift = false;
                char symbol = 0;
                for (; nextCaret >= 0; nextCaret--) {
                    symbol = mask.charAt(nextCaret);
                    if (symbol == ISFMaskConstants.CHAR_SPACE) {
                        break; // ok
                    } else {
                        wasShift = true;
                    }
                }
                if (wasShift) {
                    if (symbol != ISFMaskConstants.CHAR_SPACE) {
                        // reach end mask and last symbol - delimiter
                        e.consume();
                        // return to start caret
                        field.setCaretPosition(startCaret);
                    } else {
                        e.consume();
                        nextCaret++;
                        field.setCaretPosition(nextCaret);
                    }
                }

            }
        }
    }

    private int shiftRight(KeyEvent e, boolean needShift) {
        int startCaret = field.getCaretPosition();
        int returnCaret = startCaret;
        if (startCaret < mask.length()) {
            char startSymbol = mask.charAt(startCaret);

            int nextCaret = startCaret + 1;
            if (nextCaret < mask.length()) {
                char nextSymbol = mask.charAt(nextCaret);
                if (startSymbol != ISFMaskConstants.CHAR_SPACE
                        && nextSymbol != ISFMaskConstants.CHAR_SPACE) {
                    boolean wasShift = false;
                    char symbol = 0;
                    for (; nextCaret < mask.length(); nextCaret++) {
                        symbol = mask.charAt(nextCaret);
                        if (symbol == ISFMaskConstants.CHAR_SPACE) {
                            break; // ok
                        } else {
                            wasShift = true;
                        }
                    }
                    if (wasShift) {
                        if (symbol != ISFMaskConstants.CHAR_SPACE) {
                            // reach end mask and last symbol - delimiter
                            e.consume();
                            // return to start caret
                            field.setCaretPosition(startCaret);
                        } else {
                            e.consume();
                            field.setCaretPosition(nextCaret);
                            returnCaret = nextCaret;
                        }
                    }

                } else {
                    if (needShift) {
                        field.setCaretPosition(nextCaret);
                        returnCaret = nextCaret;
                    }
                }

            }
        }
        return returnCaret;
    }

    @Override
    public void closeHandler() {
        field = null;
        mask = null;
        wm = null;
    }
}
