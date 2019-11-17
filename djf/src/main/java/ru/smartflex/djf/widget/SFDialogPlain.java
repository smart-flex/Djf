package ru.smartflex.djf.widget;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.tool.FrameUtil;

public class SFDialogPlain extends JDialog implements ISFDialog {

    private static final long serialVersionUID = 6056639295116812875L;

    private BorderLayout borderLayout = new BorderLayout();
    private JTextArea textPane = new JTextArea();
    private JScrollPane textScroller = new JScrollPane(textPane);

    public SFDialogPlain(String title) {
        super((Frame) Djf.getConfigurator().getFrame(), title, true);

        init();
    }

    @SuppressWarnings("serial")
    private void init() {
        this.getContentPane().setLayout(borderLayout);
        this.setSize(new Dimension(700, 400));

        textScroller
                .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        textScroller
                .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        getRootPane().getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "dispose");
        getRootPane().getActionMap().put("dispose", new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        });
        this.getContentPane().add(textScroller, BorderLayout.CENTER);
    }

    @Override
    public void show(String showString) {
        textPane.setCaretPosition(0);
        textPane.setEditable(false);
        textPane.setText(showString);

        FrameUtil.centerFitFrame(this);
        this.setVisible(true);
    }

}
