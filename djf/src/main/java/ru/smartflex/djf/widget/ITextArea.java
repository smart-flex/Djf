package ru.smartflex.djf.widget;

import javax.swing.text.JTextComponent;

public interface ITextArea {

    String getText();

    void setText(String text);

    void setEnabled(boolean flag);

    JTextComponent getJTextComponent();

}
