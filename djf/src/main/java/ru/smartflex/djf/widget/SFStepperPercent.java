package ru.smartflex.djf.widget;

import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.tool.OtherUtil;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SFStepperPercent extends javax.swing.JPanel implements ISFHandler {

    private static ImageIcon imagePlus = null;
    private static ImageIcon imageMinus = null;
    private BorderLayout layout = new BorderLayout(2, 2);
    private JButton buttonPlus = new JButton();
    private JButton buttonMinus = new JButton();
    private JTextField percent = new JTextField();

    private SFStepperPercentActionListener alPlus = null;
    private SFStepperPercentActionListener alMinus = null;
    private AtomicBoolean allowedStep = new AtomicBoolean(true);
    private AtomicInteger intValue = new AtomicInteger(0);

    public SFStepperPercent() {
        super();
        initImages();
        panelInit();
    }

    public SFStepperPercent(WidgetManager widgetManager) {
        super();
        initImages();
        panelInit();
        initAl(widgetManager);
    }

    private void panelInit() {
        percent.setEditable(false); // по дефолту всегда такой
        percent.setHorizontalAlignment(JTextField.CENTER);
        buttonPlus.setPreferredSize(new Dimension(16, 16));
        buttonMinus.setPreferredSize(new Dimension(16, 16));

        this.setLayout(layout);
        buttonPlus.setIcon(imagePlus);
        buttonPlus.setBorderPainted(false);
        buttonPlus.setFocusPainted(false);

        buttonMinus.setIcon(imageMinus);
        buttonMinus.setBorderPainted(false);
        buttonMinus.setFocusPainted(false);

        this.add(buttonPlus, BorderLayout.EAST);
        this.add(buttonMinus, BorderLayout.WEST);
        this.add(percent, BorderLayout.CENTER);
    }

    //todo
    public void setItemDisabledDueToAbend() {
        buttonPlus.setEnabled(false);
        buttonMinus.setEnabled(false);
        percent.setEnabled(false);
    }

    public void enableAction() {
        allowedStep.set(true);
    }

    public void disableAction() {
        allowedStep.set(false);
    }

    private void initAl(WidgetManager widgetManager) {
        alPlus = new SFStepperPercentActionListener(this, Action.INCREMENT, widgetManager);
        alMinus = new SFStepperPercentActionListener(this, Action.DECREMENT, widgetManager);
    }

    private void initImages() {
        if (imagePlus == null) {
            imagePlus = OtherUtil.loadSFImages("magnify_plus.png");
            imageMinus = OtherUtil.loadSFImages("magnify_minus.png");
        }
    }

    void doStep(Action action) {
        if (!allowedStep.get()) {
            return;
        }
        int newVal = intValue.get();
        switch (action) {
            case INCREMENT:
                newVal += 10;
                break;
            case DECREMENT:
                newVal -= 10;
                break;
        }
        intValue.set(newVal);
        showPercent();
    }

    public void setPercent(Object perc) {
        if (perc == null) {
            showPercent();
            return;
        }
        int val = 0;
        if (perc instanceof String) {
            try {
                val = Integer.parseInt((String)perc);
            } catch (Exception e) {
            }
        }

        intValue.set(val);
        showPercent();
    }

    public Integer getPercent() {
        return intValue.get();
    }

    private void showPercent() {
        String perc = intValue.toString() + "%";
        percent.setText(perc);
    }

    JButton getButtonMinus() {
        return buttonMinus;
    }

    JButton getButtonPlus() {
        return buttonPlus;
    }

    static enum Action {
        INCREMENT, DECREMENT;
    }

    @Override
    public void closeHandler() {
        alPlus.closeHandler();
        alMinus.closeHandler();
        alPlus = null;
        alMinus = null;
    }
}
