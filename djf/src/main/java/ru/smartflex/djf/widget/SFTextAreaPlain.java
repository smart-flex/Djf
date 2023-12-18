package ru.smartflex.djf.widget;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

public class SFTextAreaPlain extends JScrollPane implements ITextArea,
        IRequestFocus {

    private static final long serialVersionUID = 4017306465940135187L;

    private JTextArea textArea = new JTextArea();

    public SFTextAreaPlain() {
        init();
    }

    private void init() {
        this.getViewport().add(textArea, null);
    }

    @Override
    public void setText(String text) {
        textArea.setText(text);
        textArea.setCaretPosition(0);
    }

    @Override
    public void setEnabled(boolean flag) {
        textArea.setEnabled(flag);
    }

    @Override
    public void setEditable(boolean flag) {
        textArea.setEditable(flag);
    }

    @Override
    public void requestFocusOnNestedWidget() {
        textArea.requestFocus();
    }

    @Override
    public JTextComponent getJTextComponent() {
        return textArea;
    }

    @Override
    public String getText() {
        return textArea.getText();
    }

}
