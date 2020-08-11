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
    private JEditorPane htmlPane = new JEditorPane();
    private JScrollPane calcScroller = new JScrollPane(htmlPane);
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

        htmlPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "dispose");
        htmlPane.getActionMap().put("dispose", new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        });

        this.getContentPane().add(calcScroller, BorderLayout.CENTER);

        htmlPane.setEditable(false);
        htmlPane.setEditorKit(kit);
    }

    public void show(String html) {
        Document doc = kit.createDefaultDocument();
        htmlPane.setDocument(doc);
        htmlPane.getDocument().putProperty("Ignore-Charset", "true");  // this line makes no difference either way

        htmlPane.setContentType("text/html");
        htmlPane.getDocument().putProperty("IgnoreCharsetDirective", Boolean.TRUE);
        htmlPane.setText(html);

        FrameUtil.centerFitFrame(this);
        this.setVisible(true);
    }

}
