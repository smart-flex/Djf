package ru.smartflex.djf.widget;

import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.controller.helper.PrefixUtil;
import ru.smartflex.djf.tool.OtherUtil;

public class SFOperator extends JPanel {

    private static final long serialVersionUID = -7981552928828809239L;

    private FlowLayout layOper = new FlowLayout(FlowLayout.RIGHT, 5, 5);
    private JButton butExit = new JButton();
    private JButton butSave = new JButton();
    private JButton butDelete = new JButton();
    private JButton butRefresh = new JButton();
    private JButton butAdd = new JButton();

    public SFOperator() {
        super();
        init();
    }

    private void init() {

        setLayout(layOper);

        add(butAdd);
        add(butRefresh);
        add(butDelete);
        add(butSave);
        add(butExit);

        ImageIcon iconExit = OtherUtil.loadSFImages("door_out.png");
        butExit.setIcon(iconExit);
        butExit.setToolTipText(PrefixUtil.getMsg(
                "${djf.oper_panel.exit.tips}", null));
        Djf.getConfigurator().getDialogButCache().setButton(SFConstants.BUTTON_NAME_EXIT, butExit);
        butExit.addActionListener(new ActionListenerCommonButton(
                SFConstants.BUTTON_NAME_EXIT));

        ImageIcon iconSave = OtherUtil.loadSFImages("save.png");
        butSave.setIcon(iconSave);
        butSave.setToolTipText(PrefixUtil.getMsg(
                "${djf.oper_panel.save.tips}", null));
        Djf.getConfigurator().getDialogButCache().setButton(SFConstants.BUTTON_NAME_SAVE, butSave);
        butSave.addActionListener(new ActionListenerCommonButton(
                SFConstants.BUTTON_NAME_SAVE));


        ImageIcon iconDelete = OtherUtil.loadSFImages("delete.png");
        butDelete.setIcon(iconDelete);
        butDelete.setToolTipText(PrefixUtil.getMsg(
                "${djf.oper_panel.delete.tips}", null));
        Djf.getConfigurator().getDialogButCache().setButton(SFConstants.BUTTON_NAME_DELETE,
                butDelete);
        butDelete.addActionListener(new ActionListenerCommonButton(
                SFConstants.BUTTON_NAME_DELETE));


        ImageIcon iconRefresh = OtherUtil.loadSFImages("refresh.png");
        butRefresh.setIcon(iconRefresh);
        butRefresh.setToolTipText(PrefixUtil.getMsg(
                "${djf.oper_panel.refresh.tips}", null));
        Djf.getConfigurator().getDialogButCache().setButton(SFConstants.BUTTON_NAME_REFRESH,
                butRefresh);
        butRefresh.addActionListener(new ActionListenerCommonButton(
                SFConstants.BUTTON_NAME_REFRESH));


        ImageIcon iconAdd = OtherUtil.loadSFImages("add.png");
        butAdd.setIcon(iconAdd);
        butAdd.setToolTipText(PrefixUtil.getMsg(
                "${djf.oper_panel.add.tips}", null));
        Djf.getConfigurator().getDialogButCache().setButton(SFConstants.BUTTON_NAME_ADD, butAdd);
        butAdd.addActionListener(new ActionListenerCommonButton(
                SFConstants.BUTTON_NAME_ADD));

        butAdd.setVisible(false);
        butRefresh.setVisible(false);
        butDelete.setVisible(false);
        butSave.setVisible(false);
    }
}
