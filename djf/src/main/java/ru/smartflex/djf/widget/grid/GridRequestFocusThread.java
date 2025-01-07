package ru.smartflex.djf.widget.grid;


import javax.swing.*;

/**
 * В потоке выставляем фокус на гриде трижды, иначе выставление фокусов на input перевешивает и перволначальный фокус с грида соскакивает
 * (свинговый костыль)
 */
public class GridRequestFocusThread extends SwingWorker<Void, Void> {

    private SFGrid grid;

    public GridRequestFocusThread(SFGrid grid) {
        this.grid = grid;
    }

    @Override
    protected Void doInBackground() throws Exception {
        // иногда два вызова не помогают
        Thread.sleep(10);
        grid.requestGridFocus();
        Thread.sleep(10);
        grid.requestGridFocus();
        Thread.sleep(10);
        grid.requestGridFocus();
        return null;
    }
}
