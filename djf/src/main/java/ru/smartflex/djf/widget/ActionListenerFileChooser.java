package ru.smartflex.djf.widget;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

public class ActionListenerFileChooser implements ActionListener, ISFHandler {

    private SFFileChooser fileChooser;

    ActionListenerFileChooser(SFFileChooser fileChooser) {
        super();
        this.fileChooser = fileChooser;

        fileChooser.getButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();

        File lastDir = fileChooser.getLastDir();
        fc.setCurrentDirectory(lastDir);

        int retVal = fc.showOpenDialog(fileChooser.getPanel());
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File fileExchange = fc.getSelectedFile();
            fileChooser.setNewSelectedFile(fileExchange);
        } else {
            fileChooser.clearDirView();
        }

    }

    @Override
    public void closeHandler() {
        fileChooser = null;
    }

}
