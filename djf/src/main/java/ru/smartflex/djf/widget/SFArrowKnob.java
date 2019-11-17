package ru.smartflex.djf.widget;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;

import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.tool.OtherUtil;
import ru.smartflex.djf.widget.grid.RightCornerActionListener;

public class SFArrowKnob extends javax.swing.JPanel implements ISFHandler {
    private static final long serialVersionUID = 6237901528813163443L;

    private static ImageIcon imageUp = null;
    private static ImageIcon imageDown = null;
    private ISFHandler alUp;
    private ISFHandler alDown;

    private GridLayout gridLayout = new GridLayout(0, 1);
    private JButton buttonUp = new JButton();
    private JButton buttonDown = new JButton();

    private SFLazyClicker clicker = new SFLazyClicker();

    public SFArrowKnob(JTable table, WidgetManager widgetManager) {
        super();
        initImages();
        panelInit();

        alUp = new RightCornerActionListener(table, buttonUp, true, widgetManager);
        alDown = new RightCornerActionListener(table, buttonDown, false, widgetManager);
    }

    @SuppressWarnings("unused")
    public SFArrowKnob() {
        super();
        initImages();
        panelInit();

        buttonUp.setPreferredSize(new Dimension(16, 16));
        buttonDown.setPreferredSize(new Dimension(16, 16));

        alUp = new SFArrowKnobActionListener(clicker, buttonUp, true);
        alDown = new SFArrowKnobActionListener(clicker, buttonDown, false);
    }

    private void initImages() {
        if (imageUp == null) {
            imageUp = OtherUtil.loadSFImages("arrow_up.png");
            imageDown = OtherUtil.loadSFImages("arrow_down.png");
        }
    }

    private void panelInit() {
        this.setLayout(gridLayout);
        buttonUp.setIcon(imageUp);
        buttonUp.setBorderPainted(false);
        buttonUp.setFocusPainted(false);

        buttonDown.setIcon(imageDown);
        buttonDown.setBorderPainted(false);
        buttonDown.setFocusPainted(false);

        this.add(buttonUp, null);
        this.add(buttonDown, null);
    }


    @Override
    public void closeHandler() {
        if (alUp != null) {
            alUp.closeHandler();
        }
        if (alDown != null) {
            alDown.closeHandler();
        }
        alUp = null;
        alDown = null;
        clicker = null;
    }

    @SuppressWarnings("unused")
    public SFLazyClicker getClicker() {
        return clicker;
    }

}
