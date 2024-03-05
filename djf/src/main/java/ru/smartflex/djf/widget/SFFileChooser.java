package ru.smartflex.djf.widget;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.tool.OtherUtil;

public class SFFileChooser extends JPanel implements IRequestFocus {

    private static final long serialVersionUID = 3409507867074017833L;

    private JButton button = new JButton();
    private JTextField field = new JTextField();
    private BorderLayout bl = new BorderLayout(7, 7);
    private File lastSelectedDir = null;
    private File lastSelectedFile = null;
    private boolean fileWasSelected = false;
    private JComponent panel = null;

    public SFFileChooser() {
        super();

        init();
    }

    private void init() {
        ImageIcon icon = OtherUtil.loadSFImages("open.gif");
        button.setIcon(icon);

        new ActionListenerFileChooser(this);
        field.setEditable(false);

        this.setLayout(bl);
        this.add(button, BorderLayout.WEST);
        this.add(field, BorderLayout.CENTER);
    }

    public void createKeyHandler(WidgetManager wm) {
        new ButtonKeyHandler(this, wm);
    }

    public ActionListener[] getActionListeners() {
        return button.getActionListeners();
    }

    public KeyListener[] getKeyListeners() {
        return button.getKeyListeners();
    }

    public void setToolTipText(String text) {
        button.setToolTipText(text);
        field.setToolTipText(text);
    }

    public JButton getButton() {
        return button;
    }

    @SuppressWarnings("WeakerAccess")
    public File getLastDir() {
        return lastSelectedDir;
    }

    void clearDirView() {
        field.setText("");
        fileWasSelected = false;
    }

    @SuppressWarnings("unused")
    public void setPreviousDir(File prevDir) {
        lastSelectedDir = prevDir;
    }

    @SuppressWarnings("WeakerAccess")
    public void setNewSelectedFile(File newFile) {

        if (newFile.isFile()) {
            field.setText(newFile.getPath());
            lastSelectedDir = newFile;
            lastSelectedFile = newFile;
            fileWasSelected = true;
        }

    }

    @SuppressWarnings("unused")
    public File getLastSelectedFile() {
        return lastSelectedFile;
    }

    @SuppressWarnings({"unused", "ResultOfMethodCallIgnored"})
    public byte[] getFileBody() {

        if (fileWasSelected) {
            try {
                byte[] br = new byte[(int) lastSelectedFile.length()];
                FileInputStream fis = new FileInputStream(lastSelectedFile);
                fis.read(br);
                fis.close();
                return br;
            } catch (IOException ioe) {
                SFLogger.error("Error file reading", ioe);
            }
        }

        return null;
    }

    @SuppressWarnings("unused")
    public boolean isFileWasSelected() {
        return fileWasSelected;
    }

    public JComponent getPanel() {
        return panel;
    }

    public void setPanel(JComponent panel) {
        this.panel = panel;
    }

    @Override
    public void requestFocusOnNestedWidget() {
        button.requestFocus();
    }

    public void setColsAttribute(BigInteger cols) {
        if (cols != null) {
            field.setColumns(cols.intValue() + 1);
        }
    }

}
