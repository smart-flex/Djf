package ru.smartflex.djf.widget;

import java.awt.Cursor;

import javax.swing.SwingWorker;

import ru.smartflex.djf.FrameHelper.WaitSemafor;

public class WaitLongThread extends SwingWorker<Void, Void> {

    private static final int NON_VISIBLE_DELAY = 120;

    private WaitLongPanel waitLongPanel;
    private WaitSemafor semafor;

    public WaitLongThread(WaitLongPanel waitLongPanel, WaitSemafor semafor) {
        this.waitLongPanel = waitLongPanel;
        this.semafor = semafor;
    }

    @SuppressWarnings({"RedundantThrows", "CatchMayIgnoreException"})
    @Override
    protected Void doInBackground() throws Exception {

        if (NON_VISIBLE_DELAY > 0) {
            // to avoid blinking from show and hide panel in short time
            try {
                Thread.sleep(NON_VISIBLE_DELAY);
            } catch (Exception e) {
            }
        }


        if (semafor.isStartBlinkingAllowed()) {
            waitLongPanel.setCursor(Cursor
                    .getPredefinedCursor(Cursor.WAIT_CURSOR));

            waitLongPanel.setVisible(true);

            semafor.finishProcessOfBlinkig();
        } // else { hode process already done

        return null;
    }

}
