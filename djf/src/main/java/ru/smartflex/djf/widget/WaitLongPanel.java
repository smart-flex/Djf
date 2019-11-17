package ru.smartflex.djf.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import ru.smartflex.djf.controller.helper.PrefixUtil;
import ru.smartflex.djf.tool.OtherUtil;

/**
 * Wait panel for long operation. It suppresses any events from mouse or
 * keyboard.
 */
public class WaitLongPanel extends JComponent {

    private static final long serialVersionUID = 7580177985401369271L;

    private static final Color TEXT_COLOR = Color.BLACK;
    private static final Color GRAD_COLOR = new Color(164, 213, 229);

    private String waitMessage;

    private static ImageIcon icon = null;

    @SuppressWarnings("FieldCanBeLocal")
    private static int shadowOffset = 3;
    @SuppressWarnings("FieldCanBeLocal")
    private static int strokeSize = 2;
    private static Dimension arcs = new Dimension(5, 5);// creates curved
    // borders and panel
    private static int shadowAlpha = 150;
    private static Color shadowColor = Color.black;
    private static Color shadowColorA = new Color(shadowColor.getRed(),
            shadowColor.getGreen(), shadowColor.getBlue(), shadowAlpha);

    @SuppressWarnings("FieldCanBeLocal")
    private int vGap = 16;
    @SuppressWarnings("FieldCanBeLocal")
    private int hGap = 16;

    private MouseAdapter mouseAdapter = null;
    private MouseMotionAdapter mouseMotionAdapter = null;
    private KeyAdapter keyAdapter = null;

    public WaitLongPanel(String waitMsg) {
        super();
        waitMessage = waitMsg;
        init();
    }

    public void close() {
        this.removeMouseListener(mouseAdapter);
        this.removeMouseMotionListener(mouseMotionAdapter);
        this.removeKeyListener(keyAdapter);
    }

    private void init() {

        mouseAdapter = new MouseAdapter() {
        };
        addMouseListener(mouseAdapter);

        mouseMotionAdapter = new MouseMotionAdapter() {
        };
        addMouseMotionListener(mouseMotionAdapter);

        keyAdapter = new KeyAdapter() {
        };
        addKeyListener(keyAdapter);

        setFocusTraversalKeysEnabled(false);
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent evt) {
                requestFocusInWindow();
            }
        });

        setFont(new Font("SansSerif", Font.BOLD, 13));

        if (icon == null) {
            icon = OtherUtil.loadSFImages("wait_rotate.gif");
        }
        if (waitMessage == null) {
            waitMessage = PrefixUtil.getMsg("${djf.message.wait.usual}", null);
        }
        if (waitMessage == null) {
            waitMessage = "Please wait ....";
        }
    }

    protected void paintComponent(Graphics g) {
        // enables anti-aliasing
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        FontMetrics metrics = g.getFontMetrics();
        int widthText = metrics.stringWidth(waitMessage) + hGap * 2;
        int heighText = metrics.getHeight() + vGap * 2;

        int x = (getWidth() - widthText) / 2;
        int y = (getHeight() - heighText) / 2;

        // draws shadow
        g2.setColor(shadowColorA);
        g2.fillRoundRect(x + strokeSize,
                y + strokeSize,
                widthText + shadowOffset, // width
                heighText + shadowOffset, // height
                arcs.width, arcs.height);// arc Dimension

        // draws gradient
        GradientPaint gp = new GradientPaint(x, y, Color.WHITE, x + widthText, y, GRAD_COLOR);
        g2.setPaint(gp);
        g2.fill(new Rectangle2D.Double(x, y, widthText, heighText));

        // draws the text
        g2.setColor(TEXT_COLOR);
        g2.drawString(waitMessage, x + hGap, y + vGap + metrics.getHeight() - 1);

        // draws rectangle
        g2.drawRect(x, y, widthText, heighText);

        if (icon != null) {
            // draw icon
            int xi = (getWidth() / 2) - (icon.getIconWidth() / 2);
            int yi = y - icon.getIconHeight() - (vGap / 2);
            icon.paintIcon(this, g, xi, yi);
        }

    }

}
