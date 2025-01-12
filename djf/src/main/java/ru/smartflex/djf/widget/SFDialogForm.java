package ru.smartflex.djf.widget;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.FormStepEnum;
import ru.smartflex.djf.FrameHelper;
import ru.smartflex.djf.controller.FormManager;
import ru.smartflex.djf.controller.FormStack;
import ru.smartflex.djf.controller.bean.FormBag;
import ru.smartflex.djf.tool.FrameUtil;

public class SFDialogForm extends JDialog implements ISFDialog, IForm {

    private static final long serialVersionUID = 6303235936532285683L;

    private FormBag formBag = null;

    public SFDialogForm() {
        super((Frame) Djf.getConfigurator().getFrame(), "", true);
        init();
    }

    private void init() {
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        setResizable(false);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeDialog();
            }

            public void windowClosed(WindowEvent e) {
            }
        });

        if (getContentPane() instanceof JComponent) {
            JComponent panel = (JComponent) getContentPane();
            panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "dispose");
            panel.getActionMap().put("dispose", new AbstractAction() {
                public void actionPerformed(ActionEvent event) {
                    if (Djf.isCurrentFormWasChanged()) {
                        Djf.showStatusWarnMessage("${label.djf.message.warn.but_no_actn_allow_formwaschanged}");
                    } else {
                        closeDialog();
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

    public void closeDialog() {
        dispose();
        FormStack.removeCurrentForm();
    }

    @Override
    public void show(String showString) {
        FormStack.registerForm(formBag);

        FrameUtil.centerFitFrameDialog(this);

        FrameHelper.showWaitLongPanel();

        new FormManager(formBag);

        String msgMain = formBag.getDescription();
        if (formBag.getAssistant() != null) {
            formBag.getAssistant().step(FormStepEnum.BEFORE_READ_DATA,
                    formBag.getFormSession());
            if (formBag.getAssistant().getCustomDescription() != null) {
                msgMain = formBag.getAssistant().getCustomDescription();
            }
        }

        if (msgMain != null) {
            if (msgMain.charAt(msgMain.length() - 1) != '.') {
                msgMain = msgMain + ". ";
            } else if (msgMain.charAt(msgMain.length() - 1) != ' ') {
                msgMain = msgMain + " ";
            }
            this.setTitle(msgMain +  "<ESC> - выход");
        }

        // this is final point of execution. All threads are stopped
        this.setVisible(true);
    }

    public void setFormBag(FormBag formBag) {
        this.formBag = formBag;
    }

}
