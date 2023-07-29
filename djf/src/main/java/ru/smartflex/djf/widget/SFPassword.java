package ru.smartflex.djf.widget;

import ru.smartflex.djf.tool.OtherUtil;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SFPassword extends javax.swing.JPanel implements IRequestFocus {

    private static ImageIcon imageEyeClosed = null;
    private static ImageIcon imageEyeOpen = null;
    private BorderLayout layPwd = new BorderLayout(0, 5);
    private JButton buttonEye = new JButton();
    private JPasswordField passwordField = new JPasswordField();

    public SFPassword() {
        init();
    }

    private void init() {
        Border usual = passwordField.getBorder();
        if (imageEyeClosed == null) {
            imageEyeClosed = OtherUtil.loadSFImages("eye_closed.png");
            imageEyeOpen = OtherUtil.loadSFImages("eye_filled.png");
        }
        this.setLayout(layPwd);
        this.setBorder(usual);

        buttonEye.setIcon(imageEyeClosed);
        buttonEye.setBorderPainted(false);
        buttonEye.setFocusPainted(false);
        buttonEye.setPreferredSize(new Dimension(16, 16));
        new ButtonEyeActionListener();

        passwordField.setBorder(null);

        this.add(passwordField, BorderLayout.CENTER);
        this.add(buttonEye, BorderLayout.EAST);
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    @Override
    public void requestFocusOnNestedWidget() {
        passwordField.requestFocus();
    }

    class ButtonEyeActionListener implements ActionListener {

        private boolean pressed = false;
        private Border borderPressed = BorderFactory.createLoweredBevelBorder();

        ButtonEyeActionListener() {
            buttonEye.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            pressed = !pressed;

            if (pressed) {
                buttonEye.setBorderPainted(true);
                buttonEye.setBorder(borderPressed);
                passwordField.setEchoChar('\u0000');
                buttonEye.setIcon(imageEyeOpen);
            } else {
                buttonEye.setBorderPainted(false);
                buttonEye.setBorder(null);
                passwordField.setEchoChar('*');
                buttonEye.setIcon(imageEyeClosed);
            }
        }
    }
}
