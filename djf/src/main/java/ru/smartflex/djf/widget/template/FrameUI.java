package ru.smartflex.djf.widget.template;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.SizeFrameEnum;
import ru.smartflex.djf.tool.OtherUtil;
import ru.smartflex.djf.widget.IForm;
import ru.smartflex.djf.widget.IFrame;
import ru.smartflex.djf.widget.TaskStatusLevelEnum;

public class FrameUI extends JFrame implements IFrame {

    private static final long serialVersionUID = -4108566683246371131L;
    private static final boolean oldFlex = true;
    private static final Color infoColor = new Color(9, 172, 242);
    private static ImageIcon iconStatusError = null;
    private static ImageIcon iconStatusInfo = null;
    private static ImageIcon iconStatusWarn = null;

    @SuppressWarnings("FieldCanBeLocal")
    private static int deltaWindow = 20;

    private FrameComponentListener fcListener = null;

    private int startWidth = 780;
    private int startHeight = 480;
    private int contentWidth = startWidth - 16;
    private int contentHeight = startHeight - 38;
    private int heightNorth = 50;
    private int heightSouth = 55; // 55 allows to see exit button more accurate

    private Dimension startDimension = new Dimension(startWidth, startHeight);
    private Dimension northPanelDim = new Dimension(contentWidth, heightNorth);
    private Dimension southPanelDim = new Dimension(contentWidth, heightSouth);
    private Dimension centerPanelDim = new Dimension(contentWidth,
            contentHeight - heightNorth - heightSouth);
    private BorderLayout blContent = new BorderLayout();
    private BorderLayout blSouth = new BorderLayout();
    private JPanel panelNorth = new JPanel();
    private BorderLayout layPnlNorth = new BorderLayout();
    private JPanel pnlNorthWest = new JPanel();
    private FlowLayout layPnlNorthWest = new FlowLayout(FlowLayout.LEFT, 5, 5);

    private JPanel pnlSouth = new JPanel();
    private JDesktopPane pnlDesktop = new JDesktopPane();

    private JLabel labelStatusInfo = new JLabel();
    private JLabel labelInfoForm = new JLabel("...");
    private JLabel labelInfoFormAdd = new JLabel("...");

    private JPanel pnlOper = new JPanel();
    private FlowLayout layOper = new FlowLayout(FlowLayout.RIGHT, 5, 5);
    private JButton butExit = new JButton();
    private JButton butSave = new JButton();
    private JButton butDelete = new JButton();
    private JButton butRefresh = new JButton();
    private JButton butAdd = new JButton();

    private boolean sizeAligned = false;

    public FrameUI() {
        super();
        try {
            init();
            flexInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("RedundantThrows")
    private void init() throws Exception {
        this.setSize(startDimension);
        this.setMinimumSize(startDimension);
        this.getContentPane().setLayout(blContent);
        this.getContentPane().add(panelNorth, BorderLayout.NORTH);
        this.getContentPane().add(pnlSouth, BorderLayout.SOUTH);
        pnlSouth.setLayout(blSouth);
        pnlSouth.add(labelStatusInfo, BorderLayout.NORTH);

        pnlOper.setLayout(layOper);

        panelNorth.setLayout(layPnlNorth);

        pnlOper.add(butAdd);
        pnlOper.add(butRefresh);
        pnlOper.add(butDelete);
        pnlOper.add(butSave);
        pnlOper.add(butExit);

        pnlSouth.add(pnlOper, BorderLayout.CENTER);
    }

    private void flexInit() {

        labelInfoForm.setFont(new Font("SansSerif", Font.BOLD, 20));
        labelInfoForm.setForeground(infoColor);
        labelInfoForm.setName(IFrame.NAME_LABEL_INFO_FORM);
        labelInfoFormAdd.setFont(new Font("SansSerif", Font.BOLD, 15));
        labelInfoFormAdd.setName(IFrame.NAME_LABEL_INFO_FORM_ADD);

        pnlNorthWest.setLayout(layPnlNorthWest);
        pnlNorthWest.add(labelInfoForm);
        pnlNorthWest.add(labelInfoFormAdd);
        panelNorth.add(pnlNorthWest, BorderLayout.WEST);

        panelNorth.setName(IFrame.PANEL_NORTH_NAME);
        pnlSouth.setName(IFrame.PANEL_SOUTH_NAME);
        panelNorth.setPreferredSize(northPanelDim);
        pnlSouth.setPreferredSize(southPanelDim);

        pnlDesktop.putClientProperty("JDesktopPane.dragMode", "outline");

        pnlDesktop.setSize(centerPanelDim);

        getContentPane().add(pnlDesktop, BorderLayout.CENTER);

        fcListener = new FrameComponentListener(this);

        this.addComponentListener(fcListener);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeFrame();
            }
        });

        if (oldFlex) {
            disableEvents(AWTEvent.WINDOW_EVENT_MASK);
            this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        } else {
            enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        }
        ImageIcon iconExit = OtherUtil.loadSFImages("door_out.png");
        butExit.setIcon(iconExit);
        butExit.setName(SFConstants.BUTTON_NAME_EXIT);
        ImageIcon iconSave = OtherUtil.loadSFImages("save.png");
        butSave.setIcon(iconSave);
        butSave.setName(SFConstants.BUTTON_NAME_SAVE);
        ImageIcon iconDelete = OtherUtil.loadSFImages("delete.png");
        butDelete.setIcon(iconDelete);
        butDelete.setName(SFConstants.BUTTON_NAME_DELETE);
        ImageIcon iconRefresh = OtherUtil.loadSFImages("refresh.png");
        butRefresh.setIcon(iconRefresh);
        butRefresh.setName(SFConstants.BUTTON_NAME_REFRESH);
        ImageIcon iconAdd = OtherUtil.loadSFImages("add.png");
        butAdd.setIcon(iconAdd);
        butAdd.setName(SFConstants.BUTTON_NAME_ADD);

        butAdd.setVisible(false);
        butRefresh.setVisible(false);
        butDelete.setVisible(false);
        butSave.setVisible(false);
    }

    public void closeFrame() {
        this.removeComponentListener(fcListener);
        Djf.getConfigurator().closeClientResources();

        System.exit(0);
    }

    @SuppressWarnings("unused")
    public void centerFrame() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        this.setLocation((int) ((tk.getScreenSize().getWidth() - (double) this
                        .getWidth()) / 2D),
                (int) ((tk.getScreenSize().getHeight() - (double) this
                        .getHeight()) / 2D));

    }

    @SuppressWarnings("unused")
    public void setHeightSouth(int heightSouth) {
        this.heightSouth = heightSouth;
        resizeToFit();
    }

    @SuppressWarnings("unused")
    public void setHeightNorth(int heightNorth) {
        this.heightNorth = heightNorth;
        resizeToFit();
    }

    void resizeToFit() {
        Dimension newSize = this.getContentPane().getSize();
        int width = (int) newSize.getWidth();
        panelNorth.setPreferredSize(new Dimension(width, heightNorth));
        pnlSouth.setPreferredSize(new Dimension(width, heightSouth));
        int height = (int) newSize.getHeight() - heightNorth - heightSouth;

        Component[] cmpns = this.pnlDesktop.getComponents();
        if (cmpns != null && cmpns.length > 0) {
            for (Component cmpn : cmpns) {
                if (cmpn instanceof IForm) {
                    cmpn.setBounds(0, 0, width, height);
                    cmpn.setMinimumSize(new Dimension(width,
                            height));
                }
            }
        }
    }

    @Override
    public void addForm(IForm iForm) {

        // Выставляем размеры
        ((JInternalFrame) iForm).setBounds(0, 0, this.pnlDesktop.getWidth(),
                this.pnlDesktop.getHeight());
        ((JInternalFrame) iForm).setMaximizable(false);
        ((JInternalFrame) iForm).setResizable(false);
        ((JInternalFrame) iForm).setMinimumSize(new Dimension(this.pnlDesktop
                .getWidth(), this.pnlDesktop.getHeight()));
        ((JInternalFrame) iForm).setClosable(false);
        ((JInternalFrame) iForm).setIconifiable(false);

        // disable title
        ((javax.swing.plaf.basic.BasicInternalFrameUI) ((JInternalFrame) iForm)
                .getUI()).setNorthPane(null);
        ((JInternalFrame) iForm).putClientProperty("JInternalFrame.isPalette",
                Boolean.TRUE);

        this.pnlDesktop.add((Component) iForm);
        try {
            ((JInternalFrame) iForm).setSelected(true);
        } catch (PropertyVetoException e) {
            SFLogger.error("addToFrame", e);
        }
    }

    @Override
    public Rectangle getFormUISCreenBound() {

        Dimension dim = pnlDesktop.getSize();
        Point pt = pnlDesktop.getLocationOnScreen();

        return new Rectangle(pt, dim);
    }

    @Override
    public void removeForm(IForm iForm) {
        this.pnlDesktop.remove((Component) iForm);
    }

    @Override
    public void showStatusMessage(TaskStatusLevelEnum status, String msg) {

        if (iconStatusError == null) {
            iconStatusError = OtherUtil.loadSFImages("cancel.png");
        }
        if (iconStatusInfo == null) {
            iconStatusInfo = OtherUtil.loadSFImages("information.png");
        }
        if (iconStatusWarn == null) {
            iconStatusWarn = OtherUtil.loadSFImages("warning.png");
        }

        ImageIcon icon = null;
        if (status == TaskStatusLevelEnum.ERROR) {
            icon = iconStatusError;
        } else if (status == TaskStatusLevelEnum.OK) {
            icon = iconStatusInfo;
        } else if (status == TaskStatusLevelEnum.WARNING) {
            icon = iconStatusWarn;
        }
        labelStatusInfo.setText(msg);
        labelStatusInfo.setIcon(icon);
    }

    @SuppressWarnings("unused")
    public void addToPanelNorthSideEast(JComponent comp) {
        panelNorth.add(comp, BorderLayout.EAST);
    }

    @Override
    public void expand(SizeFrameEnum size) {
        if (!sizeAligned) {

            sizeAligned = true;

            // all size
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            // taskbar size
            Insets taskbar = Toolkit.getDefaultToolkit().getScreenInsets(
                    getGraphicsConfiguration());

            // new size
            int width = screenSize.width - deltaWindow;
            int height = screenSize.height - deltaWindow;

            if (taskbar.bottom > 0 || taskbar.top > 0) {
                height = height - taskbar.bottom - taskbar.top;
            }
            if (taskbar.left > 0 || taskbar.right > 0) {
                width = width - taskbar.left - taskbar.right;
            }

            switch (size) {
                case HALF:
                    if ((width / 2) > startWidth && height > startHeight) {

                        Dimension newSize = new Dimension(width - screenSize.width
                                / 2, height);
                        this.setSize(newSize);
                        this.setLocation(taskbar.left + screenSize.width / 4
                                + deltaWindow / 2, taskbar.top + deltaWindow / 2);

                    }
                    break;
                case ALMOST_WHOLE:
                    if (width > startWidth && height > startHeight) {

                        Dimension newSize = new Dimension(width, height);
                        this.setSize(newSize);
                        this.setLocation(taskbar.left + deltaWindow / 2,
                                taskbar.top + deltaWindow / 2);

                    }
                    break;
                case CUSTOM:

                    Dimension newSize = new Dimension(size.getWidth(), size.getHeight());
                    this.setSize(newSize);
                    this.setLocation(taskbar.left + deltaWindow / 2,
                            taskbar.top + deltaWindow / 2);

                    break;
            }
        }
    }

    @Override
    public void toFront() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                toFrontLater();
            }
        });
    }

    private void toFrontLater() {
        super.setVisible(true);

        // int sta = super.getExtendedState() & ~JFrame.ICONIFIED &
        // JFrame.NORMAL;

        // super.setExtendedState(sta);
        super.setState(JFrame.NORMAL);
        super.setAlwaysOnTop(true);
        super.toFront();
        super.requestFocus();
        super.setAlwaysOnTop(false);
        super.repaint();
        super.toFront();
    }

    @Override
    public void showDescription(String msgMain, String msgAdd) {
        if (msgMain == null) {
            labelInfoForm.setText("");
        } else {
            labelInfoForm.setText(msgMain);
        }
        if (msgAdd == null) {
            labelInfoFormAdd.setText("");
        } else {
            labelInfoFormAdd.setText(msgAdd);
        }
    }

    @Override
    public void setMenu(JMenuBar menu) {
        this.setJMenuBar(menu);
    }

    @Override
    public void setMinimumSize(int width, int height) {
        if (width > 0 && height > 0) {
            this.setMinimumSize(new Dimension(width, height));
        }
    }
}
