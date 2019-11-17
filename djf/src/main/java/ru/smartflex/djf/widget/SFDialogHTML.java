package ru.smartflex.djf.widget;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.tool.FrameUtil;

public class SFDialogHTML extends JDialog implements ISFDialogHTML {

    private static final long serialVersionUID = 5338411383171792890L;

    private BorderLayout borderLayout = new BorderLayout();
    private JEditorPane editPane = new JEditorPane();
    private JScrollPane calcScroller = new JScrollPane(editPane);
    private HTMLEditorKit kit = new HTMLEditorKit();

    public SFDialogHTML(String title) {
        super((Frame) Djf.getConfigurator().getFrame(), title, true);

        init();
    }

    @SuppressWarnings("serial")
    private void init() {
        this.getContentPane().setLayout(borderLayout);
        this.setSize(new Dimension(700, 400));

        calcScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        calcScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "dispose");
        getRootPane().getActionMap().put("dispose", new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        });
        this.getContentPane().add(calcScroller, BorderLayout.CENTER);

        editPane.setEditable(false);
        editPane.setEditorKit(kit);
    }

    public void show(String html) {
        Document doc = kit.createDefaultDocument();
        editPane.setDocument(doc);
        editPane.getDocument().putProperty("Ignore-Charset", "true");  // this line makes no difference either way

        editPane.setContentType("text/html");
        editPane.getDocument().putProperty("IgnoreCharsetDirective", Boolean.TRUE);
        editPane.setText(html);

        FrameUtil.centerFitFrame(this);
        this.setVisible(true);
    }

}
