package ru.smartflex.djf.widget;

import javax.swing.text.JTextComponent;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

public class SFTextAreaSyntax extends RTextScrollPane implements ITextArea,
        IRequestFocus {

    private static final long serialVersionUID = -4175549838094523948L;

    private RSyntaxTextArea textArea = new RSyntaxTextArea();

    public SFTextAreaSyntax(String syntax) {
        textArea.setSyntaxEditingStyle(syntax);
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
