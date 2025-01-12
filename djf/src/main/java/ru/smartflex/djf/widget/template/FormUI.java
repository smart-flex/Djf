package ru.smartflex.djf.widget.template;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.widget.IForm;

@SuppressWarnings("unused")
public class FormUI extends JInternalFrame implements IForm {

    private static final long serialVersionUID = -7566567283656078011L;

    public FormUI() {
        super("", true, true, true, true);
        init();
        this.setLayout(new BorderLayout());
        this.setVisible(true);
    }

    private void init() {
        if (getContentPane() instanceof JComponent) {
            JComponent panel = (JComponent) getContentPane();
            panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('X', KeyEvent.ALT_MASK, false), "dispose");
            panel.getActionMap().put("dispose", new AbstractAction() {
                public void actionPerformed(ActionEvent event) {
                    if (Djf.isCurrentFormWasChanged()) {
                        Djf.showStatusWarnMessage("${label.djf.message.warn.but_no_actn_allow_formwaschanged}");
                    } else {
                        Djf.exitFormViaButonStatus();
                    }
                }
            });
            panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_MASK, false), "save");
            panel.getActionMap().put("save", new AbstractAction() {
                public void actionPerformed(ActionEvent event) {
                    Djf.saveForm();
                }
            });
        }
    }

    public void requestFocusInWindowFormUI() {
        // после закрытия текущей формы и показа предыдущей, надо на ней вызвать requestFocusInWindow с тем чтобы оживить KeyStroke (выше)
        if (getContentPane() instanceof JComponent) {
            JComponent panel = (JComponent) getContentPane();
            panel.requestFocusInWindow();
        }
    }
}
