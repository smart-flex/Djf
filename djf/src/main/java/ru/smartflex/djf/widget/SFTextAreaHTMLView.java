package ru.smartflex.djf.widget;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTMLEditorKit;

public class SFTextAreaHTMLView extends JScrollPane implements ITextArea,
        IRequestFocus {

    private static final long serialVersionUID = 8601716594488498820L;

    private JEditorPane editPane = new JEditorPane();
    private HTMLEditorKit kit = new HTMLEditorKit();

    public SFTextAreaHTMLView() {
        init();
    }

    private void init() {
        this.getViewport().add(editPane, null);

        editPane.setEditable(false);
        editPane.setEditorKit(kit);
    }

    @Override
    public void setText(String text) {
        Document doc = kit.createDefaultDocument();
        editPane.setDocument(doc);
        // this line makes no difference either way
        editPane.getDocument().putProperty("Ignore-Charset", "true");

        editPane.setContentType("text/html");
        editPane.getDocument().putProperty("IgnoreCharsetDirective",
                Boolean.TRUE);
        editPane.setText(text);
    }

    @Override
    public void setEnabled(boolean flag) {
        editPane.setEnabled(flag);
    }

    @Override
    public void requestFocusOnNestedWidget() {
        editPane.requestFocus();
    }

    @Override
    public JTextComponent getJTextComponent() {
        return editPane;
    }

    @Override
    public String getText() {
        return editPane.getText();
    }

}
